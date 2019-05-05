package lab.wesmartclothing.wefit.flyso.ble;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.IntDef;

import com.clj.fastble.data.BleDevice;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created icon_hide_password jk on 2018/5/16.
 */

public class QNBleTools {


    public static final int QN_CONNECTING = 0;//正在连接
    public static final int QN_CONNECED = 1;//连接完成，发现服务
    public static final int QN_DISCONNECED = 2;//断开连接
    public static final int QN_DISCONNECTING = 3;//连接失败
    private static QNBleTools mQNBleTools;

    public static QNBleTools getInstance() {
        if (mQNBleTools == null) {
            mQNBleTools = new QNBleTools();
        }
        return mQNBleTools;
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

    private static QNBleDevice device;

    public QNBleDevice getDevice() {
        return device;
    }

    public void setDevice(QNBleDevice device) {
        QNBleTools.device = device;
    }

    public void scanBle() {
        saveUserInfo();
        MyAPP.QNapi.startBleDeviceDiscovery(mQNResultCallback);
    }

    private void saveUserInfo() {
        QNConfig config = MyAPP.QNapi.getConfig();
        config.setDuration(0);
        config.setOnlyScreenOn(false);
        config.setAllowDuplicates(true);
        config.setScanOutTime(0);
        config.setUnit(0);

        config.save(new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：保存设备配置" + i + "----" + s);
            }
        });
    }


    //蓝牙扫描监听
    QNResultCallback mQNResultCallback = new QNResultCallback() {
        @Override
        public void onResult(int i, String s) {
            RxLogUtils.d("轻牛SDK ：扫描设备" + i + "----" + s);
        }
    };


    public void stopScan() {
        MyAPP.QNapi.stopBleDeviceDiscovery(mQNResultCallback);
    }

    public QNUser createUser() {
        UserInfo info = MyAPP.gUserInfo;
        if (info == null) return null;
        String sex = info.getSex() == 1 ? "male" : "female";
        long birthDayMillis = info.getBirthday();
        String userId = SPUtils.getString(SPKey.SP_UserId);
        int height = info.getHeight();

        Date date = new Date(birthDayMillis);
        QNUser qnUser = MyAPP.QNapi.buildUser(userId, height, sex, date, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：创建用户" + i + "----" + s);
            }
        });
        return qnUser;
    }


    public void connectDevice(QNBleDevice qnBleDevice) {
        MyAPP.QNapi.connectDevice(qnBleDevice, createUser(), new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：连接设备" + i + "----" + s);
            }
        });
    }


    public void disConnectDevice() {
        String mac = SPUtils.getString(SPKey.SP_scaleMAC);
        MyAPP.QNapi.disconnectDevice(mac, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：断开连接" + i + "----" + s);
            }
        });
    }

    public void disConnectDevice(QNBleDevice device) {
        MyAPP.QNapi.disconnectDevice(device, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：断开连接" + i + "----" + s);
            }
        });
    }


    public QNBleDevice bleDevice2QNDevice(BleDevice result) {
        return MyAPP.QNapi.buildDevice(result.getDevice(), result.getRssi(), result.getScanRecord(), new QNResultCallback() {
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
}
