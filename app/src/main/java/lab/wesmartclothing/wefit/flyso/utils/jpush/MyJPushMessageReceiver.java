package lab.wesmartclothing.wefit.flyso.utils.jpush;

import android.content.Context;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 鑷畾涔塉Push message 鎺ユ敹鍣?鍖呮嫭鎿嶄綔tag/alias鐨勭粨鏋滆繑鍥?浠呬粎鍖呭惈tag/alias鏂版帴鍙ｉ儴鍒?
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    /**
     * jPushMessage
     * tag相关操作返回的消息结果体,具体参考JPushMessage类的说明。
     */

    //tag增删查改的操作会在此方法中回调结果。
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    //查询某个tag与当前用户的绑定状态的操作会在此方法中回调结果。
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    //alias相关的操作会在此方法中回调结果。
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    //设置手机号码会在此方法中回调结果。
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
