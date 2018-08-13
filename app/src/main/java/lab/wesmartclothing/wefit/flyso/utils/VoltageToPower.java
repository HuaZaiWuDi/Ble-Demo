package lab.wesmartclothing.wefit.flyso.utils;

import com.vondear.rxtools.utils.RxLogUtils;

/**
 * Created by jk on 2018/6/14.
 */
public class VoltageToPower {

    /**
     * 电压与电池容量的关系
     * <p>
     * 100  :  4.2
     * 90  :  4.08
     * 80  :  4
     * 70  :  3.93
     * 60  :  3.87
     * 50  :  3.82
     * 40  :  3.79
     * 30  :  3.77
     * 20  :  3.73
     * 15  :  3.7
     * 10  :  3.68
     * 5  :  3.5
     * 0  :  2.5
     */

    /**
     * 得到电池容量
     *
     * @param Voltage 电池电压
     * @return 电池容量 （%）
     */

    public int getBatteryCapacity(double Voltage) {
        int batteryCapacity = 0;
        if (Voltage >= 4.2) {
            batteryCapacity = 100;
        } else if (Voltage >= 4.08 && Voltage < 4.2) {
            batteryCapacity = 90;
        } else if (Voltage >= 4 && Voltage < 4.08) {
            batteryCapacity = 80;
        } else if (Voltage >= 3.93 && Voltage < 4) {
            batteryCapacity = 70;
        } else if (Voltage >= 3.87 && Voltage < 3.93) {
            batteryCapacity = 60;
        } else if (Voltage >= 3.82 && Voltage < 3.87) {
            batteryCapacity = 50;
        } else if (Voltage >= 3.79 && Voltage < 3.82) {
            batteryCapacity = 40;
        } else if (Voltage >= 3.77 && Voltage < 3.79) {
            batteryCapacity = 30;
        } else if (Voltage >= 3.73 && Voltage < 3.77) {
            batteryCapacity = 20;
        } else if (Voltage >= 3.7 && Voltage < 3.73) {
            batteryCapacity = 15;
        } else if (Voltage >= 3.68 && Voltage < 3.7) {
            batteryCapacity = 10;
        } else if (Voltage >= 3.5 && Voltage < 3.68) {
            batteryCapacity = 5;
        } else if (Voltage < 3.5) {
            batteryCapacity = 0;
        }
        return batteryCapacity;
    }


    /**
     * 剩余使用时长
     *
     * @param isHeating 是否在加热
     * @param Voltage   电池电压
     * @return 剩余使用时长 （小时）
     */

    public double canUsedTime(double Voltage, boolean isHeating) {
        double time = 0;
        double batteryCapacity = getBatteryCapacity(Voltage) / 100f * 2200;
        RxLogUtils.d("电池：" + batteryCapacity);
        if (isHeating) {
            time = batteryCapacity / 1000f;
        } else {
            time = batteryCapacity / 10f;
        }
        return time;
    }

}
