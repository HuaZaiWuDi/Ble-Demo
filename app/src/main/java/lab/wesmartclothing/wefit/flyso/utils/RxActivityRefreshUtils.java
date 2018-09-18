package lab.wesmartclothing.wefit.flyso.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName RxActivityRefreshUtils
 * @Date 2018/9/14 17:20
 * @Author JACK
 * @Describe 控制UI界面刷新逻辑工具类 https://blog.csdn.net/lv_fq/article/details/62220555
 * @Project Android_WeFit
 */
public class RxActivityRefreshUtils {
    private static RxActivityRefreshUtils sRxActivityRefreshUtils;

    private static Map<Class, Boolean> activityMap = new HashMap<>();


    public static RxActivityRefreshUtils getInstance() {
        if (sRxActivityRefreshUtils == null) {
            sRxActivityRefreshUtils = new RxActivityRefreshUtils();
        }
        return sRxActivityRefreshUtils;
    }


    public boolean isRefresh(Class classze) {
        if (activityMap.get(classze) == null) {
            activityMap.put(classze, true);
        }
        boolean isRefresh = activityMap.get(classze);
        if (isRefresh)
            activityMap.put(classze, false);

        return isRefresh;
    }


    public void setRefresh(Class classze) {
        activityMap.put(classze, true);
    }

}
