package lab.wesmartclothing.wefit.flyso.entity;

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
        switch (range) {
            case 0:
                return 80;
            case 1:
                return 100;
            case 2:
                return 120;
            case 3:
                return 140;
            case 4:
                return 160;
            case 5:
                return 180;
        }
        return 100;
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