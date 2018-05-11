package lab.dxythch.com.commonproject.base;

import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;
import com.tencent.bugly.Bugly;
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
