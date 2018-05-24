package lab.dxythch.com.commonproject.entity;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

/**
 * Created by jk on 2018/5/22.
 */
@EBean
public class BindDeviceItem implements Serializable {


    /**
     * city : string
     * deviceName : string
     * deviceNo : string
     * firmwareVersion : string
     * lastOnlineTime : 2018-05-22T05:54:06.057Z
     * linkStatus : 0
     * macAddr : string
     * mcuVersion : string
     * onlineDuration : 0
     * productName : string
     * userId : string
     * wakeTime : 2018-05-22T05:54:06.057Z
     * productId : 001
     */

    private String city;
    private String deviceName;
    private String deviceNo;
    private String firmwareVersion;
    private String lastOnlineTime;
    private int linkStatus;
    private String macAddr;
    private String mcuVersion;
    private int onlineDuration;
    private String productName;
    private String userId;
    private String wakeTime;
    private String productId;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(String wakeTime) {
        this.wakeTime = wakeTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
