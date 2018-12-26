package lab.wesmartclothing.wefit.flyso.entity;

import lab.wesmartclothing.wefit.flyso.tools.Key;

public class AthlPlanListBean {
    /**
     * range : 0：静息，1：热身，2：燃脂，3：有氧，4：无氧，5：危险
     * duration : 5
     */

    private int range;
    private int duration;
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    /**
     * 获取心率区间名称
     *
     * @return
     */
    public String strRange() {
        switch (range) {
            case 0:
                return "静息";
            case 1:
                return "热身";
            case 2:
                return "燃脂";
            case 3:
                return "有氧";
            case 4:
                return "无氧";
            case 5:
                return "危险";
        }
        return "静息";
    }

    /**
     * 获取心率对应的范围
     *
     * @return
     */
    public int getHeartRange() {
        byte[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0] & 0xff;
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;
        int heart_5 = heartRates[5] & 0xff;
        int heart_6 = heartRates[6] & 0xff;

        switch (range) {
            case 0:
                return heart_0;
            case 1:
                return heart_1;
            case 2:
                return heart_2;
            case 3:
                return heart_3;
            case 4:
                return heart_4;
            case 5:
                return heart_5;
        }
        return heart_6;
    }

    /**
     * 获取区间中间值
     *
     * @return
     */
    public int getMidRange() {
        byte[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0] & 0xff;
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;
        int heart_5 = heartRates[5] & 0xff;
        int heart_6 = heartRates[6] & 0xff;

        int midValue = (heartRates[6] & 0xff - heartRates[5] & 0xff) / 2;

        switch (range) {
            case 0://静息
                return heart_0;
            case 1://热身
                return heart_1 + midValue;
            case 2://燃脂
                return heart_2 + midValue;
            case 3://有氧
                return heart_3 + midValue;
            case 4://无氧
                return heart_4 + midValue;
            case 5://极限
                return heart_5 + midValue;
        }
        return heart_6;
    }


    public int getRange2() {
        byte[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0] & 0xff;
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;
        int heart_5 = heartRates[5] & 0xff;
        int heart_6 = heartRates[6] & 0xff;

        switch (range) {
            case 0:
                return heart_1;
            case 1:
                return heart_2;
            case 2:
                return heart_3;
            case 3:
                return heart_4;
            case 4:
                return heart_5;
            case 5:
                return heart_6;
        }
        return heart_6;
    }


    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AthlPlanListBean{" +
                "range=" + range +
                ", duration=" + duration +
                ", time=" + time +
                '}';
    }
}