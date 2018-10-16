package lab.wesmartclothing.wefit.flyso.ui.userinfo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.aboutCarmera.RxImageTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
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
import lab.wesmartclothing.wefit.flyso.view.ScanView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddDeviceActivity extends BaseActivity {


    BindDeviceItem mBindDeviceItem = new BindDeviceItem();

    QNBleTools mQNBleTools = QNBleTools.getInstance();

    boolean forceBind = true;//是否强制绑定


    @BindView(R.id.layout_scan)
    RelativeLayout mLayoutScan;
    @BindView(R.id.img_scan)
    ScanView mImgScan;
    @BindView(R.id.layout_scan_2)
    RelativeLayout mLayoutScan2;
    @BindView(R.id.tv_nearDevice)
    TextView mTvNearDevice;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.layout_bind)
    RelativeLayout mLayoutBind;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.back)
    LinearLayout mBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.img_working)
    ImageView mImgWorking;
    @BindView(R.id.tv_working)
    TextView mTvWorking;
    @BindView(R.id.line_working)
    View mLineWorking;
    @BindView(R.id.img_bind)
    ImageView mImgBind;
    @BindView(R.id.tv_bind)
    TextView mTvBind;
    @BindView(R.id.line_bind_L)
    View mLineBindL;
    @BindView(R.id.line_bind_R)
    View mLineBindR;
    @BindView(R.id.img_startUse)
    ImageView mImgStartUse;
    @BindView(R.id.tv_startUse)
    TextView mTvStartUse;
    @BindView(R.id.line_use)
    View mLineUse;
    @BindView(R.id.layout_step)
    LinearLayout mLayoutStep;
    @BindView(R.id.btn_scan)
    QMUIRoundButton mBtnScan;
    @BindView(R.id.img_no_data)
    ImageView mImgNoData;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.layout_notDevice)
    RelativeLayout mLayoutNotDevice;


    private int stepState = 0;
    private BaseQuickAdapter adapter;
    private List<BindDeviceItem.DeviceListBean> mDeviceLists = new ArrayList<>();
    private Map<String, BindDeviceBean> scanDevice = new HashMap<>();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (stepState == 3) {
                initStep(0);
            } else {
                RxActivityUtils.finishActivity();
            }
        return true;
    }


    //监听系统蓝牙开启
    BroadcastReceiver systemBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            if (state == BluetoothAdapter.STATE_OFF) {
                mBtnScan.setEnabled(true);
                mImgScan.stopAnimation();
                RxToast.warning(getString(R.string.checkBle));
                initStep(3);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        //屏幕沉浸
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();

        forceBind = getIntent().getExtras().getBoolean(Key.BUNDLE_FORCE_BIND);

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
        mImgScan.startAnimation();
        mBtnScan.setEnabled(false);

        scanTimeout.startTimer();

    }

    private MyTimer scanTimeout = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            mBtnScan.setEnabled(true);
            mImgScan.stopAnimation();
            RxToast.warning(getString(R.string.checkBle));
            initStep(3);
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
        initStep(0);
        initRecycler();
        if (forceBind)
            mTvSkip.setVisibility(View.GONE);

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
                    initStep(2);
                }
            }
        });
        adapter.bindToRecyclerView(mMRecyclerView);
    }

    private void initStep(int stepState) {
        this.stepState = stepState;
        mLayoutNotDevice.setVisibility(stepState == 3 ? View.VISIBLE : View.GONE);
        mTvTitle.setTextColor(getResources().getColor(stepState == 0 ? R.color.white : R.color.black));
        mImgBack.setImageResource(stepState == 0 ? R.mipmap.back_icon : R.mipmap.back);
        if (!forceBind)
            mTvSkip.setVisibility(stepState == 1 ? View.GONE : View.VISIBLE);
        mTvSkip.setTextColor(getResources().getColor(stepState == 0 ? R.color.white : R.color.black));
        mTvSkip.setTextColor(getResources().getColor(stepState == 0 ? R.color.white : R.color.textHeatColor));

        Drawable drawableColor = RxImageTools.changeDrawableColor(mContext, R.mipmap.icon_wancheng3x, R.color.green_61D97F);

        switch (stepState) {
            case 0:
                mLayoutScan.setVisibility(View.VISIBLE);
                mLayoutScan2.setVisibility(View.VISIBLE);
                mLayoutBind.setVisibility(View.GONE);
                mBtnScan.setVisibility(View.VISIBLE);
                break;
            case 1:
                mLayoutScan.setVisibility(View.GONE);
                mLayoutScan2.setVisibility(View.GONE);
                mLayoutBind.setVisibility(View.VISIBLE);
                mImgWorking.setBackground(drawableColor);

                mImgBind.setBackgroundResource(R.mipmap.icon_xuanzhe);
                mLineBindL.setBackgroundColor(getResources().getColor(R.color.green_61D97F));
                mLineBindR.setBackgroundColor(getResources().getColor(R.color.green_61D97F));
                mTvBind.setTextColor(getResources().getColor(R.color.textColor));
                mBtnScan.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mImgStartUse.setBackgroundResource(R.mipmap.icon_xuanzhe);
                mImgBind.setBackground(drawableColor);
                mLineUse.setBackgroundColor(getResources().getColor(R.color.green_61D97F));
                mTvStartUse.setTextColor(getResources().getColor(R.color.textColor));
                mBtnScan.setVisibility(View.VISIBLE);
                mBtnScan.setText(R.string.startUse);
                break;
            case 3:
                mLayoutScan.setVisibility(View.GONE);
                break;
        }
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

        scanTimeout.stopTimer();
        mBtnScan.setEnabled(true);
        mImgScan.stopAnimation();
        if (stepState != 2)
            initStep(1);

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
                            initStep(2);
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

    @OnClick({R.id.back, R.id.btn_scan, R.id.tv_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (stepState == 3) {
                    initStep(0);
                } else {
                    RxActivityUtils.finishActivity();
                }
                break;
            case R.id.btn_scan:
                if (stepState == 0) {
                    startScan();
                } else if (stepState == 2) {
                    bindDevice();
                }
                break;
            case R.id.tv_tip:
                //跳转主页
                if (!forceBind)
                    RxActivityUtils.skipActivityAndFinish(mContext, MainActivity.class);
                else
                    RxActivityUtils.finishActivity();
                break;
        }
    }
}
