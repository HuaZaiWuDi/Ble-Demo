package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;
import java.util.List;


/**
 * Created icon_hide_password jk on 2018/5/22.
 */
public class BindDeviceItem implements Serializable {


    private List<DeviceListBean> deviceList;

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public static class DeviceListBean {
        /**
         * city : string
         * country : string
         * deviceName : string
         * deviceNo : string
         * firmwareVersion : string
         * lastOnlineTime : 2018-06-05T07:29:48.289Z
         * linkStatus : 0
         * macAddr : string
         * mcuVersion : string
         * onlineDuration : 0
         * productId : string
         * productName : string
         * province : string
         * wakeTime : 2018-06-05T07:29:48.289Z
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
}
