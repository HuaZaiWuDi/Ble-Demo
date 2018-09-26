package com.vondear.rxtools.model.antishake;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 解决用户连续快速点击某个按钮
 * 使用：{AntiShake.getInstance().check(view.getId()) return;}
 */
public class AntiShake {
    public static final int MIN_CLICK_DELAY_TIME = 800;

    static AntiShake mAntiShake;

    private List<OneClickUtil> utils = new ArrayList<>();

    public static AntiShake getInstance() {
        if (mAntiShake == null) mAntiShake = new AntiShake();
        return mAntiShake;
    }

    public boolean check(Object o) {
        String flag = null;
        if (o == null)
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        else
            flag = o.toString();
        for (OneClickUtil util : utils) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClickUtil clickUtil = new OneClickUtil(flag);
        utils.add(clickUtil);
        return clickUtil.check();
    }

    public boolean check() {
        return check(null);
    }


    class OneClickUtil {
        private String methodName;

        private long lastClickTime = 0;

        public OneClickUtil(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public boolean check() {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                return false;
            } else {
                return true;
            }
        }
    }
}