package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @Package lab.wesmartclothing.wefit.flyso.base
 * @FileName ActivityLifecycleImpl
 * @Date 2018/12/14 14:02
 * @Author JACK
 * @Describe TODO
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
    }

    @Override
    public void onActivityPaused(Activity activity) {

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
