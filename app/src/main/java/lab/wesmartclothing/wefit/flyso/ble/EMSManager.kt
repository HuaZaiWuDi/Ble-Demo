package lab.wesmartclothing.wefit.flyso.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.vondear.rxtools.activity.RxActivityUtils
import com.vondear.rxtools.utils.RxLogUtils
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.buryingpoint.BuryingPoint
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink
import lab.wesmartclothing.wefit.flyso.rxbus.EMSConnectBus
import lab.wesmartclothing.wefit.flyso.rxbus.HeartRateChangeBus
import lab.wesmartclothing.wefit.flyso.tools.BleKey
import lab.wesmartclothing.wefit.flyso.ui.ems.EmsDialogFragment
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.BleManagerCallbacks
import no.nordicsemi.android.ble.data.Data
import java.util.*

/**
 * @Package lab.wesmartclothing.wefit.flyso.ble
 * @FileName WesmartFlaovarManager
 * @Date 2019/8/14 15:37
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
class EMSManager(context: Context) : BleManager<BleManagerCallbacks>(context), BleInterface<BluetoothDevice> {


    private val UUID_SERVICE = "00010203-0405-0607-0809-0a0b0c0d1911"//设置主服务的uuid
    private val UUID_NOTIFY = "00010203-0405-0607-0809-0a0b0c0d2b18"//设置可写特征的uuid
    private val UUID_WRITE = "00010203-0405-0607-0809-0a0b0c0d2b19"//设置可写特征的uuid

    companion object {
        val instance: EMSManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            EMSManager(MyAPP.sMyAPP)
        }
    }

    var writeChar: BluetoothGattCharacteristic? = null
    var notifyChar: BluetoothGattCharacteristic? = null


    override fun getGattCallback() = mBleCallback


    private val mBleCallback = object : BleManagerGattCallback() {
        override fun initialize() {
            super.initialize()
            setNotificationCallback(notifyChar)
                    .with { device, data ->
                        "蓝牙通知数据$data".d()
                        EMSApi.handleBleData(data.value)
                    }

//            requestMtu(200)
//                    .fail(this@WesmartFlaovarManager::doFail)
//                    .done {
//                        "设置MTU成功:200".d(it.address)
//                    }
//                    .enqueue()

            enableNotifications(notifyChar)
                    .done { device ->
                        "开启通知成功".d(device.address)
                        if (!MyBleManager.DFUStarting)
                            initData()
                    }
                    .fail(this@EMSManager::doFail)
                    .enqueue()
        }

        override fun onDeviceDisconnected() {
            writeChar = null
            notifyChar = null
        }


        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val service = gatt.getService(UUID.fromString(UUID_SERVICE)) ?: return false
            writeChar = service.getCharacteristic(UUID.fromString(UUID_WRITE))
            notifyChar = service.getCharacteristic(UUID.fromString(UUID_NOTIFY))

            BuryingPoint.emsState(bluetoothDevice?.address
                    ?: "", "设备是否支持：${writeChar != null && notifyChar != null}")
            return writeChar != null && notifyChar != null
        }
    }

    private fun initData() {
        val dialogFragment = EmsDialogFragment.newInstance()
        dialogFragment.show((RxActivityUtils.currentActivity() as AppCompatActivity).supportFragmentManager, "EmsDialogFragment")
        EMSApi.getNotifyData()
        RxBus.getInstance().post(HeartRateChangeBus(null))

        //设备统计
        val deviceLink = DeviceLink()
        deviceLink.macAddr = MyAPP.getgUserInfo().emsMacAddr
        deviceLink.deviceNo = BleKey.TYPE_EMS
        deviceLink.deviceLink(deviceLink)

    }

    fun write(bytes: ByteArray) {
        Data(bytes).d("EMS写数据")
        if (!isConnect()) {
            return
        }
        writeCharacteristic(writeChar, bytes)
                .done { device ->

                }
                .fail { device, status ->
                    doFail(device, status)

                    write(bytes)
                }
                .enqueue()
    }

    private fun doFail(device: BluetoothDevice, status: Int) {
        RxLogUtils.d("执行失败,状态：$status--设备信息：$device")
        BuryingPoint.emsState(device.address, "执行失败,状态：$status}")
    }

    override fun getDevice(): BluetoothDevice? {
        return bluetoothDevice
    }

    override fun doConnect(bleDevice: BluetoothDevice) {
        "开始连接EMS".d()
        setGattCallbacks(object : BleManagerCallbacks {
            override fun onDeviceDisconnecting(device: BluetoothDevice) {
                "正在断开连接".d(device.address)
                BuryingPoint.emsState(device.address, "正在断开连接")
            }

            override fun onDeviceDisconnected(device: BluetoothDevice) {
                "断开连接不在重连".d(device.address)
                BuryingPoint.emsState(device.address, "断开连接不在重连")
            }

            override fun onDeviceConnected(device: BluetoothDevice) {
                "设备连接".d(device.address)
                BuryingPoint.emsState(device.address, "设备连接")
            }

            override fun onDeviceNotSupported(device: BluetoothDevice) {
                "设备不支持".d(device.address)
                BuryingPoint.emsState(device.address, "设备不支持")
            }

            override fun onBondingFailed(device: BluetoothDevice) {
                "绑定失败".d(device.address)
            }

            override fun onServicesDiscovered(device: BluetoothDevice, optionalServicesFound: Boolean) {
                "发现服务".d(device.address)
                BuryingPoint.emsState(device.address, "发现服务")
            }

            override fun onBondingRequired(device: BluetoothDevice) {
                "需要绑定".d(device.address)
            }

            override fun onLinkLossOccurred(device: BluetoothDevice) {
                "断开连接准备重新连接".d(device.address)
                RxBus.getInstance().postSticky(EMSConnectBus(false))
                BuryingPoint.emsState(device.address, "准备重新连接")
            }

            override fun onBonded(device: BluetoothDevice) {
                "绑定".d(device.address)
            }

            //initialization 中的操作都结束时调用
            override fun onDeviceReady(device: BluetoothDevice) {
                "设备准备好了".d(device.address)
                RxBus.getInstance().postSticky(EMSConnectBus(true))
                BuryingPoint.emsState(device.address, "设备准备")
            }

            override fun onError(device: BluetoothDevice, message: String, errorCode: Int) {
                "异常:$message:状态码：$errorCode".d(device.address)
                BuryingPoint.emsState(device.address, "异常:$message:状态码：$errorCode")
            }

            override fun onDeviceConnecting(device: BluetoothDevice) {
                "正在连接".d(device.address)
                BuryingPoint.emsState(device.address, "正在连接")
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

    override fun isBinded(): Boolean {
        return BluetoothAdapter.checkBluetoothAddress(MyAPP.getgUserInfo().emsMacAddr)
    }

    override fun bind(bleDevice: BluetoothDevice) {
        //瘦身衣和EMS瘦身衣为互斥
        //解绑AI瘦身衣
        MyAPP.getgUserInfo().clothesMacAddr = ""
        MyBleManager.instance.disConnect()

        //连接EMS
        disConnect()
        doConnect(bleDevice)
        MyAPP.getgUserInfo().emsMacAddr = bleDevice.address

        BuryingPoint.emsState(bleDevice.address, "绑定设备：")
    }

    override fun unBind() {
        //删除绑定
        disConnect()
        MyAPP.getgUserInfo().emsMacAddr = ""
    }

}