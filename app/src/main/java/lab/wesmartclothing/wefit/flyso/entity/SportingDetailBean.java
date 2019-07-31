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

    private int aerobicData;
    private int anaerobicData;
    private long athlDate;
    private String athlDesc;
    private double athlScore;
    private double avgHeart;
    private double calorie;
    private int duration;
    private long endTime;
    private int greaseData;
    private int heartCount;
    private int height;
    private double kilometers;
    private int limitData;
    private int maxHeart;
    private int minHeart;
    private int planFlag;
    private int restData;
    private long startTime;
    private int status;
    private int stepNumber;
    private long updateTime;
    private int warmData;
    private List<AthlPlanListBean> planAthlList;
    private List<HeartRateItemBean> realAthlList;
    private int planTotalDeplete;
    private double complete;
    private double avgPace;
    private double cadence;
    private int maxPace;
    private int minPace;
    private long planTotalTime;
    private String runningData;


    public double getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(double avgPace) {
        this.avgPace = avgPace;
    }

    public double getCadence() {
        return cadence;
    }

    public void setCadence(double cadence) {
        this.cadence = cadence;
    }

    public int getMaxPace() {
        return maxPace;
    }

    public void setMaxPace(int maxPace) {
        this.maxPace = maxPace;
    }

    public int getMinPace() {
        return minPace;
    }

    public void setMinPace(int minPace) {
        this.minPace = minPace;
    }

    public long getPlanTotalTime() {
        return planTotalTime;
    }

    public void setPlanTotalTime(long planTotalTime) {
        this.planTotalTime = planTotalTime;
    }

    public String getRunningData() {
        return runningData;
    }

    public void setRunningData(String runningData) {
        this.runningData = runningData;
    }

    public double getComplete() {
        return complete;
    }

    public int getPlanTotalDeplete() {
        return planTotalDeplete;
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

    public double getAvgHeart() {
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


    public int getHeartCount() {
        return heartCount;
    }

    public int getHeight() {
        return height;
    }

    public double getKilometers() {
        return kilometers;
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


    public List<AthlPlanListBean> getPlanAthlList() {
        return planAthlList;
    }

    public List<HeartRateItemBean> getRealAthlList() {
        return realAthlList;
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

    public void setAvgHeart(double avgHeart) {
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


    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
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


    public void setPlanAthlList(List<AthlPlanListBean> planAthlList) {
        this.planAthlList = planAthlList;
    }

    public void setRealAthlList(List<HeartRateItemBean> realAthlList) {
        this.realAthlList = realAthlList;
    }

    public void setPlanTotalDeplete(int planTotalDeplete) {
        this.planTotalDeplete = planTotalDeplete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }


    public int getAerobicData() {
        return aerobicData;
    }

    public void setAerobicData(int aerobicData) {
        this.aerobicData = aerobicData;
    }

    public int getAnaerobicData() {
        return anaerobicData;
    }

    public void setAnaerobicData(int anaerobicData) {
        this.anaerobicData = anaerobicData;
    }

    public int getGreaseData() {
        return greaseData;
    }

    public void setGreaseData(int greaseData) {
        this.greaseData = greaseData;
    }

    public int getLimitData() {
        return limitData;
    }

    public void setLimitData(int limitData) {
        this.limitData = limitData;
    }

    public int getRestData() {
        return restData;
    }

    public void setRestData(int restData) {
        this.restData = restData;
    }

    public int getWarmData() {
        return warmData;
    }

    public void setWarmData(int warmData) {
        this.warmData = warmData;
    }


}
