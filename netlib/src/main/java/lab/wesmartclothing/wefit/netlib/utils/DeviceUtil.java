package lab.wesmartclothing.wefit.netlib.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import lab.wesmartclothing.wefit.netlib.rx.RxManager;

/**
 * Created by jk on 2018/6/15.
 */
public class DeviceUtil {


    private static Application application = RxManager.getInstance().getApplication();

    /**
     * 获取App版本号
     *
     * @return
     */
    public static int getAppVersionNo() {
        if (application == null) {
            return 0;
        }
        // 获取packagemanager的实例
        PackageManager packageManager = application.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int version = packInfo.versionCode;
        return version;
    }


    /**
     * 获取App版本名称
     *
     * @return
     */
    public static String getAppVersionName() {
        if (application == null) {
            return "";
        }
        // 获取packagemanager的实例
        PackageManager packageManager = application.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 获取ANDROID ID
     *
     * @return
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    public static String getBuildMANUFACTURER() {
        return Build.MANUFACTURER;// samsung 品牌
    }
}
