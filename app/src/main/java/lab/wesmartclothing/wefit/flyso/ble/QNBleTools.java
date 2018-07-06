package lab.wesmartclothing.wefit.flyso.ble;

import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.yolanda.health.qnblesdk.listen.QNBleDeviceDiscoveryListener;
import com.yolanda.health.qnblesdk.listen.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNUser;

import org.androidannotations.annotations.EBean;

import java.util.Date;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created icon_hide_password jk on 2018/5/16.
 */

@EBean(scope = EBean.Scope.Singleton)//单例模式 传入的是using context.getApplicationContext()对象
public class QNBleTools {


    public void scanBle(int duration) {
        QNConfig config = new QNConfig();
        config.setDuration(duration);
        config.setOnlyScreenOn(false);
        config.setAllowDuplicates(false);
        config.setUnit(0);
        MyAPP.QNapi.startBleDeviceDiscovery(config, mQNResultCallback);
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


    public QNUser creatUser() {

        String sex = SPUtils.getInt(SPKey.SP_sex, 0) == 0 ? "male" : "female";
        long birthDayMillis = SPUtils.getLong(SPKey.SP_birthDayMillis);
        String userId = SPUtils.getString(SPKey.SP_UserId);
        int height = SPUtils.getInt(SPKey.SP_height, 175);

        Date date = new Date(birthDayMillis);
        QNUser qnUser = MyAPP.QNapi.buildUser(userId, height, sex, date, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：创建用户" + i + "----" + s);
            }
        });
        return qnUser;
    }


    public void connectDevice(QNBleDevice device) {
        MyAPP.QNapi.setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
            @Override
            public void onDeviceDiscover(QNBleDevice qnBleDevice) {

            }

            @Override
            public void onStartScan() {

            }

            @Override
            public void onStopScan() {

            }
        });
        scanBle(1 * 1000);
        MyAPP.QNapi.connectDevice(device, creatUser(), new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：连接设备" + i + "----" + s);
                if (i == 0) {
                    RxLogUtils.d("轻牛SDK ：连接成功");
                }
            }
        });
    }


    public void disConnectDevice(String mac) {
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

}