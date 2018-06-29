package lab.wesmartclothing.wefit.flyso.base;

import android.support.multidex.MultiDex;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;
import com.smartclothing.blelibrary.BleTools;
import com.tencent.bugly.Bugly;
import com.vondear.rxtools.model.cache.ACache;
import com.vondear.rxtools.utils.RxUtils;
import com.yolanda.health.qnblesdk.listen.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.entity.sql.SearchWordTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;

/**
 * Created by jk on 2018/5/8.
 */
public class MyAPP extends Application {

    private String BUGly_id = "11c87579c7";
    public static QNBleApi QNapi;
    private static ACache aCache;

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
        initCache();
        initShareLogin();
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


    private void initCache() {
        aCache = ACache.get(this);
    }

    public static ACache getACache() {
        return aCache;
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


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//        ActiveAndroid.dispose();
    }
}
