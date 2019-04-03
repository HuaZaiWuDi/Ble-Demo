package lab.wesmartclothing.wefit.flyso.ui.userinfo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.tbruyelle.rxpermissions2.RxPermissions;
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
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;

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
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceBean;
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceItem;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class AddDeviceActivity extends BaseActivity {


    public static final int STATUS_SCAN_DEVICE = 0;
    public static final int STATUS_FIND_DEVICE = 1;
    public static final int STATUS_BIND_DEVICE = 2;
    public static final int STATUS_NO_DEVICE = 3;

    BindDeviceItem mBindDeviceItem = new BindDeviceItem();


    boolean forceBind = true;//是否强制绑定
    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_details)
    TextView mTvDetails;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
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

                switchStatus(STATUS_NO_DEVICE);
                mTvDetails.setText("监测到未开启蓝牙，请打开蓝牙重新扫描");
            } else if (state == BluetoothAdapter.STATE_ON) {
                startScan();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.white))
                .setLightStatusBar(true)
                .process();

        initPermissions();

        if (getIntent().getExtras() != null) {
            forceBind = getIntent().getExtras().getBoolean(Key.BUNDLE_FORCE_BIND);
        }

        registerReceiver(systemBleReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        initView();
        initRxBus();

    }

    private void startScan() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            BleTools.getBleManager().enableBluetooth();
            return;
        }

        if (!RxLocationUtils.isLocationEnabled(this.getApplicationContext())) {
            RxLogUtils.d("未开启GPS定位");
//            RxToast.warning(getString(R.string.open_GPS));
            RxDialogGPSCheck rxDialogGPSCheck = new RxDialogGPSCheck(mContext);
            rxDialogGPSCheck.show();
            return;
        }

        mDeviceLists.clear();
        scanDevice.clear();
        adapter.setNewData(null);
        mImgScan.startAnimation(RxAnimationUtils.RotateAnim(10));

        isScan(false);

        scanTimeout.startTimer();

        startService(new Intent(mContext, BleService.class));
        RxLogUtils.d("开启动画");
    }

    private MyTimer scanTimeout = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            switchStatus(STATUS_NO_DEVICE);
        }
    }, 10000);


    private void initRxBus() {
        RxBus.getInstance().register2(BindDeviceBean.class)
                .compose(RxComposeUtils.<BindDeviceBean>bindLife(lifecycleSubject))
//                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new RxSubscriber<BindDeviceBean>() {
                    @Override
                    protected void _onNext(BindDeviceBean device) {
                        isBind(device);
                    }
                });
    }


    public void initView() {
        initTopBar();
        initRecycler();
        startScan();
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
        if (!forceBind) {
            Button skip = mTopBar.addRightTextButton("跳过", R.id.tv_skip);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转主页
                    RxActivityUtils.skipActivity(mContext, MainActivity.class);
                }
            });
            skip.setTextColor(ContextCompat.getColor(mContext, R.color.Gray));
        }
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
                helper.setImageResource(R.id.img_weight, BleKey.TYPE_SCALE.equals(item.getDeivceType()) ? R.mipmap.icon_scale_view : R.mipmap.icon_clothing_view);
                if (item.isBind()) {
                    helper.getView(R.id.tv_Bind).setVisibility(View.GONE);
                    helper.setVisible(R.id.tv_bindings, true);
                } else {
                    helper.getView(R.id.tv_bindings).setVisibility(View.GONE);
                    helper.setVisible(R.id.tv_Bind, true);
                }

                RxRoundProgressBar progressRssi = helper.getView(R.id.pro_rssi);
                progressRssi.setProgress(100 - (int) ((Math.abs(item.getRssi()) - 35) / 127f * 100));

                String titleStr = item.getDeviceName() + "\t" + item.getDeviceMac().substring(12);

                helper.setText(R.id.tv_weight_data, titleStr);
                helper.addOnClickListener(R.id.tv_Bind);
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_Bind) {
                    RxLogUtils.d("点击了绑定");

                    final BindDeviceBean item = (BindDeviceBean) adapter.getItem(position);
                    if (BleKey.TYPE_SCALE.equals(item.getDeivceType()) && BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
                        RxToast.normal("已绑定体脂称");
                        return;
                    }
                    if (BleKey.TYPE_CLOTHING.equals(item.getDeivceType()) && BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
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
                deviceList.setMacAddr(bean.getDeviceMac());
                deviceList.setDeviceNo(bean.getDeivceType());
                mDeviceLists.add(deviceList);
                if (BleKey.TYPE_SCALE.equals(bean.getDeivceType())) {
                    SPUtils.put(SPKey.SP_scaleMAC, bean.getDeviceMac());
                } else {
                    SPUtils.put(SPKey.SP_clothingMAC, bean.getDeviceMac());
                }
            }
        }
        mBindDeviceItem.setDeviceList(mDeviceLists);
        String s = JSON.toJSONString(mBindDeviceItem);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addBindDevice(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加绑定设备：" + s);

                        RxBus.getInstance().post(new RefreshSlimming());
                        RxBus.getInstance().post(new RefreshMe());
                        //跳转主页
                        if (!forceBind) {
                            RxActivityUtils.skipActivity(mContext, MainActivity.class);
                        } else {
                            onBackPressed();
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void isBind(final BindDeviceBean bean) {
        if (bean.getDeviceMac().equals(SPUtils.getString(SPKey.SP_scaleMAC)) ||
                bean.getDeviceMac().equals(SPUtils.getString(SPKey.SP_clothingMAC))) {
            return;
        }

        if (scanDevice.containsKey(bean.getDeviceMac())) {
            return;
        }

        if (mBtnScan.isEnabled()) return;

        scanDevice.put(bean.getDeviceMac(), bean);

        if (scanDevice.size() == 1) {
            switchStatus(STATUS_FIND_DEVICE);
            scanTimeout.stopTimer();
        }


        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().isBindDevice(bean.getDeviceMac()))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        if ("true".equals(s)) {
                            if (BleKey.TYPE_SCALE.equals(bean.getDeivceType())) {
                                SPUtils.put(SPKey.SP_scaleMAC, bean.getDeviceMac());
                            } else {
                                SPUtils.put(SPKey.SP_clothingMAC, bean.getDeviceMac());
                            }
                        }
                        bean.setBind("true".equals(s));
//                        adapter.addData(bean);
                        sortList(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
//                        adapter.addData(bean);
                        sortList(bean);
                    }

                });
    }

    /**
     * 排序，信号（rssi）值越小，越靠前
     *
     * @param bean
     */
    private void sortList(BindDeviceBean bean) {
        int index = 0;
        List<BindDeviceBean> list = adapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRssi() <= bean.getRssi()) {
                index = i;
                break;
            } else {
                index = i + 1;
            }
        }
        adapter.addData(index, bean);
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
                mTvTitle.setText("扫描搜索设备");
                mTvDetails.setText("请添加您最近购买的智能设备");

                isScan(false);

                mImgScan.setVisibility(View.VISIBLE);
                mImgNoDevice.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.GONE);
                break;
            case STATUS_FIND_DEVICE:
                mTvTitle.setText("附近的设备");
                mTvDetails.setText("请绑定并开始使用您的智能设备");
                mImgScan.clearAnimation();
                mBtnScan.setVisibility(View.GONE);
                mImgScan.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.VISIBLE);
                mImgNoDevice.setVisibility(View.GONE);
                break;
            case STATUS_BIND_DEVICE:
                mTvTitle.setText("附近的设备");
                mTvDetails.setText("请绑定并开始使用您的智能设备");
                mImgScan.clearAnimation();

                isScan(true);
                mBtnScan.setText("开始使用");

                mImgScan.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.VISIBLE);
                mImgNoDevice.setVisibility(View.GONE);
                break;
            case STATUS_NO_DEVICE:
                mTvTitle.setText("扫描设备失败");
                mTvDetails.setText("没有搜索到设备，请确保设备电量充足");
                mImgScan.clearAnimation();

                isScan(true);
                mBtnScan.setText(R.string.reScan);
                scanTimeout.stopTimer();

                mImgScan.setVisibility(View.GONE);
                mMRecyclerView.setVisibility(View.GONE);
                mImgNoDevice.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void isScan(boolean isCanScan) {
        mBtnScan.setEnabled(isCanScan);
        mBtnScan.setVisibility(isCanScan ? View.VISIBLE : View.GONE);
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


    private void initPermissions() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            new RxDialogSureCancel(mContext)
                                    .setTitle("提示")
                                    .setContent("不定位权限，手机将无法连接蓝牙")
                                    .setSure("去开启")
                                    .setSureListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initPermissions();
                                        }
                                    }).show();
                        }
                    }
                });
    }

}
