package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.utils.RxLogUtils;

/**
 * @Package lab.wesmartclothing.wefit.flyso.base
 * @FileName ActivityLifecycleImpl
 * @Date 2018/12/14 14:02
 * @Author JACK
 * @Project Android_WeFit_2.0
 */
public class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

    public static boolean APP_IS_FOREGROUND = true;


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        APP_IS_FOREGROUND = true;
        MobclickAgent.onResume(activity);
        RxLogUtils.d(activity.getComponentName().getClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        MobclickAgent.onPause(activity);
        RxLogUtils.d(activity.getComponentName().getClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        APP_IS_FOREGROUND = false;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
