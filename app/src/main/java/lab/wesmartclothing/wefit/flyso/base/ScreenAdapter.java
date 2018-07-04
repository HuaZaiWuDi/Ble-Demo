package lab.wesmartclothing.wefit.flyso.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created icon_hide_password jk on 2018/6/29.
 */
public class ScreenAdapter {

    private static float myDensity;
    private static float myScaledDensity;

    private static Application application;

    public static void init(@NonNull final Application application) {
        ScreenAdapter.application = application;
    }


    public static void setCustomDensity(@NonNull Activity activity) {
        if (application == null) {
            throw new NullPointerException("application not init");
        }
        final DisplayMetrics metrics = application.getResources().getDisplayMetrics();

        if (myDensity == 0) {
            myDensity = metrics.density;
            myScaledDensity = metrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        myScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDensity = metrics.widthPixels / 360;//这里表示的是，设计图纸宽度为360dp。
        final float targetScaleDensity = targetDensity * (myScaledDensity / myDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        metrics.density = targetDensity;
        metrics.scaledDensity = targetScaleDensity;
        metrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityMetrics = activity.getResources().getDisplayMetrics();
        activityMetrics.density = targetDensity;
        activityMetrics.scaledDensity = targetDensity;
        activityMetrics.densityDpi = targetDensityDpi;
    }

}
