package lab.wesmartclothing.wefit.flyso.ble;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qingniu.qnble.scanner.c;
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
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.listen.QNBleConnectionChangeListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.Receiver;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
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
    static boolean isFrist = true;
    private List<Integer> athlRecord = new ArrayList<>();
    private List<Integer> athlRecord_2 = new ArrayList<>();//实时心率

    public static String firmwareVersion = "";

    private boolean dfuStarting = false; //DFU升级时候需要断连重连，防止升级时做其他操作，导致升级失败


    @Bean
    QNBleTools mQNBleTools;
    @Bean
    HeartRateBean mHeartRateBean;
    @Bean
    HeartRateToKcal mHeartRateToKcal;


    //监听系统蓝牙开启
    @Receiver(actions = BluetoothAdapter.ACTION_STATE_CHANGED)
    void blueToothisOpen(@Receiver.Extra(BluetoothAdapter.EXTRA_STATE) int state) {
        if (state == BluetoothAdapter.STATE_OFF) {
        } else if (state == BluetoothAdapter.STATE_ON) {
            initBle();
        }
    }


    //监听系统蓝牙开启
    @Receiver(actions = BleKey.ACTION_DFU_STARTING)
    void dfuStarting(@Receiver.Extra(BleKey.EXTRA_DFU_STARTING) boolean state) {
        dfuStarting = state;
    }

    //监听系统蓝牙开启
    @Receiver(actions = Key.ACTION_CLOTHING_STOP)
    void CLOTHING_STOP() {
        BleAPI.clothingStop();
        if (athlRecord_2.size() > 0) {
            mHeartRateBean.setDuration(athlRecord_2.size() * 2);
            mHeartRateBean.setAthlList(athlRecord_2);
            mHeartRateBean.saveHeartRate(mHeartRateBean, mHeartRateToKcal);
            athlRecord_2.clear();
        }
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
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (!BleTools.getBleManager().isBlueEnable()) {
//            RxToast.warning("未开启蓝牙，无法搜索到蓝牙设备");
//            BleTools.getBleManager().enableBluetooth();
//        }

        if (!RxLocationUtils.isLocationEnabled(this.getApplicationContext())) {
            RxLogUtils.d("未开启GPS定位");
            RxToast.warning(getString(R.string.open_GPS));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            RxLogUtils.d("验证权限");
            RxToast.warning(getString(R.string.open_loaction));
        }

        initBle();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initBle() {

        BleScanConfig config = new BleScanConfig.Builder()
                .setServiceUuids(BleKey.UUID_QN_SCALE, BleKey.UUID_Servie)
                .setScanTimeOut(0)
                .build();
        BleTools.getInstance().startScan(config, new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, com.smartclothing.blelibrary.scanner.ScanResult result) {
                super.onScanResult(callbackType, result);
                RxLogUtils.d("蓝牙扫描：" + result.toString());
                BluetoothDevice device = result.getDevice();

                if (BleContainsUUID(result, BleKey.UUID_QN_SCALE)) {
                    RxLogUtils.d("扫描到体脂称：" + device.getAddress());
                    byte[] bytes = result.getScanRecord().getBytes();
                    RxLogUtils.d("广播数据：" + HexUtil.encodeHexStr(bytes));
                    RxLogUtils.d("广播数据长度：" + bytes.length);

                    byte[] bleBytes = new byte[92];
                    System.arraycopy(bleBytes, 0, bytes, 0, bytes.length);

                    com.qingniu.qnble.scanner.ScanResult scanResult = new com.qingniu.qnble.scanner.ScanResult(device, new c(bleBytes), result.getRssi());
                    QNBleDevice bleDevice = new QNBleDevice().getBleDevice(scanResult);//转换对象
                    RxBus.getInstance().post(bleDevice);
                    if (device.getAddress().equals(SPUtils.getString(SPKey.SP_scaleMAC))) {
                        mQNBleTools.disConnectDevice(bleDevice.getMac());
                        mQNBleTools.connectDevice(bleDevice);
                    }
                } else if (BleContainsUUID(result, BleKey.UUID_Servie)) {
                    BleDevice bleDevice = new BleDevice(device);//转换对象
                    RxLogUtils.d("扫描到瘦身衣：" + device.getAddress());
                    if (device.getAddress().equals(SPUtils.getString(SPKey.SP_clothingMAC)))
                        connectClothing(bleDevice);
                    RxBus.getInstance().post(bleDevice);

                    byte[] scanRecord = result.getScanRecord().getBytes();
                    int b1 = scanRecord[21];
                    int b2 = scanRecord[22];
                    int b3 = scanRecord[23];

                    //版本号
                    firmwareVersion = b1 + "." + b2 + "." + b3;
                    RxLogUtils.d("版本号：" + firmwareVersion);

                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                RxLogUtils.d("扫描失败：" + errorCode);
            }

            @Override
            public void onBatchScanResults(List<com.smartclothing.blelibrary.scanner.ScanResult> results) {
                super.onBatchScanResults(results);
                RxLogUtils.d("扫描扫描结果：" + results.size());
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
        MyAPP.QNapi.setBleConnectionChangeListener(new QNBleConnectionChangeListener() {
            @Override
            public void onConnecting(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onConnected(QNBleDevice qnBleDevice) {
                B.broadUpdate(BleService.this, Key.ACTION_SCALE_CONNECT, Key.EXTRA_SCALE_CONNECT, true);
                RxLogUtils.d("连接:");

                DeviceLink deviceLink = new DeviceLink();
                deviceLink.setMacAddr(qnBleDevice.getMac());
                deviceLink.setDeviceName(getString(R.string.scale));//测试数据
                deviceLink.deviceLink(deviceLink);
            }

            @Override
            public void onServiceSearchComplete(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onDisconnecting(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onDisconnected(QNBleDevice qnBleDevice) {
                B.broadUpdate(BleService.this, Key.ACTION_SCALE_CONNECT, Key.EXTRA_SCALE_CONNECT, false);
                RxLogUtils.e("断开连接:");
                initBle();
            }

            @Override
            public void onConnectError(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("连接异常：" + i);
                mQNBleTools.disConnectDevice(qnBleDevice);
                RxToast.info(getString(R.string.connectError));
                B.broadUpdate(BleService.this, Key.ACTION_SCALE_CONNECT, Key.EXTRA_SCALE_CONNECT, false);
                initBle();
            }

            @Override
            public void onScaleStateChange(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("体重秤状态变化:" + i);
            }
        });
    }


    //连接瘦身衣
    private void connectClothing(BleDevice device) {
        BleTools.getBleManager().connect(device, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                RxLogUtils.d("连接异常：" + exception.toString());
                RxToast.info(getString(R.string.connectError));
                BleTools.getBleManager().disconnect(bleDevice);
                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, false);
                initBle();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                RxLogUtils.d("瘦身衣连接成功");
                if (dfuStarting) return;

                //设备统计
                DeviceLink deviceLink = new DeviceLink();
                deviceLink.setMacAddr(bleDevice.getMac());
                deviceLink.setDeviceName(getString(R.string.clothing));//测试数据
                deviceLink.setFirmwareVersion(firmwareVersion);
                deviceLink.deviceLink(deviceLink);


                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, true);
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
                RxLogUtils.d("断开连接：");
                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, false);
                initBle();
                if (athlRecord_2.size() > 0) {
                    mHeartRateBean.setDuration(athlRecord_2.size() * 2);
                    mHeartRateBean.setAthlList(athlRecord_2);
                    mHeartRateBean.saveHeartRate(mHeartRateBean, mHeartRateToKcal);
                    athlRecord_2.clear();
                }
                if (athlRecord.size() > 0) {
                    mHeartRateBean.setDuration(athlRecord.size() * 10);
                    mHeartRateBean.setAthlList(athlRecord);
                    mHeartRateBean.saveHeartRate(mHeartRateBean, mHeartRateToKcal);
                    athlRecord.clear();
                }
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
        BleAPI.syncSetting(35, 50, 0x00, new BleChartChangeCallBack() {
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
                    synData();
                    RxToast.info("开始同步本地数据");
                    athlRecord.clear();
                } else RxLogUtils.d("没有数据同步");

                checkVersion();
            }
        });
    }

    private void checkVersion() {
        if (isFrist) {
            isFrist = false;
            BleAPI.readDeviceInfo(new BleChartChangeCallBack() {
                @Override
                public void callBack(byte[] data) {
                    RxLogUtils.d("读设备信息" + HexUtil.encodeHexStr(data));
                    //021309 010203000400050607090a0b0c10111213

                    JsonObject object = new JsonObject();
                    object.addProperty("category", data[3]);//设备类型
                    object.addProperty("modelNo", data[4]);//待定
                    object.addProperty("manufacture", com.vondear.rxtools.aboutByte.ByteUtil.bytesToIntD2(new byte[]{data[5], data[6]}));
                    object.addProperty("hwVersion", com.vondear.rxtools.aboutByte.ByteUtil.bytesToIntD2(new byte[]{data[7], data[8]}));
                    object.addProperty("firmwareVersion", data[9] + "." + data[10] + "." + data[11]);//当前固件版本
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
                    RxLogUtils.d("数据同步结束");
                    RxToast.success("同步本地数据成功");
                    if (athlRecord.size() > 0) {
                        mHeartRateBean.setDuration(athlRecord.size() * 10);
                        mHeartRateBean.setAthlList(athlRecord);
                        mHeartRateBean.saveHeartRate(mHeartRateBean, mHeartRateToKcal);
                        athlRecord.clear();
                    }
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

                    athlRecord.add(heartRate);
                }

                BleAPI.syncData(bytes);
                synData();

            }
        });
    }

    private int maxHeart = 0;//最大心率
    private int minHeart = 0;//最小心率

    private void initHeartRate() {
        BleTools.getInstance().setBleCallBack(new BleCallBack() {
            @Override
            public void onNotify(byte[] data) {
                RxLogUtils.d("蓝牙Notify数据:" + HexUtil.encodeHexStr(data));

                B.broadUpdate(BleService.this, Key.ACTION_HEART_RATE_CHANGED, Key.EXTRA_HEART_RATE_CHANGED, data);
                if (data.length < 17) return;

                int heartRate = data[8] & 0xff;
                athlRecord_2.add(heartRate);

                RxLogUtils.d("时间：" + RxFormat.setFormatDate(ByteUtil.bytesToLongD4(data, 3) * 1000, RxFormat.Date_Date_CH) +
                        "----------心率:" + heartRate +
                        "----------温度：" + (data[10] & 0xff) +
                        "----------步数：" + ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}) +
                        "----------电压：" + ByteUtil.bytesToIntD2(new byte[]{data[15], data[16]}));

                maxHeart = heartRate > maxHeart ? heartRate : maxHeart;
                minHeart = heartRate < minHeart ? heartRate : minHeart;

                SportsDataTab dataTab = new SportsDataTab();
                dataTab.setAthlRecord_2(athlRecord_2);
                dataTab.setCurHeart(heartRate);
                dataTab.setMaxHeart(maxHeart);
                dataTab.setMinHeart(minHeart);
                dataTab.setDuration(athlRecord_2.size() * 2);
                dataTab.setSteps(ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}));
                double calorie = mHeartRateToKcal.getCalorie(heartRate, athlRecord_2.size() * 2f / 3600);
                dataTab.setKcal(calorie);
//                    RxLogUtils.d("SportsDataTab：" + dataTab.toString());
                RxBus.getInstance().post(dataTab);

            }
        });
    }

    private void checkFirmwareVersion(JsonObject object) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getUpgradeInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        FirmwareVersionUpdate firmwareVersionUpdate = new Gson().fromJson(s, FirmwareVersionUpdate.class);
                        if (firmwareVersionUpdate.isHasNewVersion()) {
                            RxLogUtils.d("有最新的版本");
                            RxBus.getInstance().post(firmwareVersionUpdate);
                        } else {
                            RxLogUtils.d("已经是最新的版本");
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
