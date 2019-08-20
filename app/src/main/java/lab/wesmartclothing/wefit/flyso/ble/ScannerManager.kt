package lab.wesmartclothing.wefit.flyso.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.buryingpoint.BuryingPoint
import lab.wesmartclothing.wefit.flyso.entity.BindDeviceBean
import lab.wesmartclothing.wefit.flyso.tools.BleKey
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName ScannerManager
 * @Date 2019/8/14 15:46
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
@SuppressLint("StaticFieldLeak")
object ScannerManager {

    private var context: Context = MyAPP.sMyAPP


    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    fun isBLEEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun enableBLE(): Boolean {
        if (!isBLEEnabled())
            return bluetoothAdapter.enable()
        else return true
    }

    fun disableBLE(): Boolean {
        if (isBLEEnabled())
            return bluetoothAdapter.disable()
        else return true
    }

    fun startScan() {
        stopScan()

        val build = ScanSettings.Builder()
//                .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().startScan(null, build, scanCallback)
        BuryingPoint.clothingState("", "开启：${isBLEEnabled()}")
    }


    fun stopScan() {
        if (isBLEEnabled())
            BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
        BuryingPoint.clothingState("", "停止扫描：${isBLEEnabled()}")
    }


    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val macAddress = result.device.address
            val name = result.device.name?.trim() ?: "UNKNOWN"

            when (name) {
                BleKey.Smart_Clothing -> {
                    if (macAddress == MyAPP.getgUserInfo().clothesMacAddr) {
                        MyBleManager.instance().doConnect(result.device)
                        BuryingPoint.clothingState(macAddress, "扫描到绑定设备$name-$macAddress")
                    } else {
                        val bindDeviceBean = BindDeviceBean(BleKey.TYPE_CLOTHING,
                                context.getString(R.string.clothing), macAddress, result.rssi)
                        bindDeviceBean.bluetoothDevice = result.device
                        RxBus.getInstance().post(bindDeviceBean)
                    }
                }
                BleKey.ScaleName -> {
                    val qnBleDevice = QNBleManager.getInstance().convert2QNDevice(result.device, result.rssi, result.scanRecord?.bytes)
                    if (macAddress == MyAPP.getgUserInfo().scalesMacAddr) {
                        QNBleManager.getInstance().doConnect(qnBleDevice)
                        BuryingPoint.scaleState(macAddress, "扫描到绑定设备$name-$macAddress")
                    } else {
                        val bindDeviceBean = BindDeviceBean(BleKey.TYPE_SCALE,
                                context.getString(R.string.scale), macAddress, result.rssi)
                                .apply {
                                    this.qnBleDevice = qnBleDevice
                                }
                        RxBus.getInstance().post(bindDeviceBean)
                    }
                }
                BleKey.EMS_Clothing -> {
                    if (macAddress == MyAPP.getgUserInfo().emsMacAddr) {
                        EMSManager.instance.doConnect(result.device)
                        BuryingPoint.emsState(macAddress, "扫描到绑定设备$name-$macAddress")
                    } else {
                        val bindDeviceBean = BindDeviceBean(BleKey.TYPE_EMS,
                                context.getString(R.string.EMS_clothing), macAddress, result.rssi)
                                .apply {
                                    bluetoothDevice = result.device
                                }
                        RxBus.getInstance().post(bindDeviceBean)
                    }
                }
                else -> {
//                    "其他设备：$name".d()
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


}