package lab.wesmartclothing.wefit.flyso.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;

import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.qingniu.qnble.scanner.ScanResult;
import com.qingniu.qnble.scanner.c;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.utils.RxLogUtils;
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
                if (device.getMac().equals(mPrefs.scaleIsBind().get())) {
                    RxLogUtils.d("扫描到秤：" + device.getMac());
                    ScanResult scanResult = new ScanResult(device.getDevice(), new c(device.getScanRecord()), device.getRssi());
                    QNBleDevice bleDevice = new QNBleDevice().getBleDevice(scanResult);
                    RxBus.getInstance().post(bleDevice);
                } else if (device.getMac().equals(mPrefs.clothing().get())) {
                    RxLogUtils.d("扫描到瘦身衣：" + device.getMac());
                    RxBus.getInstance().post(device);
                }
            }
        });

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
