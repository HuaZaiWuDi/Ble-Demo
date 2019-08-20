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


    fun doConnect(bleDevice: B)

    fun disConnect()

    fun isConnect(): Boolean

    fun getDevice(): B?

    fun isBinded(): Boolean

    fun bind(bleDevice: B)

    fun unBind()
}