package com.smartclothing.module_wefit.base;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.smartclothing.module_wefit.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.StatusBarUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 华 on 2017/5/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Activity mActivity;

    public QMUITipDialog tipDialog;
    private Disposable subscribe;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        //屏幕沉浸
//        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
//        屏幕沉浸
        StatusBarUtils.from(this).setStatusBarColor(getResources().getColor(R.color.colorTheme)).process();

        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        initDialog();
    }

    public void checkLocation(Consumer<Permission> consumer) {
        subscribe = new RxPermissions(this)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(consumer);
    }

    public void checkStorage(Consumer<Permission> consumer) {
        subscribe = new RxPermissions(this)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(consumer);
    }


    private void initDialog() {
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
    }

    public void loadCricle(String img_url, @NonNull ImageView img) {
        Glide.with(mContext)
                .load(img_url)
                .placeholder(R.mipmap.group15)
                .bitmapTransform(new CropCircleTransformation(mContext))//圆角图片
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img);
    }

    public abstract void initView();


    @Override
    protected void onPause() {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null) subscribe.dispose();
        subscribe = null;
        tipDialog.dismiss();
        tipDialog = null;
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }

}
