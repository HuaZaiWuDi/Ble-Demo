package lab.wesmartclothing.wefit.flyso.utils;

import com.yolanda.health.qnblesdk.out.QNScaleItemData;

import lab.wesmartclothing.wefit.flyso.entity.HealthyInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;

/**
 * Created icon_hide_password jk on 2018/5/17.
 */
public class WeightTools {


    /**
     * 1	隐形肥胖型
     * 2	运动不足型
     * 3	偏瘦型
     * 4	标准型
     * 5	偏瘦肌肉型
     * 6	肥胖型
     * 7	偏胖型
     * 8	标准肌肉型
     * 9	非常肌肉型
     */
    private static String body2String(int value) {
        String bodyType = "标准型";
        switch (value) {
            case 1:
                bodyType = "隐形肥胖型";
                break;
            case 2:
                bodyType = "运动不足型";
                break;
            case 3:
                bodyType = "偏瘦型";
                break;
            case 4:
                bodyType = "标准型";
                break;
            case 5:
                bodyType = "偏瘦肌肉型";
                break;
            case 6:
                bodyType = "肥胖型";
                break;
            case 7:
                bodyType = "偏胖型";
                break;
            case 8:
                bodyType = "标准肌肉型";
                break;
            case 9:
                bodyType = "非常肌肉型";
                break;
        }
        return bodyType;
    }

    public static void ble2Backstage(QNScaleItemData item, WeightAddBean bean) {
        if (item == null) return;
        double value = item.getValue();
        switch (item.getType()) {
            case 1:
                bean.setWeight(value);
                break;
            case 2:
                bean.setBmi(value);
                break;
            case 3:
                bean.setBodyFat(value);
                break;
            case 4:
                bean.setSubfat(value);
                break;
            case 5:
                bean.setVisfat(value);
                break;
            case 6:
                bean.setWater(value);
                break;
            case 7:
                bean.setMuscle(value);
                break;
            case 8:
                bean.setBone(value);
                break;
            case 9:
                bean.setBmr(value);
                break;
            case 10:
                bean.setBodyType(body2String((int) value));
                bean.setBodyTypeIndex((int) value);
                break;
            case 11:
                bean.setProtein(value);
                break;
            case 12://去脂体重
                bean.setBodyFfm(value);
                break;
            case 13://肌肉量
                bean.setSinew(value);
                break;
            case 14:
                bean.setBodyAge((int) value);
                break;
            case 15://分数
                bean.setHealthScore(value);
                break;
            case 16://心率
                break;
            case 17://心脏指数
                break;
        }
    }

    public static HealthyInfoBean ble2Backstage2(QNScaleItemData item) {
        HealthyInfoBean bean = new HealthyInfoBean();
        if (item == null) return null;
        double value = item.getValue();
        switch (item.getType()) {
            case 1:
                bean.setWeight(value);
                break;
            case 2:
                bean.setBmi((int) value);
                break;
            case 3:
                bean.setBodyFat(value);
                break;
            case 4:
                bean.setSubfat(value);
                break;
            case 5:
                bean.setVisfat(value);
                break;
            case 6:
                bean.setWater(value);
                break;
            case 7:
                bean.setMuscle(value);
                break;
            case 8:
                bean.setBone(value);
                break;
            case 9:
                bean.setBmr(value);
                break;
            case 10:
                bean.setBodyType(body2String((int) value));
                break;
            case 11:
                bean.setProtein(value);
                break;
            case 12://去脂体重
                bean.setBodyFfm(value);
                break;
            case 13://肌肉量
                bean.setSinew(value);
                break;
            case 14:
                bean.setBodyAge((int) value);
                break;
            case 15://分数
                bean.setHealthScore(value);
                break;
            case 16://心率
                break;
            case 17://心脏指数
                break;
        }
        return bean;
    }
}
