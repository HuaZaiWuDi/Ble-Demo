package lab.wesmartclothing.wefit.flyso.ble

import android.bluetooth.BluetoothDevice
import com.yolanda.health.qnblesdk.out.QNBleDevice

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName BleManagerInterface
 * @Date 2019/7/5 14:33
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
interface BleManagerInterface {

    fun getClothingDevice(): BluetoothDevice

    fun getScaleDevice(): QNBleDevice

    fun write(bytes: ByteArray)

    fun scanMacAddress(macAddress: String)

    fun scanService()

    fun stopScan()

    fun doConnect()

    fun disConnect()

    fun isConnect(): Boolean


}