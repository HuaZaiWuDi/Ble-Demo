package com.vondear.rxtools.model.unbond;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 项目名称：Ali_Sophix
 * 类描述：利用反射实现与蓝牙的配对
 * 创建人：oden
 * 创建时间：2017/12/21
 */
public class ClsUtlis {


    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean createBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {

        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();

    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {

        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();

    }

    static public boolean setPin(Class btClass, BluetoothDevice btDevice,
                                 String str) throws Exception {

        try {

            Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                            {
                                    str.getBytes()
                            });
            Log.e("returnValue", "" + returnValue);

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;


    }

    static public boolean setPassKey(Class btClass, BluetoothDevice btDevice,
                                     String str) throws Exception {
        try {

            Method removeBondMethod = btClass.getDeclaredMethod("setPasskey", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                            {
                                    str.getBytes()
                            });
            Log.e("returnValue", "" + returnValue);

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return true;


    }

    // 取消用户输入
    static public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {

        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        // cancelBondProcess()
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();

    }

    // 取消配对
    static public boolean cancelBondProcess(Class btClass,
                                            BluetoothDevice device)

            throws Exception {

        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();

    }

    /**
     * @param clsShow
     */
    static public void printAllInform(Class clsShow) {

        try {

            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {

                Log.e("method name", hideMethod[i].getName() + ";and the i is:"
                        + i);

            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {

                Log.e("Field name", allFields[i].getName());

            }

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static boolean isWantedMac(String macStr) {
        if (macStr == null || macStr.isEmpty()) return false;
        if (macStr.equals("55:44:33:22:11:00")) {
            return true;
        } else return false;
    }


}
