package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.BleAPI;
import lab.wesmartclothing.wefit.flyso.ble.EMSManager;
import lab.wesmartclothing.wefit.flyso.ble.MyBleManager;
import lab.wesmartclothing.wefit.flyso.ble.QNBleManager;
import lab.wesmartclothing.wefit.flyso.entity.DeviceListbean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus;
import lab.wesmartclothing.wefit.flyso.rxbus.DeviceVoltageBus;
import lab.wesmartclothing.wefit.flyso.rxbus.EMSConnectBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleConnectBus;
import lab.wesmartclothing.wefit.flyso.tools.BleKey;
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
    @BindView(R.id.iv_clothingIcon)
    ImageView mIvClothingIcon;
    @BindView(R.id.tv_clothingName)
    TextView mTvClothingName;


    private String clothingType = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_device;
    }

    @Override
    public void initViews() {
        initTopBar();
        initRxBus();
        BleAPI.getVoltage();
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();

    }


    @SuppressLint("CheckResult")
    protected void initRxBus() {
        RxBus.getInstance().registerSticky(DeviceVoltageBus.class)
                .compose(RxComposeUtils.<DeviceVoltageBus>bindLife(lifecycleSubject))
                .subscribe(deviceVoltageBus -> {
                    if (BleKey.TYPE_CLOTHING.equals(clothingType)) {
                        mTvClothingUseTime.setText(deviceVoltageBus.getCapacity() + "");
                        mTvClothingStandbyTime.setText(RxFormatValue.fromat4S5R(deviceVoltageBus.getTime() / 24, 1));
                    }
                });

        RxBus.getInstance().registerSticky(ScaleConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(scaleConnectBus ->
                        mTvConnectStateScale.setText(scaleConnectBus.isConnect() ? R.string.connected : R.string.disConnected));

        RxBus.getInstance().registerSticky(ClothingConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(cloting ->
                        mTvConnectStateClothing.setText(cloting.isConnect() ? R.string.connected : R.string.disConnected));
        RxBus.getInstance().registerSticky(EMSConnectBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(cloting ->
                        mTvConnectStateClothing.setText(cloting.isConnect() ? R.string.connected : R.string.disConnected));
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle(R.string.myDevice);
    }


    @OnClick({R.id.btn_unbind_scale, R.id.btn_unbind_clothing, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_unbind_scale:
                deleteDeviceById(true);
                break;
            case R.id.btn_unbind_clothing:
                deleteDeviceById(false);
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
                        DeviceListbean deviceListbean = JSON.parseObject(s, DeviceListbean.class);
                        List<DeviceListbean.ListBean> beanList = deviceListbean.getList();
                        if (beanList != null)
                            updateUI(beanList);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    private void updateUI(List<DeviceListbean.ListBean> beanList) {
        mTvNoDeviceTip.setVisibility(!RxDataUtils.isEmpty(beanList) ? View.GONE : View.VISIBLE);
        mBtnBind.setVisibility(beanList.size() == 2 ? View.GONE : View.VISIBLE);
        mLayoutScale.setVisibility(View.GONE);
        mLayoutClothing.setVisibility(View.GONE);

        for (int i = 0; i < beanList.size(); i++) {
            DeviceListbean.ListBean device = beanList.get(i);
            switch (device.getDeviceNo()) {
                case BleKey.TYPE_SCALE:
                    mLayoutScale.setVisibility(View.VISIBLE);
                    mLayoutScale.setTag(device.getGid());
                    mTvScaleId.setText(device.getMacAddr());
                    int hour = device.getOnlineDuration() / 3600;
                    mTvScaleUseTime.setText(Math.max(1, hour) + "");
                    break;
                case BleKey.TYPE_CLOTHING:
                    clothingType = BleKey.TYPE_CLOTHING;
                    mLayoutClothing.setVisibility(View.VISIBLE);
                    mTvClothingId.setText(device.getMacAddr());
                    mIvClothingIcon.setImageResource(R.mipmap.icon_clothing_view);
                    mTvClothingName.setText(R.string.clothing);
                    mLayoutClothing.setTag(device.getGid());
                    break;
                case BleKey.TYPE_EMS:
                    clothingType = BleKey.TYPE_EMS;
                    mTvClothingName.setText(R.string.EMS_clothing);
                    mIvClothingIcon.setImageResource(R.mipmap.ic_ems_s);
                    mLayoutClothing.setVisibility(View.VISIBLE);
                    mTvClothingId.setText(device.getMacAddr());
                    mLayoutClothing.setTag(device.getGid());
                    break;
                default:

                    break;
            }
        }
    }


    private void deleteDeviceById(final boolean isScale) {
        String gid = (String) (isScale ? mLayoutScale.getTag() : mLayoutClothing.getTag());
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().removeBind(gid))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if (isScale) {
                            //删除绑定
                            QNBleManager.getInstance().unBind();
                        } else if (BleKey.TYPE_CLOTHING.equals(clothingType)) {
                            MyBleManager.Companion.getInstance().unBind();
                        } else {
                            EMSManager.Companion.getInstance().unBind();
                        }
                        initData();
                        RxBus.getInstance().post(new RefreshMe());
                        RxBus.getInstance().post(new RefreshSlimming());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);
                    }
                });
    }
}
