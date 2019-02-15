package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.DeviceListbean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.DeviceVoltageBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.VoltageToPower;

/**
 * Created by jk on 2018/8/10.
 */
public class DeviceFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_connectState_scale)
    TextView mTvConnectStateScale;
    @BindView(R.id.tv_scale_id)
    TextView mTvScaleId;
    @BindView(R.id.tv_scale_useTime)
    TextView mTvScaleUseTime;
    @BindView(R.id.btn_unbind_scale)
    QMUIRoundButton mBtnUnbindScale;
    @BindView(R.id.layout_scale)
    LinearLayout mLayoutScale;
    @BindView(R.id.tv_connectState_clothing)
    TextView mTvConnectStateClothing;
    @BindView(R.id.tv_clothing_id)
    TextView mTvClothingId;
    @BindView(R.id.tv_clothing_useTime)
    TextView mTvClothingUseTime;
    @BindView(R.id.tv_clothing_standbyTime)
    TextView mTvClothingStandbyTime;
    @BindView(R.id.btn_unbind_clothing)
    QMUIRoundButton mBtnUnbindClothing;
    @BindView(R.id.layout_clothing)
    LinearLayout mLayoutClothing;
    @BindView(R.id.tv_noDeviceTip)
    TextView mTvNoDeviceTip;
    @BindView(R.id.btn_bind)
    QMUIRoundLinearLayout mBtnBind;
    Unbinder unbinder;

    QNBleTools mQNBleTools = QNBleTools.getInstance();


    private List<DeviceListbean.ListBean> beanList;


    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Key.ACTION_SCALE_CONNECT.equals(intent.getAction())) {
                //体脂称连接状态
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_SCALE_CONNECT);
                mTvConnectStateScale.setText(mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected);
            } else if (Key.ACTION_CLOTHING_CONNECT.equals(intent.getAction())) {
                //监听瘦身衣连接情况
                boolean state = intent.getExtras().getBoolean(Key.EXTRA_CLOTHING_CONNECT);
                mTvConnectStateClothing.setText(BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected);
//                if (state) {
//                    getVoltage();
//                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_device);
        unbinder = ButterKnife.bind(this);
        initView();


        IntentFilter filter = new IntentFilter();
        filter.addAction(Key.ACTION_SCALE_CONNECT);
        filter.addAction(Key.ACTION_CLOTHING_CONNECT);
        registerReceiver(registerReceiver, filter);

    }


    private void initView() {
        initTopBar();
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
        notifyData();
        getVoltage();
        initRxBus();
    }


    protected void initRxBus() {
        RxBus.getInstance().register2(DeviceVoltageBus.class)
                .compose(RxComposeUtils.<DeviceVoltageBus>bindLife(lifecycleSubject))
                .subscribe(new Consumer<DeviceVoltageBus>() {
                    @Override
                    public void accept(DeviceVoltageBus deviceVoltageBus) throws Exception {
                        mTvClothingUseTime.setText(deviceVoltageBus.getCapacity() + "");
                        mTvClothingStandbyTime.setText(RxFormatValue.fromat4S5R(deviceVoltageBus.getTime() / 24, 1));
                    }
                });

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(registerReceiver);
        super.onDestroy();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("我的设备");
    }


    @OnClick({R.id.btn_unbind_scale, R.id.btn_unbind_clothing, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_unbind_scale:
                deleteDeviceById(BleKey.TYPE_SCALE);
                break;
            case R.id.btn_unbind_clothing:
                deleteDeviceById(BleKey.TYPE_CLOTHING);
                break;
            case R.id.btn_bind:
                RxActivityUtils.skipActivity(mActivity, AddDeviceActivity.class);
                break;
        }
    }

    private void initData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().deviceList())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);

                        DeviceListbean deviceListbean = JSON.parseObject(s, DeviceListbean.class);
                        beanList = deviceListbean.getList();
                        for (int i = 0; i < beanList.size(); i++) {
                            DeviceListbean.ListBean device = beanList.get(i);
                            if (BleKey.TYPE_SCALE.equals(device.getDeviceNo())) {
                                int hour = device.getOnlineDuration() / 3600;
                                mTvScaleUseTime.setText((hour <= 0 ? 1 : hour) + "");
                            }
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    private void notifyData() {
        boolean clothingIsBind = BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC));
        boolean scaleIsBind = BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC));

        mTvNoDeviceTip.setVisibility(clothingIsBind || scaleIsBind ? View.GONE : View.VISIBLE);
        mBtnBind.setVisibility(clothingIsBind && scaleIsBind ? View.GONE : View.VISIBLE);
        mLayoutScale.setVisibility(View.GONE);
        mLayoutClothing.setVisibility(View.GONE);

        if (scaleIsBind) {
            mLayoutScale.setVisibility(View.VISIBLE);
            mTvScaleId.setText(SPUtils.getString(SPKey.SP_scaleMAC));
            mTvConnectStateScale.setText(mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected);
        }
        if (clothingIsBind) {
            mLayoutClothing.setVisibility(View.VISIBLE);
            mTvClothingId.setText(SPUtils.getString(SPKey.SP_clothingMAC));
            mTvConnectStateClothing.setText(BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected);
        }
    }

    private void getVoltage() {
        BleAPI.getVoltage(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("读电压" + HexUtil.encodeHexStr(data));
                int voltage = ByteUtil.bytesToIntD2(new byte[]{data[3], data[4]});
                RxLogUtils.d("电压：" + voltage);
                VoltageToPower toPower = new VoltageToPower();
                int capacity = toPower.getBatteryCapacity(voltage / 1000f);
                double time = toPower.canUsedTime(voltage / 1000f, false);
                RxLogUtils.d("capacity:" + capacity + "time：" + time);
                mTvClothingUseTime.setText(capacity + "");
                mTvClothingStandbyTime.setText(RxFormatValue.fromat4S5R(time / 24, 1));
            }
        });
    }


    private void deleteDeviceById(final String position) {
        String gid = "";
        if (beanList != null)
            for (int i = 0; i < beanList.size(); i++) {
                DeviceListbean.ListBean device = beanList.get(i);
                if (position.equals(device.getDeviceNo())) {
                    gid = device.getGid();
                }
            }
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().removeBind(gid))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        //添加绑定设备，这里实在不会
                        if (BleKey.TYPE_SCALE.equals(position)) {
                            //删除绑定
                            mQNBleTools.disConnectDevice();
                            SPUtils.remove(SPKey.SP_scaleMAC);
                        } else if (BleKey.TYPE_CLOTHING.equals(position)) {
                            SPUtils.remove(SPKey.SP_clothingMAC);
                            BleTools.getInstance().disConnect();
                        }
                        notifyData();
                        RxBus.getInstance().post(new RefreshMe());
                        RxBus.getInstance().post(new RefreshSlimming());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error("删除失败");
                    }


                });
    }
}
