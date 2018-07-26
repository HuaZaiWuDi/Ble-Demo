package com.smartclothing.module_wefit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.adapter.DeviceRvAdapter;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.smartclothing.module_wefit.bean.Device;
import com.smartclothing.module_wefit.tools.VoltageToPower;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/*我的设备*/

public class MyDeviceActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private RecyclerView rv_device;
    //默认加一条添加设备的item
    private DeviceRvAdapter rvAdapter;
    private final List<Device> mDevices = new ArrayList<>();
    private boolean scale_connect = false;

    //体质秤连接状态
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ACTION_SCALE_CONNECT".equals(intent.getAction())) {
                boolean scale_connect = intent.getBooleanExtra("EXTRA_SCALE_CONNECT", false);
                if (rvAdapter != null && rvAdapter.getItem(0) != null) {
                    rvAdapter.getItem(0).setConnect(scale_connect);
                    rvAdapter.notifyItemChanged(0);
                }
            } else if ("ACTION_CLOTHING_CONNECT".equals(intent.getAction())) {
                boolean scale_connect = intent.getBooleanExtra("EXTRA_CLOTHING_CONNECT", false);
                if (rvAdapter != null && rvAdapter.getItem(1) != null) {
                    rvAdapter.getItem(1).setConnect(scale_connect);
                    rvAdapter.notifyItemChanged(1);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device0);
        initView();

        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_SCALE_CONNECT");
        filter.addAction("ACTION_CLOTHING_CONNECT");
        registerReceiver(mBroadcastReceiver, filter);

        scale_connect = getIntent().getBooleanExtra("ACTION_SCALE_CONNECT", false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        rv_device = findViewById(R.id.rv_device);
        initRecyclerView();
        listener();
    }

    private void listener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        rvAdapter.onDestroy();
        super.onDestroy();
    }

    private void initRecyclerView() {
        rvAdapter = new DeviceRvAdapter();
        rv_device.setLayoutManager(new LinearLayoutManager(this));
        rv_device.setAdapter(rvAdapter);
        rvAdapter.setUpdateClickListener(new DeviceRvAdapter.deleteClickListener() {
            @Override
            public void onUpdateClick(final int position) {
                RxLogUtils.d("下标：" + position);
                if (RxUtils.isFastClick(1000)) return;
                final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
                dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
                dialog.getTvContent().setText("确定删除该设备？");
                dialog.setCancel("删除");
                dialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        deleteDeviceById(position);
                    }
                });
                dialog.setSure("取消");
                dialog.show();
                dialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mDevices.add(new Device(0));
        mDevices.add(new Device(0));
        rvAdapter.setNewData(mDevices);
        rvAdapter.setAddDeviceClickListener(new DeviceRvAdapter.addDeviceClickListener() {
            @Override
            public void onAddDeviceClick(int position) {
                //添加绑定设备，这里实在不会
                RxBus.getInstance().post(position == 0 ? BleKey.TYPE_SCALE : BleKey.TYPE_CLOTHING);
            }
        });
    }

    private void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.deviceList())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(s);
                            Type typeList = new TypeToken<List<Device>>() {
                            }.getType();
                            List<Device> beanList = gson.fromJson(jsonObject.getString("list"), typeList);
                            if (beanList.size() == 1) {
                                Device device = beanList.get(0);
                                if (BleKey.TYPE_SCALE.equals(device.getDeviceNo())) {
                                    device.setType(1);
                                    device.setConnect(scale_connect);
                                    rvAdapter.setData(0, device);
                                } else if (BleKey.TYPE_CLOTHING.equals(device.getDeviceNo())) {
                                    device.setType(1);
                                    device.setConnect(BleTools.getInstance().isConnect());
                                    rvAdapter.setData(1, device);
                                }
                            } else {
                                for (int i = 0; i < beanList.size(); i++) {
                                    Device device = beanList.get(i);
                                    if (BleKey.TYPE_SCALE.equals(device.getDeviceNo())) {
                                        device.setType(1);
                                        device.setConnect(scale_connect);
                                        rvAdapter.setData(0, device);
                                    } else if (BleKey.TYPE_CLOTHING.equals(device.getDeviceNo())) {
                                        device.setType(1);
                                        device.setConnect(BleTools.getInstance().isConnect());
                                        rvAdapter.setData(1, device);
                                    }
                                }
                            }

                            BleAPI.getVoltage(new BleChartChangeCallBack() {
                                @Override
                                public void callBack(byte[] data) {
                                    RxLogUtils.d("读电压" + HexUtil.encodeHexStr(data));
                                    int voltage = ByteUtil.bytesToIntD2(new byte[]{data[3], data[4]});
                                    RxLogUtils.d("电压：" + voltage);
                                    VoltageToPower toPower = new VoltageToPower();
                                    int capacity = toPower.getBatteryCapacity(voltage);
                                    double time = toPower.canUsedTime(voltage, false);
                                    RxLogUtils.d("capacity:" + capacity + "time：" + time);
                                    Device item = rvAdapter.getItem(1);
                                    item.setQuantity(capacity);
                                    item.setStandby((int) time);
                                    rvAdapter.notifyItemChanged(1);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void deleteDeviceById(final int position) {
        final Device item = rvAdapter.getItem(position);
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.removeBind(item.getGid()))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        //添加绑定设备，这里实在不会
                        item.setBind(false);
                        rvAdapter.setData(position, new Device(0));

                        if (position == 0) {
                            SPUtils.remove("SP_scaleMAC");

                        } else {
                            SPUtils.remove("SP_clothingMAC");
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
