package lab.wesmartclothing.wefit.flyso.ble;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.CountDownTimer;
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
import com.clj.fastble.utils.HexUtil;
import com.vondear.rxtools.boradcast.B;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleCallBack;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleChartChangeCallBack;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleOpenNotifyCallBack;
import lab.wesmartclothing.wefit.flyso.ble.listener.SynDataCallBack;


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


    public static synchronized BleManager getBleManager() {
        if (bleManager == null) {
            bleManager = BleManager.getInstance();
        }
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
    private byte[] bytes;
    private final int resetCount = 2;    //重连次数
    private int currentCount = 0;          //当前次数
    private static final int timeOut = 3000;          //超时
    private static final int connectTimeout = 15 * 1000;


    public static void initBLE(Application application) {
        bleManager = BleManager.getInstance();
        bleManager.init(application);

        if (!bleManager.isSupportBle()) {
            return;
        }
//        if (!bleManager.isBlueEnable())
//            bleManager.enableBluetooth();
//        bleManager.disableBluetooth();//关闭蓝牙
        bleManager.enableLog(false);//是否开启蓝牙日志
        bleManager.setMaxConnectCount(1);
        bleManager.setOperateTimeout(timeOut);//设置操作超时时间

        bleManager.setReConnectCount(3, timeOut);
        bleManager.setConnectOverTime(connectTimeout);

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


    final CountDownTimer mCountDownTimer = new CountDownTimer(timeOut, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            write(bytes, bleChartChange);
        }

        @Override
        public void onFinish() {
            bleChartChange = null;
        }
    };


    public void write(final byte[] bytes, final BleChartChangeCallBack bleChartChange) {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }

        this.bytes = bytes;
        this.bleChartChange = bleChartChange;

        if (currentCount > resetCount) {
            Log.e(TAG, "写失败--次数：" + currentCount);
            currentCount = 0;
        } else {
            TimeOut.postDelayed(reWrite, timeOut);
            currentCount++;
            getBleManager().write(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {

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
        getBleManager().write(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {
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
        getBleManager().read(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleReadCallback() {
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


    public void openNotify(final BleOpenNotifyCallBack mBleOpenNotifyCallBack) {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        getBleManager().notify(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                Log.d(TAG, "打开通知成功");
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success(true);
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                Log.e(TAG, "打开通知失败:" + exception.toString());
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success(false);
                TimeOut.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openNotify(mBleOpenNotifyCallBack);
                    }
                }, timeOut);
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
//                Log.d(TAG, "蓝牙数据更新:" + HexUtil.encodeHexStr(data));
                //notify数据
                if (mBleCallBack != null && data[2] == 0x07) {
                    mBleCallBack.onNotify(data);
                }

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
        getBleManager().indicate(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleIndicateCallback() {
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
        getBleManager().readRssi(bleDevice, new BleRssiCallback() {
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

    public void stopScan() {
        if (getBleManager().getScanSate() == BleScanState.STATE_SCANNING) {
            Log.d("bleManager", "结束扫描");
            getBleManager().cancelScan();
        }
    }

    public boolean isConnect() {
        if (getBleManager() == null || bleDevice == null) return false;

        return getBleManager().isConnected(bleDevice);
    }

    public boolean connectedState() {
        if (getBleManager() == null || bleDevice == null) return false;
        return getBleManager().getConnectState(bleDevice) == 1 || getBleManager().getConnectState(bleDevice) == 2;
    }

    public void disConnect() {
        if (getBleManager() != null && bleDevice != null)
            getBleManager().disconnect(bleDevice);
    }


    public boolean isBind(String Mac) {
        return BluetoothAdapter.checkBluetoothAddress(Mac);
    }


}
