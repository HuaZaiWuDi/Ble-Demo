package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;


/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName SportingDetailBean
 * @Date 2018/11/7 17:06
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class SportingDetailBean {


    /**
     * aerobicCalorie : 0
     * age : 18
     * anaerobicCalorie : 0
     * athlDate : 2018-11-07T08:57:09.109Z
     * athlDesc : string
     * athlScore : 0
     * avgHeart : 1
     * birthday : 2018-11-07T08:57:09.109Z
     * calorie : 1
     * createTime : 1511248354000
     * createUser : 1
     * duration : 1
     * endTime : 2018-11-07T08:57:09.109Z
     * gid : 1
     * greaseCalorie : 0
     * heartCount : 1
     * height : 170
     * kilometers : 1
     * limitCalorie : 0
     * maxHeart : 1
     * minHeart : 1
     * planAthlList : [{"duration":0,"range":0}]
     * planFlag : 0
     * realAthlList : [{"heartRate":120,"heartTime":"2018-11-07T08:57:09.109Z","stepTime":2}]
     * restCalorie : 0
     * sex : 1
     * startTime : 2018-11-07T08:57:09.109Z
     * status : 101
     * stepNumber : 1
     * updateTime : 1511248354000
     * updateUser : 1
     * userId : string
     * warmCalorie : 0
     */

    private int aerobicCalorie;
    private int anaerobicCalorie;
    private long athlDate;
    private String athlDesc;
    private double athlScore;
    private int avgHeart;
    private double calorie;
    private int duration;
    private long endTime;
    private int greaseCalorie;
    private int heartCount;
    private int height;
    private int kilometers;
    private int limitCalorie;
    private int maxHeart;
    private int minHeart;
    private int planFlag;
    private int restCalorie;
    private long startTime;
    private int status;
    private int stepNumber;
    private long updateTime;
    private int warmCalorie;
    private List<AthlPlanListBean> planAthlList;
    private List<RealAthlListBean> realAthlList;
    private int planTotalDeplete;
    private double complete;


    public double getComplete() {
        return complete;
    }

    public int getPlanTotalDeplete() {
        return planTotalDeplete;
    }

    public int getAerobicCalorie() {
        return aerobicCalorie;
    }

    public int getAnaerobicCalorie() {
        return anaerobicCalorie;
    }

    public long getAthlDate() {
        return athlDate;
    }

    public String getAthlDesc() {
        return athlDesc;
    }

    public double getAthlScore() {
        return athlScore;
    }

    public int getAvgHeart() {
        return avgHeart;
    }

    public double getCalorie() {
        return calorie;
    }

    public int getDuration() {
        return duration;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getGreaseCalorie() {
        return greaseCalorie;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public int getHeight() {
        return height;
    }

    public int getKilometers() {
        return kilometers;
    }

    public int getLimitCalorie() {
        return limitCalorie;
    }

    public int getMaxHeart() {
        return maxHeart;
    }

    public int getMinHeart() {
        return minHeart;
    }

    public int getPlanFlag() {
        return planFlag;
    }

    public int getRestCalorie() {
        return restCalorie;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getStatus() {
        return status;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public int getWarmCalorie() {
        return warmCalorie;
    }

    public List<AthlPlanListBean> getPlanAthlList() {
        return planAthlList;
    }

    public List<RealAthlListBean> getRealAthlList() {
        return realAthlList;
    }


    public void setAerobicCalorie(int aerobicCalorie) {
        this.aerobicCalorie = aerobicCalorie;
    }

    public void setAnaerobicCalorie(int anaerobicCalorie) {
        this.anaerobicCalorie = anaerobicCalorie;
    }

    public void setAthlDate(long athlDate) {
        this.athlDate = athlDate;
    }

    public void setAthlDesc(String athlDesc) {
        this.athlDesc = athlDesc;
    }

    public void setAthlScore(double athlScore) {
        this.athlScore = athlScore;
    }

    public void setAvgHeart(int avgHeart) {
        this.avgHeart = avgHeart;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setGreaseCalorie(int greaseCalorie) {
        this.greaseCalorie = greaseCalorie;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public void setLimitCalorie(int limitCalorie) {
        this.limitCalorie = limitCalorie;
    }

    public void setMaxHeart(int maxHeart) {
        this.maxHeart = maxHeart;
    }

    public void setMinHeart(int minHeart) {
        this.minHeart = minHeart;
    }

    public void setPlanFlag(int planFlag) {
        this.planFlag = planFlag;
    }

    public void setRestCalorie(int restCalorie) {
        this.restCalorie = restCalorie;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setWarmCalorie(int warmCalorie) {
        this.warmCalorie = warmCalorie;
    }

    public void setPlanAthlList(List<AthlPlanListBean> planAthlList) {
        this.planAthlList = planAthlList;
    }

    public void setRealAthlList(List<RealAthlListBean> realAthlList) {
        this.realAthlList = realAthlList;
    }

    public void setPlanTotalDeplete(int planTotalDeplete) {
        this.planTotalDeplete = planTotalDeplete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }

    public static class RealAthlListBean {
        /**
         * heartRate : 120
         * heartTime : 2018-11-07T08:57:09.109Z
         * stepTime : 2
         */

        private int heartRate;
        private String heartTime;
        private int stepTime;

        public int getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }

        public String getHeartTime() {
            return heartTime;
        }

        public void setHeartTime(String heartTime) {
            this.heartTime = heartTime;
        }

        public int getStepTime() {
            return stepTime;
        }

        public void setStepTime(int stepTime) {
            this.stepTime = stepTime;
        }
    }
}
