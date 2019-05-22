package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity.sql
 * @FileName HeartRateTab
 * @Date 2018/11/6 17:29
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class HeartRateItemBean implements Serializable {

    public int heartRate = 0;//心率数据

    public long heartTime = 0;//心率时间

    public int stepTime = 0;//心率间隔
    public int step;
    public int pace;


    public HeartRateItemBean() {
    }


    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public long getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }

    public int getStepTime() {
        return stepTime;
    }

    public void setStepTime(int stepTime) {
        this.stepTime = stepTime;
    }


    @Override
    public String toString() {
        return "HeartRateItemBean{" +
                "heartRate=" + heartRate +
                ", heartTime=" + heartTime +
                ", stepTime=" + stepTime +
                ", step=" + step +
                ", pace=" + pace +
                '}';
    }
}
