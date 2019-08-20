package lab.wesmartclothing.wefit.flyso.ble

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.vondear.rxtools.utils.RxLogUtils
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.BuildConfig
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.buryingpoint.BuryingPoint
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.BleManagerCallbacks
import java.util.*

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName MyBleManager
 * @Date 2019/7/5 9:31
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */

fun MyBleManager.Companion.instance(): MyBleManager {
    return instance
}

class MyBleManager(context: Context) : BleManager<BleManagerCallbacks>(context), BleInterface<BluetoothDevice> {


    companion object {

        val UUID_SERVICE = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase()
        val UUID_CHART_WRITE = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase()
        val UUID_CHART_READ_NOTIFY = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase()

        var DFUStarting = false
        val instance: MyBleManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MyBleManager(MyAPP.sMyAPP)
        }
    }


    private var mBytes: ByteArray? = null
    var writeChar: BluetoothGattCharacteristic? = null
    var notifyChar: BluetoothGattCharacteristic? = null

    private val bluetoothManager: BluetoothManager by lazy {
        getContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    override fun getGattCallback() = mBleCallback


    private val mBleCallback = object : BleManagerGattCallback() {
        override fun initialize() {
            super.initialize()
            setNotificationCallback(notifyChar)
                    .with { device, data ->
                        "蓝牙通知数据$data".d()
                        BleAPI.handleBleData(data.value)
                    }

            requestMtu(200)
                    .fail(this@MyBleManager::doFail)
                    .done {
                        "设置MTU成功:200".d(it.address)
                    }
                    .enqueue()

            enableNotifications(notifyChar)
                    .done { device ->
                        "开启通知成功".d(device.address)
                        if (!DFUStarting)
                            initData()
                    }
                    .fail(this@MyBleManager::doFail)
                    .enqueue()
        }

        override fun onDeviceDisconnected() {
            writeChar = null
            notifyChar = null

        }

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val service = gatt.getService(UUID.fromString(UUID_SERVICE)) ?: return false
            writeChar = service.getCharacteristic(UUID.fromString(UUID_CHART_WRITE))
            notifyChar = service.getCharacteristic(UUID.fromString(UUID_CHART_READ_NOTIFY))

            BuryingPoint.clothingState(bluetoothDevice?.address
                    ?: "", "设备是否支持：${writeChar != null && notifyChar != null}")
            return writeChar != null && notifyChar != null
        }
    }

    //蓝牙数据初始化操作
    private fun initData() {
        BleAPI.syncSetting(!BuildConfig.DEBUG)
        BleAPI.readDeviceInfo()
        BleAPI.getVoltage()
    }

    fun write(bytes: ByteArray) {
        writeCharacteristic(writeChar, bytes)
                .done { device -> mBytes = bytes }
                .fail(this::doFail)
                .enqueue()
    }

    private fun doFail(device: BluetoothDevice, status: Int) {
        RxLogUtils.d("执行失败,状态：$status--设备信息：$device")
        BuryingPoint.clothingState(device.address, "执行失败,状态：$status}")
    }


    override fun doConnect(bleDevice: BluetoothDevice) {
        setGattCallbacks(object : BleManagerCallbacks {
            override fun onDeviceDisconnecting(device: BluetoothDevice) {
                "正在断开连接".d(device.address)
                BuryingPoint.clothingState(device.address, "正在断开连接")
            }

            override fun onDeviceDisconnected(device: BluetoothDevice) {
                "断开连接不在重连".d(device.address)
                BuryingPoint.clothingState(device.address, "断开连接不在重连")
            }

            override fun onDeviceConnected(device: BluetoothDevice) {
                "设备连接".d(device.address)
                BuryingPoint.clothingState(device.address, "设备连接")
            }

            override fun onDeviceNotSupported(device: BluetoothDevice) {
                "设备不支持".d(device.address)
                BuryingPoint.clothingState(device.address, "设备不支持")
            }

            override fun onBondingFailed(device: BluetoothDevice) {
                "绑定失败".d(device.address)
            }

            override fun onServicesDiscovered(device: BluetoothDevice, optionalServicesFound: Boolean) {
                "发现服务".d(device.address)
                BuryingPoint.clothingState(device.address, "发现服务")
            }

            override fun onBondingRequired(device: BluetoothDevice) {
                "需要绑定".d(device.address)
            }

            override fun onLinkLossOccurred(device: BluetoothDevice) {
                "断开连接准备重新连接".d(device.address)
                RxBus.getInstance().postSticky(ClothingConnectBus(false))
                BuryingPoint.clothingState(device.address, "准备重新连接")
            }

            override fun onBonded(device: BluetoothDevice) {
                "绑定".d(device.address)
            }

            //initialization 中的操作都结束时调用
            override fun onDeviceReady(device: BluetoothDevice) {
                "设备准备好了".d(device.address)
                RxBus.getInstance().postSticky(ClothingConnectBus(true))
                BuryingPoint.clothingState(device.address, "设备准备")
            }

            override fun onError(device: BluetoothDevice, message: String, errorCode: Int) {
                "异常:$message:状态码：$errorCode".d(device.address)
                BuryingPoint.clothingState(device.address, "异常:$message:状态码：$errorCode")
            }

            override fun onDeviceConnecting(device: BluetoothDevice) {
                "正在连接".d(device.address)
                BuryingPoint.clothingState(device.address, "正在连接")
            }
        })

        connect(bleDevice)
                .retry(Int.MAX_VALUE)
                .timeout(10 * 1000)
                .useAutoConnect(true)
                .enqueue()
    }

    override fun disConnect() {
        disconnect().enqueue()
    }

    override fun isConnect(): Boolean {
        return isConnected
    }

    override fun getDevice(): BluetoothDevice? {
        return bluetoothDevice
    }

    override fun isBinded(): Boolean {
        return BluetoothAdapter.checkBluetoothAddress(MyAPP.getgUserInfo().clothesMacAddr)
    }

    override fun bind(bleDevice: BluetoothDevice) {
        //瘦身衣和EMS瘦身衣为互斥
        //解绑EMS瘦身衣
        MyAPP.getgUserInfo().emsMacAddr = ""
        EMSManager.instance.disConnect()

        disConnect()
        doConnect(bleDevice)
        MyAPP.getgUserInfo().clothesMacAddr = bleDevice.address

        BuryingPoint.clothingState(bleDevice.address, "绑定设备：")
    }

    override fun unBind() {
        //删除绑定
        disConnect()
        MyAPP.getgUserInfo().clothesMacAddr = ""
    }

}

fun Any.d(tag: String = "【智瘦】") {
    Log.d(tag, this.toString())
}