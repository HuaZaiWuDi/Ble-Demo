package com.smartclothing.module_wefit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.smartclothing.module_wefit.widget.dialog.DepositDialog;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/*设置*/

public class SetActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_set_exit, tv_set_data_count;
    private RelativeLayout rl_set_clean;
    private DepositDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set);

        initView();

        String size = RxFileUtils.getTotalCacheSize(getApplicationContext());
        tv_set_data_count.setText(size);

    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        rl_set_clean = findViewById(R.id.rl_set_clean);
        tv_set_exit = findViewById(R.id.tv_set_exit);
        tv_set_data_count = findViewById(R.id.tv_set_data_count);
        listener();
    }

    private void listener() {
        iv_back.setOnClickListener(this);
        rl_set_clean.setOnClickListener(this);
        tv_set_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();

        } else if (i == R.id.rl_set_clean) {//清楚数据
            final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
            dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
            dialog.getTvContent().setText("清除缓存？");
            dialog.setCancel("清除");
            dialog.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    RxFileUtils.clearAllCache(getApplicationContext());
                    tv_set_data_count.setText("0MB");
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
        } else if (i == R.id.tv_set_exit) {//退出登录
            final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
            dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
            dialog.getTvContent().setText("退出登录？");
            dialog.setCancel("退出");
            dialog.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    logout();
                }
            });
            dialog.setSure("取消");
            dialog.setSureListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    //跳转温度设置界面
    public void setTemp(View view) {
        RxBus.getInstance().post("temp");
    }


    private void logout() {
        dialog1 = new DepositDialog(SetActivity.this, "请求中");
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.logout())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        RxLogUtils.d("doOnSubscribe");
                        dialog1.show();
                    }
                })
                .doFinally(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                RxLogUtils.d("结束");
                                dialog1.dismiss();
                            }
                        })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        RxBus.getInstance().post("logout");
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }
}
