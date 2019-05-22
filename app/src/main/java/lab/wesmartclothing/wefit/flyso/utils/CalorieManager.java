package lab.wesmartclothing.wefit.flyso.utils;

import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;

import java.util.Calendar;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName CalorieManager
 * @Date 2019/5/16 11:42
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class CalorieManager {


    /**
     * 运动距离转换成卡路里
     * <p>
     * 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
     * <p>
     * 例如：体重60公斤的人，长跑8公里，那么消耗的热量＝60×8×1.036＝497.28 kcal(千卡)
     *
     * @return
     */
    public static double run2Calorie(double weight, double kilometre) {
        return weight * kilometre * 1.036 * 2.5;
    }

    /**
     * 步幅（cm）=身高（cm）*0.45
     *
     * @param height
     * @return
     */
    public static double getStride(double height) {
        return height * 0.45;
    }


    /**
     * 距离（cm）=步幅（cm）*步数
     *
     * @param height
     * @return
     */
    public static double getKilometre(double height, int steps) {
        return getStride(height) * steps * 0.00001;
    }

    /**
     * 步频（/h）=步数（int）/时长(小时)
     *
     * @param steps
     * @param time
     * @return
     */
    public static double getStepRate(int steps, double time) {
        return steps / time;
    }

    /**
     * 配速（格式化时间/km）=时长(秒)/距离（km）
     *
     * @param kilometre
     * @param time
     * @return 11'11''
     */
    public static int getStepSpeed(double time, double kilometre) {
        return (int) (time / kilometre);
    }


    /**
     * icon_men_select：卡路里=((-55.0969 + (0.6309 x HR) + (0.1988 x W) + (0.2017 x A))/4.184) x 60 x T
     * 女：卡路里=((-20.4022 + (0.4472 x HR) - (0.1263 x W) + (0.074 x A))/4.184) x 60 x T
     * <p>
     * HR =心率（以节拍/分钟计）
     * W =体重（以公斤计）
     * A =年龄（以年计）
     * T =运动持续时间（小时）double
     *
     * @param HR 心率区间;
     * @param T  =运动持续时间（小时）double
     * @return 千卡
     */
    public static double getCalorie(int HR, double T) {
        UserInfo info = MyAPP.gUserInfo;
        if (info == null) {
            RxLogUtils.e("UserInfo is null");
            return 0;
        }

        int sex = info.getSex();
        float W = SPUtils.getFloat(SPKey.SP_realWeight);
        long birthDayMillis = info.getBirthday();

        Calendar calendar = Calendar.getInstance();
        int newYear = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(birthDayMillis);
        int birthYear = calendar.get(Calendar.YEAR);
        int A = newYear - birthYear;

        double kcal = 0;
        if (sex == 1) {
            kcal = ((-55.0969 + (0.6309 * HR) + (0.1988 * W) + (0.2017 * A)) / 4.184) * 60 * T;
        } else {
            kcal = ((-20.4022 + (0.4472 * HR) - (0.1263 * W) + (0.074 * A)) / 4.184) * 60 * T;
        }
        kcal = RxFormatValue.format4S5R(Math.abs(kcal), 1);

        return addCalorie(HR, kcal);
    }


    /**
     * 不同心率区间调节消耗热量的kcal
     *
     * @param HR
     * @param calorie
     * @return
     */
    private static double addCalorie(int HR, double calorie) {
        int[] HRRates = Key.HRART_SECTION;
        int HR_0 = HRRates[0] & 0xff;
        int HR_1 = HRRates[1] & 0xff;
        int HR_2 = HRRates[2] & 0xff;
        int HR_3 = HRRates[3] & 0xff;
        int HR_4 = HRRates[4] & 0xff;
        int HR_5 = HRRates[5] & 0xff;
        int HR_6 = HRRates[6] & 0xff;
        if (HR < HR_2) {
            calorie *= 1.08f;
        } else if (HR >= HR_2 && HR < HR_3) {
            calorie *= 1.22f;
        } else if (HR >= HR_3 && HR < HR_4) {
            calorie *= 1.38f;
        } else if (HR >= HR_4 && HR < HR_5) {
            calorie *= 1.55f;
        } else if (HR >= HR_5) {
            calorie *= 1.06f;
        }
        return calorie;
    }

}
