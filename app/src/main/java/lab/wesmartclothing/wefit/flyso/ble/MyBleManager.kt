package lab.wesmartclothing.wefit.flyso.ble

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.vondear.rxtools.utils.RxLogUtils
import com.vondear.rxtools.utils.SPUtils
import com.vondear.rxtools.utils.aboutByte.HexUtil
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.BuildConfig
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.buryingpoint.BuryingPoint
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceBean
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus
import lab.wesmartclothing.wefit.flyso.tools.BleKey
import lab.wesmartclothing.wefit.flyso.tools.SPKey
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.BleManagerCallbacks
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
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

class MyBleManager(context: Context) : BleManager<BleManagerCallbacks>(context) {

    companion object {
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
            val service = gatt.getService(UUID.fromString(BleKey.UUID_Servie)) ?: return false
            writeChar = service.getCharacteristic(UUID.fromString(BleKey.UUID_CHART_WRITE))
            notifyChar = service.getCharacteristic(UUID.fromString(BleKey.UUID_CHART_READ_NOTIFY))

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
        BuryingPoint.clothingState(device.address, "执行失败,状态：$status-${HexUtil.encodeHexStr(mBytes)}")
//        disConnect()
    }

    fun isBLEEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun enableBLE(): Boolean {
        return bluetoothAdapter.enable()
    }

    fun disableBLE(): Boolean {
        return bluetoothAdapter.disable()
    }

    fun scanMacAddress() {
        stopScan()

        val build = ScanSettings.Builder()
//                .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().startScan(null, build, scanCallback)
        BuryingPoint.clothingState("", "开启：${isBLEEnabled()}")
    }


    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (BleKey.Smart_Clothing == result.device.name) {
                "扫描到设备${result.device.name}".d(result.device.address)
                if (result.device.address == SPUtils.getString(SPKey.SP_clothingMAC)) {
                    doConnect(result.device)
                    BuryingPoint.clothingState(result.device.address, "扫描到绑定设备${result.device.name}-${result.device.address}")
                } else {
                    val bindDeviceBean = BindDeviceBean(BleKey.TYPE_CLOTHING,
                            getContext().getString(R.string.clothing), result.device.address, result.rssi)
                    bindDeviceBean.bluetoothDevice = result.device
                    RxBus.getInstance().post(bindDeviceBean)
                }
            } else if (BleKey.ScaleName == result.device.name) {
                "扫描到设备${result.device.name}".d(result.device.address)
                val qnBleDevice = QNBleManager.getInstance().convert2QNDevice(result.device, result.rssi, result.scanRecord?.bytes)
                if (result.device.address == SPUtils.getString(SPKey.SP_scaleMAC)) {
                    QNBleManager.getInstance().connectDevice(qnBleDevice)
                    BuryingPoint.scaleState(result.device.address, "扫描到绑定设备${result.device.name}-${result.device.address}")
                } else {
                    val bindDeviceBean = BindDeviceBean(BleKey.TYPE_SCALE,
                            getContext().getString(R.string.scale), result.device.address, result.rssi)
                    bindDeviceBean.qnBleDevice = qnBleDevice
                    RxBus.getInstance().post(bindDeviceBean)
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)
            "onBatchScanResults：${results.size}".d()
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            "扫描失败状态码：$errorCode".d()
            BuryingPoint.clothingState("", "扫描失败状态码：$errorCode")
        }
    }

    fun stopScan() {
        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
        BuryingPoint.clothingState("", "停止扫描：${isBLEEnabled()}")
    }

    fun doConnect(bleDevice: BluetoothDevice) {
//        stopScan()
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

    fun disConnect() {
        disconnect().enqueue()
    }


    fun isConnect(): Boolean {
        return isConnected
    }

    fun String.d(tag: String = "【MyBleManager】") {
        Log.d(tag, this)
    }

}