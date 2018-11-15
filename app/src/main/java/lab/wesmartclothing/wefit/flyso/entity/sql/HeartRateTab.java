package lab.wesmartclothing.wefit.flyso.entity.sql;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.io.Serializable;
import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity.sql
 * @FileName HeartRateTab
 * @Date 2018/11/6 17:29
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
@Table(name = "HeartRateTabs")
public class HeartRateTab extends Model implements Serializable {

    @Column
    public int heartRate = 0;//心率数据

    @Column
    public long heartTime = 0;//心率时间

    @Column
    public int stepTime = 0;//心率间隔

    @Column
    public boolean isfree = true;//是否是自由运动

    @Column
    public int count = 0;//分次


    public HeartRateTab() {
        super();
    }


    public static List<HeartRateTab> getAll() {
        return new Select()
                .from(HeartRateTab.class)
                .orderBy("Id ASC")
                .execute();
    }


    public static List<HeartRateTab> getFree(boolean isFree) {
        return new Select()
                .from(HeartRateTab.class)
                .where("isfree = ?", isFree)
                .orderBy("RANDOM()")
                .execute();
    }

    public static void update(long addTime, String searchKey) {
        new Update(HeartRateTab.class)
                .set("addTime = ?", addTime)
                .where("searchKey = ?", searchKey)
                .execute();
    }


    public static void deleteAll() {
        new Delete().from(HeartRateTab.class).execute();
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

    public boolean isIsfree() {
        return isfree;
    }

    public void setIsfree(boolean isfree) {
        this.isfree = isfree;
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
                ", isfree=" + isfree +
                '}';
    }
}
