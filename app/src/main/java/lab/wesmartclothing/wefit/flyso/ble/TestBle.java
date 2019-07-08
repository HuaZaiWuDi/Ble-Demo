package lab.wesmartclothing.wefit.flyso.ble;

import android.bluetooth.BluetoothDevice;

import org.jetbrains.annotations.NotNull;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName TestBle
 * @Date 2019/7/8 12:00
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class TestBle implements BleInterface<BluetoothDevice> {
    @Override
    public void write(@NotNull byte[] bytes) {

    }

    @Override
    public void scanMacAddress(@NotNull String macAddress) {

    }

    @Override
    public void stopScan() {

    }

    @Override
    public void doConnect(BluetoothDevice bleDevice) {

    }

    @Override
    public void disConnect() {

    }

    @Override
    public boolean isConnect() {
        return false;
    }

    @Override
    public boolean isScaning() {
        return false;
    }
}
