package com.smartclothing.module_wefit.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.smartclothing.module_wefit.widget.dialog.DepositDialog;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/*设置*/

public class SetActivity extends AppCompatActivity implements View.OnClickListener {

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

    private void initView() {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("清除缓存？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final QMUITipDialog tipDialog = new QMUITipDialog.Builder(SetActivity.this)
                                    .setTipWord("清除成功")
                                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                    .create();
                            tipDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tipDialog.dismiss();
                                }
                            }, 2000);

                            RxFileUtils.clearAllCache(getApplicationContext());
                            tv_set_data_count.setText("0MB");
                        }
                    }).setNegativeButton(R.string.cancel, null);
            final AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            window.setDimAmount(0.3f);
            window.setWindowAnimations(R.style.dialogWindowAnim);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

        } else if (i == R.id.tv_set_exit) {//退出登录
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                    .setTitle("退出登录？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }
                    }).setNegativeButton(R.string.cancel, null);
            final AlertDialog dialog1 = builder1.create();
            Window window1 = dialog1.getWindow();
            window1.setDimAmount(0.3f);
            window1.setWindowAnimations(R.style.dialogWindowAnim);
            dialog1.show();

        }
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
