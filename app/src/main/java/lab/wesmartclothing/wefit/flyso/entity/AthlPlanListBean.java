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

    public int getRange() {
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

    public int getRange3() {
        return range;
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