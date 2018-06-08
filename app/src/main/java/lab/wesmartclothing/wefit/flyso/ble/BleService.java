package lab.wesmartclothing.wefit.flyso.ble;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.qingniu.qnble.scanner.ScanResult;
import com.qingniu.qnble.scanner.c;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

@EService
public class BleService extends Service {

    @Pref
    Prefs_ mPrefs;

    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
        } else if (state == BluetoothAdapter.STATE_ON) {
            initBle();
        }
    }

    public BleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!BleTools.getBleManager().isBlueEnable()) {
            RxToast.warning("未开启蓝牙，无法搜索到蓝牙设备");
            BleTools.getBleManager().enableBluetooth();
        }

        if (!RxLocationUtils.isLocationEnabled(this.getApplicationContext())) {
            RxLogUtils.d("未开启GPS定位");
            RxToast.warning("未开启GPS定位，部分手机可能搜索不到蓝牙设备");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            RxLogUtils.d("验证权限");
            RxToast.warning("未给予定位权限，无法搜索到蓝牙设备");
        }

        initBle();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initBle() {
        BleTools.getInstance().stopScan();
        BleTools.getInstance().configScan(0);
        BleTools.getBleManager().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                RxLogUtils.d("结束扫描：" + scanResultList.size());
            }

            @Override
            public void onScanStarted(boolean success) {
                RxLogUtils.d("开始扫描：");
            }

            @Override
            public void onScanning(BleDevice device) {
                RxLogUtils.d("正在扫描：" + device.getName());
                if ("QN-Scale".equals(device.getName())) {
                    RxLogUtils.d("扫描到秤：" + device.getMac());
                    ScanResult scanResult = new ScanResult(device.getDevice(), new c(device.getScanRecord()), device.getRssi());
                    QNBleDevice bleDevice = new QNBleDevice().getBleDevice(scanResult);
                    RxBus.getInstance().post(bleDevice);
                } else if ("WeSmartCloth".equals(device.getName())) {
                    RxLogUtils.d("扫描到瘦身衣：" + device.getMac());
                    RxBus.getInstance().post(device);
                }

//                if (device.getName().equals("WeSmartCloth")) {
//                    RxLogUtils.d("扫描到瘦身衣：" + device.getMac());
//                    RxBus.getInstance().post(device);
//                }
            }
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
