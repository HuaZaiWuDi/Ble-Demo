package lab.wesmartclothing.wefit.flyso.entity;

import android.content.Context;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;

public class AthlPlanListBean {
    /**
     * range : 0：静息，1：热身，2：燃脂，3：有氧，4：无氧，5：极限
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
    public String strRange(Context context) {
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


    /**
     * 获取区间中间值
     *
     * @return
     */
    public int getMidRange() {
        int[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0];
        int heart_1 = heartRates[1];
        int heart_2 = heartRates[2];
        int heart_3 = heartRates[3];
        int heart_4 = heartRates[4];
        int heart_5 = heartRates[5];
        int heart_6 = heartRates[6];

        int midValue = (heart_6 - heart_5) / 2;

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