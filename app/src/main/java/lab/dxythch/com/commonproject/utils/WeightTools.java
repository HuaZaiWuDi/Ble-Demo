package lab.dxythch.com.commonproject.utils;

import com.yolanda.health.qnblesdk.out.QNScaleItemData;

import lab.dxythch.com.commonproject.entity.WeightAddBean;

/**
 * Created by jk on 2018/5/17.
 */
public class WeightTools {


    public static void ble2Backstage(QNScaleItemData item, WeightAddBean bean) {
        double value = item.getValue();
        switch (item.getType()) {
            case 1:
                bean.setWeight(value);
                break;
            case 2:
                bean.setBmi(value);
                break;
            case 3:
                bean.setBodyfat(value);
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
                bean.setBodyType((int) value);
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
}
