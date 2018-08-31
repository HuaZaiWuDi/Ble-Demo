package lab.wesmartclothing.wefit.flyso.base;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;
import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.smartclothing.blelibrary.BleTools;
import com.tencent.bugly.Bugly;
import com.vondear.rxtools.utils.RxUtils;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.diskconverter.GsonDiskConverter;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.entity.sql.SearchWordTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.JPushUtils;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;

/**
 * Created icon_hide_password jk on 2018/5/8.
 */
public class MyAPP extends Application {

    private String BUGly_id = "11c87579c7";
    public static QNBleApi QNapi;
    private static RxCache rxCache;
    public static Typeface typeface;
    private static Gson sGson;
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
        RxManager.getInstance().setAPPlication(this);
        RxUtils.init(this);
        Bugly.init(getApplicationContext(), BUGly_id, BuildConfig.DEBUG);
        initDB();
        MultiDex.install(this);
        initQN();
        BleTools.initBLE(this);
        initShareLogin();
        ScreenAdapter.init(this);
        JPushUtils.init(this);
        initLeakCanary();
        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/DIN-Regular.ttf");
        TextSpeakUtils.init(this);
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
        if (rxCache == null) {
//            rxCache = RxCache.getDefault();
//
            rxCache = new RxCache.Builder()
                    .appVersion(1)//当版本号改变,缓存路径下存储的所有数据都会被清除掉
                    .diskDir(Environment.getDownloadCacheDirectory())
                    .diskConverter(new GsonDiskConverter())//支持Serializable、Json(GsonDiskConverter)
                    .memoryMax(10 * 1024 * 1024)
                    .diskMax(0)
                    .build();
        }
        return rxCache;
    }

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
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

    private void initDB() {
        Configuration.Builder builder = new Configuration.Builder(this);
        //手动的添加模型类
        builder.addModelClasses(SearchWordTab.class)
                .setDatabaseName("Wefit.db")
                .setDatabaseVersion(1);

        ActiveAndroid.initialize(builder.create());
    }


    /**
     * 内存泄露
     */
    private void initLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }

    /**
     * oppo (Android4.4.4 , api19) 手机上运行项目,一直闪退 ,
     * 可能是添加MultiDex分包，但未初始化的原因，在Application中重写attachBaseContext函数，对MultiDex初始化即可。
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        TextSpeakUtils.getInstance().shutdown();
//        ActiveAndroid.dispose();
    }

}
