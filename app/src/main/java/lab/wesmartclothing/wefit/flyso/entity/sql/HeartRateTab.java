package lab.wesmartclothing.wefit.flyso.entity.sql;

import java.io.Serializable;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity.sql
 * @FileName HeartRateTab
 * @Date 2018/11/6 17:29
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class HeartRateTab implements Serializable {

    private int heartRate = 0;//心率数据
    private long heartTime = 0;//心率时间
    private int stepTime = 0;//心率间隔
    private boolean isfree = true;//是否是自由运动


    public boolean isIsfree() {
        return isfree;
    }

    public void setIsfree(boolean isfree) {
        this.isfree = isfree;
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


    public HeartRateTab() {
        super();
    }


    @Override
    public String toString() {
        return "HeartRateTab{" +
                "heartRate=" + heartRate +
                ", heartTime=" + heartTime +
                ", stepTime=" + stepTime +
                ", isfree=" + isfree +
                '}';
    }
}
