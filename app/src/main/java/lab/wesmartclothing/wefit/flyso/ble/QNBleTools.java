package lab.wesmartclothing.wefit.flyso.ble;

import com.vondear.rxtools.utils.RxLogUtils;
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.listen.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNUser;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Date;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;

/**
 * Created by jk on 2018/5/16.
 */

@EBean
public class QNBleTools {


    @Pref
    Prefs_ mPrefs;

    public void scanBle(int duration) {
        QNConfig config = new QNConfig();
        config.setDuration(duration);
        config.setOnlyScreenOn(false);
        config.setAllowDuplicates(false);
        config.setUnit(QNUnit.WEIGHT_UNIT_KG);
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
        String userId = mPrefs.UserId().getOr("testuser");
        int height = mPrefs.height().getOr(175);
        int sexIndex = mPrefs.sex().getOr(0);
        String sex = sexIndex == 0 ? "male" : "female";
        long birthDayMillis = mPrefs.birthDayMillis().getOr(System.currentTimeMillis());

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
        scanBle(15 * 1000);
        MyAPP.QNapi.connectDevice(device, creatUser(), new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：创建用户" + i + "----" + s);
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
                RxLogUtils.d("轻牛SDK ：创建用户" + i + "----" + s);
            }
        });
    }

    public void disConnectDevice(QNBleDevice device) {
        MyAPP.QNapi.disconnectDevice(device, new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                RxLogUtils.d("轻牛SDK ：创建用户" + i + "----" + s);
            }
        });
    }

}
