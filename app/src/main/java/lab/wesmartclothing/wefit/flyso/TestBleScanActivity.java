package lab.wesmartclothing.wefit.flyso;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.utils.RxLogUtils;

import java.util.ArrayList;
import java.util.List;

public class TestBleScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ble_scan);

        initBle();

    }


    private void initBle() {

    }


    BluetoothLeScanner scanner;

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bleScan(View v) {
        RxLogUtils.d("开始扫描：");
        BluetoothAdapter adapter = BleTools.getBleManager().getBluetoothAdapter();
        scanner = adapter.getBluetoothLeScanner();
        List<ScanFilter> filters = new ArrayList<>();
        ScanFilter filter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(BleKey.UUID_Servie)).build();
        ScanFilter filter_Scale = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(BleKey.UUID_QN_SCALE)).build();

        filters.add(filter);
        filters.add(filter_Scale);
        scanner.startScan(filters, new ScanSettings.Builder().build(), mScanCallback);
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
            RxLogUtils.d("扫描设备：" + result.toString());
            super.onScanResult(callbackType, result);
        }

        @Override
        public void onScanFailed(int errorCode) {
            RxLogUtils.d("扫描结束：" + errorCode);
            super.onScanFailed(errorCode);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            RxLogUtils.d("扫描设备：" + results.size());
            for (ScanResult result : results) {
                RxLogUtils.d("扫描设备：" + result.toString());
            }
            super.onBatchScanResults(results);
        }
    };


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopScan(View v) {
        RxLogUtils.d("结束扫描：");
        scanner.stopScan(mScanCallback);
    }

}
