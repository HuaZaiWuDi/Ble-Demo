package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.ble.BleKey;
import lab.wesmartclothing.wefit.flyso.ble.BleTools;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.DeviceListbean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus;
import lab.wesmartclothing.wefit.flyso.rxbus.DeviceVoltageBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleConnectBus;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_device);
        unbinder = ButterKnife.bind(this);
        initView();


    }


    private void initView() {
        initTopBar();
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
        notifyData();
        initRxBus();
    }


    @SuppressLint("CheckResult")
    protected void initRxBus() {
        RxBus.getInstance().registerSticky(DeviceVoltageBus.class)
                .compose(RxComposeUtils.<DeviceVoltageBus>bindLife(lifecycleSubject))
                .subscribe(deviceVoltageBus -> {
                    mTvClothingUseTime.setText(deviceVoltageBus.getCapacity() + "");
                    mTvClothingStandbyTime.setText(RxFormatValue.fromat4S5R(deviceVoltageBus.getTime() / 24, 1));
                });

        RxBus.getInstance().registerSticky(ScaleConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(scaleConnectBus -> {
                    mTvConnectStateScale.setText(scaleConnectBus.isConnect() ? R.string.connected : R.string.disConnected);
                });

        RxBus.getInstance().registerSticky(ClothingConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(cloting -> {
                    mTvConnectStateClothing.setText(cloting.isConnect() ? R.string.connected : R.string.disConnected);
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle(R.string.myDevice);
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

        }
        if (clothingIsBind) {
            mLayoutClothing.setVisibility(View.VISIBLE);
            mTvClothingId.setText(SPUtils.getString(SPKey.SP_clothingMAC));
        }
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
                        RxToast.error(getString(R.string.deleteFail));
                    }


                });
    }
}
