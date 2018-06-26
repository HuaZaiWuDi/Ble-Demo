package com.smartclothing.module_wefit.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class Device implements Serializable, MultiItemEntity {
    private String gid;
    private int status;
    private String createUser;
    private long createTime;
    private String updateUser;
    private long updateTime;
    private String userId;
    private String productId;
    private String productName;
    private String macAddr;
    private String deviceNo;
    private String deviceName;
    private int linkStatus;
    private long wakeTime;
    private int onlineDuration;
    private long lastOnlineTime;
    private String firmwareVersion;
    private String mcuVersion;
    private String city;
    private int type;
    private int quantity;
    private boolean isConnect = false;
    private int standby;
    private String newFirmwareVersion = "";
    private boolean isUpdate = false;
    private boolean isBind;


    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getNewFirmwareVersion() {
        return newFirmwareVersion;
    }

    public void setNewFirmwareVersion(String newFirmwareVersion) {
        this.newFirmwareVersion = newFirmwareVersion;
    }

    public int getStandby() {
        return standby;
    }

    public void setStandby(int standby) {
        this.standby = standby;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Device(int type) {
        this.type = type;
    }

    public Device() {
    }

    public Device(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }

    public long getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(long wakeTime) {
        this.wakeTime = wakeTime;
    }

    public int getOnlineDuration() {
        return onlineDuration;
    }

    public void setOnlineDuration(int onlineDuration) {
        this.onlineDuration = onlineDuration;
    }

    public long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getMcuVersion() {
        return mcuVersion;
    }

    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
