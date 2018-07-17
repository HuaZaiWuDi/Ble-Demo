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
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.smartclothing.module_wefit.bean.Device;
import com.smartclothing.module_wefit.bean.FirmwareVersionUpdate;
import com.smartclothing.module_wefit.widget.dialog.AboutUpdateDialog;
import com.vondear.rxtools.aboutByte.ByteUtil;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.vondear.rxtools.utils.RxUtils.getContext;

/*关于*/


public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_version, tv_protocol;
    private RecyclerView rv_about;
    private BaseQuickAdapter adapter;
    private String updateURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        initView();

    }


    @Override
    protected void onStart() {
        super.onStart();
        BleAPI.readDeviceInfo(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("读设备信息" + HexUtil.encodeHexStr(data));
                //021309 010203000400050607090a0b0c10111213

                JsonObject object = new JsonObject();
                object.addProperty("category", data[3]);//设备类型
                object.addProperty("modelNo", data[4]);//待定
                object.addProperty("manufacture", ByteUtil.bytesToIntD2(new byte[]{data[5], data[6]}));
                object.addProperty("hwVersion", ByteUtil.bytesToIntD2(new byte[]{data[7], data[8]}));
                object.addProperty("firmwareVersion", data[9] + "." + data[10] + "." + data[11]);//当前固件版本
                checkFirmwareVersion(object);
            }
        });
    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_version = findViewById(R.id.tv_version);
        tv_protocol = findViewById(R.id.tv_protocol);
        //版本号
        tv_version.setText("版本号v" + RxDeviceUtils.getAppVersionName());

        listener();

        initRv();
    }

    private void initRv() {
        rv_about = findViewById(R.id.rv_about);
        rv_about.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<Device, BaseViewHolder>(R.layout.rv_item_about) {
            @Override
            protected void convert(final BaseViewHolder helper, Device item) {

                QMUIRelativeLayout layout_item = helper.getView(R.id.layout_item);
                layout_item.setRadiusAndShadow(QMUIDisplayHelper.dp2px(getContext(), 10),
                        QMUIDisplayHelper.dp2px(getContext(), 10),
                        0.2f);

                helper.setText(R.id.tv_device_title, getString(R.string.clothing));
                String FirmwareVersion = "";
                if (RxDataUtils.isNullString(item.getFirmwareVersion()))
                    FirmwareVersion = "--";
                else FirmwareVersion = item.getFirmwareVersion();
                helper.setText(R.id.tv_device_version, getString(R.string.FirmwareVersion, FirmwareVersion));
                if (RxDataUtils.isNullString(item.getNewFirmwareVersion()))
                    FirmwareVersion = "--";
                else FirmwareVersion = item.getNewFirmwareVersion();
                helper.setText(R.id.tv_device_newVersion, "最新" + getString(R.string.FirmwareVersion, FirmwareVersion));
                TextView update = helper.getView(R.id.tv_about_update);
                update.setEnabled(item.isUpdate());
                update.setBackgroundResource(item.isUpdate() ? R.drawable.update_btn_bg : R.drawable.update_btn_pressed_bg);
                update.setTextColor(getResources().getColor(item.isUpdate() ? R.color.colorTheme : R.color.textHeatColor));
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdateDialog dialog = new AboutUpdateDialog(AboutActivity.this, updateURL, false);
                        //set进度值
                        dialog.show();
                        //后续再做进度判断，如果到达100，
                    }
                });
            }
        };
        adapter.bindToRecyclerView(rv_about);
        adapter.addData(new Device(getString(R.string.clothing)));
    }

    private void listener() {
        iv_back.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
    }


    private void checkFirmwareVersion(final JsonObject object) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getUpgradeInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取固件版本：" + s);

                        FirmwareVersionUpdate firmwareVersionUpdate = new Gson().fromJson(s, FirmwareVersionUpdate.class);
                        if (firmwareVersionUpdate.isHasNewVersion()) {
                            RxLogUtils.d("有最新的版本");
                            updateURL = firmwareVersionUpdate.getFileUrl();
                            Device device = (Device) adapter.getItem(0);
                            device.setFirmwareVersion(object.get("firmwareVersion").getAsString());
                            device.setNewFirmwareVersion(firmwareVersionUpdate.getFirmwareVersion());
                            device.setUpdate(true);
                            adapter.setData(0, device);
                        } else {
                            RxLogUtils.d("已经是最新的版本");
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
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
