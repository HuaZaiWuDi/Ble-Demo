package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.vondear.rxtools.activity.RxActivityUtils;

import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.netutil.utils.LifeCycleEvent;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragmentActivity extends BaseActivity {


    public Context mContext;
    public Activity mActivity;
    protected final BehaviorSubject<LifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
    public TipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(LifeCycleEvent.CREATE);
//        //屏幕沉浸
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mContext = this;
        mActivity = this;
        RxActivityUtils.addActivity(this);

        ScreenAdapter.setCustomDensity(this);
        tipDialog = new TipDialog(mContext);
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

    @Override
    protected void onDestroy() {
        if (tipDialog != null) tipDialog.dismiss();
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }


}
