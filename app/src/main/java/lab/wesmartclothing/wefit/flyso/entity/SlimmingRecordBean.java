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
     * ableintake : 1636
     * athlDone : false
     * athleticsInfoList : [{"age":0,"athlDate":1542988800000,"athlDesc":"","avgHeart":79,"calorie":68,"complete":0,"createTime":1542988800000,"createUser":"","duration":1282,"gid":"","heartCount":0,"height":1,"kilometers":0,"maxHeart":118,"minHeart":59,"sex":1,"status":1,"stepNumber":0}]
     * bodyType : 偏瘦
     * breakfast : 0
     * breakfastDone : false
     * complete : -0.15
     * dataList : [{"athlCalorie":0,"athlCount":0,"athlPlan":0,"basalCalorie":0,"createTime":1544424700000,"createUser":"9801eee7ffce4364bed693e9cca8b681","dietPlan":0,"duration":0,"gid":"9198663ad31f47c699111d778c3e144e","heatCalorie":0,"heatCount":0,"recordDate":1544371200000,"status":101,"updateTime":1544424700000,"updateUser":"9801eee7ffce4364bed693e9cca8b681","userId":"9801eee7ffce4364bed693e9cca8b681","weight":66.15,"weightCount":1}]
     * dietList : [{"calorie":968,"heatDate":1544025600000}]
     * dinner : 0
     * dinnerDone : false
     * hasDays : 179
     * initialWeight : 64
     * intakePercent : 0
     * levelDesc : 低
     * lunch : 0
     * lunchDone : false
     * normWeight : 64
     * peakValue : 0
     * sickLevel : A
     * snacks : 0
     * targetWeight : 49.6
     * totalCompleteDays : 0
     * unreadCount : 0
     * warning : false
     * weightDone : false
     * weightInfo : {"basalHeat":1636,"bmi":0,"bmiLevel":"A","bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyLevel":7,"bodyType":"偏瘦","bone":0,"createTime":1544424700000,"createUser":"9801eee7ffce4364bed693e9cca8b681","flesh":0,"gid":"90795f6aa1e3402698747728ea9c1bb5","healthScore":0,"initialFlag":0,"measureTime":1544424620000,"muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1544424700000,"updateUser":"9801eee7ffce4364bed693e9cca8b681","userId":"9801eee7ffce4364bed693e9cca8b681","visfat":0,"water":0,"weight":66.15,"weightDate":1544371200000}
     * weightInfoList : [{"basalHeat":1636,"bmi":0,"bmiLevel":"A","bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyLevel":7,"bodyType":"偏瘦","bone":0,"createTime":1544424700000,"createUser":"9801eee7ffce4364bed693e9cca8b681","flesh":0,"gid":"90795f6aa1e3402698747728ea9c1bb5","healthScore":0,"initialFlag":0,"measureTime":1544424620000,"muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1544424700000,"updateUser":"9801eee7ffce4364bed693e9cca8b681","userId":"9801eee7ffce4364bed693e9cca8b681","visfat":0,"water":0,"weight":66.15,"weightDate":1544371200000}]
     */

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
    private double targetWeight;
    private int totalCompleteDays;
    private int unreadCount;
    private boolean warning;
    private boolean weightDone;
    private WeightInfoBean weightInfo;
    private List<DataListBean> heatList;
    private List<WeightInfoListBean> weightInfoList;

    public int getIntakePercent() {
        return intakePercent;
    }

    public void setIntakePercent(int intakePercent) {
        this.intakePercent = intakePercent;
    }

    public List<DataListBean> getHeatList() {
        return heatList;
    }

    public void setHeatList(List<DataListBean> heatList) {
        this.heatList = heatList;
    }

    public List<WeightInfoListBean> getWeightInfoList() {
        return weightInfoList;
    }

    public void setWeightInfoList(List<WeightInfoListBean> weightInfoList) {
        this.weightInfoList = weightInfoList;
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

    public int getintakePercent() {
        return intakePercent;
    }

    public void setintakePercent(int intakePercent) {
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

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public int getTotalCompleteDays() {
        return totalCompleteDays;
    }

    public void setTotalCompleteDays(int totalCompleteDays) {
        this.totalCompleteDays = totalCompleteDays;
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


    public List<DataListBean> getDataList() {
        return heatList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.heatList = dataList;
    }


    public static class WeightInfoBean {
        /**
         * basalHeat : 1636
         * bmi : 0
         * bmiLevel : A
         * bmr : 0
         * bodyAge : 0
         * bodyFat : 0
         * bodyFfm : 0
         * bodyLevel : 7
         * bodyType : 偏瘦
         * bone : 0
         * createTime : 1544424700000
         * createUser : 9801eee7ffce4364bed693e9cca8b681
         * flesh : 0
         * gid : 90795f6aa1e3402698747728ea9c1bb5
         * healthScore : 0
         * initialFlag : 0
         * measureTime : 1544424620000
         * muscle : 0
         * protein : 0
         * sinew : 0
         * status : 101
         * subfat : 0
         * updateTime : 1544424700000
         * updateUser : 9801eee7ffce4364bed693e9cca8b681
         * userId : 9801eee7ffce4364bed693e9cca8b681
         * visfat : 0
         * water : 0
         * weight : 66.15
         * weightDate : 1544371200000
         */

        private int basalHeat;
        private double bmi;
        private String bmiLevel;
        private int bmr;
        private int bodyAge;
        private double bodyFat;
        private double bodyFfm;
        private int bodyLevel;
        private String bodyType;
        private double bone;
        private long createTime;
        private String createUser;
        private double flesh;
        private String gid;
        private double healthScore;
        private int initialFlag;
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

        public int getBasalHeat() {
            return basalHeat;
        }

        public void setBasalHeat(int basalHeat) {
            this.basalHeat = basalHeat;
        }

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public String getBmiLevel() {
            return bmiLevel;
        }

        public void setBmiLevel(String bmiLevel) {
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

        public double getBodyFat() {
            return bodyFat;
        }

        public void setBodyFat(double bodyFat) {
            this.bodyFat = bodyFat;
        }

        public double getBodyFfm() {
            return bodyFfm;
        }

        public void setBodyFfm(double bodyFfm) {
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

        public double getBone() {
            return bone;
        }

        public void setBone(double bone) {
            this.bone = bone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public double getFlesh() {
            return flesh;
        }

        public void setFlesh(double flesh) {
            this.flesh = flesh;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public double getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(double healthScore) {
            this.healthScore = healthScore;
        }

        public int getInitialFlag() {
            return initialFlag;
        }

        public void setInitialFlag(int initialFlag) {
            this.initialFlag = initialFlag;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public double getMuscle() {
            return muscle;
        }

        public void setMuscle(double muscle) {
            this.muscle = muscle;
        }

        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public double getSinew() {
            return sinew;
        }

        public void setSinew(double sinew) {
            this.sinew = sinew;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getSubfat() {
            return subfat;
        }

        public void setSubfat(double subfat) {
            this.subfat = subfat;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getVisfat() {
            return visfat;
        }

        public void setVisfat(double visfat) {
            this.visfat = visfat;
        }

        public double getWater() {
            return water;
        }

        public void setWater(double water) {
            this.water = water;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public long getWeightDate() {
            return weightDate;
        }

        public void setWeightDate(long weightDate) {
            this.weightDate = weightDate;
        }
    }


    public static class WeightInfoListBean {
        /**
         * basalHeat : 1636
         * bmi : 0
         * bmiLevel : A
         * bmr : 0
         * bodyAge : 0
         * bodyFat : 0
         * bodyFfm : 0
         * bodyLevel : 7
         * bodyType : 偏瘦
         * bone : 0
         * createTime : 1544424700000
         * createUser : 9801eee7ffce4364bed693e9cca8b681
         * flesh : 0
         * gid : 90795f6aa1e3402698747728ea9c1bb5
         * healthScore : 0
         * initialFlag : 0
         * measureTime : 1544424620000
         * muscle : 0
         * protein : 0
         * sinew : 0
         * status : 101
         * subfat : 0
         * updateTime : 1544424700000
         * updateUser : 9801eee7ffce4364bed693e9cca8b681
         * userId : 9801eee7ffce4364bed693e9cca8b681
         * visfat : 0
         * water : 0
         * weight : 66.15
         * weightDate : 1544371200000
         */

        private int basalHeat;
        private double bmi;
        private String bmiLevel;
        private double bmr;
        private int bodyAge;
        private double bodyFat;
        private double bodyFfm;
        private double bodyLevel;
        private String bodyType;
        private double bone;
        private long createTime;
        private String createUser;
        private double flesh;
        private String gid;
        private double healthScore;
        private double initialFlag;
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

        public int getBasalHeat() {
            return basalHeat;
        }

        public void setBasalHeat(int basalHeat) {
            this.basalHeat = basalHeat;
        }

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public String getBmiLevel() {
            return bmiLevel;
        }

        public void setBmiLevel(String bmiLevel) {
            this.bmiLevel = bmiLevel;
        }

        public double getBmr() {
            return bmr;
        }

        public void setBmr(double bmr) {
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

        public void setBodyFat(double bodyFat) {
            this.bodyFat = bodyFat;
        }

        public double getBodyFfm() {
            return bodyFfm;
        }

        public void setBodyFfm(double bodyFfm) {
            this.bodyFfm = bodyFfm;
        }

        public double getBodyLevel() {
            return bodyLevel;
        }

        public void setBodyLevel(double bodyLevel) {
            this.bodyLevel = bodyLevel;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public double getBone() {
            return bone;
        }

        public void setBone(double bone) {
            this.bone = bone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public double getFlesh() {
            return flesh;
        }

        public void setFlesh(double flesh) {
            this.flesh = flesh;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public double getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(double healthScore) {
            this.healthScore = healthScore;
        }

        public double getInitialFlag() {
            return initialFlag;
        }

        public void setInitialFlag(double initialFlag) {
            this.initialFlag = initialFlag;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public double getMuscle() {
            return muscle;
        }

        public void setMuscle(double muscle) {
            this.muscle = muscle;
        }

        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public double getSinew() {
            return sinew;
        }

        public void setSinew(double sinew) {
            this.sinew = sinew;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getSubfat() {
            return subfat;
        }

        public void setSubfat(double subfat) {
            this.subfat = subfat;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getVisfat() {
            return visfat;
        }

        public void setVisfat(double visfat) {
            this.visfat = visfat;
        }

        public double getWater() {
            return water;
        }

        public void setWater(double water) {
            this.water = water;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
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