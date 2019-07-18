package lab.wesmartclothing.wefit.flyso.ble

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName BleInterface
 * @Date 2019/7/5 14:33
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
interface BleInterface<B> {

    var device: B

    fun write(bytes: ByteArray)

    fun scanMacAddress(macAddress: String)

    fun stopScan()

    fun doConnect(bleDevice: B)

    fun disConnect()

    fun isConnect(): Boolean

    fun isScaning(): Boolean

    fun connected(bleDevice: B)

    fun disconnected(bleDevice: B)


}