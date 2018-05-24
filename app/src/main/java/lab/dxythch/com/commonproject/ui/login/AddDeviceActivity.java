package lab.dxythch.com.commonproject.ui.login;

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
import com.google.gson.Gson;
import com.smartclothing.blelibrary.BleTools;
import com.tbruyelle.rxpermissions2.Permission;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.listen.QNBleDeviceDiscoveryListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.base.MyAPP;
import lab.dxythch.com.commonproject.ble.QNBleTools;
import lab.dxythch.com.commonproject.entity.BindDeviceBean;
import lab.dxythch.com.commonproject.entity.BindDeviceItem;
import lab.dxythch.com.commonproject.netserivce.RetrofitService;
import lab.dxythch.com.commonproject.view.ScanView;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
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

    private int stepState = 0;
    private BaseQuickAdapter adapter;

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
            //跳转主页

        }
    }

    @Click
    void tv_skip() {
        //跳转主页
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

//        BleTools.getInstance().configScan();
//        BleTools.getBleManager().scan(new BleScanCallback() {
//            @Override
//            public void onScanFinished(List<BleDevice> scanResultList) {
//                RxLogUtils.d("结束扫描：" + scanResultList.size());
//                img_scan.stopAnimation();
//                stepState = scanResultList.size() == 0 ? 3 : 1;
//                initStep(stepState);
//                for (BleDevice device : scanResultList) {
//                    RxLogUtils.d("扫描设备：" + device.getName());
//                    BindDeviceBean bean = new BindDeviceBean(1, device.getName(), false, device.getMac());
//                    isBind(bean);
//                }
//            }
//
//            @Override
//            public void onScanStarted(boolean success) {
//                img_scan.startAnimation();
//                RxLogUtils.d("开始扫描：");
//            }
//
//            @Override
//            public void onScanning(BleDevice bleDevice) {
//            }
//        });


        MyAPP.QNapi.setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
            @Override
            public void onDeviceDiscover(QNBleDevice device) {
                RxLogUtils.v("扫描的设备:" + device.getName());
                BindDeviceBean bean = new BindDeviceBean(0, device.getName(), false, device.getMac());
                isBind(bean);
            }

            @Override
            public void onStartScan() {

            }

            @Override
            public void onStopScan() {

            }
        });
        mQNBleTools.scanBle(15 * 1000);
    }

    @Override
    @AfterViews
    public void initView() {
        initStep(0);
        initRecycler();
        if (BUNDLE_FORCE_BIND)
            tv_skip.setVisibility(View.GONE);


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
                helper.setImageResource(R.id.img_weight, item.getDeivceType() == 0 ? R.mipmap.icon_scale : R.mipmap.icon_ranzhiyi3x);
                helper.setBackgroundRes(R.id.tv_receive, item.isBind() ? R.drawable.round_indicator_bg_blue : R.mipmap.continue_button);
                helper.setText(R.id.tv_receive, item.isBind() ? R.string.bindings : R.string.bind);
                helper.setTextColor(R.id.tv_receive, getResources().getColor(item.isBind() ? R.color.colorTheme : R.color.white));
                helper.setText(R.id.tv_weight_data, item.getDeivceName());
                helper.addOnClickListener(R.id.tv_receive);
                TextView tv_receive = helper.getView(R.id.tv_receive);
                if (item.isBind()) tv_receive.setEnabled(false);
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_receive) {
                    RxLogUtils.d("点击了绑定");
                    bindDevice(position);
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


    private void bindDevice(final int position) {
        final BindDeviceBean item = (BindDeviceBean) adapter.getItem(position);
        if (item != null && !item.isBind()) {
            mBindDeviceItem.setCity("深圳市");
            mBindDeviceItem.setDeviceName(item.getDeivceType() == 0 ? "体脂称" : "瘦身衣");
            mBindDeviceItem.setMacAddr(item.getMac());
        }

        String s = new Gson().toJson(mBindDeviceItem, BindDeviceItem.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addBindDevice(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxLogUtils.d("doOnSubscribe：");
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        RxLogUtils.d("结束：");
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        item.setBind(true);
                        adapter.setData(position, item);
                        initStep(2);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private void isBind(final BindDeviceBean bean) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.isBindDevice(1234567 + ""))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        bean.setBind("true".equals(s));
                        adapter.addData(bean);
                        initStep(1);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


}
