package lab.wesmartclothing.wefit.flyso.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.TextView;

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
import com.vondear.rxtools.aboutByte.BitUtils;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.interfaces.onRequestPermissionsListener;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxPermissionsUtils;
import com.vondear.rxtools.utils.RxSystemBroadcastUtil;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.yolanda.health.qnblesdk.listener.QNDataListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.zchu.rxcache.RxCache;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.Receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleHistoryData;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleUnsteadyWeight;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SportingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
import lab.wesmartclothing.wefit.flyso.view.AboutUpdateDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@EService
public class BleService extends Service {
    int packageCounts = 0;
    long lastTime = 0;
    static boolean isFirst = true;
    private List<HeartRateBean.AthlList> athlHistoryRecord = new ArrayList<>();//需要上传的心率数据
    private List<Integer> athlRecord_2 = new ArrayList<>();//实时心率

    public static String firmwareVersion = "";

    private boolean dfuStarting = false; //DFU升级时候需要断连重连，防止升级时做其他操作，导致升级失败

    private Map<String, Object> connectDevices = new HashMap<>();
    private static final int heartDeviation = 5;//心率误差值
    private static int heartSupplement = (int) (Math.random() * 3 + 2);//补差值：修改补差值为2-5的随机数

    public static boolean clothingFinish = true;

    QNBleTools mQNBleTools = QNBleTools.getInstance();

    HeartRateBean mHeartRateBean = new HeartRateBean();


    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void bluetoothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
            BleTools.getInstance().stopScanByM();
        } else if (state == BluetoothAdapter.STATE_ON) {
            initBle();
        }
    }

    @Receiver(actions = RxSystemBroadcastUtil.SCREEN_ON)
    void screenOn() {
        RxLogUtils.d("亮屏");
        initBle();
    }

    @Receiver(actions = RxSystemBroadcastUtil.SCREEN_OFF)
    void screenOff() {
        RxLogUtils.d("息屏");
        BleTools.getInstance().stopScanByM();
    }


//    //DFU升级会断开连接，这个时候不要判断版本数据
//    @Receiver(actions = BleKey.ACTION_DFU_STARTING)
//    void dfuStarting(@Receiver.Extra(BleKey.EXTRA_DFU_STARTING) boolean state) {
//        dfuStarting = state;
//        if (!dfuStarting) {
//            openConnect();
//        }
//    }

    //监听系统蓝牙开启
    @Receiver(actions = Key.ACTION_CLOTHING_STOP)
    void CLOTHING_STOP() {
        clothingFinish = true;
        BleAPI.clothingStop();
        shopSporting();
    }

    public BleService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initHeartRate();
        connectScaleCallBack();
    }


    @Override
    public void onDestroy() {
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
        RxPermissionsUtils.requestLoaction(this, new onRequestPermissionsListener() {
            @Override
            public void onRequestBefore() {
                RxLogUtils.d("验证权限");
                RxToast.warning(getString(R.string.open_loaction));
            }

            @Override
            public void onRequestLater() {
                initBle();
            }
        });

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
                RxBus.getInstance().post(new ScaleUnsteadyWeight(v));
                RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), WeightAddFragment.class);
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
                RxLogUtils.e("稳定体重");
                RxBus.getInstance().post(qnScaleData);
                RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), WeightAddFragment.class);
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
                deviceLink.setFirmwareVersion(firmwareVersion);
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
//                shopSporting();
                sportingStop.startTimer();
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
                    firmwareVersion = data[9] + "." + data[10] + "." + data[11];
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

                long time = ByteUtil.bytesToLongD4(data, 3);

                byte[] bytes = new byte[4];
                bytes[0] = data[3];
                bytes[1] = data[4];
                bytes[2] = data[5];
                bytes[3] = data[6];

                //根据时间去重
                if (lastTime == time) {
                    RxLogUtils.d("表示重复包");
                } else {
                    packageCounts++;
                    RxLogUtils.d("包序号：" + packageCounts);
                    lastTime = time;
                    int heartRate = data[8] & 0xff;

                    HeartRateBean.AthlList bean = new HeartRateBean.AthlList();
                    bean.setHeartRate(heartRate);
                    bean.setHeartTime(time * 1000);
                    bean.setStepTime(10);

                    athlHistoryRecord.add(bean);
                    //添加本地缓存
                    RxCache.getDefault().save(Key.CACHE_ATHL_RECORD, athlHistoryRecord);
                }

                BleAPI.syncData(bytes);
                synData();
            }
        });
    }

    private int maxHeart = 0;//最大心率
    private int minHeart = 0;//最小心率
    private double kcalTotal = 0;//总卡路里
    private int lastHeartRate = 0;//上一次的心率值
    private int realHeartRate = 0;//真实心率

    private void initHeartRate() {
        BleTools.getInstance().setBleCallBack(new BleCallBack() {
            @Override
            public void onNotify(byte[] data) {
                RxLogUtils.d("蓝牙Notify数据:" + HexUtil.encodeHexStr(data));

                B.broadUpdate(BleService.this, Key.ACTION_HEART_RATE_CHANGED, Key.EXTRA_HEART_RATE_CHANGED, data);
                if (data.length < 17) return;

                int heartRate = realHeartRate = data[8] & 0xff;

                if (lastHeartRate == 0) {
                    lastHeartRate = heartRate;
                }

                if (lastHeartRate - heartRate > heartDeviation) {
                    heartRate = lastHeartRate - heartSupplement;
                } else if (lastHeartRate - heartRate < -heartDeviation) {
                    heartRate = lastHeartRate + heartSupplement;
                }
                lastHeartRate = heartRate;

                long time = ByteUtil.bytesToLongD4(data, 3) * 1000;

                HeartRateBean.AthlList bean = new HeartRateBean.AthlList();
                bean.setHeartRate(heartRate);
                bean.setHeartTime(time);
                bean.setStepTime(2);

                if (BuildConfig.VERSION_TEST) {
                    /**
                     * 现场演示版本：
                     * 实时上传心率数据，最后上传总数据
                     *
                     * */
                    upLoadData(bean);
                }

                athlHistoryRecord.add(bean);
                athlRecord_2.add(heartRate);
                //添加本地缓存
                RxCache.getDefault().save(Key.CACHE_ATHL_RECORD, athlHistoryRecord);


                maxHeart = heartRate > maxHeart ? heartRate : maxHeart;
                minHeart = heartRate < minHeart ? heartRate : minHeart;

                SportsDataTab mSportsDataTab = new SportsDataTab();
                mSportsDataTab.setAthlRecord_2(athlRecord_2);
                mSportsDataTab.setCurHeart(heartRate);
                mSportsDataTab.setMaxHeart(maxHeart);
                mSportsDataTab.setMinHeart(minHeart);
                mSportsDataTab.setRealHeart(realHeartRate);
                mSportsDataTab.setVoltage(ByteUtil.bytesToIntD2(new byte[]{data[15], data[16]}));
                mSportsDataTab.setLightColor((BitUtils.setBitValue(data[17], 7, (byte) 0) & 0xff));
                mSportsDataTab.setTemp((data[10] & 0xff));
                mSportsDataTab.setDuration(athlRecord_2.size() * 2);
                mSportsDataTab.setSteps(ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}));
                mSportsDataTab.setData(data);
                mSportsDataTab.setDate(RxFormat.setFormatDate(ByteUtil.bytesToLongD4(data, 3) * 1000, RxFormat.Date_Date_CH));
                mSportsDataTab.setPower((BitUtils.checkBitValue(data[17], 7)));
                mHeartRateBean.setStepNumber(mSportsDataTab.getSteps());
                //卡路里累加计算
                kcalTotal += HeartRateToKcal.getCalorie(heartRate, 2f / 3600);
                mSportsDataTab.setKcal(kcalTotal);//统一使用卡为基本热量单位
                RxBus.getInstance().post(mSportsDataTab);


                if (BleService.clothingFinish) {
                    final RxDialogSureCancel dialog = new RxDialogSureCancel(RxActivityUtils.currentActivity());
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getTvTitle().setVisibility(View.GONE);
                    dialog.setContent("运动已开始，是否进入运动界面");
                    dialog.setCancel("进入")
                            .setCancelListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), SportingFragment.class);
                                }
                            })
                            .setSure("取消")
                            .setSureListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
                BleService.clothingFinish = false;

            }
        });
    }

    //运动结束
    private void shopSporting() {
        sportingStop.stopTimer();
        lastHeartRate = 0;
        athlRecord_2.clear();
        if (BuildConfig.DEBUG)
            RxToast.success("运动结束");
        if (athlHistoryRecord.size() > 0) {
            mHeartRateBean.setAthlList(athlHistoryRecord);
            mHeartRateBean.saveHeartRate(mHeartRateBean);
            athlHistoryRecord.clear();
        }
    }

    private void upLoadData(HeartRateBean.AthlList bean) {
        ArrayList<HeartRateBean.AthlList> list = new ArrayList<>();
        list.add(bean);
        mHeartRateBean.setAthlList(list);
        mHeartRateBean.saveHeartRate(mHeartRateBean);
    }


    MyTimer sportingStop = new MyTimer(new MyTimerListener() {
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

                            final RxDialogSureCancel dialog = new RxDialogSureCancel(RxActivityUtils.currentActivity());
                            TextView tvTitle = dialog.getTvTitle();
                            tvTitle.setVisibility(View.VISIBLE);
                            tvTitle.setText("固件升级");
                            dialog.getTvContent().setText("是否升级到最新的版本");
                            dialog.setCancel("升级");
                            dialog.setCancelListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    AboutUpdateDialog updatedialog = new AboutUpdateDialog(RxActivityUtils.currentActivity(), firmwareVersionUpdate.getFileUrl(), firmwareVersionUpdate.getMustUpgrade() == 0);
                                    updatedialog.show();
                                }
                            });
                            dialog.setSure(firmwareVersionUpdate.getMustUpgrade() != 0 ? "退出" : "取消");
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setSureListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    if (firmwareVersionUpdate.getMustUpgrade() != 0) {
                                        RxActivityUtils.AppExit(RxActivityUtils.currentActivity());
                                    }
                                }
                            });
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
