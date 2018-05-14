package lab.dxythch.com.commonproject.base;

import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;
import com.kitnew.ble.QNApiManager;
import com.kitnew.ble.QNResultCallback;
import com.tencent.bugly.Bugly;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;

import lab.dxythch.com.commonproject.BuildConfig;
import lab.dxythch.com.commonproject.entity.sql.SearchWordTab;
import lab.dxythch.com.netlib.rx.RxManager;

/**
 * Created by jk on 2018/5/8.
 */
public class MyAPP extends Application {

    private String BUGly_id = "11c87579c7";

    @Override
    public void onCreate() {
        super.onCreate();
        RxManager.getInstance().setAPPlication(this);
        RxUtils.init(this);
        Bugly.init(getApplicationContext(), BUGly_id, BuildConfig.DEBUG);
        initDB();
        MultiDex.install(this);

        initQN();
    }

    private void initQN() {
        /**
         * 初始化轻牛SDK,仅在Application中的 onCreate中调用，保证每次app实例都只调用一次。调用这个方法时，尽量要联网
         *
         * @param  AppId 由轻牛所分配的 appId
         * @param  callback 执行结果的回调,轻牛会尽量保证各种情况都会进行回调
         */
        QNApiManager.getApi(getApplicationContext()).initSDK("szzskjyxgs2018", new QNResultCallback() {
            @Override
            public void onCompete(int errorCode) {
                //执行结果，为0则成功，其它则参考api文档的种的错误码
                RxLogUtils.d("轻牛SDK：" + errorCode);
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
