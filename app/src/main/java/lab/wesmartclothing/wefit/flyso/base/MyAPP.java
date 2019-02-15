package lab.wesmartclothing.wefit.flyso.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.smartclothing.blelibrary.BleTools;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxThreadPoolUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.diskconverter.SerializableDiskConverter;

import java.util.Arrays;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;

/**
 * Created icon_hide_password jk on 2018/5/8.
 */
public class MyAPP extends Application {

    public static QNBleApi QNapi;
    public static Typeface typeface;
    public static AMapLocation aMapLocation = null;//定位信息
    public static GlideImageLoader sImageLoader;

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


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("Myapp" + BuildConfig.DEBUG);
        Log.d("Myapp", BuildConfig.DEBUG + "");
        RxLogUtils.i("启动时长：初始化" + BuildConfig.DEBUG);
        initQN();


        //优化启动速度，把一些没必要立即初始化的操作放到子线程
        new RxThreadPoolUtils(RxThreadPoolUtils.Type.SingleThread, 1).execute(new Runnable() {
            @Override
            public void run() {
                RxUtils.init(MyAPP.this);
                RxLogUtils.setLogSwitch(true);
                //开发版直接使用发布版API
                if (!BuildConfig.DEBUG) {
                    ServiceAPI.switchURL(ServiceAPI.BASE_RELEASE);
                } else {
                    String baseUrl = SPUtils.getString(SPKey.SP_BSER_URL, ServiceAPI.BASE_URL);
                    if (!RxDataUtils.isNullString(baseUrl)) {
                        ServiceAPI.switchURL(baseUrl);
                    }
                }
                MultiDex.install(MyAPP.this);
                initShareLogin();
                initLeakCanary();
                /**
                 * 过滤开发设备
                 *
                 * */
                String[] androidIds = {"171e7dfb5b3005f2", "54409e1a3d1be330"};
                boolean isDevelopmentDevice = BuildConfig.DEBUG && Arrays.asList(androidIds).contains(RxDeviceUtils.getAndroidId());
                RxLogUtils.d("是否是开发设备：" + isDevelopmentDevice);

                Bugly.init(getApplicationContext(), Key.BUGly_id, BuildConfig.DEBUG);
                Bugly.setIsDevelopmentDevice(MyAPP.this, isDevelopmentDevice);

                TextSpeakUtils.init(MyAPP.this);
                MyAPP.typeface = Typeface.createFromAsset(MyAPP.this.getAssets(), "fonts/DIN-Regular.ttf");
                BleTools.initBLE(MyAPP.this);

                try {
                    RxCache.initializeDefault(new RxCache.Builder()
                            .appVersion(2)
                            .diskDir(RxFileUtils.getCecheFolder(MyAPP.this, "Timetofit-cache"))
                            .diskConverter(new SerializableDiskConverter())
                            .diskMax((20 * 1024 * 1024))
                            .memoryMax((20 * 1024 * 1024))
                            .setDebug(false)
                            .build());
                } catch (Exception e) {
                    RxLogUtils.e(e);
                }

                ActivityLifecycle();

                RxLogUtils.i("启动时长：初始化结束");
            }
        });

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


    private void initShareLogin() {
        // init
        ShareConfig config = ShareConfig.instance()
                .qqId(Key.QQ_ID)
                .wxId(Key.WX_ID)
                .weiboId(Key.WEIBO_ID)
                // 下面两个，如果不需要登录功能，可不填写
                .weiboRedirectUrl(Key.WB_URL)
                .wxSecret(Key.WX_SECRET);
        ShareManager.init(config);
    }

    public static RxCache getRxCache() {
        return RxCache.getDefault();
    }


    private void initQN() {
        QNapi = QNBleApi.getInstance(this);
        //加密文件
        String encryptPath = "file:///android_asset/szzskjyxgs2018.qn";

        QNapi.initSdk("szzskjyxgs2018", encryptPath, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                Log.d("轻牛SDK", i + "----初始化文件" + s);
            }
        });
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


    /**
     * oppo (Android4.4.4 , api19) 手机上运行项目,一直闪退 ,
     * 可能是添加MultiDex分包，但未初始化的原因，在Application中重写attachBaseContext函数，对MultiDex初始化即可。
     */
    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);

        RxLogUtils.i("启动时长：开始启动");
        MultiDex.install(base);


    }


}
