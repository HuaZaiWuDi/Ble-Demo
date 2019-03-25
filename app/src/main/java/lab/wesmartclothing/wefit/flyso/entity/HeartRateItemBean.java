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


    public int count = 0;//分次


    public HeartRateItemBean() {
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


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "HeartRateTab{" +
                "heartRate=" + heartRate +
                ", heartTime=" + heartTime +
                ", stepTime=" + stepTime +
                '}';
    }
}
