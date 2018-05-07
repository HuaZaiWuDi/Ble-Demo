package lab.dxythch.com.netlib.net;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;

/**
 * 项目名称：Bracelet
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/5/24
 */
public class ServiceAPI {

    public static final String BASE_URL = "http://www.vvjoin.com:8080/";

    public static String merchantNo = "";
    public static String userId = "";
    public static Location CurrentLocation = null;


    public static final String UPDATE_LODERA_OTA_URL = "http://d.dxycloud.com/statistics/getDownLoadAddr/passivityDownAndroidBin?file_name=";
    public static final String UPDATE_LODER_APP_URL = "http://d.dxycloud.com/statistics/getDownLoadAddr/passivityDownAndroidApp?file_name=";


    public static String formatData() {
        String DATE_FORMAT = "yyyyMMddHHmmss";
        return new SimpleDateFormat(DATE_FORMAT, Locale.CHINA).format(new Date().getTime());
    }


    public static void setMerchantNo(String merchantNo) {
        ServiceAPI.merchantNo = merchantNo;
    }

    public static void setUserId(String userId) {
        ServiceAPI.userId = userId;
    }


    public static void gainUserId(Location location) {
        CurrentLocation = location;
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.gainUserId(merchantNo,
                formatData(), "gps84", location.getLongitude() + "", location.getAltitude() + ""), new RxNetSubscriber<String>() {
            @Override
            protected void _onNext(String s) {
                userId = s;
            }
        });
    }

    public static void gainAppConfig(String appVersion, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.gainAppConfig(merchantNo, userId,
                formatData(), appVersion, "gps84", CurrentLocation.getLongitude() + "", CurrentLocation.getAltitude() + ""), rxNetSubscriber);
    }

    public static void bindDevice(String mac, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.bindDevice(merchantNo, userId,
                formatData(), mac), rxNetSubscriber);
    }

    public static void bindDevice(String mac, String historyData, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.refreshHistory(merchantNo, userId,
                formatData(), mac, historyData), rxNetSubscriber);
    }

    public static void gainFirmwareConfig(String mac, String firmwareVersionNo, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.gainFirmwareConfig(merchantNo, userId,
                formatData(), mac, firmwareVersionNo), rxNetSubscriber);
    }

    public static void gainUserInfo(String mac, String firmwareVersionNo, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.gainUserInfo(merchantNo, userId,
                formatData()), rxNetSubscriber);
    }

    public static void updateUserInfo(String userName, String phoneNumber, String email, String wechat, String qq, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.updateUserInfo(merchantNo, userId,
                formatData(), userName, phoneNumber, email, wechat, qq), rxNetSubscriber);
    }

    public static void updateFamilyUserInfo(String familyUser, String familyType, String familyAction, RxNetSubscriber<String> rxNetSubscriber) {
        RetrofitService serviceAPI = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(serviceAPI.updateFamilyUserInfo(merchantNo, userId,
                formatData(), familyUser, familyType, familyAction), rxNetSubscriber);
    }

}
