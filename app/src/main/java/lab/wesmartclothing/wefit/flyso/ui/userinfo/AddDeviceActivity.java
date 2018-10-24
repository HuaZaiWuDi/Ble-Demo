package lab.wesmartclothing.wefit.flyso.ui.userinfo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.aboutCarmera.RxImageTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogGPSCheck;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceBean;
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceItem;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddDeviceActivity extends BaseActivity {


    public static final int STATUS_SCAN_DEVICE = 0;
    public static final int STATUS_FIND_DEVICE = 1;
    public static final int STATUS_BIND_DEVICE = 2;
    public static final int STATUS_NO_DEVICE = 3;

    BindDeviceItem mBindDeviceItem = new BindDeviceItem();

    QNBleTools mQNBleTools = QNBleTools.getInstance();

    boolean forceBind = true;//是否强制绑定
    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_details)
    TextView mTvDetails;
    @BindView(R.id.img_scan)
    ImageView mImgScan;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.btn_scan)
    QMUIRoundButton mBtnScan;
    @BindView(R.id.img_noDevice)
    ImageView mImgNoDevice;


    private int stepState = 0;
    private BaseQuickAdapter adapter;
    private List<BindDeviceItem.DeviceListBean> mDeviceLists = new ArrayList<>();//添加绑定设备
    private Map<String, BindDeviceBean> scanDevice = new HashMap<>();//防止重复添加


    //监听系统蓝牙开启
    BroadcastReceiver systemBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            if (state == BluetoothAdapter.STATE_OFF) {
                mBtnScan.setEnabled(true);

                mImgScan.clearAnimation();
                RxToast.warning(getString(R.string.checkBle));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();

        if (getIntent().getExtras() != null) {
            forceBind = getIntent().getExtras().getBoolean(Key.BUNDLE_FORCE_BIND);
        }

        registerReceiver(systemBleReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        initView();
        initRxBus();
    }

    private void startScan() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            RxToast.warning(getString(R.string.open_BLE));
            BleTools.getBleManager().enableBluetooth();
        }

        if (!RxLocationUtils.isLocationEnabled(this.getApplicationContext())) {
            RxLogUtils.d("未开启GPS定位");
//            RxToast.warning(getString(R.string.open_GPS));
            RxDialogGPSCheck rxDialogGPSCheck = new RxDialogGPSCheck(mContext);
            rxDialogGPSCheck.show();
        }

        mDeviceLists.clear();
        scanDevice.clear();
        mImgScan.startAnimation(RxAnimationUtils.RotateAnim(15));
        mBtnScan.setEnabled(false);
        scanTimeout.stopTimer();
        scanTimeout.startTimer();

        RxLogUtils.d("开启动画");
    }

    private MyTimer scanTimeout = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            switchStatus(STATUS_NO_DEVICE);

        }
    }, 15000);


    private void initRxBus() {
        //瘦身衣
        RxBus.getInstance().register2(BleDevice.class)
                .compose(RxComposeUtils.<BleDevice>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<BleDevice>() {
                    @Override
                    protected void _onNext(BleDevice device) {
                        BindDeviceBean bean = new BindDeviceBean(1, device.getMac(), false, device.getMac());
                        isBind(bean);
                    }
                });
        //体脂称
        RxBus.getInstance().register2(QNBleDevice.class)
                .compose(RxComposeUtils.<QNBleDevice>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<QNBleDevice>() {
                    @Override
                    protected void _onNext(QNBleDevice device) {
                        BindDeviceBean bean = new BindDeviceBean(0, device.getMac(), false, device.getMac());
                        isBind(bean);
                    }
                });
    }


    public void initView() {
        initTopBar();
        initRecycler();
        switchStatus(STATUS_SCAN_DEVICE);
    }

    private void initTopBar() {
        mTopBar.setTitle("添加设备");
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (!forceBind)
            mTopBar.addRightTextButton("跳过", R.id.tv_skip)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转主页
                            RxActivityUtils.skipActivityAndFinish(mContext, MainActivity.class);
                        }
                    });
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(systemBleReceiver);
        scanTimeout.stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initRecycler() {
        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<BindDeviceBean, BaseViewHolder>(R.layout.item_bind_device) {
            @Override
            protected void convert(BaseViewHolder helper, BindDeviceBean item) {
                Drawable drawableColor = RxImageTools.changeDrawableColor(mContext, item.getDeivceType() == 0 ? R.mipmap.icon_scale : R.mipmap.icon_ranzhiyi3x, R.color.green_61D97F);
                helper.setImageDrawable(R.id.img_weight, drawableColor);
                if (item.isBind()) {
                    helper.getView(R.id.tv_Bind).setVisibility(View.GONE);
                    helper.setVisible(R.id.tv_bindings, true);
                } else {
                    helper.getView(R.id.tv_bindings).setVisibility(View.GONE);
                    helper.setVisible(R.id.tv_Bind, true);
                }
                helper.setText(R.id.tv_weight_data, item.getDeivceName());
                helper.addOnClickListener(R.id.tv_Bind);
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_Bind) {
                    RxLogUtils.d("点击了绑定");

                    final BindDeviceBean item = (BindDeviceBean) adapter.getItem(position);
                    if (item.getDeivceType() == 0 && BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
                        RxToast.normal("已绑定体脂称");
                        return;
                    }
                    if (item.getDeivceType() == 1 && BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
                        RxToast.normal("已绑定瘦身衣");
                        return;
                    }
                    List<BindDeviceBean> data = adapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getDeivceType() == item.getDeivceType()) {
                            data.get(i).setBind(false);
                        }
                    }
                    item.setBind(true);
                    adapter.notifyDataSetChanged();
                    switchStatus(STATUS_BIND_DEVICE);
                }
            }
        });
        adapter.bindToRecyclerView(mMRecyclerView);
    }


    private void bindDevice() {
        List<BindDeviceBean> beans = adapter.getData();
        for (BindDeviceBean bean : beans) {
            if (bean.isBind()) {
                BindDeviceItem.DeviceListBean deviceList = new BindDeviceItem.DeviceListBean();
                if (MyAPP.aMapLocation != null) {
                    deviceList.setCity(MyAPP.aMapLocation.getCity());
                    deviceList.setCountry(MyAPP.aMapLocation.getCountry());
                    deviceList.setProvince(MyAPP.aMapLocation.getProvince());
                }
                deviceList.setMacAddr(bean.getMac());
                deviceList.setDeviceNo(bean.getDeivceType() == 0 ? BleKey.TYPE_SCALE : BleKey.TYPE_CLOTHING);
                mDeviceLists.add(deviceList);
                if (bean.getDeivceType() == 0) {
                    SPUtils.put(SPKey.SP_scaleMAC, bean.getMac());
                } else {
                    SPUtils.put(SPKey.SP_clothingMAC, bean.getMac());
                }
            }
        }
        mBindDeviceItem.setDeviceList(mDeviceLists);
        String s = new Gson().toJson(mBindDeviceItem, BindDeviceItem.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addBindDevice(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加绑定设备：" + s);
                        //跳转主页
                        if (!forceBind) {
                            RxActivityUtils.skipActivityAndFinish(mContext, MainActivity.class);
                        } else {
                            onBackPressed();
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        RxBus.getInstance().post(new RefreshSlimming());
                        RxBus.getInstance().post(new RefreshMe());
                    }
                });
    }

    private void isBind(final BindDeviceBean bean) {
        if (bean.getMac().equals(SPUtils.getString(SPKey.SP_scaleMAC)) ||
                bean.getMac().equals(SPUtils.getString(SPKey.SP_clothingMAC))) {
            return;
        }
        if (scanDevice.containsKey(bean.getMac())) {
            return;
        }
        scanDevice.put(bean.getMac(), bean);

        switchStatus(STATUS_FIND_DEVICE);
        scanTimeout.stopTimer();

        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.isBindDevice(bean.getMac()))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        if ("true".equals(s)) {
                            if (bean.getDeivceType() == 0) {
                                SPUtils.put(SPKey.SP_scaleMAC, bean.getMac());
                            } else {
                                SPUtils.put(SPKey.SP_clothingMAC, bean.getMac());
                            }
                        }
                        bean.setBind("true".equals(s));
                        adapter.addData(bean);
                    }

                    @Override
                    protected void _onError(String error) {
                        //网络获取异常不可用
                        RxToast.normal(error);
                    }
                });
    }


    /**
     * 修改布局状态
     *
     * @param stepState
     */
    private void switchStatus(int stepState) {
        this.stepState = stepState;
        switch (stepState) {
            case STATUS_SCAN_DEVICE:
                mBtnScan.setEnabled(true);
                mBtnScan.setVisibility(View.VISIBLE);
                mBtnScan.setText(R.string.scan);
                mImgScan.setVisibility(View.VISIBLE);
                mImgNoDevice.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.GONE);
                break;
            case STATUS_FIND_DEVICE:
                mImgScan.clearAnimation();
                mBtnScan.setVisibility(View.GONE);
                mImgScan.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.VISIBLE);
                mImgNoDevice.setVisibility(View.GONE);
                break;
            case STATUS_BIND_DEVICE:
                mImgScan.clearAnimation();
                mBtnScan.setEnabled(true);
                mBtnScan.setVisibility(View.VISIBLE);
                mBtnScan.setText("开始使用");
                mImgScan.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.VISIBLE);
                mImgNoDevice.setVisibility(View.GONE);
                break;
            case STATUS_NO_DEVICE:
                RxToast.warning(getString(R.string.checkBle));
                mImgScan.clearAnimation();
                mBtnScan.setEnabled(true);
                mBtnScan.setVisibility(View.VISIBLE);
                mBtnScan.setText("重新扫描");
                mImgScan.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.GONE);
                mImgNoDevice.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick({R.id.btn_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                if (stepState == STATUS_SCAN_DEVICE) {
                    startScan();
                } else if (stepState == STATUS_BIND_DEVICE) {
                    bindDevice();
                } else if (stepState == STATUS_NO_DEVICE) {
                    switchStatus(STATUS_SCAN_DEVICE);
                    startScan();
                }
        }
    }
}
