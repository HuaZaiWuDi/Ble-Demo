package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.bluetooth.BluetoothAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
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

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.DeviceListbean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.VoltageToPower;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

/**
 * Created by jk on 2018/8/10.
 */
@EFragment
public class DeviceFragment extends BaseAcFragment {

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

    @Bean
    QNBleTools mQNBleTools;

    //体脂称连接状态
    @Receiver(actions = Key.ACTION_SCALE_CONNECT)
    void scaleIsConnect(@Receiver.Extra(Key.EXTRA_SCALE_CONNECT) boolean state) {
        if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)))
            mTvConnectStateScale.setText(mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected);
    }

    //监听瘦身衣连接情况
    @Receiver(actions = Key.ACTION_CLOTHING_CONNECT)
    void clothingConnectStatus(@Receiver.Extra(Key.EXTRA_CLOTHING_CONNECT) boolean state) {
        if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC)))
            mTvConnectStateClothing.setText(BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected);
    }

    public static QMUIFragment getInstance() {
        return new DeviceFragment_();
    }

    private List<DeviceListbean.ListBean> beanList;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_device, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("我的设备");
    }

    @OnClick({R.id.btn_unbind_scale, R.id.btn_unbind_clothing, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_unbind_scale:
                deleteDeviceById(0);
                break;
            case R.id.btn_unbind_clothing:
                deleteDeviceById(1);
                break;
            case R.id.btn_bind:
                RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class);
                break;
        }
    }


    private void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.deviceList())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);

                        DeviceListbean deviceListbean = new Gson().fromJson(s, DeviceListbean.class);
                        beanList = deviceListbean.getList();
                        notifyData();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void notifyData() {
        mTvNoDeviceTip.setVisibility(beanList.size() > 0 ? View.GONE : View.VISIBLE);
        mBtnBind.setVisibility(beanList.size() == 2 ? View.GONE : View.VISIBLE);
        mLayoutScale.setVisibility(View.GONE);
        mLayoutClothing.setVisibility(View.GONE);

        for (int i = 0; i < beanList.size(); i++) {
            DeviceListbean.ListBean device = beanList.get(i);
            if (BleKey.TYPE_SCALE.equals(device.getDeviceNo())) {
                mLayoutScale.setVisibility(View.VISIBLE);
                mTvScaleId.setText(device.getMacAddr());
                int hour = device.getOnlineDuration() / 3600;
                mTvScaleUseTime.setText((hour <= 0 ? 1 : hour) + "");
                mTvConnectStateScale.setText(mQNBleTools.isConnect() ? R.string.connected : R.string.disConnected);
            } else if (BleKey.TYPE_CLOTHING.equals(device.getDeviceNo())) {
                mLayoutClothing.setVisibility(View.VISIBLE);
                mTvClothingId.setText(device.getMacAddr());
                mTvConnectStateClothing.setText(BleTools.getInstance().isConnect() ? R.string.connected : R.string.disConnected);
            }
        }

        BleAPI.getVoltage(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("读电压" + HexUtil.encodeHexStr(data));
                int voltage = ByteUtil.bytesToIntD2(new byte[]{data[3], data[4]});
                RxLogUtils.d("电压：" + voltage);
                VoltageToPower toPower = new VoltageToPower();
                int capacity = toPower.getBatteryCapacity(voltage / 1000);
                double time = toPower.canUsedTime(voltage, false);
                RxLogUtils.d("capacity:" + capacity + "time：" + time);
                mTvClothingUseTime.setText(capacity + "");
                mTvClothingStandbyTime.setText(RxFormatValue.fromat4S5R(time / 24, 1));
            }
        });
    }

    private void deleteDeviceById(final int position) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.removeBind(beanList.get(position % beanList.size()).getGid()))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        //添加绑定设备，这里实在不会
                        if (position == 0) {
                            //删除绑定
                            new QNBleTools().disConnectDevice(SPUtils.getString(SPKey.SP_scaleMAC));
                            SPUtils.remove(SPKey.SP_scaleMAC);
                        } else {
                            SPUtils.remove(SPKey.SP_clothingMAC);
                            BleTools.getInstance().disConnect();
                        }
                        initData();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }
}
