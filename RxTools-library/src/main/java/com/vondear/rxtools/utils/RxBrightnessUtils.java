package com.vondear.rxtools.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

/**
 * detail: 亮度相关工具类
 * Created by Ttt
 */
public final class RxBrightnessUtils {

    private RxBrightnessUtils() {
    }

    // 日志TAG
    private static final String TAG = RxBrightnessUtils.class.getSimpleName();

    /**
     * 判断是否开启自动调节亮度
     * @return true : 是, false : 否
     */
    public static boolean isAutoBrightnessEnabled() {
        try {
            int mode = Settings.System.getInt(RxUtils.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            return mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception e) {
            RxLogUtils.e(TAG, e);
            return false;
        }
    }

    /**
     * 设置是否开启自动调节亮度
     * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
     * @param enabled true : 打开, false : 关闭
     * @return true : 成功, false : 失败
     */
    public static boolean setAutoBrightnessEnabled(final boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(RxUtils.getContext())) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + RxUtils.getContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RxUtils.getContext().startActivity(intent);
            } catch (Exception e){
                RxLogUtils.e(TAG, e);
            }
            return false;
        }
        try {
            return Settings.System.putInt(RxUtils.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                    enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (Exception e){
            RxLogUtils.e(TAG, e);
        }
        return false;
    }

    /**
     * 获取屏幕亮度
     * @return 屏幕亮度 0-255
     */
    public static int getBrightness() {
        try {
            return Settings.System.getInt(RxUtils.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            RxLogUtils.e(TAG, e);
            return 0;
        }
    }

    /**
     * 设置屏幕亮度
     * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
     * @param brightness 亮度值
     */
    public static boolean setBrightness(@IntRange(from = 0, to = 255) final int brightness) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(RxUtils.getContext())) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + RxUtils.getContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RxUtils.getContext().startActivity(intent);
            } catch (Exception e){
                RxLogUtils.e(TAG, e);
            }
            return false;
        }
        try {
            ContentResolver resolver = RxUtils.getContext().getContentResolver();
            boolean result = Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null);
            return result;
        } catch (Exception e){
            RxLogUtils.e(TAG, e);
        }
        return false;
    }

    /**
     * 设置窗口亮度
     * @param window 窗口
     * @param brightness 亮度值
     */
    public static void setWindowBrightness(@NonNull final Window window, @IntRange(from = 0, to = 255) final int brightness) {
        if (window == null) return;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255f;
        window.setAttributes(lp);
    }

    /**
     * 获取窗口亮度
     * @param window 窗口
     * @return 屏幕亮度 0-255
     */
    public static int getWindowBrightness(final Window window) {
        if (window == null) return -1;
        WindowManager.LayoutParams lp = window.getAttributes();
        float brightness = lp.screenBrightness;
        if (brightness < 0) return getBrightness();
        return (int) (brightness * 255);
    }
}