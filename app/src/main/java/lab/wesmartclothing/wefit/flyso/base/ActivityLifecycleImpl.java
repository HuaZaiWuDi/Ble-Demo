package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.vondear.rxtools.utils.RxLogUtils;

import java.util.List;

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

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                RxLogUtils.d("后台信息：" + ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND);
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
