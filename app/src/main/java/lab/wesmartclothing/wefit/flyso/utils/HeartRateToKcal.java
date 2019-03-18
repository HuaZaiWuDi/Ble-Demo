package lab.wesmartclothing.wefit.flyso.utils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;

import java.util.Calendar;

import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created icon_hide_password jk on 2018/6/13.
 */
public class HeartRateToKcal {


    /**
     * icon_men_select：卡路里=((-55.0969 + (0.6309 x HR) + (0.1988 x W) + (0.2017 x A))/4.184) x 60 x T
     * 女：卡路里=((-20.4022 + (0.4472 x HR) - (0.1263 x W) + (0.074 x A))/4.184) x 60 x T
     * <p>
     * HR =心率（以节拍/分钟计）
     * W =体重（以公斤计）
     * A =年龄（以年计）
     * T =运动持续时间（小时）double
     **/


    /**
     * @param HR 心率区间;
     * @param T  =运动持续时间（小时）double
     * @return 千卡
     */
    public static double getCalorie(int HR, double T) {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = JSON.parseObject(string, UserInfo.class);
        if (info == null) {
            RxLogUtils.e("UserInfo is null");
            return 0;
        }

        int sex = info.getSex();
        float W = SPUtils.getFloat(SPKey.SP_realWeight, SPUtils.getInt(SPKey.SP_weight));
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
        byte[] HRRates = Key.HRART_SECTION;
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


    /**
     * 更换卡路里计算公式
     */
    public double getCalorie2() {
        return 0;
    }


}
