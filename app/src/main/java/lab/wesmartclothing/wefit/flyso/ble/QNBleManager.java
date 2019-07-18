package lab.wesmartclothing.wefit.flyso.ble;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.yolanda.health.qnblesdk.listener.QNBleDeviceDiscoveryListener;
import com.yolanda.health.qnblesdk.listener.QNDataListener;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.yolanda.health.qnblesdk.out.QNUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.base.SportInterface;
import lab.wesmartclothing.wefit.flyso.buryingpoint.BuryingPoint;
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleConnectBus;
import lab.wesmartclothing.wefit.flyso.rxbus.ScaleHistoryData;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.BleKey;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;

/**
 * Created icon_hide_password jk on 2018/5/16.
 */

public class QNBleManager {


    public static final int QN_CONNECTING = 0;//正在连接
    public static final int QN_CONNECED = 1;//连接完成，发现服务
    public static final int QN_DISCONNECED = 2;//断开连接
    public static final int QN_DISCONNECTING = 3;//连接失败
    private static QNBleManager mQNBleTools;
    private static QNBleApi QNapi;
    private static QNBleDevice device;

    public static QNBleManager getInstance() {
        if (mQNBleTools == null) {
            mQNBleTools = new QNBleManager();
        }
        return mQNBleTools;
    }

    private QNBleManager() {
        initCallback();
    }

    public static void init(Application application) {
        QNapi = QNBleApi.getInstance(application);
        //加密文件
        String encryptPath = "file:///android_asset/szzskjyxgs2018.qn";

        QNapi.initSdk("szzskjyxgs2018", encryptPath, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                Log.d("轻牛SDK", i + "----初始化文件" + s);
            }
        });
    }

    public static QNBleApi getQNApi() {
        return QNapi;
    }

    private int connectState = 2;

    public int getConnectState() {
        return connectState;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({QN_CONNECTING, QN_CONNECED, QN_DISCONNECED, QN_DISCONNECTING})
    public @interface ConnectState {

    }

    public void setConnectState(@ConnectState int connectState) {
        this.connectState = connectState;
    }


    public QNBleDevice getDevice() {
        return device;
    }


    public void scanBle() {
//        stopScan();
        saveQNConfig();
        QNapi.startBleDeviceDiscovery(mQNResultCallback);
    }

    private void saveQNConfig() {
        QNConfig config = QNapi.getConfig();
        config.setDuration(0);
        config.setOnlyScreenOn(false);
        config.setAllowDuplicates(false);
        config.setScanOutTime(0);
        config.setUnit(0);
        config.setConnectOutTime(30 * 1000);

        config.save((i, s) -> RxLogUtils.d("轻牛SDK ：保存设备配置" + i + "----" + s));
    }


    //蓝牙扫描监听
    QNResultCallback mQNResultCallback = new QNResultCallback() {
        @Override
        public void onResult(int i, String s) {
            RxLogUtils.d("轻牛SDK ：扫描设备" + i + "----" + s);
        }
    };


    public void stopScan() {
        QNapi.stopBleDeviceDiscovery(mQNResultCallback);
    }

    public QNUser createUser() {
        UserInfo info = MyAPP.getgUserInfo();
        String sex = info.getSex() == 1 ? "male" : "female";
        long birthDayMillis = info.getBirthday();
        String userId = SPUtils.getString(SPKey.SP_UserId);
        int height = info.getHeight();

        Date date = new Date(birthDayMillis);
        QNUser qnUser = QNapi.buildUser(userId, height, sex, date, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：创建用户" + i + "----" + s);
            }
        });
        RxLogUtils.d("轻牛SDK ：创建用户：" + qnUser.toString());
        if (device != null)
            BuryingPoint.INSTANCE.scaleState(device.getMac(), "体重用户：身高：" + height + "-性别：" + sex + "-age：" + info.getAge());
        return qnUser;
    }


    public void connectDevice(QNBleDevice qnBleDevice) {
        QNBleManager.device = qnBleDevice;
        QNapi.connectDevice(qnBleDevice, createUser(), new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：连接设备" + i + "----" + s);
            }
        });
    }

    public void disConnectDevice() {
        disConnectDevice(device);
    }

    private void disConnectDevice(QNBleDevice device) {
        QNapi.disconnectDevice(device, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：断开连接" + i + "----" + s);
            }
        });
    }

    public QNBleDevice convert2QNDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
        return QNapi.buildDevice(device, rssi, scanRecord, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：构建BleDevice" + i + "----" + s);
            }
        });
    }

    public boolean isConnect() {
        return connectState == QN_CONNECED;
    }

    public boolean isBind() {
        return BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC));
    }

    private void initCallback() {
        //扫描体脂称
        QNBleManager.getQNApi().setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
            @Override
            public void onDeviceDiscover(QNBleDevice bleDevice) {
                RxLogUtils.d("扫描体脂称：" + bleDevice.getMac() + ":" + bleDevice.getRssi() + ":" + bleDevice.getModeId());
//                BindDeviceBean bindDeviceBean = new BindDeviceBean(BleKey.TYPE_SCALE, bleDevice.getName(), bleDevice.getMac(), bleDevice.getRssi());
//                bindDeviceBean.setQNBleDevice(bleDevice);
//                RxBus.getInstance().post(bindDeviceBean);
//
//                if (bleDevice.getMac().equals(SPUtils.getString(SPKey.SP_scaleMAC))) {//判断是否正在连接，或者已经连接则不在连接
//                    mQNBleTools.connectDevice(bleDevice);
//                    stopScan();
//                }
            }

            @Override
            public void onStartScan() {
                RxLogUtils.d("轻牛SDK ：开始扫描");
            }

            @Override
            public void onStopScan() {
                RxLogUtils.d("轻牛SDK ：停止扫描");
            }

            @Override
            public void onScanFail(int i) {
                RxLogUtils.d("轻牛SDK ：扫描失败");
            }
        });

        /**
         * 2018-10-16
         * 修改逻辑判断有实时数据或者稳定数据时在跳转
         *
         * */
        QNBleManager.getQNApi().setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
                Bundle bundle = new Bundle();
                bundle.putDouble(Key.BUNDLE_WEIGHT_UNSTEADY, v);
                if (RxActivityUtils.currentActivity() instanceof SportInterface) {
                } else {
                    RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), WeightAddFragment.class, bundle);
                }
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
                RxLogUtils.d("实时的稳定测量数据是否有效：" + Arrays.toString(qnScaleData.getAllItem().toArray()));
                String hmac = MyAPP.getgUserInfo().getHmac();

                RxLogUtils.d("HMac:" + hmac);
                qnScaleData.setFatThreshold(hmac, 1.0, new QNResultCallback() {
                    @Override
                    public void onResult(int i, String s) {
                        RxLogUtils.d("设置体脂率误差值：" + s + "---状态码：" + i);
                        BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "体脂率误差值：" + s + "---状态码：" + i);
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_WEIGHT_QNDATA, JSON.toJSONString(qnScaleData));
                if (RxActivityUtils.currentActivity() instanceof SportInterface) {
                } else {
                    RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), WeightAddFragment.class, bundle);
                }

                MyAPP.getgUserInfo().setHmac(qnScaleData.getHmac());
                RxLogUtils.d("HMac:" + qnScaleData.getHmac());

                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "稳定体重：");
            }

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, final List<QNScaleStoreData> list) {
                RxLogUtils.d("历史数据：" + list.size());
                BleService.historyWeightData = list;
                RxBus.getInstance().post(new ScaleHistoryData(list));
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "历史体重数据个数：" + list.size());
            }

            @Override
            public void onGetElectric(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("onGetElectric:" + i);
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "onGetElectric：" + i);
            }
        });

        QNBleManager.getQNApi().setBleConnectionChangeListener(new com.yolanda.health.qnblesdk.listener.QNBleConnectionChangeListener() {
            @Override
            public void onConnecting(QNBleDevice qnBleDevice) {
                RxLogUtils.e("正在连接:");
                setConnectState(QNBleManager.QN_CONNECTING);
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "正在连接");
            }

            @Override
            public void onConnected(QNBleDevice qnBleDevice) {
                RxLogUtils.d("连接成功:");
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "连接成功");
            }

            @Override
            public void onServiceSearchComplete(QNBleDevice qnBleDevice) {
                setConnectState(QNBleManager.QN_CONNECED);
                RxBus.getInstance().postSticky(new ScaleConnectBus(true));

                DeviceLink deviceLink = new DeviceLink();
                deviceLink.setMacAddr(SPUtils.getString(SPKey.SP_scaleMAC));
                deviceLink.setDeviceNo(BleKey.TYPE_SCALE);
                deviceLink.deviceLink(deviceLink);
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "发现服务");

            }

            @Override
            public void onDisconnecting(QNBleDevice qnBleDevice) {
                RxLogUtils.e("正在断开连接:");
                setConnectState(QNBleManager.QN_DISCONNECTING);
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "正在断开连接");
            }

            @Override
            public void onDisconnected(QNBleDevice qnBleDevice) {
                setConnectState(QNBleManager.QN_DISCONNECED);
                RxBus.getInstance().postSticky(new ScaleConnectBus(false));
                RxLogUtils.e("断开连接:");

                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "断开连接");

            }

            @Override
            public void onConnectError(QNBleDevice qnBleDevice, int i) {
                setConnectState(QNBleManager.QN_DISCONNECED);
                RxLogUtils.d("连接异常：" + i);
                disConnectDevice();
                RxBus.getInstance().postSticky(new ScaleConnectBus(false));
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "连接异常");

            }

            @Override
            public void onScaleStateChange(QNBleDevice qnBleDevice, int i) {
                RxLogUtils.d("体重秤状态变化:" + i);
                BuryingPoint.INSTANCE.scaleState(qnBleDevice.getMac(), "体重秤状态变化:" + i);
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
}
