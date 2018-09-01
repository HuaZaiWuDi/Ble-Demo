package lab.wesmartclothing.wefit.flyso.utils.jpush;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;

import com.vondear.rxtools.utils.SPUtils;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created by jk on 2018/7/9.
 */
public class JPushUtils {
    private static Application mApplication;
    private static int sequence = 0;  // 用户自定义的操作序列号,同操作结果一起返回，用来标识一次操作的唯一性。
    private static Set<String> tags = new HashSet<>();

    public static void init(Application application) {
        mApplication = application;
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(application);

        setAliasOrTags(null);
    }


    public static void setAliasOrTags(String... tag) {
        if (tag != null)
            for (String t : tag) {
                tags.add(t);
            }
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
        tagAliasBean.tags = tags;
        tagAliasBean.isAliasAction = SPUtils.getBoolean(SPKey.SP_PUSH_ALIAS);
        tagAliasBean.alias = SPUtils.getString(SPKey.SP_UserId);
        TagAliasOperatorHelper.getInstance().handleAction(mApplication, sequence, tagAliasBean);
    }


    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic(Activity activity) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(activity);
        builder.statusBarDrawable = R.mipmap.app_name;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }


    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom(Activity activity, int layout) {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(activity, layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.app_name;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }


}
