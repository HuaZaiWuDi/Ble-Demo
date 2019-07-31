package lab.wesmartclothing.wefit.flyso.entity;

import android.bluetooth.BluetoothDevice;

import com.yolanda.health.qnblesdk.out.QNBleDevice;

import java.io.Serializable;

/**
 * Created icon_hide_password Jack on 2018/5/22.
 */
public class BindDeviceBean implements Serializable {

    /**
     * 0:体重秤
     * 1：瘦身衣
     */

    String deivceType;
    boolean isBind = false;
    String deviceName;
    String deviceMac;
    int rssi;
    QNBleDevice mQNBleDevice;
    BluetoothDevice mBluetoothDevice;

    public BindDeviceBean(String deivceType, String deviceName, String deviceMac, int rssi) {
        this.deivceType = deivceType;
        this.deviceName = deviceName;
        this.deviceMac = deviceMac;
        this.rssi = rssi;
    }

    public QNBleDevice getQNBleDevice() {
        return mQNBleDevice;
    }

    public void setQNBleDevice(QNBleDevice QNBleDevice) {
        mQNBleDevice = QNBleDevice;
    }

    public BluetoothDevice getBluetoothDevice() {
        return mBluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getDeivceType() {
        return deivceType;
    }

    public void setDeivceType(String deivceType) {
        this.deivceType = deivceType;
    }


    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

}
