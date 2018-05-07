package com.vondear.rxtools.model.unbond;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * 项目名称：Ali_Sophix
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/12/21
 */
public class BleBondBroadcast extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (intent.getAction().equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {

//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            //TODO 判断设备地址是否是自己需要连接的地址
            if (device.getAddress().equals("需要连接的MAC地址"))
                try {
                    ClsUtlis.setPin(device.getClass(), device, "0000");
                    ClsUtlis.createBond(device.getClass(), device);
                    ClsUtlis.cancelPairingUserInput(device.getClass(), device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //TODO 设置广播优先级最高，收到广播后禁止向下传播--android:priority=”2147483647“
            if (Build.VERSION.SDK_INT >= 19)
                device.setPairingConfirmation(true);
            abortBroadcast();
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDED:
                    Log.d(TAG, "已配对");
                    break;
                case BluetoothDevice.BOND_BONDING:
                    Log.d(TAG, "正在配对");
                    break;
                case BluetoothDevice.BOND_NONE:
                    Log.d(TAG, "取消配对");
                    break;
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {


        }
    }
}
