package lab.wesmartclothing.wefit.flyso.rxbus;

import java.util.List;

/**
 * Created icon_hide_password jk on 2018/6/14.
 */
public class SportsDataTab {

    List<Integer> athlRecord_2;
    int steps;
    int duration;
    double kcal;
    int maxHeart;
    int minHeart;
    int curHeart;

    public List<Integer> getAthlRecord_2() {
        return athlRecord_2;
    }

    public void setAthlRecord_2(List<Integer> athlRecord_2) {
        this.athlRecord_2 = athlRecord_2;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public int getMaxHeart() {
        return maxHeart;
    }

    public void setMaxHeart(int maxHeart) {
        this.maxHeart = maxHeart;
    }

    public int getMinHeart() {
        return minHeart;
    }

    public void setMinHeart(int minHeart) {
        this.minHeart = minHeart;
    }

    public int getCurHeart() {
        return curHeart;
    }

    public void setCurHeart(int curHeart) {
        this.curHeart = curHeart;
    }


    @Override
    public String toString() {
        return "SportsDataTab{" +
                ", steps=" + steps +
                ", duration=" + duration +
                ", kcal=" + kcal +
                ", maxHeart=" + maxHeart +
                ", minHeart=" + minHeart +
                ", curHeart=" + curHeart +
                '}';
    }
}
