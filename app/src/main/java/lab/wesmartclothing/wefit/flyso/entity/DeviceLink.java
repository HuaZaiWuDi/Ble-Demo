package lab.wesmartclothing.wefit.flyso.entity;

import com.google.gson.Gson;
import com.vondear.rxtools.utils.RxLogUtils;

import java.util.concurrent.TimeUnit;

import lab.wesmartclothing.wefit.flyso.base.ActivityBaseLocation;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/5/29.
 */
public class DeviceLink {


    /**
     * city : string
     * country : string
     * province : string
     * deviceName : string
     * deviceNo : string
     * firmwareVersion : string
     * lastOnlineTime : 2018-05-29T07:40:33.795Z
     * linkStatus : 0
     * macAddr : string
     * mcuVersion : string
     * onlineDuration : 0
     * productId : string
     * productName : string
     * wakeTime : 2018-05-29T07:40:33.795Z
     */

    private String city;
    private String country;
    private String deviceName;
    private String deviceNo;
    private String firmwareVersion;
    private String lastOnlineTime;
    private int linkStatus;
    private String macAddr;
    private String mcuVersion;
    private int onlineDuration;
    private String productId;
    private String productName;
    private String province;
    private String wakeTime;

    //数据统计接口
    public void deviceLink(DeviceLink deviceLink) {
        if (deviceLink == null) return;
        deviceLink.setCity(ActivityBaseLocation.City);
        deviceLink.setCountry(ActivityBaseLocation.Country);
        deviceLink.setProvince(ActivityBaseLocation.Province);
        String s = new Gson().toJson(deviceLink, DeviceLink.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.deviceLink(body))
                .throttleFirst(30, TimeUnit.MINUTES)
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("统计接口：" + s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxLogUtils.e("统计接口：" + error);
//                        RxToast.error(error);
                    }
                });
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public int getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getMcuVersion() {
        return mcuVersion;
    }

    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }

    public int getOnlineDuration() {
        return onlineDuration;
    }

    public void setOnlineDuration(int onlineDuration) {
        this.onlineDuration = onlineDuration;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(String wakeTime) {
        this.wakeTime = wakeTime;
    }
}
