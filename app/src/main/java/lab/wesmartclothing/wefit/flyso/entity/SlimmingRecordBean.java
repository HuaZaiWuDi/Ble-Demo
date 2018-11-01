package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName SlimmingRecordBean
 * @Date 2018/11/1 16:42
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class SlimmingRecordBean {


    /**
     * ableIntake : 0
     * athlDone : true
     * athleticsInfoList : [{"age":18,"athlDate":"2018-11-01T08:41:11.469Z","athlDesc":"string","athlRecord":"string","avgHeart":1,"birthday":"2018-11-01T08:41:11.469Z","calorie":1,"createTime":1511248354000,"createUser":1,"duration":1,"gid":1,"heartCount":1,"height":170,"kilometers":1,"maxHeart":1,"minHeart":1,"planflag":0,"sex":1,"status":101,"stepNumber":1,"updateTime":1511248354000,"updateUser":1,"userId":"string"}]
     * bodyType : string
     * breakfast : 0
     * breakfastDone : true
     * complete : 0
     * dataList : [{"athlCalorie":1,"athlCount":1,"athlPlan":0,"basalCalorie":1,"createTime":1511248354000,"createUser":1,"dietPlan":0,"duration":1,"gid":1,"heatCalorie":1,"heatCount":1,"recordDate":"2018-11-01T08:41:11.469Z","status":101,"updateTime":1511248354000,"updateUser":1,"userId":"string","weight":1,"weightCount":1}]
     * dinner : 0
     * dinnerDone : true
     * hasDays : 0
     * initialWeight : 0
     * intakePercent : 0
     * levelDesc : string
     * lunch : 0
     * lunchDone : true
     * normWeight : 0
     * peakValue : 0
     * sickLevel : string
     * snacks : 0
     * targetWeight : 0
     * unreadCount : 0
     * warning : true
     * weightDone : true
     * weightInfo : {"age":18,"basalHeat":1,"birthday":"2018-11-01T08:41:11.469Z","bmi":1,"bmiLevel":1,"bmr":1,"bodyAge":1,"bodyFat":1,"bodyFfm":1,"bodyLevel":1,"bodyType":"string","bone":1,"createTime":1511248354000,"createUser":1,"flesh":1,"gid":1,"healthScore":1,"height":170,"initialFlag":0,"measureTime":"2018-11-01T08:41:11.469Z","muscle":1,"protein":1,"sex":1,"sinew":1,"status":101,"subfat":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":1,"water":1,"weight":60,"weightDate":"2018-11-01T08:41:11.469Z"}
     * weightInfoList : [{"age":18,"basalHeat":1,"birthday":"2018-11-01T08:41:11.470Z","bmi":1,"bmiLevel":1,"bmr":1,"bodyAge":1,"bodyFat":1,"bodyFfm":1,"bodyLevel":1,"bodyType":"string","bone":1,"createTime":1511248354000,"createUser":1,"flesh":1,"gid":1,"healthScore":1,"height":170,"initialFlag":0,"measureTime":"2018-11-01T08:41:11.470Z","muscle":1,"protein":1,"sex":1,"sinew":1,"status":101,"subfat":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":1,"water":1,"weight":60,"weightDate":"2018-11-01T08:41:11.470Z"}]
     */

    private int ableIntake;
    private boolean athlDone;
    private String bodyType;
    private int breakfast;
    private boolean breakfastDone;
    private double complete;
    private int dinner;
    private boolean dinnerDone;
    private int hasDays;
    private int initialWeight;
    private int intakePercent;
    private String levelDesc;
    private int lunch;
    private boolean lunchDone;
    private int normWeight;
    private int peakValue;
    private String sickLevel;
    private int snacks;
    private int targetWeight;
    private int unreadCount;
    private boolean warning;
    private boolean weightDone;
    private WeightInfoBean weightInfo;
    private List<AthleticsInfoListBean> athleticsInfoList;
    private List<DataListBean> dataList;
    private List<WeightInfoListBean> weightInfoList;

    public int getAbleIntake() {
        return ableIntake;
    }

    public void setAbleIntake(int ableIntake) {
        this.ableIntake = ableIntake;
    }

    public boolean isAthlDone() {
        return athlDone;
    }

    public void setAthlDone(boolean athlDone) {
        this.athlDone = athlDone;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isBreakfastDone() {
        return breakfastDone;
    }

    public void setBreakfastDone(boolean breakfastDone) {
        this.breakfastDone = breakfastDone;
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

    public boolean isDinnerDone() {
        return dinnerDone;
    }

    public void setDinnerDone(boolean dinnerDone) {
        this.dinnerDone = dinnerDone;
    }

    public int getHasDays() {
        return hasDays;
    }

    public void setHasDays(int hasDays) {
        this.hasDays = hasDays;
    }

    public int getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(int initialWeight) {
        this.initialWeight = initialWeight;
    }

    public int getIntakePercent() {
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

    public boolean isLunchDone() {
        return lunchDone;
    }

    public void setLunchDone(boolean lunchDone) {
        this.lunchDone = lunchDone;
    }

    public int getNormWeight() {
        return normWeight;
    }

    public void setNormWeight(int normWeight) {
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

    public boolean isWeightDone() {
        return weightDone;
    }

    public void setWeightDone(boolean weightDone) {
        this.weightDone = weightDone;
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

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public List<WeightInfoListBean> getWeightInfoList() {
        return weightInfoList;
    }

    public void setWeightInfoList(List<WeightInfoListBean> weightInfoList) {
        this.weightInfoList = weightInfoList;
    }

    public static class WeightInfoBean {
        /**
         * age : 18
         * basalHeat : 1
         * birthday : 2018-11-01T08:41:11.469Z
         * bmi : 1
         * bmiLevel : 1
         * bmr : 1
         * bodyAge : 1
         * bodyFat : 1
         * bodyFfm : 1
         * bodyLevel : 1
         * bodyType : string
         * bone : 1
         * createTime : 1511248354000
         * createUser : 1
         * flesh : 1
         * gid : 1
         * healthScore : 1
         * height : 170
         * initialFlag : 0
         * measureTime : 2018-11-01T08:41:11.469Z
         * muscle : 1
         * protein : 1
         * sex : 1
         * sinew : 1
         * status : 101
         * subfat : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * visfat : 1
         * water : 1
         * weight : 60
         * weightDate : 2018-11-01T08:41:11.469Z
         */

        private int age;
        private int basalHeat;
        private String birthday;
        private double bmi;
        private int bmiLevel;
        private double bmr;
        private int bodyAge;
        private double bodyFat;
        private int bodyFfm;
        private int bodyLevel;
        private String bodyType;
        private int bone;
        private long createTime;
        private int createUser;
        private int flesh;
        private int gid;
        private double healthScore;
        private int height;
        private int initialFlag;
        private String measureTime;
        private int muscle;
        private int protein;
        private int sex;
        private int sinew;
        private int status;
        private int subfat;
        private long updateTime;
        private int updateUser;
        private String userId;
        private int visfat;
        private int water;
        private double weight;
        private String weightDate;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getBasalHeat() {
            return basalHeat;
        }

        public void setBasalHeat(int basalHeat) {
            this.basalHeat = basalHeat;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public int getBmiLevel() {
            return bmiLevel;
        }

        public void setBmiLevel(int bmiLevel) {
            this.bmiLevel = bmiLevel;
        }

        public double getBmr() {
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

        public double getBodyFat() {
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

        public int getBodyLevel() {
            return bodyLevel;
        }

        public void setBodyLevel(int bodyLevel) {
            this.bodyLevel = bodyLevel;
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

        public double getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(int healthScore) {
            this.healthScore = healthScore;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getInitialFlag() {
            return initialFlag;
        }

        public void setInitialFlag(int initialFlag) {
            this.initialFlag = initialFlag;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public double getWeight() {
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
         * age : 18
         * athlDate : 2018-11-01T08:41:11.469Z
         * athlDesc : string
         * athlRecord : string
         * avgHeart : 1
         * birthday : 2018-11-01T08:41:11.469Z
         * calorie : 1
         * createTime : 1511248354000
         * createUser : 1
         * duration : 1
         * gid : 1
         * heartCount : 1
         * height : 170
         * kilometers : 1
         * maxHeart : 1
         * minHeart : 1
         * planflag : 0
         * sex : 1
         * status : 101
         * stepNumber : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         */

        private int age;
        private long athlDate;
        private String athlDesc;
        private String athlRecord;
        private int avgHeart;
        private String birthday;
        private int calorie;
        private long createTime;
        private int createUser;
        private int duration;
        private int gid;
        private int heartCount;
        private int height;
        private int kilometers;
        private int maxHeart;
        private int minHeart;
        private int planflag;
        private int sex;
        private int status;
        private int stepNumber;
        private long updateTime;
        private int updateUser;
        private String userId;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public long getAthlDate() {
            return athlDate;
        }

        public void setAthlDate(long athlDate) {
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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public int getHeartCount() {
            return heartCount;
        }

        public void setHeartCount(int heartCount) {
            this.heartCount = heartCount;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
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

        public int getPlanflag() {
            return planflag;
        }

        public void setPlanflag(int planflag) {
            this.planflag = planflag;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

    public static class DataListBean {
        /**
         * athlCalorie : 1
         * athlCount : 1
         * athlPlan : 0
         * basalCalorie : 1
         * createTime : 1511248354000
         * createUser : 1
         * dietPlan : 0
         * duration : 1
         * gid : 1
         * heatCalorie : 1
         * heatCount : 1
         * recordDate : 2018-11-01T08:41:11.469Z
         * status : 101
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * weight : 1
         * weightCount : 1
         */

        private int athlCalorie;
        private int athlCount;
        private int athlPlan;
        private int basalCalorie;
        private long createTime;
        private int createUser;
        private int dietPlan;
        private int duration;
        private int gid;
        private int heatCalorie;
        private int heatCount;
        private long recordDate;
        private int status;
        private long updateTime;
        private int updateUser;
        private String userId;
        private int weight;
        private int weightCount;

        public int getAthlCalorie() {
            return athlCalorie;
        }

        public void setAthlCalorie(int athlCalorie) {
            this.athlCalorie = athlCalorie;
        }

        public int getAthlCount() {
            return athlCount;
        }

        public void setAthlCount(int athlCount) {
            this.athlCount = athlCount;
        }

        public int getAthlPlan() {
            return athlPlan;
        }

        public void setAthlPlan(int athlPlan) {
            this.athlPlan = athlPlan;
        }

        public int getBasalCalorie() {
            return basalCalorie;
        }

        public void setBasalCalorie(int basalCalorie) {
            this.basalCalorie = basalCalorie;
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

        public int getDietPlan() {
            return dietPlan;
        }

        public void setDietPlan(int dietPlan) {
            this.dietPlan = dietPlan;
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

        public int getHeatCalorie() {
            return heatCalorie;
        }

        public void setHeatCalorie(int heatCalorie) {
            this.heatCalorie = heatCalorie;
        }

        public int getHeatCount() {
            return heatCount;
        }

        public void setHeatCount(int heatCount) {
            this.heatCount = heatCount;
        }

        public long getRecordDate() {
            return recordDate;
        }

        public void setRecordDate(long recordDate) {
            this.recordDate = recordDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getWeightCount() {
            return weightCount;
        }

        public void setWeightCount(int weightCount) {
            this.weightCount = weightCount;
        }
    }

    public static class WeightInfoListBean {
        /**
         * age : 18
         * basalHeat : 1
         * birthday : 2018-11-01T08:41:11.470Z
         * bmi : 1
         * bmiLevel : 1
         * bmr : 1
         * bodyAge : 1
         * bodyFat : 1
         * bodyFfm : 1
         * bodyLevel : 1
         * bodyType : string
         * bone : 1
         * createTime : 1511248354000
         * createUser : 1
         * flesh : 1
         * gid : 1
         * healthScore : 1
         * height : 170
         * initialFlag : 0
         * measureTime : 2018-11-01T08:41:11.470Z
         * muscle : 1
         * protein : 1
         * sex : 1
         * sinew : 1
         * status : 101
         * subfat : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * visfat : 1
         * water : 1
         * weight : 60
         * weightDate : 2018-11-01T08:41:11.470Z
         */

        private int age;
        private int basalHeat;
        private String birthday;
        private int bmi;
        private int bmiLevel;
        private int bmr;
        private int bodyAge;
        private int bodyFat;
        private int bodyFfm;
        private int bodyLevel;
        private String bodyType;
        private int bone;
        private long createTime;
        private int createUser;
        private int flesh;
        private int gid;
        private int healthScore;
        private int height;
        private int initialFlag;
        private String measureTime;
        private int muscle;
        private int protein;
        private int sex;
        private int sinew;
        private int status;
        private int subfat;
        private long updateTime;
        private int updateUser;
        private String userId;
        private int visfat;
        private int water;
        private int weight;
        private long weightDate;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getBasalHeat() {
            return basalHeat;
        }

        public void setBasalHeat(int basalHeat) {
            this.basalHeat = basalHeat;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getBmi() {
            return bmi;
        }

        public void setBmi(int bmi) {
            this.bmi = bmi;
        }

        public int getBmiLevel() {
            return bmiLevel;
        }

        public void setBmiLevel(int bmiLevel) {
            this.bmiLevel = bmiLevel;
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

        public int getBodyLevel() {
            return bodyLevel;
        }

        public void setBodyLevel(int bodyLevel) {
            this.bodyLevel = bodyLevel;
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

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getInitialFlag() {
            return initialFlag;
        }

        public void setInitialFlag(int initialFlag) {
            this.initialFlag = initialFlag;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public long getWeightDate() {
            return weightDate;
        }

        public void setWeightDate(long weightDate) {
            this.weightDate = weightDate;
        }
    }
}
