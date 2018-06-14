package lab.wesmartclothing.wefit.flyso.ble;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qingniu.qnble.scanner.ScanResult;
import com.qingniu.qnble.scanner.c;
import com.smartclothing.blelibrary.BleAPI;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.listener.BleCallBack;
import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.listener.SynDataCallBack;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.vondear.rxtools.aboutByte.HexUtil;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
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
    private List<Integer> athlRecord = new ArrayList<>();
    private List<Integer> athlRecord_2 = new ArrayList<>();//实时心率

    public static String firmwareVersion;

    private boolean dfuStarting = false;

    @Pref
    Prefs_ mPrefs;

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

    public BleService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initHeartRate();
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


    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initBle() {

//        BluetoothAdapter adapter = BleTools.getBleManager().getBluetoothAdapter();
//        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
//        List<ScanFilter> filters = new ArrayList<>();
//        ScanFilter filter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(BleKey.UUID_Servie)).build();
//        ScanFilter filter_Scale = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(BleKey.UUID_QN_SCALE)).build();
//
//        filters.add(filter);
//        filters.add(filter_Scale);
//        scanner.startScan(filters, new ScanSettings.Builder().build(), new ScanCallback() {
//            @Override
//            public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
//                super.onScanResult(callbackType, result);
//            }
//        });

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
                RxLogUtils.d("设备的UUID:" + Arrays.toString(device.getDevice().getUuids()));

                RxLogUtils.d("扫描到：" + device.getName());
                if (BleKey.ScaleName.equals(device.getName())) {
                    RxLogUtils.d("扫描到秤：" + device.getMac());
                    ScanResult scanResult = new ScanResult(device.getDevice(), new c(device.getScanRecord()), device.getRssi());
                    QNBleDevice bleDevice = new QNBleDevice().getBleDevice(scanResult);
                    RxBus.getInstance().post(bleDevice);
                    if (device.getMac().equals(mPrefs.scaleIsBind().get())) {
                        mQNBleTools.disConnectDevice(bleDevice.getMac());
                        mQNBleTools.connectDevice(bleDevice);
                    }
                } else if (BleKey.Smart_Clothing.equals(device.getName())) {
                    RxLogUtils.d("扫描到瘦身衣：" + device.getMac());
                    if (device.getMac().equals(mPrefs.clothing().get()))
                        connectClothing(device);

                    byte[] scanRecord = device.getScanRecord();
                    byte b1 = scanRecord[21];
                    byte b2 = scanRecord[22];
                    byte b3 = scanRecord[23];

                    //版本号
                    firmwareVersion = b1 + "-" + b2 + "-" + b3;
                    RxLogUtils.d("版本号：" + firmwareVersion);

                    RxBus.getInstance().post(device);
                }
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
                RxToast.info("连接瘦身衣异常，请开关蓝牙后再连接");
                BleTools.getBleManager().disconnect(bleDevice);
                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, false);
                initBle();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                //设备统计
//                DeviceLink deviceLink = new DeviceLink();
//                deviceLink.setMacAddr(bleDevice.getMac());
//                deviceLink.setDeviceName(getString(R.string.scale));//测试数据
//                deviceLink.deviceLink(deviceLink);

                RxLogUtils.d("瘦身衣连接成功");
                if (dfuStarting) return;

                B.broadUpdate(BleService.this, Key.ACTION_CLOTHING_CONNECT, Key.EXTRA_CLOTHING_CONNECT, true);
                BleTools.getInstance().setBleDevice(bleDevice);
                BleTools.getInstance().openNotify();

                checkFirmwareVersion(0);

                BleAPI.syncDeviceTime(new BleChartChangeCallBack() {
                    @Override
                    public void callBack(byte[] data) {
                        RxLogUtils.d("同步时间成功");

                        BleAPI.syncSetting(35, 50, 0x00, new BleChartChangeCallBack() {
                            @Override
                            public void callBack(byte[] data) {
                                BleAPI.syncDataCount(new BleChartChangeCallBack() {
                                    @Override
                                    public void callBack(byte[] data) {
                                        long packageCount = ByteUtil.bytesToLongD4(data, 3);
                                        RxLogUtils.d("包总数：" + packageCount);

                                        if (packageCount > 0) {
                                            RxLogUtils.d("开始同步包数据");
                                            synData();
                                            athlRecord.clear();
                                        } else RxLogUtils.d("没有数据同步");
                                    }
                                });
                            }
                        });
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
            }
        });
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

    private void initHeartRate() {
        BleTools.getInstance().setBleCallBack(new BleCallBack() {
            @Override
            public void onNotify(byte[] data) {
                RxLogUtils.d("蓝牙Notify数据:" + HexUtil.encodeHexStr(data));
                if (data[2] == 0x08) {
                    BleAPI.clothingStop();
                    if (athlRecord_2.size() > 0) {
                        mHeartRateBean.setDuration(athlRecord_2.size() * 2);
                        mHeartRateBean.setAthlList(athlRecord_2);
                        mHeartRateBean.saveHeartRate(mHeartRateBean, mHeartRateToKcal);
                        athlRecord_2.clear();
                    }
                } else {
                    B.broadUpdate(BleService.this, Key.ACTION_HEART_RATE_CHANGED, Key.EXTRA_HEART_RATE_CHANGED, data);
                    if (data.length < 17) return;

                    int heartRate = data[8] & 0xff;
                    athlRecord_2.add(heartRate);

                    RxLogUtils.d("时间：" + RxFormat.setFormatDate(ByteUtil.bytesToLongD4(data, 3) * 1000, RxFormat.Date_Date_CH) +
                            "----------心率:" + heartRate +
                            "----------温度：" + (data[10] & 0xff) +
                            "----------步数：" + ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}) +
                            "----------电压：" + ByteUtil.bytesToIntD2(new byte[]{data[15], data[16]}));
                }
            }
        });
    }


    private void checkFirmwareVersion(int curVersion) {
        JsonObject object = new JsonObject();
        object.addProperty("category", 1);//设备类型
        object.addProperty("modelNo", 1);//待定
        object.addProperty("hwVersion", curVersion);//当前固件版本

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getUpgradeInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加心率：" + s);

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
