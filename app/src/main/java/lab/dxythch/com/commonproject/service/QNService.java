package lab.dxythch.com.commonproject.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;

import com.vondear.rxtools.utils.RxDataUtils;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import lab.dxythch.com.commonproject.prefs.Prefs_;

@EService
public class QNService extends Service {


    public QNService() {
    }


    @Pref
    Prefs_ mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initScanBle();

        String mac = mPrefs.QN_mac().get();
        //验证蓝牙地址是否为空或可用
        if (!RxDataUtils.isNullString(mac) && BluetoothAdapter.checkBluetoothAddress(mac)) {
//            connectQN(mac);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void initScanBle() {


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
