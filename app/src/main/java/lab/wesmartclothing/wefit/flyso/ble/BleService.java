package lab.wesmartclothing.wefit.flyso.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.view.View;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.google.gson.JsonObject;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleScanConfig;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.listener.BleCallBack;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.listener.BleOpenNotifyCallBack;
import com.smartclothing.blelibrary.listener.SynDataCallBack;
import com.smartclothing.blelibrary.scanner.ScanCallback;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxSystemBroadcastUtil;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.yolanda.health.qnblesdk.listener.QNDataListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleHistoryData;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.PlanSportingActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportingActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.view.AboutUpdateDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BleService extends Service {
    static boolean isFirst = true;//固件升级检查弹窗提示


    private boolean dfuStarting = false; //DFU升级时候需要断连重连，防止升级时做其他操作，导致升级失败

    private Map<String, Object> connectDevices = new HashMap<>();

    public static boolean clothingFinish = true;
    QNBleTools mQNBleTools = QNBleTools.getInstance();

    private HeartRateUtil mHeartRateUtil = new HeartRateUtil();

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        BleTools.getInstance().stopScanByM();
                    } else if (state == BluetoothAdapter.STATE_ON) {
                        initBle();
                    }

                    break;
                case RxSystemBroadcastUtil.SCREEN_ON:
                    RxLogUtils.d("亮屏");
                    initBle();
                    break;
                case RxSystemBroadcastUtil.SCREEN_OFF:
                    RxLogUtils.d("息屏");
                    BleTools.getInstance().stopScanByM();
                    break;
                case Key.ACTION_CLOTHING_STOP:
                    clothingFinish = true;
                    BleAPI.clothingStop();
                    shopSporting();
                    break;
            }
        }
    };


    public BleService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initHeartRate();
        connectScaleCallBack();
        initBroadcast();

    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(RxSystemBroadcastUtil.SCREEN_ON);
        filter.addAction(RxSystemBroadcastUtil.SCREEN_OFF);
        filter.addAction(Key.ACTION_CLOTHING_STOP);
        registerReceiver(mBroadcastReceiver, filter);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        BleTools.getInstance().disConnect();
        if (BuildConfig.DEBUG) {
            RefWatcher refWatcher = LeakCanary.installedRefWatcher();
            // We expect schrodingerCat to be gone soon (or not), let's watch it.
            refWatcher.watch(this);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BleTools.getBleManager().isBlueEnable())
            initBle();

        return super.onStartCommand(intent, flags, startId);
    }

    private void initBle() {
        BleScanConfig config = new BleScanConfig.Builder()
                .setServiceUuids(BleKey.UUID_QN_SCALE, BleKey.UUID_Servie)
//                .setDeviceName(true, BleKey.ScaleName, BleKey.Smart_Clothing)
                .setScanTimeOut(0)
                .build();
        BleTools.getInstance().startScan(config, new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, com.smartclothing.blelibrary.scanner.ScanResult result) {
                super.onScanResult(callbackType, result);
                RxLogUtils.d("扫描扫描结果：" + result.toString());
                BluetoothDevice device = result.getDevice();

                if (BleContainsUUID(result, BleKey.UUID_QN_SCALE)) {
//                        RxLogUtils.d("扫描到体脂称：" + device.getAddress());
                    QNBleDevice bleDevice = mQNBleTools.bleDevice2QNDevice(result);
                    RxBus.getInstance().post(bleDevice);
                    if (device.getAddress().equals(SPUtils.getString(SPKey.SP_scaleMAC)) &&
                            mQNBleTools.getConnectState() == QNBleTools.QN_DISCONNECED &&
                            !connectDevices.containsKey(bleDevice.getMac())) {//判断是否正在连接，或者已经连接则不在连接
                        mQNBleTools.connectDevice(bleDevice);
                        mQNBleTools.setDevice(bleDevice);
                        connectDevices.put(bleDevice.getMac(), bleDevice);
                    }
                } else if (BleContainsUUID(result, BleKey.UUID_Servie)) {
                    BleDevice bleDevice = new BleDevice(device);//转换对象
//                        RxLogUtils.d("扫描到瘦身衣：" + device.getAddress());
                    if (device.getAddress().equals(SPUtils.getString(SPKey.SP_clothingMAC)) &&
                            !BleTools.getInstance().connectedState() &&
                            !connectDevices.containsKey(bleDevice.getMac())) {//判断是否正在连接，或者已经连接则不在连接
                        connectClothing(bleDevice);
                        connectDevices.put(bleDevice.getMac(), bleDevice);
                    }
                    RxBus.getInstance().post(bleDevice);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                RxLogUtils.d("扫描失败：" + errorCode);
                if (errorCode == ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED || errorCode == ScanCallback.SCAN_FAILED_INTERNAL_ERROR) {
                    initBle();
                }
            }

            @Override
            public void onBatchScanResults(List<com.smartclothing.blelibrary.scanner.ScanResult> results) {
                super.onBatchScanResults(results);
                for (int i = 0; i < results.size(); i++) {
//                    RxLogUtils.d("扫描扫描结果：" + results.get(0).toString());
                    com.smartclothing.blelibrary.scanner.ScanResult result = results.get(0);
                    BluetoothDevice device = result.getDevice();

                    if (BleContainsUUID(result, BleKey.UUID_QN_SCALE)) {
//                        RxLogUtils.d("扫描到体脂称：" + device.getAddress());
                        QNBleDevice bleDevice = mQNBleTools.bleDevice2QNDevice(result);
                        RxBus.getInstance().post(bleDevice);
                        if (device.getAddress().equals(SPUtils.getString(SPKey.SP_scaleMAC)) &&
                                mQNBleTools.getConnectState() == QNBleTools.QN_DISCONNECED &&
                                !connectDevices.containsKey(bleDevice.getMac())) {//判断是否正在连接，或者已经连接则不在连接
                            mQNBleTools.connectDevice(bleDevice);
                            mQNBleTools.setDevice(bleDevice);
                            connectDevices.put(bleDevice.getMac(), bleDevice);
                        }
                    } else if (BleContainsUUID(result, BleKey.UUID_Servie)) {
                        BleDevice bleDevice = new BleDevice(device);//转换对象
//                        RxLogUtils.d("扫描到瘦身衣：" + device.getAddress());
                        if (device.getAddress().equals(SPUtils.getString(SPKey.SP_clothingMAC)) &&
                                !BleTools.getInstance().connectedState() &&
                                !connectDevices.containsKey(bleDevice.getMac())) {//判断是否正在连接，或者已经连接则不在连接
                            connectClothing(bleDevice);
                            connectDevices.put(bleDevice.getMac(), bleDevice);
                        }
                        RxBus.getInstance().post(bleDevice);
                    }
                }
            }
        });
    }

    //通过UUID验证设备类型
    public boolean BleContainsUUID(com.smartclothing.blelibrary.scanner.ScanResult result, String UUID) {
        List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids();
        for (ParcelUuid uuid : uuids) {
            if (UUID.equals(uuid.getUuid().toString())) return true;
        }
        return false;
    }

    private void connectScaleCallBack() {
        /**
         * 2018-10-16
         * 修改逻辑判断有实时数据或者稳定数据时在跳转
         *
         * */
        MyAPP.QNapi.setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
//                RxBus.getInstance().post(new ScaleUnsteadyWeight(v));
                Bundle bundle = new Bundle();
                bundle.putDouble(Key.BUNDLE_WEIGHT_UNSTEADY, v);
                if (!RxActivityUtils.currentActivity().getClass().equals(PlanSportingActivity.class)
                        && !RxActivityUtils.currentActivity().getClass().equals(SportingActivity.class))
                    RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), WeightAddFragment.class, bundle);
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
                RxLogUtils.d("实时的稳定测量数据是否有效：" + Arrays.toString(qnScaleData.getAllItem().toArray()));
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_WEIGHT_QNDATA, MyAPP.getGson().toJson(qnScaleData));
                if (!RxActivityUtils.currentActivity().getClass().equals(PlanSportingActivity.class)
                        && !RxActivityUtils.currentActivity().getClass().equals(SportingActivity.class))
                    RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), WeightAddFragment.class, bundle);
//                RxBus.getInstance().post(qnScaleData);
            }

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, final List<QNScaleStoreData> list) {
                RxLogUtils.d("历史数据：" + list.size());
                RxBus.getInstance().post(new ScaleHistoryData(list));
            }
        });

        MyAPP.QNapi.setBleConnectionChangeListener(new com.yolanda.health.qnblesdk.listener.QNBleConnectionChangeListener() {
            @Override
            public void onConnecting(QNBleDevice qnBleDevice) {
                RxLogUtils.e("正在连接:");
                mQNBleTools.setConnectState(QNBleTools.QN_CONNECTING);
            }

            @Override
            public void onConnected(QNBleDevice qnBleDevice) {
                RxLogUtils.d("连接成功:");
            }

            @Override
            public void onServiceSearchComplete(QNBleDevice qnBleDevice) {
                RxLogUtils.e("服务发现完成:");
                mQNBleTools.setConnectState(QNBleTools.QN_CONNECED);
                B.broadUpdate(BleService.this, Key.ACTION_SCALE_CONNECT, Key.EXTRA_SCALE_CONNECT, true);

                DeviceLink deviceLink = new DeviceLink();
                deviceLink.setMacAddr(qnBleDevice.getMac());
                deviceLink.setDeviceName(getString(R.string.scale));//测试数据
                deviceLink.deviceLink(deviceLink);
            }

            @Override
            public void onDisconnecting(QNBleDevice qnBleDevice) {
                RxLogUtils.e("正在断开连接:");
                mQNBleTools.setConnectState(QNBleTools.QN_DISCONNECTING);
            }

            @Override
            public void onDisconnected(QNBleDevice qnBleDevice) {
                mQNBleTools.setConnectState(QNBleTools.QN_DISCONNECED);
                B.broadUpdate(BleService.this, Key.ACTION_SCALE_CONNECT, Key.EXTRA_SCALE_CONNECT, false);
                RxLogUtils.e("断开连接:");
                connectDevices.remove(qnBleDevice.getMac());
            }

            @Override
            public void onConnectError(QNBleDevice qnBleDevice, int i) {
                mQNBleTools.setConnectState(QNBleTools.QN_DISCONNECED);
                RxLogUtils.d("连接异常：" + i);
                mQNBleTools.disConnectDevice();
//                RxToast.info(getString(R.string.connectError));
                B.broadUpdate(BleService.this, Key.ACTION_SCALE_CONNECT, Key.EXTRA_SCALE_CONNECT, false);
                connectDevices.remove(qnBleDevice.getMac());
            }

            @Override
            public void onScaleStateChange(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("体重秤状态变化:" + i);
                switch (i) {
                    case 5://正在测量
                        break;
                    case 6://正在测量试试体重
                        break;
                    case 7://正在测试生物阻抗
                        break;
                    case 8://正在测试心率
                        break;
                    case 9://测量完成
                        break;
                }
            }
        });
    }


    //连接瘦身衣
    private void connectClothing(BleDevice device) {
        BleTools.getBleManager().connect(device, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                RxLogUtils.e("开始连接瘦身衣：");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                RxLogUtils.d("连接失败：" + exception.toString());
//                RxToast.info(getString(R.string.connectError));
                BleTools.getBleManager().disconnect(bleDevice);
                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, false);
                connectDevices.remove(bleDevice.getMac());
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                RxLogUtils.d("瘦身衣连接成功");

                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, true);
                if (dfuStarting) return;

                //设备统计
                DeviceLink deviceLink = new DeviceLink();
                deviceLink.setMacAddr(bleDevice.getMac());
                deviceLink.setDeviceName(getString(R.string.clothing));//测试数据
                deviceLink.deviceLink(deviceLink);

                BleTools.getInstance().setBleDevice(bleDevice);
                BleTools.getInstance().openNotify(new BleOpenNotifyCallBack() {
                    @Override
                    public void success(boolean isSuccess) {
                        syncBleSetting();
                    }
                });
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                RxLogUtils.d("断开连接");
                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, false);
                connectDevices.remove(device.getMac());
                sportingStopTimer.startTimer();
            }
        });
    }


    /**
     * 流程：同步时间->同步配置->同步历史数据->检查固件版本（仅一次）
     */
    private void syncBleSetting() {
        BleAPI.syncDeviceTime(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("同步时间成功");
                syncSetting();
            }
        });
    }

    private void syncSetting() {
        BleAPI.syncSetting(60, 50, true, new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                RxLogUtils.d("配置参数");
                syncHistoryData();
            }
        });
    }

    private void syncHistoryData() {
        BleAPI.syncDataCount(new BleChartChangeCallBack() {
            @Override
            public void callBack(byte[] data) {
                long packageCount = ByteUtil.bytesToLongD4(data, 3);
                RxLogUtils.d("包总数：" + packageCount);
                if (packageCount > 0) {
                    RxLogUtils.d("开始同步包数据");
//                    synData();
//                    RxToast.info("开始同步本地数据");
                } else RxLogUtils.d("没有数据同步");
                checkVersion();
            }
        });
    }

    private void checkVersion() {
        if (isFirst) {
            isFirst = false;
            BleAPI.readDeviceInfo(new BleChartChangeCallBack() {
                @Override
                public void callBack(byte[] data) {
                    RxLogUtils.d("读设备信息" + HexUtil.encodeHexStr(data));
                    //021309 010203000400050607090a0b0c10111213
                    String firmwareVersion = data[9] + "." + data[10] + "." + data[11];
                    JsonObject object = new JsonObject();
                    object.addProperty("category", data[3]);//设备类型
                    object.addProperty("modelNo", data[4]);//待定
                    object.addProperty("manufacture", ByteUtil.bytesToIntD2(new byte[]{data[5], data[6]}));
                    object.addProperty("hwVersion", ByteUtil.bytesToIntD2(new byte[]{data[7], data[8]}));
                    object.addProperty("firmwareVersion", firmwareVersion);//当前固件版本
                    checkFirmwareVersion(object);
                }
            });
        }
    }


    private void synData() {
        BleAPI.queryData();
        BleTools.getInstance().setSynDataCallBack(new SynDataCallBack() {
            @Override
            public void data(byte[] data) {
                //400e0642bd135b 0128022303000004e012
                RxLogUtils.d("接收的蓝牙数据包：" + HexUtil.encodeHexStr(data));

                //验证没有数据则同步结束
                if (data.length <= 3) {
                    return;
                }
                byte[] bytes = new byte[4];
                bytes[0] = data[3];
                bytes[1] = data[4];
                bytes[2] = data[5];
                bytes[3] = data[6];

                mHeartRateUtil.addHsitoryData(data);

                BleAPI.syncData(bytes);
                synData();
            }
        });
    }


    private void initHeartRate() {
        BleTools.getInstance().setBleCallBack(new BleCallBack() {
            @Override
            public void onNotify(byte[] data) {
                RxLogUtils.d("蓝牙Notify数据:" + HexUtil.encodeHexStr(data));

                B.broadUpdate(BleService.this, Key.ACTION_HEART_RATE_CHANGED, Key.EXTRA_HEART_RATE_CHANGED, data);
                if (data.length < 17) return;

                mHeartRateUtil.addRealTimeData(data);

            }
        });
    }

    //运动结束
    private void shopSporting() {
        sportingStopTimer.stopTimer();
    }


    MyTimer sportingStopTimer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_STOP);
        }
    }, 10000);


    /**
     * 检测瘦身衣是否需要升级
     *
     * @param object
     */
    private void checkFirmwareVersion(JsonObject object) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getUpgradeInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        final FirmwareVersionUpdate firmwareVersionUpdate = MyAPP.getGson().fromJson(s, FirmwareVersionUpdate.class);
                        if (firmwareVersionUpdate.isHasNewVersion()) {
                            RxLogUtils.d("有最新的版本");
                            RxDialogSureCancel rxDialog = new RxDialogSureCancel(RxActivityUtils.currentActivity())
                                    .setContent("是否升级到最新的固件版本")
                                    .setSure("升级")
                                    .setSureListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AboutUpdateDialog updatedialog = new AboutUpdateDialog(RxActivityUtils.currentActivity(), firmwareVersionUpdate.getFileUrl(), firmwareVersionUpdate.getMustUpgrade() == 0);
                                            updatedialog.show();
                                        }
                                    }).setCancelListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (firmwareVersionUpdate.getMustUpgrade() != 0) {
                                                RxActivityUtils.AppExit(RxActivityUtils.currentActivity());
                                            }
                                        }
                                    });
                            rxDialog.show();
                        } else {
                            RxLogUtils.d("已经是最新的版本");
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
