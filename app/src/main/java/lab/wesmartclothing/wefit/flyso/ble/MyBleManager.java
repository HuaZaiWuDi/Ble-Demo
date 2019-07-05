//package lab.wesmartclothing.wefit.flyso.ble;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//
//import com.vondear.rxtools.utils.RxLogUtils;
//
//import java.util.UUID;
//
//import androidx.annotation.NonNull;
//import lab.wesmartclothing.wefit.flyso.ble.listener.BleCallBack;
//import lab.wesmartclothing.wefit.flyso.ble.listener.BleChartChangeCallBack;
//import no.nordicsemi.android.ble.BleManager;
//import no.nordicsemi.android.ble.BleManagerCallbacks;
//
///**
// * @Package lab.wesmartclothing.wefit.flyso.ble
// * @FileName MyBleManager
// * @Date 2019/6/24 11:32
// * @Author JACK
// * @Describe TODO
// * @Project Android_WeFit_2.0
// */
//public class MyBleManager extends BleManager<BleManagerCallbacks> {
//
//    private Context applicationContext;
//    private static BluetoothManager bluetoothManager;
//    public BluetoothGattCharacteristic writeChar;
//    public BluetoothGattCharacteristic notifyChar;
//
//    private static MyBleManager instance = null;
//
//
//    public static MyBleManager getInstance(Context applicationContext) {
//        if (instance == null) {
//            instance = new MyBleManager(applicationContext);
//        }
//        return instance;
//    }
//
//    /**
//     * The manager constructor.
//     * <p>
//     * After constructing the manager, the callbacks object must be set with
//     * {@link #setGattCallbacks(BleManagerCallbacks)}.
//     * <p>
//     *
//     * @param context the context.
//     */
//    public MyBleManager(@NonNull Context context) {
//        super(context);
//        applicationContext = context.getApplicationContext();
//        bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
//    }
//
////    public static boolean isConnect() {
////        List<BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
////
////    }
//
//
//    public void startScan() {
//        if (!isBLEEnabled()) {
//            enableBLE();
//            return;
//        }
//
//    }
//
//
//    public static boolean isBLEEnabled() {
//        BluetoothAdapter adapter = bluetoothManager.getAdapter();
//
//        return adapter != null && adapter.isEnabled();
//    }
//
//    private static void enableBLE() {
//        BluetoothAdapter adapter = bluetoothManager.getAdapter();
//        if (adapter != null) adapter.enable();
//    }
//
//    private static void disableBLE() {
//        BluetoothAdapter adapter = bluetoothManager.getAdapter();
//        if (adapter != null) adapter.disable();
//    }
//
//
//    @NonNull
//    @Override
//    protected BleManagerGattCallback getGattCallback() {
//        return new BleManagerGattCallback() {
//
//            @Override
//            protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt) {
//                BluetoothGattService service = gatt.getService(UUID.fromString(BleKey.UUID_Servie));
//                if (service == null) return false;
//                writeChar =
//                        service.getCharacteristic(UUID.fromString(BleKey.UUID_CHART_WRITE));
//                notifyChar =
//                        service.getCharacteristic(UUID.fromString(BleKey.UUID_CHART_READ_NOTIFY));
//                return writeChar != null && notifyChar != null;
//            }
//
//            @Override
//            protected void onDeviceDisconnected() {
//                writeChar = null;
//                notifyChar = null;
//            }
//
//            @Override
//            protected void initialize() {
//                super.initialize();
//                //监听通知
//                setNotificationCallback(notifyChar)
//                        .with((device, data) -> {
//                            Byte aByte = data.getByte(2);
//                            if (aByte == null) return;
//
//                            byte[] value = data.getValue();
//                            if (bleChartChange != null && aByte == mBytes[2]) {
//                                bleChartChange.callBack(data.getValue());
//                            }
//                            //notify数据
//                            if (mBleCallBack != null && aByte == 0x07) {
//                                mBleCallBack.onNotify(data.getValue());
//                            }
//                        });
//
//                enableNotifications(notifyChar)
//                        .done((device) -> {
//                            RxLogUtils.d("通知开启成功");
//                        })
//                        .fail((device, status) -> doFail(device, status)).enqueue();
//            }
//        };
//    }
//
//    byte[] mBytes;
//    BleChartChangeCallBack bleChartChange;
//    private BleCallBack mBleCallBack;
//
//
//    public void write(byte[] bytes) {
//        writeCharacteristic(writeChar, bytes)
//                .done((device) -> {
//                    mBytes = bytes;
//                })
//                .fail(this::doFail).enqueue();
//    }
//
//    private void doFail(BluetoothDevice device, int status) {
//        RxLogUtils.d("执行失败,状态：" + status + "--设备信息：" + device.toString());
//    }
//
//}
