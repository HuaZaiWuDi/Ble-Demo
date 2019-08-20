package lab.wesmartclothing.wefit.flyso.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.today.step.lib.TodayStepService;
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
import lab.wesmartclothing.wefit.flyso.base.ActivityLifecycleImpl;
import lab.wesmartclothing.wefit.flyso.base.SportInterface;
import lab.wesmartclothing.wefit.flyso.ble.BleAPI;
import lab.wesmartclothing.wefit.flyso.ble.ScannerManager;
import lab.wesmartclothing.wefit.flyso.rxbus.BleStateChangedBus;
import lab.wesmartclothing.wefit.flyso.rxbus.PhoneStepBus;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;

public class BleService extends Service {

    //Channel ID 必须保证唯一
    public static final String CHANNEL_ID = "lab.wesmartclothing.wefit.flyso.channel";

    private static boolean isFirstJoin = true;

    public static List<QNScaleStoreData> historyWeightData;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        stopScan();
                        RxBus.getInstance().postSticky(new BleStateChangedBus(false));
                    } else if (state == BluetoothAdapter.STATE_ON) {
                        ScannerManager.INSTANCE.startScan();
                        RxBus.getInstance().postSticky(new BleStateChangedBus(true));
                    }
                    break;
                case RxSystemBroadcastUtil.SCREEN_ON:
                    RxLogUtils.d("亮屏");
                    break;
                case RxSystemBroadcastUtil.SCREEN_OFF:
                    RxLogUtils.d("息屏");
                    if (RxActivityUtils.currentActivity() instanceof SportInterface) {
                        RxActivityUtils.skipActivityTop(RxActivityUtils.currentActivity(), RxActivityUtils.currentActivity().getClass());
                    }
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
                case TodayStepService.BROADCAST_STEP_UPDATE:
                    //手机步数更新
                    int step = intent.getIntExtra(TodayStepService.EXTRA_STEP_CURRENT, 0);
                    RxLogUtils.d("当前步数：" + step);
                    RxBus.getInstance().post(new PhoneStepBus(step));
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
        filter.addAction(TodayStepService.BROADCAST_STEP_RESET);
        filter.addAction(TodayStepService.BROADCAST_STEP_UPDATE);
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
        RxLogUtils.d("【BleService】：onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    private void stopScan() {
        ScannerManager.INSTANCE.stopScan();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
