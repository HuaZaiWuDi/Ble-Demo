package lab.wesmartclothing.wefit.flyso.ble

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.vondear.rxtools.utils.RxLogUtils
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.BuildConfig
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceBean
import lab.wesmartclothing.wefit.flyso.rxbus.ClothingConnectBus
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.BleManagerCallbacks
import no.nordicsemi.android.support.v18.scanner.*
import java.util.*

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName MyBleManager
 * @Date 2019/7/5 9:31
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
class MyBleManager(context: Context) : BleManager<BleManagerCallbacks>(context) {

    companion object {
        var DFUStarting = false
        val instance: MyBleManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MyBleManager(MyAPP.sMyAPP)
        }
    }

    var autoConnect = false
    private var mBytes: ByteArray? = null
    var writeChar: BluetoothGattCharacteristic? = null
    var notifyChar: BluetoothGattCharacteristic? = null
    var bleDevice: BluetoothDevice? = null

    private val bluetoothManager: BluetoothManager by lazy {
        MyAPP.sMyAPP?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
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
                        "蓝牙通知数据${data.toString()}".d()
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

    fun scanMacAddress(macAddress: String) {
        if (!BluetoothAdapter.checkBluetoothAddress(macAddress))
            return
        stopScan()
        autoConnect = true

        val scanFilter = ScanFilter.Builder()
//                .setServiceUuid(ParcelUuid.fromString(BleKey.UUID_Servie))
//                .setDeviceAddress(macAddress)
                .build()

        val build = ScanSettings.Builder()
                .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build()

        val list = mutableListOf(scanFilter)

        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().startScan(list, build, scanCallback)
    }

    fun scanService() {
        stopScan()
        autoConnect = false
        val scanFilter = ScanFilter.Builder()
//                .setServiceUuid(ParcelUuid.fromString(BleKey.UUID_Servie))
//                .setDeviceName("WeSma")
                .build()

        val build = ScanSettings.Builder()
                .setLegacy(false)
                .setUseHardwareBatchingIfSupported(true)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build()

        val list = mutableListOf(scanFilter)

        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().startScan(list, build, scanCallback)
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            if (BleKey.Smart_Clothing.equals(result.device.name)) {
                "扫描到设备${result.device.name}".d(result.device.address)
                if (autoConnect)
                    doConnect(result.device)
                else {
                    val bindDeviceBean = BindDeviceBean(BleKey.TYPE_CLOTHING,
                            getContext().getString(R.string.clothing), result.device.address, result.rssi)
                    bindDeviceBean.bluetoothDevice = result.device
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
        }
    }

    fun stopScan() {
        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
    }

    fun doConnect(bleDevice: BluetoothDevice) {
        stopScan()
        setGattCallbacks(object : BleManagerCallbacks {
            override fun onDeviceDisconnecting(device: BluetoothDevice) {
                "正在断开连接".d(device.address)
            }

            override fun onDeviceDisconnected(device: BluetoothDevice) {
                "断开连接不在重连".d(device.address)
            }

            override fun onDeviceConnected(device: BluetoothDevice) {
                "设备连接".d(device.address)
            }

            override fun onDeviceNotSupported(device: BluetoothDevice) {
                "设备不支持".d(device.address)
            }

            override fun onBondingFailed(device: BluetoothDevice) {
                "绑定失败".d(device.address)
            }

            override fun onServicesDiscovered(device: BluetoothDevice, optionalServicesFound: Boolean) {
                "发现服务".d(device.address)
            }

            override fun onBondingRequired(device: BluetoothDevice) {
                "需要绑定".d(device.address)
            }

            override fun onLinkLossOccurred(device: BluetoothDevice) {
                "断开连接准备重新连接".d(device.address)
                RxBus.getInstance().postSticky(ClothingConnectBus(false))
            }

            override fun onBonded(device: BluetoothDevice) {
                "绑定".d(device.address)
            }

            //initialization 中的操作都结束时调用
            override fun onDeviceReady(device: BluetoothDevice) {
                "设备准备好了".d(device.address)
                RxBus.getInstance().postSticky(ClothingConnectBus(true))
            }

            override fun onError(device: BluetoothDevice, message: String, errorCode: Int) {
                "异常:$message:状态码：$errorCode".d(device.address)
            }

            override fun onDeviceConnecting(device: BluetoothDevice) {
                "正在连接".d(device.address)
            }
        })

        connect(bleDevice)
                .retry(Int.MAX_VALUE)
                .timeout(10 * 1000)
                .useAutoConnect(true)
                .enqueue()
    }

    fun disConnect() {
        refreshDeviceCache().enqueue()
        disconnect().enqueue()
    }


    fun isConnect(): Boolean {
        return isConnected
    }

    fun String.d(tag: String = "【MyBleManager】") {
        Log.d(tag, this)
    }

}