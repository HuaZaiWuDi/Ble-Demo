package lab.wesmartclothing.wefit.flyso.utils;

import java.math.BigDecimal;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName BLEUtil
 * @Date 2018/12/6 16:15
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class BLEUtil {


    public static void main(String[] a) {

        for (int i = 30; i <= 110; i++) {
            System.out.println("rssi:" + i + "---" + rssi2Distance(i, 2));
        }

    }

    /**
     * 信号强度与距离的公司
     * <p>
     * 计算公式：
     * <p>
     * d = 10^((abs(RSSI) - A) / (10 * n))
     * <p>
     * 其中：
     * <p>
     * d - 计算所得距离
     * <p>
     * RSSI - 接收信号强度（负值）
     * <p>
     * A - 发射端和接收端相隔1米时的信号强度
     * <p>
     * n - 环境衰减因子
     */
    public static double rssi2Distance(int rssi, int scale) {
        rssi = Math.abs(rssi);
        double power = (rssi - 59) / (10 * 4.5);
        BigDecimal bigDecimal = new BigDecimal(Math.pow(10, power));
        BigDecimal decimal = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);//保留小数点后2位，直接去掉值。

        return decimal.doubleValue();
    }
}
