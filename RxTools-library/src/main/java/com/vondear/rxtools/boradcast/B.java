package com.vondear.rxtools.boradcast;

import android.content.Context;
import android.content.Intent;

/**
 * 公司名称：DXY鼎芯
 * 项目名称：DXYBle_GM
 * 类描述：广播类
 * 创建人：Jack
 * 创建时间：2017/6/7
 */
public class B {

    private B() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void broadUpdate(Context mContext, final String action) {
        final Intent intent = new Intent(action);
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public static void broadUpdate(Context mContext, final String action, String key, String value) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, value);
        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public static void broadUpdate(Context mContext, final String action, String key, int value) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, value);
        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public static void broadUpdate(Context mContext, final String action, String key, boolean value) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, value);
//        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public static void broadUpdate(Context mContext, final String action, String key, byte value) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, value);
        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public static void broadUpdate(Context mContext, final String action, String key, int[] value) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, value);
        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public static void broadUpdate(Context mContext, final String action, String key, byte[] value) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, value);
        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

}
