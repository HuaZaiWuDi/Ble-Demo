package com.smartclothing.module_wefit.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.adapter.DeviceRvAdapter;
import com.smartclothing.module_wefit.bean.Device;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.smartclothing.module_wefit.widget.dialog.DepositDialog;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/*我的设备*/

public class MyDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private RecyclerView rv_device;
    private DeviceRvAdapter rvAdapter;
    private DepositDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device0);
        initView();
        initData();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        rv_device = findViewById(R.id.rv_device);
        initRecyclerView();
        listener();
        dialog1 = new DepositDialog(this, "加载中...");
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

    private void initRecyclerView() {
        rvAdapter = new DeviceRvAdapter();
        rv_device.setLayoutManager(new LinearLayoutManager(this));
        rv_device.setAdapter(rvAdapter);
        rvAdapter.setUpdateClickListener(new DeviceRvAdapter.deleteClickListener() {
            @Override
            public void onUpdateClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyDeviceActivity.this)
                        .setTitle("确定删除该设备？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDeviceById(position);
                                dialog1.show();
                            }
                        }).setNegativeButton(R.string.cancel, null);
                final AlertDialog dialog = builder.create();
                Window window = dialog.getWindow();
                window.setDimAmount(0.3f);
                window.setWindowAnimations(R.style.dialogWindowAnim);
                dialog.show();

            }
        });
        //默认加一条添加设备的item
        final List<Device> emptyList = new ArrayList<>();
        final Device device = new Device(0);
        final Device device_2 = new Device(0);
        emptyList.add(device);
        emptyList.add(device_2);
        rvAdapter.setNewData(emptyList);
        rvAdapter.setAddDeviceClickListener(new DeviceRvAdapter.addDeviceClickListener() {
            @Override
            public void onAddDeviceClick() {
                //添加绑定设备，这里实在不会
                RxBus.getInstance().post("ScanBle");
            }
        });
    }

    private void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.deviceList())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        RxLogUtils.d("doOnSubscribe");
                    }
                })
                .doFinally(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                RxLogUtils.d("结束");
                            }
                        })
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
                            if (beanList.size() == 0) {
                                beanList.add(new Device(0));
                                beanList.add(new Device(0));
                            } else if (beanList.size() == 1) {
                                Device device = beanList.get(0);
                                device.setType(1);
                                beanList.set(0, device);
                                beanList.add(new Device(0));
//                                String position = device.getDeviceNo();
//                                beanList.add(Integer.parseInt(position), new Device(1));
                            } else {
                                for (int i = 0; i < beanList.size(); i++) {
                                    Device device = beanList.get(i);
                                    device.setType(1);
                                    beanList.set(i, device);
                                }
                            }

                            if (beanList.size() > 0)
                                rvAdapter.setNewData(beanList);
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
                        try {
                            JSONObject obj = new JSONObject(s);
                            RxToast.showToast(obj.getString("msg"));
                            //添加绑定设备，这里实在不会
                            RxBus.getInstance().post(item);
                            rvAdapter.setData(position, new Device(0));
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

}
