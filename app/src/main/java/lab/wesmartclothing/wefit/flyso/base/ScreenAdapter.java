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

    private static int BaseSize = 360;


    public static void setBaseSize(int baseSize) {
        BaseSize = baseSize;
    }

    public static void setCustomDensity(@NonNull final Application application, @NonNull Activity activity) {
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
        //这里表示的是，设计图纸宽度为360dp。
        final float targetDensity = metrics.widthPixels / BaseSize;
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
