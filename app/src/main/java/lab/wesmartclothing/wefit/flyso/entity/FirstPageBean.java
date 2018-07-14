package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/7/13.
 */
public class FirstPageBean {


    /**
     * ableIntake : 0
     * athleticsInfoList : [{"athlDate":"2018-07-13T09:52:25.868Z","athlDesc":"string","athlRecord":"string","avgHeart":0,"calorie":0,"createTime":1511248354000,"createUser":1,"duration":0,"gid":1,"kilometers":0,"maxHeart":0,"minHeart":0,"status":101,"stepNumber":0,"updateTime":1511248354000,"updateUser":1,"userId":"string"}]
     * breakfast : 0
     * dinner : 0
     * hasDays : 0
     * lunch : 0
     * sickLevel : string
     * snacks : 0
     * targetWeight : 0
     * unreadCount : 0
     * warning : true
     * weightInfo : {"bmi":0,"bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyType":"string","bone":0,"createTime":1511248354000,"createUser":1,"flesh":0,"gid":1,"healthScore":0,"measureTime":"2018-07-13T09:52:25.869Z","muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":0,"water":0,"weight":0,"weightDate":"2018-07-13T09:52:25.869Z"}
     * weightInfoList : [{"bmi":0,"bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyType":"string","bone":0,"createTime":1511248354000,"createUser":1,"flesh":0,"gid":1,"healthScore":0,"measureTime":"2018-07-13T09:52:25.869Z","muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":0,"water":0,"weight":0,"weightDate":"2018-07-13T09:52:25.869Z"}]
     */

    private int ableIntake;
    private int breakfast;
    private int dinner;
    private int hasDays;
    private int lunch;
    private String sickLevel;
    private int snacks;
    private int targetWeight;
    private int unreadCount;
    private boolean warning;
    private WeightInfoBean weightInfo;
    private List<AthleticsInfoListBean> athleticsInfoList;
    private List<WeightInfoListBean> weightInfoList;

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

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
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

    public int getTargetWeight() {
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
         * measureTime : 2018-07-13T09:52:25.869Z
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
         * weightDate : 2018-07-13T09:52:25.869Z
         */

        private int bmi;
        private int bmr;
        private int bodyAge;
        private int bodyFat;
        private int bodyFfm;
        private String bodyType;
        private int bone;
        private long createTime;
        private int createUser;
        private int flesh;
        private int gid;
        private int healthScore;
        private String measureTime;
        private int muscle;
        private int protein;
        private int sinew;
        private int status;
        private int subfat;
        private long updateTime;
        private int updateUser;
        private String userId;
        private int visfat;
        private int water;
        private int weight;
        private String weightDate;

        public int getBmi() {
            return bmi;
        }

        public void setBmi(int bmi) {
            this.bmi = bmi;
        }

        public int getBmr() {
            return bmr;
        }

        public void setBmr(int bmr) {
            this.bmr = bmr;
        }

        public int getBodyAge() {
            return bodyAge;
        }

        public void setBodyAge(int bodyAge) {
            this.bodyAge = bodyAge;
        }

        public int getBodyFat() {
            return bodyFat;
        }

        public void setBodyFat(int bodyFat) {
            this.bodyFat = bodyFat;
        }

        public int getBodyFfm() {
            return bodyFfm;
        }

        public void setBodyFfm(int bodyFfm) {
            this.bodyFfm = bodyFfm;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public int getBone() {
            return bone;
        }

        public void setBone(int bone) {
            this.bone = bone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
            this.createUser = createUser;
        }

        public int getFlesh() {
            return flesh;
        }

        public void setFlesh(int flesh) {
            this.flesh = flesh;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public int getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(int healthScore) {
            this.healthScore = healthScore;
        }

        public String getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(String measureTime) {
            this.measureTime = measureTime;
        }

        public int getMuscle() {
            return muscle;
        }

        public void setMuscle(int muscle) {
            this.muscle = muscle;
        }

        public int getProtein() {
            return protein;
        }

        public void setProtein(int protein) {
            this.protein = protein;
        }

        public int getSinew() {
            return sinew;
        }

        public void setSinew(int sinew) {
            this.sinew = sinew;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSubfat() {
            return subfat;
        }

        public void setSubfat(int subfat) {
            this.subfat = subfat;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getVisfat() {
            return visfat;
        }

        public void setVisfat(int visfat) {
            this.visfat = visfat;
        }

        public int getWater() {
            return water;
        }

        public void setWater(int water) {
            this.water = water;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getWeightDate() {
            return weightDate;
        }

        public void setWeightDate(String weightDate) {
            this.weightDate = weightDate;
        }
    }

    public static class AthleticsInfoListBean {
        /**
         * athlDate : 2018-07-13T09:52:25.868Z
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

        private String athlDate;
        private String athlDesc;
        private String athlRecord;
        private int avgHeart;
        private int calorie;
        private long createTime;
        private int createUser;
        private int duration;
        private int gid;
        private int kilometers;
        private int maxHeart;
        private int minHeart;
        private int status;
        private int stepNumber;
        private long updateTime;
        private int updateUser;
        private String userId;

        public String getAthlDate() {
            return athlDate;
        }

        public void setAthlDate(String athlDate) {
            this.athlDate = athlDate;
        }

        public String getAthlDesc() {
            return athlDesc;
        }

        public void setAthlDesc(String athlDesc) {
            this.athlDesc = athlDesc;
        }

        public String getAthlRecord() {
            return athlRecord;
        }

        public void setAthlRecord(String athlRecord) {
            this.athlRecord = athlRecord;
        }

        public int getAvgHeart() {
            return avgHeart;
        }

        public void setAvgHeart(int avgHeart) {
            this.avgHeart = avgHeart;
        }

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
            this.createUser = createUser;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public int getKilometers() {
            return kilometers;
        }

        public void setKilometers(int kilometers) {
            this.kilometers = kilometers;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public void setStepNumber(int stepNumber) {
            this.stepNumber = stepNumber;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
        }

        public String getUserId() {
            return userId;
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
         * measureTime : 2018-07-13T09:52:25.869Z
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
         * weightDate : 2018-07-13T09:52:25.869Z
         */

        private int bmi;
        private int bmr;
        private int bodyAge;
        private int bodyFat;
        private int bodyFfm;
        private String bodyType;
        private int bone;
        private long createTime;
        private int createUser;
        private int flesh;
        private int gid;
        private int healthScore;
        private String measureTime;
        private int muscle;
        private int protein;
        private int sinew;
        private int status;
        private int subfat;
        private long updateTime;
        private int updateUser;
        private String userId;
        private int visfat;
        private int water;
        private int weight;
        private String weightDate;

        public int getBmi() {
            return bmi;
        }

        public void setBmi(int bmi) {
            this.bmi = bmi;
        }

        public int getBmr() {
            return bmr;
        }

        public void setBmr(int bmr) {
            this.bmr = bmr;
        }

        public int getBodyAge() {
            return bodyAge;
        }

        public void setBodyAge(int bodyAge) {
            this.bodyAge = bodyAge;
        }

        public int getBodyFat() {
            return bodyFat;
        }

        public void setBodyFat(int bodyFat) {
            this.bodyFat = bodyFat;
        }

        public int getBodyFfm() {
            return bodyFfm;
        }

        public void setBodyFfm(int bodyFfm) {
            this.bodyFfm = bodyFfm;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public int getBone() {
            return bone;
        }

        public void setBone(int bone) {
            this.bone = bone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
            this.createUser = createUser;
        }

        public int getFlesh() {
            return flesh;
        }

        public void setFlesh(int flesh) {
            this.flesh = flesh;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public int getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(int healthScore) {
            this.healthScore = healthScore;
        }

        public String getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(String measureTime) {
            this.measureTime = measureTime;
        }

        public int getMuscle() {
            return muscle;
        }

        public void setMuscle(int muscle) {
            this.muscle = muscle;
        }

        public int getProtein() {
            return protein;
        }

        public void setProtein(int protein) {
            this.protein = protein;
        }

        public int getSinew() {
            return sinew;
        }

        public void setSinew(int sinew) {
            this.sinew = sinew;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSubfat() {
            return subfat;
        }

        public void setSubfat(int subfat) {
            this.subfat = subfat;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getVisfat() {
            return visfat;
        }

        public void setVisfat(int visfat) {
            this.visfat = visfat;
        }

        public int getWater() {
            return water;
        }

        public void setWater(int water) {
            this.water = water;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getWeightDate() {
            return weightDate;
        }

        public void setWeightDate(String weightDate) {
            this.weightDate = weightDate;
        }
    }
}
