package lab.wesmartclothing.wefit.flyso.utils;

import android.content.Context;

import com.vondear.rxtools.utils.RxLogUtils;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName HeartSectionUtil
 * @Date 2018/11/16 14:04
 * @Author JACK
 * @Describe TODO用户最大心率的计算公式
 * @Project Android_WeFit_2.0
 */
public class HeartSectionUtil {


    /**
     * 8:00
     * 慢走
     * 7:00
     * 热身
     * 6:00
     * 燃脂
     * 5:00
     * 有氧
     * 4:00
     * 无氧
     * 3:00
     * 极限
     * 2:00
     *
     * <p>
     * 最大心率
     * //最大心率计算公式改为“最大心率=208-0.7*年龄”,请知悉！
     */
    public static void initMaxHeart(UserInfo info) {
//        if (info != null) {
//            //最大心率计算公式改为“最大心率=208-0.7*年龄”,请知悉！
//            int maxHeart = 190;
////            int maxHeart = (int) (208 - info.getAge() * 0.7);
//            maxHeart = info.getSex() == 1 ? (220 - info.getAge()) : (226 - info.getAge());
//            Key.HRART_SECTION[0] = (byte) (maxHeart * 0.46);
//            Key.HRART_SECTION[1] = (byte) (maxHeart * 0.55);
//            Key.HRART_SECTION[2] = (byte) (maxHeart * 0.64);
//            Key.HRART_SECTION[3] = (byte) (maxHeart * 0.73);
//            Key.HRART_SECTION[4] = (byte) (maxHeart * 0.82);
//            Key.HRART_SECTION[5] = (byte) (maxHeart * 0.91);
//            Key.HRART_SECTION[6] = (byte) (maxHeart);
//
//            for (byte b : Key.HRART_SECTION) {
//                RxLogUtils.d("心率区间：" + (b & 0xff));
//            }
//
//            Key.heartRates[0] = Key.HRART_SECTION[1];
//            Key.heartRates[1] = Key.HRART_SECTION[2];
//            Key.heartRates[2] = Key.HRART_SECTION[3];
//            Key.heartRates[3] = Key.HRART_SECTION[4];
//            Key.heartRates[4] = Key.HRART_SECTION[5];
//        }


        Key.HRART_SECTION[0] = (int) (6 * 60);
        Key.HRART_SECTION[1] = (int) (7 * 60);
        Key.HRART_SECTION[2] = (int) (8 * 60);
        Key.HRART_SECTION[3] = (int) (9 * 60);
        Key.HRART_SECTION[4] = (int) (10 * 60);
        Key.HRART_SECTION[5] = (int) (11 * 60);
        Key.HRART_SECTION[6] = (int) (12 * 60);


//        Key.heartRates[0] = Key.HRART_SECTION[1];
//        Key.heartRates[1] = Key.HRART_SECTION[2];
//        Key.heartRates[2] = Key.HRART_SECTION[3];
//        Key.heartRates[3] = Key.HRART_SECTION[4];
//        Key.heartRates[4] = Key.HRART_SECTION[5];


        for (int b : Key.HRART_SECTION) {
            RxLogUtils.d("心率区间：" + b);
        }
    }


    public static int currentSection(int heart) {
        int type = 0;
        if (heart < Key.HRART_SECTION[1]) {
            type = 0;
        } else if (heart < Key.HRART_SECTION[2]) {
            type = 1;
        } else if (heart < Key.HRART_SECTION[3]) {
            type = 2;
        } else if (heart < Key.HRART_SECTION[4]) {
            type = 3;
        } else if (heart < Key.HRART_SECTION[5]) {
            type = 4;
        } else {
            type = 5;
        }
        return type;
    }

    /**
     * 获取心率区间名称
     *
     * @return
     */
    public static String strRange(Context context, int range) {
        switch (range) {
            case 0:
                return context.getString(R.string.calm);
            case 1:
                return context.getString(R.string.warm);
            case 2:
                return context.getString(R.string.grease);
            case 3:
                return context.getString(R.string.aerobic);
            case 4:
                return context.getString(R.string.anaerobic);
            case 5:
                return context.getString(R.string.limit);
        }
        return context.getString(R.string.calm);
    }


    public static void main(String[] args) {
        initMaxHeart(null);
    }

}
