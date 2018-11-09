package lab.wesmartclothing.wefit.flyso.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.StatusBarUtils;

import butterknife.ButterKnife;
import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;
import lab.wesmartclothing.wefit.netlib.utils.LifeCycleEvent;

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

        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(statusBarColor())
                .setLightStatusBar(statusBarColor() != ContextCompat.getColor(mContext, R.color.Gray))
                .process();


        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);
        initDialog();

        if (layoutId() != 0) {
            setContentView(layoutId());
            ButterKnife.bind(this);
            initViews();
            if (getIntent().getExtras() != null)
                initBundle(getIntent().getExtras());
            initRxBus2();
            initNetData();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null)
            initBundle(intent.getExtras());
    }

    protected @ColorInt
    int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.Gray);
    }

    protected @LayoutRes
    int layoutId() {
        return 0;
    }


    //初始化Bundle数据
    protected void initBundle(Bundle bundle) {

    }

    //初始化布局逻辑
    protected void initViews() {

    }

    //初始化网络数据
    protected void initNetData() {

    }

    protected void initRxBus2() {

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

//    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
//    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
//    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
//
//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        View view = null;
//        if (LAYOUT_FRAMELAYOUT.equals(name)) {
//            view = new AutoFrameLayout(context, attrs);
//        }
//
//        if (LAYOUT_LINEARLAYOUT.equals(name)) {
//            view = new AutoLinearLayout(context, attrs);
//        }
//
//        if (LAYOUT_RELATIVELAYOUT.equals(name)) {
//            view = new AutoRelativeLayout(context, attrs);
//        }
//
//        if (view != null) return view;
//
//        return super.onCreateView(name, context, attrs);
//    }


    @Override
    public void onBackPressed() {
        RxActivityUtils.finishActivity();
    }


}
