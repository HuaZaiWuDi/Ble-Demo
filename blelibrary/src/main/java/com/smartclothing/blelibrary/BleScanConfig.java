package com.smartclothing.blelibrary;

import com.clj.fastble.BleManager;

/**
 * Created by jk on 2018/6/26.
 */
public class BleScanConfig {


    private String[] mServiceUuids = {};
    private String[] mDeviceNames = {};
    private String[] mDeviceMac = {};
    private boolean mAutoConnect = false;
    private boolean mFuzzy = false;
    private long mScanTimeOut = BleManager.DEFAULT_SCAN_TIME;
    private long reportDelayMillis = 0;//如果为0，则回调onScanResult()方法，如果大于0, 则每隔你设置的时长回调onBatchScanResults()方法，不能小于0

    public long getReportDelayMillis() {
        return reportDelayMillis;
    }

    public String[] getServiceUuids() {
        return mServiceUuids;
    }

    public String[] getDeviceNames() {
        return mDeviceNames;
    }

    public String[] getDeviceMac() {
        return mDeviceMac;
    }

    public boolean isAutoConnect() {
        return mAutoConnect;
    }

    public boolean isFuzzy() {
        return mFuzzy;
    }

    public long getScanTimeOut() {
        return mScanTimeOut;
    }

    public static class Builder {

        private String[] mServiceUuids = {};
        private String[] mDeviceNames = {};
        private String[] mDeviceMac = {};
        private boolean mAutoConnect = false;
        private boolean mFuzzy = false;
        private long mTimeOut = BleManager.DEFAULT_SCAN_TIME;
        private long reportDelayMillis = 0;

        public BleScanConfig.Builder setServiceUuids(String... uuids) {
            this.mServiceUuids = uuids;
            return this;
        }

        public BleScanConfig.Builder setDeviceName(boolean fuzzy, String... name) {
            this.mFuzzy = fuzzy;
            this.mDeviceNames = name;
            return this;
        }

        public BleScanConfig.Builder setDeviceMac(String... mac) {
            this.mDeviceMac = mac;
            return this;
        }

        public BleScanConfig.Builder setAutoConnect(boolean autoConnect) {
            this.mAutoConnect = autoConnect;
            return this;
        }

        public BleScanConfig.Builder setScanTimeOut(long timeOut) {
            this.mTimeOut = timeOut;
            return this;
        }

        public BleScanConfig.Builder setReportDelayMillis(long delay) {
            this.reportDelayMillis = delay;
            return this;
        }

        void applyConfig(BleScanConfig config) {
            config.mServiceUuids = this.mServiceUuids;
            config.mDeviceNames = this.mDeviceNames;
            config.mDeviceMac = this.mDeviceMac;
            config.mAutoConnect = this.mAutoConnect;
            config.mFuzzy = this.mFuzzy;
            config.mScanTimeOut = this.mTimeOut;
            config.reportDelayMillis = this.reportDelayMillis;
        }

        public BleScanConfig build() {
            BleScanConfig config = new BleScanConfig();
            applyConfig(config);
            return config;
        }

    }


}
