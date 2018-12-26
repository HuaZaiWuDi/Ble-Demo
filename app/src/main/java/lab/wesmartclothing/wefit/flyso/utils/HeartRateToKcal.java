package lab.wesmartclothing.wefit.flyso.utils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;

import java.util.Calendar;

import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
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
     * @param HR true 为男，false为女;
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
        return RxFormatValue.format4S5R(Math.abs(kcal), 1);
    }


    /**
     * 更换卡路里计算公式
     */
    public double getCalorie2() {
        return 0;
    }


}
