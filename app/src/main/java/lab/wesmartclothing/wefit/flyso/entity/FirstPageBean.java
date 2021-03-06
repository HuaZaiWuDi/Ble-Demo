package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/7/13.
 */
public class FirstPageBean {


    /**
     * ableIntake : 0
     * athleticsInfoList : [{"athlDate":"2018-07-16T09:08:56.384Z","athlDesc":"string","athlRecord":"string","avgHeart":0,"calorie":0,"createTime":1511248354000,"createUser":1,"duration":0,"gid":1,"kilometers":0,"maxHeart":0,"minHeart":0,"status":101,"stepNumber":0,"updateTime":1511248354000,"updateUser":1,"userId":"string"}]
     * breakfast : 0
     * complete : 0
     * dinner : 0
     * hasDays : 0
     * initialWeight : 0
     * intakePercent : 0
     * levelDesc : string
     * lunch : 0
     * normWeight : 0
     * peakValue : 0
     * sickLevel : string
     * snacks : 0
     * targetWeight : 0
     * unreadCount : 0
     * warning : true
     * weightInfo : {"bmi":0,"bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyType":"string","bone":0,"createTime":1511248354000,"createUser":1,"flesh":0,"gid":1,"healthScore":0,"measureTime":"2018-07-16T09:08:56.384Z","muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":0,"water":0,"weight":0,"weightDate":"2018-07-16T09:08:56.384Z"}
     * weightInfoList : [{"bmi":0,"bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyType":"string","bone":0,"createTime":1511248354000,"createUser":1,"flesh":0,"gid":1,"healthScore":0,"measureTime":"2018-07-16T09:08:56.384Z","muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":0,"water":0,"weight":0,"weightDate":"2018-07-16T09:08:56.384Z"}]
     */
    private String bodyType;
    private int ableIntake;
    private int breakfast;
    private double complete;
    private int dinner;
    private int hasDays;
    private double initialWeight;
    private double intakePercent;
    private String levelDesc;
    private int lunch;
    private double normWeight;
    private int peakValue;
    private String sickLevel;
    private int snacks;
    private double targetWeight;
    private int unreadCount;
    private boolean warning;
    private WeightInfoBean weightInfo;
    private List<AthleticsInfoListBean> athleticsInfoList;
    private List<WeightInfoListBean> weightInfoList;


    public String getBodyType() {
        return bodyType;
    }

    public int getAbleIntake() {
        return ableIntake;
    }

    public void setAbleIntake(int ableIntake) {
        this.ableIntake = ableIntake;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public double getComplete() {
        return complete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }

    public int getDinner() {
        return dinner;
    }

    public void setDinner(int dinner) {
        this.dinner = dinner;
    }

    public int getHasDays() {
        return hasDays;
    }

    public void setHasDays(int hasDays) {
        this.hasDays = hasDays;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(int initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getIntakePercent() {
        return intakePercent;
    }

    public void setIntakePercent(int intakePercent) {
        this.intakePercent = intakePercent;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public double getNormWeight() {
        return normWeight;
    }

    public void setNormWeight(double normWeight) {
        this.normWeight = normWeight;
    }

    public int getPeakValue() {
        return peakValue;
    }

    public void setPeakValue(int peakValue) {
        this.peakValue = peakValue;
    }

    public String getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(String sickLevel) {
        this.sickLevel = sickLevel;
    }

    public int getSnacks() {
        return snacks;
    }

    public void setSnacks(int snacks) {
        this.snacks = snacks;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight) {
        this.targetWeight = targetWeight;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public WeightInfoBean getWeightInfo() {
        return weightInfo;
    }

    public void setWeightInfo(WeightInfoBean weightInfo) {
        this.weightInfo = weightInfo;
    }

    public List<AthleticsInfoListBean> getAthleticsInfoList() {
        return athleticsInfoList;
    }

    public void setAthleticsInfoList(List<AthleticsInfoListBean> athleticsInfoList) {
        this.athleticsInfoList = athleticsInfoList;
    }

    public List<WeightInfoListBean> getWeightInfoList() {
        return weightInfoList;
    }

    public void setWeightInfoList(List<WeightInfoListBean> weightInfoList) {
        this.weightInfoList = weightInfoList;
    }

    public static class WeightInfoBean {
        /**
         * bmi : 0
         * bmr : 0
         * bodyAge : 0
         * bodyFat : 0
         * bodyFfm : 0
         * bodyType : string
         * bone : 0
         * createTime : 1511248354000
         * createUser : 1
         * flesh : 0
         * gid : 1
         * healthScore : 0
         * measureTime : 2018-07-16T09:08:56.384Z
         * muscle : 0
         * protein : 0
         * sinew : 0
         * status : 101
         * subfat : 0
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * visfat : 0
         * water : 0
         * weight : 0
         * weightDate : 2018-07-16T09:08:56.384Z
         */

        private double bmi;
        private double bmr;
        private double bodyAge;
        private double bodyFat;
        private double bodyFfm;
        private String bodyType;
        private double bone;
        private long createTime;
        private String createUser;
        private double flesh;
        private String gid;
        private double healthScore;
        private String measureTime;
        private double muscle;
        private double protein;
        private double sinew;
        private int status;
        private double subfat;
        private long updateTime;
        private String updateUser;
        private String userId;
        private double visfat;
        private double water;
        private double weight;
        private long weightDate;
        private double basalHeat;

        public double getBmi() {
            return bmi;
        }

        public double getBmr() {
            return bmr;
        }

        public double getBodyAge() {
            return bodyAge;
        }

        public double getBodyFat() {
            return bodyFat;
        }

        public double getBodyFfm() {
            return bodyFfm;
        }

        public String getBodyType() {
            return bodyType;
        }

        public double getBone() {
            return bone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public double getFlesh() {
            return flesh;
        }

        public String getGid() {
            return gid;
        }

        public double getHealthScore() {
            return healthScore;
        }

        public String getMeasureTime() {
            return measureTime;
        }

        public double getMuscle() {
            return muscle;
        }

        public double getProtein() {
            return protein;
        }

        public double getSinew() {
            return sinew;
        }

        public int getStatus() {
            return status;
        }

        public double getSubfat() {
            return subfat;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public String getUserId() {
            return userId;
        }

        public double getVisfat() {
            return visfat;
        }

        public double getWater() {
            return water;
        }

        public double getWeight() {
            return weight;
        }

        public long getWeightDate() {
            return weightDate;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public double getBasalHeat() {
            return basalHeat;
        }

        public void setBasalHeat(double basalHeat) {
            this.basalHeat = basalHeat;
        }

        public void setBmr(double bmr) {
            this.bmr = bmr;
        }

        public void setBodyAge(double bodyAge) {
            this.bodyAge = bodyAge;
        }

        public void setBodyFat(double bodyFat) {
            this.bodyFat = bodyFat;
        }

        public void setBodyFfm(double bodyFfm) {
            this.bodyFfm = bodyFfm;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public void setBone(double bone) {
            this.bone = bone;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public void setFlesh(double flesh) {
            this.flesh = flesh;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public void setHealthScore(double healthScore) {
            this.healthScore = healthScore;
        }

        public void setMeasureTime(String measureTime) {
            this.measureTime = measureTime;
        }

        public void setMuscle(double muscle) {
            this.muscle = muscle;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public void setSinew(double sinew) {
            this.sinew = sinew;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setSubfat(double subfat) {
            this.subfat = subfat;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setVisfat(double visfat) {
            this.visfat = visfat;
        }

        public void setWater(double water) {
            this.water = water;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public void setWeightDate(long weightDate) {
            this.weightDate = weightDate;
        }
    }

    public static class AthleticsInfoListBean {
        /**
         * athlDate : 2018-07-16T09:08:56.384Z
         * athlDesc : string
         * athlRecord : string
         * avgHeart : 0
         * calorie : 0
         * createTime : 1511248354000
         * createUser : 1
         * duration : 0
         * gid : 1
         * kilometers : 0
         * maxHeart : 0
         * minHeart : 0
         * status : 101
         * stepNumber : 0
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         */

        private long athlDate;
        private String athlDesc;
        private String athlRecord;
        private int avgHeart;
        private int calorie;
        private long createTime;
        private String createUser;
        private int duration;
        private String gid;
        private double kilometers;
        private int maxHeart;
        private int minHeart;
        private int status;
        private int stepNumber;
        private long updateTime;
        private String updateUser;
        private String userId;

        public long getAthlDate() {
            return athlDate;
        }

        public String getAthlDesc() {
            return athlDesc;
        }

        public String getAthlRecord() {
            return athlRecord;
        }

        public int getAvgHeart() {
            return avgHeart;
        }

        public int getCalorie() {
            return calorie;
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public int getDuration() {
            return duration;
        }

        public String getGid() {
            return gid;
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

        public int getStatus() {
            return status;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public String getUserId() {
            return userId;
        }


        public void setAthlDate(long athlDate) {
            this.athlDate = athlDate;
        }

        public void setAthlDesc(String athlDesc) {
            this.athlDesc = athlDesc;
        }

        public void setAthlRecord(String athlRecord) {
            this.athlRecord = athlRecord;
        }

        public void setAvgHeart(int avgHeart) {
            this.avgHeart = avgHeart;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setGid(String gid) {
            this.gid = gid;
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

        public void setStatus(int status) {
            this.status = status;
        }

        public void setStepNumber(int stepNumber) {
            this.stepNumber = stepNumber;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public static class WeightInfoListBean {
        /**
         * bmi : 0
         * bmr : 0
         * bodyAge : 0
         * bodyFat : 0
         * bodyFfm : 0
         * bodyType : string
         * bone : 0
         * createTime : 1511248354000
         * createUser : 1
         * flesh : 0
         * gid : 1
         * healthScore : 0
         * measureTime : 2018-07-16T09:08:56.384Z
         * muscle : 0
         * protein : 0
         * sinew : 0
         * status : 101
         * subfat : 0
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * visfat : 0
         * water : 0
         * weight : 0
         * weightDate : 2018-07-16T09:08:56.384Z
         */

        private double bmi;
        private double bmr;
        private int bodyAge;
        private double bodyFat;
        private double bodyFfm;
        private String bodyType;
        private double bone;
        private long createTime;
        private String createUser;
        private double flesh;
        private String gid;
        private double healthScore;
        private long measureTime;
        private double muscle;
        private double protein;
        private double sinew;
        private int status;
        private double subfat;
        private long updateTime;
        private String updateUser;
        private String userId;
        private double visfat;
        private double water;
        private double weight;
        private long weightDate;

        public double getBmi() {
            return bmi;
        }

        public double getBmr() {
            return bmr;
        }

        public int getBodyAge() {
            return bodyAge;
        }

        public double getBodyFat() {
            return bodyFat;
        }

        public double getBodyFfm() {
            return bodyFfm;
        }

        public String getBodyType() {
            return bodyType;
        }

        public double getBone() {
            return bone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public double getFlesh() {
            return flesh;
        }

        public String getGid() {
            return gid;
        }

        public double getHealthScore() {
            return healthScore;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public double getMuscle() {
            return muscle;
        }

        public double getProtein() {
            return protein;
        }

        public double getSinew() {
            return sinew;
        }

        public int getStatus() {
            return status;
        }

        public double getSubfat() {
            return subfat;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public String getUserId() {
            return userId;
        }

        public double getVisfat() {
            return visfat;
        }

        public double getWater() {
            return water;
        }

        public double getWeight() {
            return weight;
        }

        public long getWeightDate() {
            return weightDate;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public void setBmr(double bmr) {
            this.bmr = bmr;
        }

        public void setBodyAge(int bodyAge) {
            this.bodyAge = bodyAge;
        }

        public void setBodyFat(double bodyFat) {
            this.bodyFat = bodyFat;
        }

        public void setBodyFfm(double bodyFfm) {
            this.bodyFfm = bodyFfm;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public void setBone(double bone) {
            this.bone = bone;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public void setFlesh(double flesh) {
            this.flesh = flesh;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public void setHealthScore(double healthScore) {
            this.healthScore = healthScore;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public void setMuscle(double muscle) {
            this.muscle = muscle;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public void setSinew(double sinew) {
            this.sinew = sinew;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setSubfat(double subfat) {
            this.subfat = subfat;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setVisfat(double visfat) {
            this.visfat = visfat;
        }

        public void setWater(double water) {
            this.water = water;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public void setWeightDate(long weightDate) {
            this.weightDate = weightDate;
        }
    }


    @Override
    public String toString() {
        return "FirstPageBean{" +
                "bodyType='" + bodyType + '\'' +
                ", ableIntake=" + ableIntake +
                ", breakfast=" + breakfast +
                ", complete=" + complete +
                ", dinner=" + dinner +
                ", hasDays=" + hasDays +
                ", initialWeight=" + initialWeight +
                ", intakePercent=" + intakePercent +
                ", levelDesc='" + levelDesc + '\'' +
                ", lunch=" + lunch +
                ", normWeight=" + normWeight +
                ", peakValue=" + peakValue +
                ", sickLevel='" + sickLevel + '\'' +
                ", snacks=" + snacks +
                ", targetWeight=" + targetWeight +
                ", unreadCount=" + unreadCount +
                ", warning=" + warning +
                ", weightInfo=" + weightInfo +
                ", athleticsInfoList=" + athleticsInfoList +
                ", weightInfoList=" + weightInfoList +
                '}';
    }
}
