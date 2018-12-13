package lab.wesmartclothing.wefit.flyso.utils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;

import java.util.Arrays;

import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

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
     * 最大心率
     * //最大心率计算公式改为“最大心率=208-0.7*年龄”,请知悉！
     */
    public static void initMaxHeart() {
        UserInfo info = JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
        if (info != null) {
            //最大心率计算公式改为“最大心率=208-0.7*年龄”,请知悉！
            int maxHeart = (int) (208 - info.getAge() * 0.7);
            Key.HRART_SECTION[0] = (byte) (maxHeart * 0.4);
            Key.HRART_SECTION[1] = (byte) (maxHeart * 0.5);
            Key.HRART_SECTION[2] = (byte) (maxHeart * 0.6);
            Key.HRART_SECTION[3] = (byte) (maxHeart * 0.7);
            Key.HRART_SECTION[4] = (byte) (maxHeart * 0.8);
            Key.HRART_SECTION[5] = (byte) (maxHeart * 0.9);
            Key.HRART_SECTION[6] = (byte) (maxHeart);
            RxLogUtils.d("心率区间：" + Arrays.toString(Key.HRART_SECTION));


            Key.heartRates[0] = (byte) (maxHeart * 0.5);
            Key.heartRates[1] = (byte) (maxHeart * 0.6);
            Key.heartRates[2] = (byte) (maxHeart * 0.7);
            Key.heartRates[3] = (byte) (maxHeart * 0.8);
            Key.heartRates[4] = (byte) (maxHeart * 0.9);
        }
    }

}
