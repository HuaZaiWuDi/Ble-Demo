package lab.wesmartclothing.wefit.flyso.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.utils.RxSystemBroadcastUtil;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.ActivityLifecycleImpl;
import lab.wesmartclothing.wefit.flyso.base.SportInterface;
import lab.wesmartclothing.wefit.flyso.ble.BleAPI;
import lab.wesmartclothing.wefit.flyso.ble.MyBleManager;
import lab.wesmartclothing.wefit.flyso.rxbus.BleStateChangedBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;

import static no.nordicsemi.android.dfu.DfuBaseService.NOTIFICATION_ID;

public class BleService extends Service {

    //Channel ID 必须保证唯一
    public static final String CHANNEL_ID = "lab.wesmartclothing.wefit.flyso.channel";

    public static boolean clothingFinish = true;


    private static boolean isFirstJoin = true;

    public static List<QNScaleStoreData> historyWeightData;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Bundle extras = intent.getExtras();
                    if (extras == null) break;
                    int state = extras.getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        RxBus.getInstance().postSticky(new BleStateChangedBus(false));
                        stopScan();
                    } else if (state == BluetoothAdapter.STATE_ON) {
                        initBle();
                        RxBus.getInstance().postSticky(new BleStateChangedBus(true));
                    }
                    break;
                case RxSystemBroadcastUtil.SCREEN_ON:
                    RxLogUtils.d("亮屏");
//                    initBle();
                    break;
                case RxSystemBroadcastUtil.SCREEN_OFF:
                    RxLogUtils.d("息屏");
                    if (RxActivityUtils.currentActivity() instanceof SportInterface) {
                        RxActivityUtils.skipActivityTop(RxActivityUtils.currentActivity(), RxActivityUtils.currentActivity().getClass());
                    }
//                    stopScan();
                    break;
                case Key.ACTION_CLOTHING_STOP:
                    BleAPI.clothingStop();
                    break;
                case Intent.ACTION_DATE_CHANGED://日期的变化
                    RxLogUtils.d("日期变化");
                    RxBus.getInstance().post(new RefreshSlimming());
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    int workType = RxNetUtils.getNetWorkType(context);
                    if (isFirstJoin) {
                        isFirstJoin = false;
                        if (workType == -1 || workType == 5) {
                            if (ActivityLifecycleImpl.APP_IS_FOREGROUND)
                                RxToast.normal(RxNetUtils.getNetType(workType));
                        }
                    } else {
                        if (ActivityLifecycleImpl.APP_IS_FOREGROUND)
                            RxToast.normal(RxNetUtils.getNetType(workType));
                        if (workType != -1 && workType != 5) {
                            RxBus.getInstance().post(new RefreshSlimming());
                            RxBus.getInstance().post(new RefreshMe());
                        }
                    }
                    if (workType != -1 && workType != 5) {
                        //一分钟之内只执行一次
                        if (!RxUtils.isFastClick(60 * 1000)) {
                            new HeartRateUtil().uploadHeartRate();
                        }
                    }
                    RxLogUtils.d("网络状态：" + workType);
                    break;
            }
        }
    };


    public BleService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcast();

    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(RxSystemBroadcastUtil.SCREEN_ON);
        filter.addAction(RxSystemBroadcastUtil.SCREEN_OFF);
        filter.addAction(Key.ACTION_CLOTHING_STOP);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver, filter);
    }


    @Override
    public void onDestroy() {
        stopScan();
        unregisterReceiver(mBroadcastReceiver);
        if (BuildConfig.LeakCanary) {
            RefWatcher refWatcher = LeakCanary.installedRefWatcher();
            // We expect schrodingerCat to be gone soon (or not), let's watch it.
            refWatcher.watch(this);
        }
        stopForeground(true);
        RxLogUtils.d("【BleService】：onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setForegroundService();
        if (MyBleManager.Companion.getInstance().isBLEEnabled())
            initBle();
        return START_STICKY;
    }

    /**
     * 通过通知启动服务
     */
    public void setForegroundService() {
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //设定的通知渠道名称
        String channelName = getString(R.string.appName);
        //设置通知的重要程度
        //构建通知渠道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("描述");
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.icon_app) //设置通知图标
                .setContentTitle(getString(R.string.tip))//设置通知标题
                .setContentText(getString(R.string.appName) + "正在后台运行")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, builder.build());
    }


    private void initBle() {
        MyBleManager.Companion.getInstance().scanMacAddress();
    }

    private void stopScan() {
//        QNBleManager.getInstance().stopScan();
        MyBleManager.Companion.getInstance().stopScan();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
