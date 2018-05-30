package lab.wesmartclothing.wefit.flyso.base;


import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;

/**
 * Created by 华 on 2017/5/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;

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
        //屏幕沉浸
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
        mContext = this;
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
    }

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
