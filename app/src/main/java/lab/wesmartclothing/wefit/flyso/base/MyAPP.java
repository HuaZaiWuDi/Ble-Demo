package lab.wesmartclothing.wefit.flyso.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxThreadPoolUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.diskconverter.SerializableDiskConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.ble.QNBleManager;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import lab.wesmartclothing.wefit.flyso.utils.soinc.SonicRuntimeImpl;

/**
 * Created icon_hide_password jk on 2018/5/8.
 */
public class MyAPP extends Application {

    public static Typeface typeface;
    public static AMapLocation aMapLocation = null;//定位信息
    private static GlideImageLoader sImageLoader;
    public static MyAPP sMyAPP;
    public static UserInfo gUserInfo;


    //指定全局的上啦刷新，下拉加载的样式
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.Gray, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });

//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }

    //获取进程名字
    private String getCurrentProcessName() {
        String currentProcName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcName = processInfo.processName;
                break;
            }
        }
        return currentProcName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxLogUtils.i("启动时长：初始化" + BuildConfig.DEBUG);
        RxLogUtils.d("当前进程：" + getCurrentProcessName());
        if (!this.getPackageName().equals(getCurrentProcessName())) return;
        QNBleManager.init(this);
        sMyAPP = this;
        initSonic();

        //优化启动速度，把一些没必要立即初始化的操作放到子线程
        new RxThreadPoolUtils(RxThreadPoolUtils.Type.SingleThread, 1).execute(() -> {
            initRxCache();
            RxUtils.init(MyAPP.this);
            RxLogUtils.setLogSwitch(BuildConfig.DEBUG);
            initUrl();
            MultiDex.install(MyAPP.this);
            initUM();
            initLeakCanary();
            initBugly();
            TextSpeakUtils.init(MyAPP.this);
            MyAPP.typeface = Typeface.createFromAsset(MyAPP.this.getAssets(), "fonts/DIN-Regular.ttf");

            ActivityLifecycle();
            RxLogUtils.i("启动时长：初始化结束");
            AdaptationOppo();
        });
    }

    /**
     * 部分OPPO机型 AssetManager.finalize() timed out的修复
     */
    private void AdaptationOppo() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));
        } catch (Throwable e) {
            e.printStackTrace();
            RxLogUtils.e(e);
        }
    }

    private void initSonic() {
        // step 1: 必要时初始化sonic引擎，或者在创建应用程序时进行初始化
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(this), new SonicConfig.Builder().build());
        }
    }

    private void initRxCache() {
        try {
            RxCache.initializeDefault(new RxCache.Builder()
                    .appVersion(2)
                    .diskDir(RxFileUtils.getCecheFolder(MyAPP.this, Key.COMPANY_KEY))
                    .diskConverter(new SerializableDiskConverter())
                    .diskMax((20 * 1024 * 1024))
                    .memoryMax(5 * 1024 * 1024)
                    .setDebug(false)
                    .build());
            RxLogUtils.d("RxCache 缓存框架初始化成功");
        } catch (Exception e) {
            RxLogUtils.e(e);
        }
    }

    private void initBugly() {
        /**
         * 过滤开发设备
         *
         * */
        String[] androidIds = {"171e7dfb5b3005f2", "54409e1a3d1be330"};
        boolean isDevelopmentDevice = BuildConfig.DEBUG && Arrays.asList(androidIds).contains(RxDeviceUtils.getAndroidId());
        RxLogUtils.d("是否是开发设备：" + isDevelopmentDevice);
        if (!isDevelopmentDevice) {
            Bugly.init(getApplicationContext(), Key.BUGLY_KEY, BuildConfig.DEBUG);
        }
    }

    private void initUrl() {
        //开发版直接使用发布版API
        if (BuildConfig.DEBUG) {
            String baseUrl = SPUtils.getString(SPKey.SP_BSER_URL, ServiceAPI.BASE_URL);
            if (!RxDataUtils.isNullString(baseUrl)) {
                ServiceAPI.switchURL(baseUrl);
            }
        } else {
            ServiceAPI.switchURL(ServiceAPI.BASE_RELEASE);
        }
    }

    private void ActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleImpl());
    }


    public static GlideImageLoader getImageLoader() {
        if (sImageLoader == null) {
            sImageLoader = new GlideImageLoader();
        }
        return sImageLoader;
    }


    private void initUM() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(MyAPP.this, Key.UM_KEY, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin(Key.WX_KEY, Key.WX_SECRET);
        PlatformConfig.setQQZone(Key.QQ_KEY, Key.QQ_SECRET);
        PlatformConfig.setSinaWeibo(Key.SINA_KEY, Key.SINA_SECRET, "https://sns.whalecloud.com/sina2/callback");


    }


    /**
     * 内存泄露
     */
    private void initLeakCanary() {
        if (BuildConfig.LeakCanary) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());

            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }


    public static UserInfo getgUserInfo() {
        if (gUserInfo == null) {
            gUserInfo = JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
        }
        return gUserInfo;
    }

    /**
     * oppo (Android4.4.4 , api19) 手机上运行项目,一直闪退 ,
     * 可能是添加MultiDex分包，但未初始化的原因，在Application中重写attachBaseContext函数，对MultiDex初始化即可。
     */
    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        RxLogUtils.i("启动时长：开始启动");
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }


}
