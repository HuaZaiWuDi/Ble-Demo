package com.smartclothing.blelibrary;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.smartclothing.blelibrary.listener.BleCallBack;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.listener.BleOpenNotifyCallBack;
import com.smartclothing.blelibrary.listener.StopDataCallBack;
import com.smartclothing.blelibrary.listener.SynDataCallBack;
import com.smartclothing.blelibrary.util.B;


/**
 * 项目名称：BLEPlatformSDKDemo
 * 类描述：
 * 创建人：Jack
 * 创建时间：2018/1/24
 */
public class BleTools {
    private static BleTools bleTools;

    private BleDevice bleDevice;
    private Handler TimeOut = new Handler();
    private static BleManager bleManager;


    private static final String TAG = "【BleTools】";

    //燃脂衣主UUID


    public BleTools() {

    }


    public static BleManager getBleManager() {
        return bleManager;
    }

    public static synchronized BleTools getInstance() {

        if (bleTools == null) {
            bleTools = new BleTools();
        }
        return bleTools;
    }


    private BleChartChangeCallBack bleChartChange;
    private SynDataCallBack mSynDataCallBack;
    private StopDataCallBack mStopDataCallBack;
    private byte[] bytes;
    private final int reWriteCount = 2;    //重连次数
    private int currentCount = 0;          //当前次数
    private final int timeOut = 3000;          //超时


    public static void initBLE(Application application) {
        bleManager = BleManager.getInstance();
        bleManager.init(application);

        if (!bleManager.isSupportBle()) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "设备不支持BLE");
            return;
        }
        if (bleManager.isBlueEnable())
            bleManager.enableBluetooth();
//        bleManager.disableBluetooth();//关闭蓝牙
        bleManager.enableLog(false);//是否开启蓝牙日志
        bleManager.setMaxConnectCount(1);
        bleManager.setOperateTimeout(500000);//设置超时时间
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    private Runnable reWrite = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "重新写");
            write(bytes, bleChartChange);
        }
    };

    public void write(final byte[] bytes, final BleChartChangeCallBack bleChartChange) {
        this.bytes = bytes;
        this.bleChartChange = bleChartChange;

        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }

        TimeOut.postDelayed(reWrite, timeOut);
        if (currentCount > reWriteCount) {
            Log.e(TAG, "写失败--次数：" + currentCount);
            currentCount = 0;
        } else {
            currentCount++;
            bleManager.write(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {

                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {
//                    Log.d(TAG, "写成功" + "【current】" + current + "【total】" + total + "【justWrite】" + HexUtil.encodeHexStr(justWrite));
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    Log.e(TAG, "写失败" + exception.toString());
                }
            });
        }
    }

    //没有响应的写
    private BleCallBack mBleCallBack;

    public void writeNo(byte[] bytes) {
        bleManager.write(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
//                Log.e(TAG, "无响应写成功:" + HexUtil.encodeHexStr(justWrite));
            }

            @Override
            public void onWriteFailure(BleException exception) {
                Log.e(TAG, "写失败");
            }
        });
    }

    public void read() {
        bleManager.read(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleReadCallback() {
            @Override
            public void onReadSuccess(byte[] data) {
                TimeOut.removeCallbacks(reWrite);
                HexUtil.encodeHexStr(data);
                if (bytes[2] == data[2]) {
                    currentCount = 0;
                    Log.d(TAG, "数据正确");
                    if (bleChartChange != null)
                        bleChartChange.callBack(data);
                } else {
                    Log.e(TAG, "数据错误");
                    write(bytes, bleChartChange);
                }
            }

            @Override
            public void onReadFailure(BleException exception) {
                TimeOut.removeCallbacks(reWrite);
                Log.e(TAG, "读失败");
                write(bytes, bleChartChange);
            }
        });
    }

    public void setBleCallBack(BleCallBack bleCallBack) {
        mBleCallBack = bleCallBack;
    }

    public void setSynDataCallBack(SynDataCallBack synDataCallBack) {
        mSynDataCallBack = synDataCallBack;
    }

    public void setStopDataCallBack(StopDataCallBack stopDataCallBack) {
        mStopDataCallBack = stopDataCallBack;
    }

    public void openNotify(final BleOpenNotifyCallBack mBleOpenNotifyCallBack) {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        bleManager.notify(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                Log.d(TAG, "打开通知成功");
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success(true);
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                Log.e(TAG, "打开通知失败:" + exception.toString());
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success(false);
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
//                Log.d(TAG, "蓝牙数据更新:" + HexUtil.encodeHexStr(data));
                //notify数据
                if (mBleCallBack != null && data[2] == 0x07)
                    mBleCallBack.onNotify(data);

                if (data[2] == 0x08) {
                    Log.d(TAG, "蓝牙停止:" + HexUtil.encodeHexStr(data));
                    B.broadUpdate(bleManager.getContext(), "ACTION_CLOTHING_STOP");
                }

                //命令数据
                if (bleChartChange != null) {
                    if (data[2] == bytes[2]) {
                        Log.d(TAG, "蓝牙命令反馈:" + HexUtil.encodeHexStr(data));
                        currentCount = 0;
                        bleChartChange.callBack(data);
                        TimeOut.removeCallbacks(reWrite);
                    }
                }

                //同步数据
                if (mSynDataCallBack != null && data[2] == 0x06) {
                    mSynDataCallBack.data(data);
                }
            }
        });
    }

    public void openIndicate() {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        bleManager.indicate(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleIndicateCallback() {
            @Override
            public void onIndicateSuccess() {
                Log.e(TAG, "打开indicate成功");
            }

            @Override
            public void onIndicateFailure(BleException exception) {
                Log.e(TAG, "打开indicate失败:" + exception.toString());
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                Log.d(TAG, "蓝牙数据更新:" + HexUtil.encodeHexStr(data));
                if (mBleCallBack != null)
                    mBleCallBack.onNotify(data);

                TimeOut.removeCallbacks(reWrite);
                currentCount = 0;
                Log.d(TAG, "数据正确");
                if (bleChartChange != null)
                    bleChartChange.callBack(data);
            }
        });
    }


    public void readRssi() {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        bleManager.readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.e(TAG, "读取蓝牙信号失败:" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.e(TAG, "读取蓝牙信号成功: 【" + rssi + "】");
            }
        });
    }


    public BleDevice mac2Device(String MAC) {
        BleDevice bleDevice = null;
        if (!BluetoothAdapter.checkBluetoothAddress(MAC)) {
            return null;
        }
        BluetoothAdapter bluetoothAdapter = BleManager.getInstance().getBluetoothAdapter();
        if (bluetoothAdapter != null) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(MAC);
            if (device != null) {
                bleDevice = BleManager.getInstance().convertBleDevice(device);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return bleDevice;
    }

    public void configScan(long timeout) {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[]{UUID.fromString(BleKey.UUID_Servie), UUID.fromString(BleKey.QN_SCALE_UUID)})      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, BleKey.ScaleName, BleKey.Smart_Clothing)        // 只扫描指定广播名的设备，可选
//                .setDeviceMac()                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(timeout)  // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();

        bleManager.initScanRule(scanRuleConfig);
    }


    public void configScanByMac(String mac) {
        Log.d("bleManager", "开始扫描");
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[]{UUID.fromString(BleKey.UUID_Servie), UUID.fromString(BleKey.QN_SCALE_UUID)})      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, "QN-Scale")        // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(0)  // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();

        bleManager.initScanRule(scanRuleConfig);
    }

    public void configScanByName(String name) {
        Log.d("bleManager", "开始扫描");
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[]{UUID.fromString(BleKey.UUID_Servie), UUID.fromString(BleKey.QN_SCALE_UUID)})      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, name)        // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(0)  // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();

        bleManager.initScanRule(scanRuleConfig);
    }

    public void stopScan() {
        if (bleManager.getScanSate() == BleScanState.STATE_SCANNING) {
            Log.d("bleManager", "结束扫描");
            bleManager.cancelScan();
        }
    }

    public boolean isConnect() {
        if (bleManager == null || bleDevice == null) return false;
        return bleManager.isConnected(bleDevice);
    }

    public void disConnect() {
        if (bleManager != null && bleDevice != null)
            bleManager.disconnect(bleDevice);
    }

}
