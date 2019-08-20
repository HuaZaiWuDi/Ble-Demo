package lab.wesmartclothing.wefit.flyso.ble

import com.wesmarclothing.mylibrary.net.RxBus

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName EMSApi
 * @Date 2019/8/14 16:36
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */

data class DeviceInfo(
        val model: Int,
        val strength: Int,
        val min: Int,
        val sec: Int
)

object EMSApi {


    /**
     * 处理数据
     */
    fun handleBleData(bytes: ByteArray?) {
        if (bytes == null || bytes.size < 4) return

        when (bytes[1]) {
            //读设备信息
            0x77.toByte() -> {
                val deviceInfo = DeviceInfo(
                        setupModel2Index(bytes.get(2).toInt()),
                        bytes.get(3).toInt(),
                        bytes.get(4).toInt(),
                        bytes.get(5).toInt()
                )
                RxBus.getInstance().post(deviceInfo)
            }
            else -> {
                val deviceInfo = DeviceInfo(
                        setupModel2Index(bytes.get(0).toInt()),
                        bytes.get(1).toInt(),
                        bytes.get(2).toInt(),
                        bytes.get(3).toInt()
                )
                RxBus.getInstance().post(deviceInfo)
            }
        }
    }

    //设备强度
    fun setupStrength(isAdd: Boolean) {
        if (isAdd) {
            EMSManager.instance.write(byteArrayOf(0xAA.toByte(), 0x51))
        } else {
            EMSManager.instance.write(byteArrayOf(0xAA.toByte(), 0x52))
        }
    }

    //设备模式
    fun setupModel(model: Int) {
        when (model) {
            0 -> byteArrayOf(0xBB.toByte(), 0x67)//燃脂|训练
            1 -> byteArrayOf(0xBB.toByte(), 0x66)//增肌|塑性
            2 -> byteArrayOf(0xBB.toByte(), 0x63)//经脉|疏通
            3 -> byteArrayOf(0xBB.toByte(), 0x68)//恢复|理疗
            else -> byteArrayOf(0xBB.toByte(), 0x67)//燃脂|训练
        }.apply {
            EMSManager.instance.write(this)
        }
    }

    fun setupModel2Index(model: Int): Int = when (model) {
        3 -> 2
        6 -> 1
        7 -> 0
        8 -> 3
        else -> -1
    }


    //设备时长
    fun setupDuration(min: Int) {
        EMSManager.instance.write(byteArrayOf(0xEE.toByte(), min.toByte()))
    }

    //停止
    fun setupStop() {
        EMSManager.instance.write(byteArrayOf(0xCC.toByte(), 0x77.toByte()))
    }

    //暂停
    fun setupPause() {
        EMSManager.instance.write(byteArrayOf(0xCC.toByte(), 0x88.toByte()))
    }

    //开始
    fun setupStart() {
        EMSManager.instance.write(byteArrayOf(0xCC.toByte(), 0x99.toByte()))
    }

    //获取设备数据
    fun getNotifyData() {
        EMSManager.instance.write(byteArrayOf(0xDD.toByte(), 0x77.toByte()))
    }

}