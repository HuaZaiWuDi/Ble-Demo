package lab.wesmartclothing.wefit.flyso.utils;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName EnergyUtil
 * @Date 2018/11/23 17:45
 * @Author JACK
 * @Describe TODO综合热量消耗
 * @Project Android_WeFit_2.0
 */
public class EnergyUtil {


    /**
     * 综合消耗 = 运动消耗 + 基础代谢*（当前时间/24） -饮食摄入；
     *
     * @param sportingKcal
     * @param heatKcal
     * @param baseKcal
     * @return
     */
    public static double energy(double sportingKcal, double heatKcal, double baseKcal) {
//        float v = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) / 24f;
        float v = 1f;
        return sportingKcal + (int) (baseKcal * (v)) - heatKcal;
    }

}
