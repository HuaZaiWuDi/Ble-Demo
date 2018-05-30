package lab.wesmartclothing.wefit.flyso.ui.login;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.google.gson.Gson;
import com.smartclothing.blelibrary.BleTools;
import com.tbruyelle.rxpermissions2.Permission;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleTools;
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceBean;
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceItem;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity_;
import lab.wesmartclothing.wefit.flyso.view.ScanView;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxNetUtils;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_add_device)
public class AddDeviceActivity extends BaseActivity {

    @ViewById
    ImageView img_working;
    @ViewById
    ImageView img_startUse;
    @ViewById
    ImageView img_bind;
    @ViewById
    ImageView img_back;
    @ViewById
    ScanView img_scan;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_skip;
    @ViewById
    TextView tv_working;
    @ViewById
    TextView tv_startUse;
    @ViewById
    TextView tv_bind;
    @ViewById
    TextView tv_info;
    @ViewById
    View line_working;
    @ViewById
    View line_bind_L;
    @ViewById
    View line_bind_R;
    @ViewById
    View line_use;
    @ViewById
    Button btn_scan;
    @ViewById
    RelativeLayout layout_scan;
    @ViewById
    RelativeLayout layout_bind;
    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    SmoothRefreshLayout refresh;
    @ViewById
    RelativeLayout layout_notDevice;
    @ViewById
    RelativeLayout layout_scan_2;

    @Bean
    BindDeviceItem mBindDeviceItem;

    @Bean
    QNBleTools mQNBleTools;

    @Extra
    boolean BUNDLE_FORCE_BIND;

    @Pref
    Prefs_ mPrefs;

    private int stepState = 0;
    private BaseQuickAdapter adapter;

    private List<BindDeviceItem.deviceList> mDeviceLists = new ArrayList<>();

    @Click
    void back() {
        if (stepState == 3) {
            initStep(0);
        } else
            onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (stepState == 3) {
                initStep(0);
            } else
                onBackPressed();
        return true;
    }

    @Click
    void btn_scan() {
        if (stepState == 0) {
            startScan();
        } else if (stepState == 2) {
            bindDevice();
            //跳转主页
            RxActivityUtils.skipActivityAndFinish(mContext, MainActivity_.class);
        }
    }

    @Click
    void tv_skip() {
        //跳转主页
        RxActivityUtils.skipActivityAndFinish(mContext, MainActivity_.class);
    }

    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
        } else if (state == BluetoothAdapter.STATE_ON) {
            startScan();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void startScan() {
        mDeviceLists.clear();
        if (!BleTools.getBleManager().isBlueEnable())
            BleTools.getBleManager().enableBluetooth();
        checkLocation(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (!permission.granted) {
                    return;
                }
            }
        });

        mQNBleTools.stopScan();
        BleTools.getInstance().stopScan();
        BleTools.getInstance().configScan();
        BleTools.getBleManager().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                RxLogUtils.d("结束扫描：" + scanResultList.size());
                img_scan.stopAnimation();
                stepState = scanResultList.size() == 0 ? 3 : 1;
                initStep(stepState);
                for (BleDevice device : scanResultList) {
                    RxLogUtils.d("扫描设备：" + device.getName());
                }
            }

            @Override
            public void onScanStarted(boolean success) {
                img_scan.startAnimation();
                RxLogUtils.d("开始扫描：");
            }

            @Override
            public void onScanning(BleDevice device) {
                BindDeviceBean bean = new BindDeviceBean(device.getName().contains("QN") ? 0 : 1, device.getName(), false, device.getMac());
                isBind(bean);
            }
        });
    }

    @Override
    @AfterViews
    public void initView() {
        initStep(1);
        initRecycler();
        if (BUNDLE_FORCE_BIND)
            tv_skip.setVisibility(View.GONE);

        isBind(new BindDeviceBean(0, getString(R.string.scale), false, "321"));
        isBind(new BindDeviceBean(0, getString(R.string.scale), false, "321"));
        isBind(new BindDeviceBean(0, getString(R.string.scale), false, "321"));
        isBind(new BindDeviceBean(1, getString(R.string.clothing), false, "321"));
        isBind(new BindDeviceBean(1, getString(R.string.clothing), false, "321"));
        isBind(new BindDeviceBean(1, getString(R.string.clothing), false, "321"));
    }

    @Override
    protected void onDestroy() {
        BleTools.getInstance().stopScan();
        super.onDestroy();
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<BindDeviceBean, BaseViewHolder>(R.layout.item_bind_device) {
            @Override
            protected void convert(BaseViewHolder helper, BindDeviceBean item) {
                TextView tv_receive = helper.getView(R.id.tv_receive);
                if (item.isBind()) tv_receive.setEnabled(false);
                else tv_receive.setEnabled(true);
                helper.setImageResource(R.id.img_weight, item.getDeivceType() == 0 ? R.mipmap.icon_scale : R.mipmap.icon_ranzhiyi3x);
                helper.setBackgroundRes(R.id.tv_receive, item.isBind() ? R.drawable.round_indicator_bg_blue : R.mipmap.continue_button);
                helper.setText(R.id.tv_receive, item.isBind() ? R.string.bindings : R.string.bind);
                helper.setTextColor(R.id.tv_receive, getResources().getColor(item.isBind() ? R.color.colorTheme : R.color.white));
                helper.setText(R.id.tv_weight_data, item.getDeivceName());
                helper.addOnClickListener(R.id.tv_receive);
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_receive) {
                    RxLogUtils.d("点击了绑定");

                    final BindDeviceBean item = (BindDeviceBean) adapter.getItem(position);
                    BindDeviceItem.deviceList deviceList = new BindDeviceItem.deviceList();

                    List<BindDeviceBean> data = adapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getDeivceType() == item.getDeivceType()) {
                            data.get(i).setBind(false);
                            adapter.setData(i, data.get(i));
                        }
                    }
                    item.setBind(true);
                    adapter.setData(position, item);
                    initStep(2);

                }
            }
        });
        adapter.bindToRecyclerView(mRecyclerView);
    }

    private void initStep(int stepState) {
        this.stepState = stepState;
        layout_notDevice.setVisibility(stepState == 3 ? View.VISIBLE : View.GONE);
        tv_title.setTextColor(getResources().getColor(stepState == 0 ? R.color.white : R.color.black));
        img_back.setImageResource(stepState == 0 ? R.mipmap.back_icon : R.mipmap.back);
        if (!BUNDLE_FORCE_BIND)
            tv_skip.setVisibility(stepState == 1 ? View.GONE : View.VISIBLE);
        tv_skip.setTextColor(getResources().getColor(stepState == 0 ? R.color.white : R.color.black));
        tv_info.setTextColor(getResources().getColor(stepState == 0 ? R.color.white : R.color.textHeatColor));
        switch (stepState) {
            case 0:
                layout_scan.setVisibility(View.VISIBLE);
                layout_scan_2.setVisibility(View.VISIBLE);
                layout_bind.setVisibility(View.GONE);
                break;
            case 1:
                layout_scan.setVisibility(View.GONE);
                layout_scan_2.setVisibility(View.GONE);
                layout_bind.setVisibility(View.VISIBLE);
                img_working.setBackgroundResource(R.mipmap.icon_wancheng3x);
                img_bind.setBackgroundResource(R.mipmap.icon_xuanzhe3x);
                line_bind_L.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                line_bind_R.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                tv_bind.setTextColor(getResources().getColor(R.color.textColor));
                btn_scan.setVisibility(View.INVISIBLE);
                break;
            case 2:
                img_startUse.setBackgroundResource(R.mipmap.icon_xuanzhe3x);
                img_bind.setBackgroundResource(R.mipmap.icon_wancheng3x);
                line_use.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                tv_startUse.setTextColor(getResources().getColor(R.color.textColor));
                btn_scan.setVisibility(View.VISIBLE);
                btn_scan.setText(R.string.startUse);
                break;
            case 3:
                layout_scan.setVisibility(View.GONE);
                break;
        }
    }


    private void bindDevice() {
        List<BindDeviceBean> beans = adapter.getData();
        for (BindDeviceBean bean : beans) {
            if (bean.isBind()) {
                BindDeviceItem.deviceList deviceList = new BindDeviceItem.deviceList();
                deviceList.setDeviceName(bean.getDeivceName());
                deviceList.setCity("深圳市");
                deviceList.setMacAddr(bean.getMac());
                mDeviceLists.add(deviceList);
            }
        }

        mBindDeviceItem.setDeviceList(mDeviceLists);

        String s = new Gson().toJson(mBindDeviceItem, BindDeviceItem.class);
        MyAPP.getACache().put(Key.CACHE_BIND_INFO, s);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addBindDevice(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加绑定设备：" + s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void isBind(final BindDeviceBean bean) {
        //网络不可用
        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            String s = MyAPP.getACache().getAsString(Key.CACHE_BIND_INFO);
            if (s != null) {
                BindDeviceItem item = new Gson().fromJson(s, BindDeviceItem.class);
                List<BindDeviceItem.deviceList> deviceList = item.getDeviceList();
                if (deviceList.size() > 0)
                    for (int i = 0; i < deviceList.size(); i++) {
                        if (deviceList.get(i).getDeviceName().equals(bean.getDeivceName())) {
                            bean.setBind(true);
                        }
                    }
            }
            adapter.addData(bean);
            return;
        }

        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.isBindDevice(bean.getMac()))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        if (bean.getDeivceType() == 0) {
                            mPrefs.scaleIsBind().put(Boolean.parseBoolean(s));
                        } else {
                            mPrefs.clothingBind().put(Boolean.parseBoolean(s));
                        }
                        bean.setBind("true".equals(s));
                        adapter.addData(bean);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
