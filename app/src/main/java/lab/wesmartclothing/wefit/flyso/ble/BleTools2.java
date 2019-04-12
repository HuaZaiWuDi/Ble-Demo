package lab.wesmartclothing.wefit.flyso.ble;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
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

import java.util.LinkedList;
import java.util.Queue;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleCallBack;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleChartChangeCallBack;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleOpenNotifyCallBack;
import lab.wesmartclothing.wefit.flyso.ble.listener.SynDataCallBack;
import lab.wesmartclothing.wefit.flyso.ble.util.B;
import lab.wesmartclothing.wefit.flyso.ble.util.Request;


/**
 * 项目名称：BLEPlatformSDKDemo
 * 类描述：
 * 创建人：Jack
 * 创建时间：2018/1/24
 */
public class BleTools2 {
    private static final String TAG = "【BleTools】";

    private static BleTools2 bleTools;

    private BleDevice bleDevice;
    private Handler TimeOut = new Handler();
    private static BleManager bleManager;
    private BleQueue mBleQueue = new BleQueue();

    //没有响应的写
    private BleCallBack mBleCallBack;
    private BleOpenNotifyCallBack mBleOpenNotifyCallBack;
    private BleChartChangeCallBack bleChartChange;
    private SynDataCallBack mSynDataCallBack;

    boolean isScanning = false;
    private ScanCallback scanCallback;
    private byte[] bytes;
    private final int timeOut = 3000;          //超时
    private final long interval = 200;          //每条命令之间的间隔


    public static BleManager getBleManager() {
        if (bleManager == null) {
            bleManager = BleManager.getInstance();
        }
        return bleManager;
    }

    public static synchronized BleTools2 getInstance() {
        if (bleTools == null) {
            bleTools = new BleTools2();
        }
        return bleTools;
    }


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


    public void write(final byte[] bytes, final BleChartChangeCallBack bleChartChange) {
        this.bytes = bytes;
        this.bleChartChange = bleChartChange;
        mBleQueue.addRequest(new Request(Request.Type.WRITE));
    }


    private void bleWrite() {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
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

    public void writeNo(byte[] bytes) {
        this.bytes = bytes;
        mBleQueue.addRequest(new Request(Request.Type.WRITE_NO));
    }

    private void writeNoResponse() {
        bleManager.write(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                Log.e(TAG, "无响应写成功:" + HexUtil.encodeHexStr(justWrite));
                mBleQueue.next();
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
                HexUtil.encodeHexStr(data);
                if (bytes[2] == data[2]) {
                    Log.d(TAG, "数据正确");
                    if (bleChartChange != null)
                        bleChartChange.callBack(data);
                } else {
                    Log.e(TAG, "数据错误");
                }
            }

            @Override
            public void onReadFailure(BleException exception) {
                Log.e(TAG, "读失败");
            }
        });
    }

    public void setBleCallBack(BleCallBack bleCallBack) {
        mBleCallBack = bleCallBack;
    }

    public void setSynDataCallBack(SynDataCallBack synDataCallBack) {
        mSynDataCallBack = synDataCallBack;
    }


    public void openNotify() {
        mBleQueue.addRequest(new Request(Request.Type.ENABLE_NOTIFICATIONS));
    }

    private void openBleNotify() {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        bleManager.notify(bleDevice, BleKey.UUID_Servie, BleKey.UUID_CHART_READ_NOTIFY, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                Log.d(TAG, "打开通知成功");
                mBleQueue.next();
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success(true);
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                Log.e(TAG, "打开通知失败:" + exception.toString());
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success(false);
                mBleQueue.next();
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
                if (bleChartChange != null && data[2] == bytes[2]) {
                    Log.d(TAG, "蓝牙命令反馈:" + HexUtil.encodeHexStr(data));
                    bleChartChange.callBack(data);
                    mBleQueue.next();
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


    public void setScanning(boolean scanning) {
        isScanning = scanning;
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

    public boolean connectedState() {
        if (bleManager == null || bleDevice == null) return false;
        return bleManager.getConnectState(bleDevice) == 1 || bleManager.getConnectState(bleDevice) == 2;
    }

    public void disConnect() {
        if (bleManager != null && bleDevice != null)
            bleManager.disconnect(bleDevice);
    }


    public boolean isBind(String Mac) {
        return BluetoothAdapter.checkBluetoothAddress(Mac);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 蓝牙命令队列 先入先出，并且失败重试三次
    ///////////////////////////////////////////////////////////////////////////


    class BleQueue {

        private Queue<Request> mRequestQueue = new LinkedList<>();

        void addRequest(Request request) {
            int oldSize = mRequestQueue.size();
            mRequestQueue.add(request);
            if (mRequestQueue.size() == 1 && oldSize == 0) {
                mCountDownTimer.start();
            }
        }

        void next() {
            mCountDownTimer.cancel();
            TimeOut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runQueue();
                }
            }, interval);//每条命令之间预留时间
        }

        void runQueue() {
            mRequestQueue.poll();
            if (mRequestQueue != null && mRequestQueue.size() > 0) {
                mCountDownTimer.start();
            }
        }

        void cancelAll() {
            mRequestQueue.clear();
            mCountDownTimer.cancel();
        }


        final CountDownTimer mCountDownTimer = new CountDownTimer(timeOut, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Request request = mRequestQueue.peek();
                if (request == null) return;
                if (request.getType() == Request.Type.WRITE_NO) {
                    writeNoResponse();
                } else if (request.getType() == Request.Type.WRITE) {
                    bleWrite();
                } else if (request.getType() == Request.Type.ENABLE_NOTIFICATIONS) {
                    openBleNotify();
                }
                Log.e(TAG, "写失败--次数：" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mBleQueue.next();
            }
        };
    }


}
