package lab.wesmartclothing.wefit.flyso.base;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import io.reactivex.subjects.BehaviorSubject;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;

/**
 * Created icon_hide_password 华 on 2017/5/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Activity mActivity;
    protected final BehaviorSubject<LifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
    public TipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(LifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        //华为底部虚拟按键遮挡问题
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        //屏幕沉浸
        StatusBarUtils.from(this).setStatusBarColor(getResources().getColor(R.color.white)).process();

        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);
        initDialog();
    }

    @Override
    protected void onStart() {
        lifecycleSubject.onNext(LifeCycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        lifecycleSubject.onNext(LifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        lifecycleSubject.onNext(LifeCycleEvent.RESTART);
        super.onRestart();
    }


    private void initDialog() {
        tipDialog = new TipDialog(mContext);
    }


    public abstract void initView();


    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        tipDialog.dismiss();
        tipDialog = null;
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (LAYOUT_FRAMELAYOUT.equals(name)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (LAYOUT_LINEARLAYOUT.equals(name)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (LAYOUT_RELATIVELAYOUT.equals(name)) {
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
