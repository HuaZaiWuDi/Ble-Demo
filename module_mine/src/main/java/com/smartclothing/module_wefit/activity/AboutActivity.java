package com.smartclothing.module_wefit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.adapter.AboutRvAdapter;
import com.smartclothing.module_wefit.bean.AboutDeviceBean;
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

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/*关于*/

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_version, tv_protocol;
    private RecyclerView rv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        initView();

        initData();
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
                            RxLogUtils.e("PP", "beanList'size " + beanList.size());
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

    private void initView() {
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

        final AboutRvAdapter rvAdapter = new AboutRvAdapter(this);

        rv_about.setAdapter(rvAdapter);

        List<AboutDeviceBean> list = new ArrayList<>();
        AboutDeviceBean bean = new AboutDeviceBean("", "", "");
        list.add(bean);
        list.add(bean);
        rvAdapter.setNewData(list);
        rvAdapter.setUpdateClickListener(new AboutRvAdapter.updateClickListener() {
            @Override
            public void onUpdateClick(int position) {
                AboutUpdateDialog dialog = new AboutUpdateDialog(AboutActivity.this, 0.9f);
                //set进度值
                dialog.setProgressWithFloat(100);
                dialog.setTip("升级中，请勿断开连接");
                dialog.show();
                //后续再做进度判断，如果到达100，
//                dialog.dismiss();
                RxToast.showToast("升级成功");

            }
        });
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
