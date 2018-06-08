package com.smartclothing.module_wefit.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.smartclothing.module_wefit.bean.Device;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.smartclothing.module_wefit.widget.dialog.AboutUpdateDialog;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

import static com.vondear.rxtools.utils.RxUtils.getContext;

/*关于*/


public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_version, tv_protocol;
    private RecyclerView rv_about;
    private List<Device> list = new ArrayList<>();
    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        initView();

    }


    @Override
    protected void onStart() {
        super.onStart();
        initData();
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
                            for (int i = 0; i < beanList.size(); i++) {
                                beanList.get(i).setUpdate(true);
                            }
                            if (beanList.size() == 1) {
                                Device device = beanList.get(0);
                                if ("0".equals(device.getDeviceNo())) {
                                    adapter.setData(0, device);
                                } else if ("1".equals(device.getDeviceNo())) {
                                    adapter.setData(1, device);
                                }
                            } else if (beanList.size() == 2) {
                                Device device = beanList.get(0);
                                device.setUpdate(true);
                                adapter.setData(0, device);
                                Device device_1 = beanList.get(1);
                                device.setUpdate(false);
                                adapter.setData(1, device_1);
                            }
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

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_version = findViewById(R.id.tv_version);
        tv_protocol = findViewById(R.id.tv_protocol);
        //版本号
        tv_version.setText("版本号v" + RxDeviceUtils.getAppVersionName());

        RxTextUtils.getBuilder("智裳科技")
                .append("软件服务协议").setForegroundColor(getResources().getColor(R.color.colorTheme))
                .append("和").setForegroundColor(getResources().getColor(R.color.textColor))
                .append("隐私协议").setForegroundColor(getResources().getColor(R.color.colorTheme))
                .into(tv_protocol);

        listener();

        initRv();
    }

    private void initRv() {
        rv_about = findViewById(R.id.rv_about);
        rv_about.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<Device, BaseViewHolder>(R.layout.rv_item_about) {
            @Override
            protected void convert(BaseViewHolder helper, Device item) {
                QMUIRelativeLayout layout_item = helper.getView(R.id.layout_item);
                layout_item.setRadiusAndShadow(QMUIDisplayHelper.dp2px(getContext(), 10),
                        QMUIDisplayHelper.dp2px(getContext(), 10),
                        0.2f);
                helper.setText(R.id.tv_device_title, item.getDeviceName());
                helper.setText(R.id.tv_device_version, getString(R.string.FirmwareVersion, item.getFirmwareVersion()));
                helper.setText(R.id.tv_device_newVersion, "最新" + getString(R.string.FirmwareVersion, item.getNewFirmwareVersion()));
                TextView update = helper.getView(R.id.tv_about_update);
                update.setEnabled(item.isUpdate());
                update.setBackgroundResource(item.isUpdate() ? R.drawable.update_btn_bg : R.drawable.update_btn_pressed_bg);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdateDialog dialog = new AboutUpdateDialog(AboutActivity.this, 0.9f);
                        //set进度值
                        dialog.setProgressWithFloat(100);
                        dialog.setTip("升级中，请勿断开连接");
                        dialog.show();
                        //后续再做进度判断，如果到达100，
                    }
                });
            }
        };
        adapter.bindToRecyclerView(rv_about);
        adapter.addData(new Device(getString(R.string.scale)));
        adapter.addData(new Device(getString(R.string.clothing)));
    }

    private void listener() {
        iv_back.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();
        }
        if (i == R.id.tv_protocol) {
            RxBus.getInstance().post("BUNDLE_WEB_URL");
        }
    }
}
