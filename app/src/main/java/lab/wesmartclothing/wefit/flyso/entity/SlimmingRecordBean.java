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
     * unreadCount : 0
     * breakfastDone : false
     * lunchDone : false
     * dinnerDone : false
     * weightDone : false
     * athlDone : false
     * ableIntake : 1537
     * warning : false
     * breakfast : 0
     * lunch : 0
     * dinner : 0
     * snacks : 0
     * intakePercent : 0
     * bodyType : 正常
     * sickLevel : S
     * levelDesc : 低
     * targetWeight : 56.9
     * initialWeight : 59
     * hasDays : 21
     * complete : -1
     * normWeight : 59
     * weightInfo : {"gid":"4745b7f8c347463a9b978f8c9bd3e4a3","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540621751000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540622061000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1540569600000,"measureTime":1540622060000,"weight":61.1,"initialFlag":1,"basalHeat":1537,"bmi":22.44,"bmr":0,"bmiLevel":"S","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0}
     * peakValue : 11269
     * athleticsInfoList : [{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1537372800000,"planflag":null,"athlDesc":null,"calorie":2220,"duration":10190,"avgHeart":149,"minHeart":122,"maxHeart":235,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1537804800000,"planflag":null,"athlDesc":null,"calorie":1084,"duration":7622,"avgHeart":119,"minHeart":131,"maxHeart":222,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1537891200000,"planflag":null,"athlDesc":null,"calorie":8669,"duration":77220,"avgHeart":107,"minHeart":100,"maxHeart":235,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1537977600000,"planflag":null,"athlDesc":null,"calorie":7469,"duration":68148,"avgHeart":106,"minHeart":0,"maxHeart":140,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1538064000000,"planflag":null,"athlDesc":null,"calorie":28,"duration":114,"avgHeart":162,"minHeart":137,"maxHeart":194,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1539878400000,"planflag":null,"athlDesc":null,"calorie":0,"duration":0,"avgHeart":0,"minHeart":0,"maxHeart":0,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":null,"sex":null,"age":null,"birthday":null,"height":null,"athlDate":1540310400000,"planflag":null,"athlDesc":null,"calorie":6,"duration":40,"avgHeart":130,"minHeart":128,"maxHeart":132,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null}]
     * weightInfoList : [{"gid":"b4671616c6954ba1905ebc26ce0aac65","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1534932049000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1534932049000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1534867200000,"measureTime":1534932050000,"weight":62.95,"initialFlag":0,"basalHeat":0,"bmi":23.1,"bmr":0,"bmiLevel":"","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0},{"gid":"48752ab999ba44db858f5cca964cf354","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1536215605000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1536215605000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1536163200000,"measureTime":1536215605000,"weight":49.9,"initialFlag":0,"basalHeat":0,"bmi":20,"bmr":1178,"bmiLevel":"","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":21,"bone":2.25,"muscle":43.7,"sinew":35.18,"protein":17.79,"bodyFat":25,"bodyFfm":37.42,"subfat":23.7,"visfat":3,"flesh":0,"water":51.5,"healthScore":94.3},{"gid":"44833377c3d64452a74cb6690a7cbbea","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1536717314000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1536717314000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1536681600000,"measureTime":1536717313000,"weight":50.75,"initialFlag":0,"basalHeat":1295,"bmi":20.3,"bmr":1177,"bmiLevel":"","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":24,"bone":2.25,"muscle":43,"sinew":35.16,"protein":17.36,"bodyFat":26.3,"bodyFfm":37.42,"subfat":24.9,"visfat":3,"flesh":0,"water":50.6,"healthScore":94.3},{"gid":"3430b51b265e42d6b80f6d63d793dedc","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1538978468000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1539659184000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1538928000000,"measureTime":1538978468000,"weight":167.4,"initialFlag":0,"basalHeat":1588,"bmi":40,"bmr":0,"bmiLevel":"","bodyLevel":2,"bodyType":"偏胖","bodyAge":0,"bone":5,"muscle":100,"sinew":100,"protein":0,"bodyFat":15,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0},{"gid":"81f42c276b4945c295ecc5b419cd5a07","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1539674009000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1539674009000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1539619200000,"measureTime":1539674011000,"weight":13.7,"initialFlag":0,"basalHeat":888,"bmi":5.03,"bmr":0,"bmiLevel":"","bodyLevel":7,"bodyType":"偏瘦","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0},{"gid":"15fbe37087fa4a0d8a51aa12ae0b97aa","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1539932117000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540621595000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1539878400000,"measureTime":1540621594000,"weight":61.1,"initialFlag":1,"basalHeat":1537,"bmi":22.44,"bmr":0,"bmiLevel":"S","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0},{"gid":"4745b7f8c347463a9b978f8c9bd3e4a3","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540621751000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540622061000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1540569600000,"measureTime":1540622060000,"weight":61.1,"initialFlag":1,"basalHeat":1537,"bmi":22.44,"bmr":0,"bmiLevel":"S","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0}]
     * dataList : [{"gid":"50a0fb34364d45c08554e01760dd0846","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540603577000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540622061000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1540569600000,"athlCount":0,"athlCalorie":0,"duration":0,"athlPlan":0,"heatCount":0,"basalCalorie":0,"heatCalorie":0,"dietPlan":0,"weightCount":11,"weight":61.1},{"gid":"c5ec885a4bbe485796fa0f8da62e074f","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540456724000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540456749000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1540396800000,"athlCount":0,"athlCalorie":0,"duration":0,"athlPlan":0,"heatCount":3,"basalCalorie":1342,"heatCalorie":534,"dietPlan":0,"weightCount":0,"weight":0},{"gid":"7c63ae79077b4dcaaa413584d36b0f1d","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540367070000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540367070000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1540310400000,"athlCount":1,"athlCalorie":6,"duration":40,"athlPlan":0,"heatCount":0,"basalCalorie":0,"heatCalorie":0,"dietPlan":0,"weightCount":0,"weight":0},{"gid":"e5f29c1d2b3f43fabcedcd9783450d42","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1539931685000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1539939330000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1539878400000,"athlCount":1,"athlCalorie":0,"duration":0,"athlPlan":0,"heatCount":1,"basalCalorie":1342,"heatCalorie":30,"dietPlan":0,"weightCount":2,"weight":46.85},{"gid":"723648e94702451d95b8fa5900e0a777","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1539850046000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1539850046000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1539792000000,"athlCount":0,"athlCalorie":0,"duration":0,"athlPlan":0,"heatCount":1,"basalCalorie":888,"heatCalorie":200,"dietPlan":0,"weightCount":0,"weight":0},{"gid":"c27306bf25b3446fbcac4c2d0af79561","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1539658611000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1539674009000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1539619200000,"athlCount":0,"athlCalorie":0,"duration":0,"athlPlan":0,"heatCount":0,"basalCalorie":0,"heatCalorie":0,"dietPlan":0,"weightCount":95,"weight":13.7},{"gid":"3fe500eb7eb94d109bc718321c99100d","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1539596866000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1539597894000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","recordDate":1539532800000,"athlCount":0,"athlCalorie":0,"duration":0,"athlPlan":0,"heatCount":0,"basalCalorie":0,"heatCalorie":0,"dietPlan":0,"weightCount":2,"weight":61.95}]
     */

    private int unreadCount;
    private boolean breakfastDone;
    private boolean lunchDone;
    private boolean dinnerDone;
    private boolean weightDone;
    private boolean athlDone;
    private int ableIntake;
    private boolean warning;
    private int breakfast;
    private int lunch;
    private int dinner;
    private int snacks;
    private double intakePercent;
    private String bodyType;
    private String sickLevel;
    private String levelDesc;
    private double targetWeight;
    private double initialWeight;
    private int hasDays;
    private double complete;
    private double normWeight;
    private WeightInfoBean weightInfo;
    private int peakValue;
    private int totalCompleteDays;
    private List<AthleticsInfoListBean> athleticsInfoList;
    private List<WeightInfoBean> weightInfoList;
    private List<DataListBean> dataList;
    private List<DietListBean> dietList;


    @Override
    public String toString() {
        return "SlimmingRecordBean{" +
                "unreadCount=" + unreadCount +
                ", breakfastDone=" + breakfastDone +
                ", lunchDone=" + lunchDone +
                ", dinnerDone=" + dinnerDone +
                ", weightDone=" + weightDone +
                ", athlDone=" + athlDone +
                ", ableIntake=" + ableIntake +
                ", warning=" + warning +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", snacks=" + snacks +
                ", intakePercent=" + intakePercent +
                ", bodyType='" + bodyType + '\'' +
                ", sickLevel='" + sickLevel + '\'' +
                ", levelDesc='" + levelDesc + '\'' +
                ", targetWeight=" + targetWeight +
                ", initialWeight=" + initialWeight +
                ", hasDays=" + hasDays +
                ", complete=" + complete +
                ", normWeight=" + normWeight +
                ", weightInfo=" + weightInfo +
                ", peakValue=" + peakValue +
                ", totalCompleteDays=" + totalCompleteDays +
                ", athleticsInfoList=" + athleticsInfoList +
                ", weightInfoList=" + weightInfoList +
                ", dataList=" + dataList +
                ", dietList=" + dietList +
                '}';
    }


    public void setDietList(List<DietListBean> dietList) {
        this.dietList = dietList;
    }

    public int getTotalCompleteDays() {
        return totalCompleteDays;
    }

    public void setTotalCompleteDays(int totalCompleteDays) {
        this.totalCompleteDays = totalCompleteDays;
    }

    public List<DietListBean> getDietList() {
        return dietList;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isBreakfastDone() {
        return breakfastDone;
    }

    public void setBreakfastDone(boolean breakfastDone) {
        this.breakfastDone = breakfastDone;
    }

    public boolean isLunchDone() {
        return lunchDone;
    }

    public void setLunchDone(boolean lunchDone) {
        this.lunchDone = lunchDone;
    }

    public boolean isDinnerDone() {
        return dinnerDone;
    }

    public void setDinnerDone(boolean dinnerDone) {
        this.dinnerDone = dinnerDone;
    }

    public boolean isWeightDone() {
        return weightDone;
    }

    public void setWeightDone(boolean weightDone) {
        this.weightDone = weightDone;
    }

    public boolean isAthlDone() {
        return athlDone;
    }

    public void setAthlDone(boolean athlDone) {
        this.athlDone = athlDone;
    }

    public int getAbleIntake() {
        return ableIntake;
    }

    public void setAbleIntake(int ableIntake) {
        this.ableIntake = ableIntake;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public int getDinner() {
        return dinner;
    }

    public void setDinner(int dinner) {
        this.dinner = dinner;
    }

    public int getSnacks() {
        return snacks;
    }

    public void setSnacks(int snacks) {
        this.snacks = snacks;
    }

    public double getIntakePercent() {
        return intakePercent;
    }

    public void setIntakePercent(double intakePercent) {
        this.intakePercent = intakePercent;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(String sickLevel) {
        this.sickLevel = sickLevel;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public int getHasDays() {
        return hasDays;
    }

    public void setHasDays(int hasDays) {
        this.hasDays = hasDays;
    }

    public double getComplete() {
        return complete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }

    public double getNormWeight() {
        return normWeight;
    }

    public void setNormWeight(double normWeight) {
        this.normWeight = normWeight;
    }

    public WeightInfoBean getWeightInfo() {
        return weightInfo;
    }

    public void setWeightInfo(WeightInfoBean weightInfo) {
        this.weightInfo = weightInfo;
    }

    public int getPeakValue() {
        return peakValue;
    }

    public void setPeakValue(int peakValue) {
        this.peakValue = peakValue;
    }

    public List<AthleticsInfoListBean> getAthleticsInfoList() {
        return athleticsInfoList;
    }

    public void setAthleticsInfoList(List<AthleticsInfoListBean> athleticsInfoList) {
        this.athleticsInfoList = athleticsInfoList;
    }

    public List<WeightInfoBean> getWeightInfoList() {
        return weightInfoList;
    }

    public void setWeightInfoList(List<WeightInfoBean> weightInfoList) {
        this.weightInfoList = weightInfoList;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class WeightInfoBean {
        /**
         * gid : 4745b7f8c347463a9b978f8c9bd3e4a3
         * status : 101
         * createUser : e3e35aaff6b84cc29195f270ab7b95a1
         * createTime : 1540621751000
         * updateUser : e3e35aaff6b84cc29195f270ab7b95a1
         * updateTime : 1540622061000
         * userId : e3e35aaff6b84cc29195f270ab7b95a1
         * sex : null
         * age : null
         * birthday : null
         * height : null
         * weightDate : 1540569600000
         * measureTime : 1540622060000
         * weight : 61.1
         * initialFlag : 1
         * basalHeat : 1537
         * bmi : 22.44
         * bmr : 0
         * bmiLevel : S
         * bodyLevel : 4
         * bodyType : 缺乏运动
         * bodyAge : 0
         * bone : 0
         * muscle : 0
         * sinew : 0
         * protein : 0
         * bodyFat : 0
         * bodyFfm : 0
         * subfat : 0
         * visfat : 0
         * flesh : 0
         * water : 0
         * healthScore : 0
         */

        private String gid;
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private String userId;
        private Object sex;
        private Object age;
        private Object birthday;
        private Object height;
        private long weightDate;
        private long measureTime;
        private double weight;
        private int initialFlag;
        private int basalHeat;
        private double bmi;
        private double bmr;
        private String bmiLevel;
        private int bodyLevel;
        private String bodyType;
        private int bodyAge;
        private double bone;
        private double muscle;
        private double sinew;
        private double protein;
        private double bodyFat;
        private double bodyFfm;
        private double subfat;
        private double visfat;
        private double flesh;
        private double water;
        private double healthScore;

        public String getGid() {
            return gid;
        }

        public int getStatus() {
            return status;
        }

        public String getCreateUser() {
            return createUser;
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public Object getSex() {
            return sex;
        }

        public Object getAge() {
            return age;
        }

        public Object getBirthday() {
            return birthday;
        }

        public Object getHeight() {
            return height;
        }

        public long getWeightDate() {
            return weightDate;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public double getWeight() {
            return weight;
        }

        public int getInitialFlag() {
            return initialFlag;
        }

        public int getBasalHeat() {
            return basalHeat;
        }

        public double getBmi() {
            return bmi;
        }

        public double getBmr() {
            return bmr;
        }

        public String getBmiLevel() {
            return bmiLevel;
        }

        public int getBodyLevel() {
            return bodyLevel;
        }

        public String getBodyType() {
            return bodyType;
        }

        public int getBodyAge() {
            return bodyAge;
        }

        public double getBone() {
            return bone;
        }

        public double getMuscle() {
            return muscle;
        }

        public double getSinew() {
            return sinew;
        }

        public double getProtein() {
            return protein;
        }

        public double getBodyFat() {
            return bodyFat;
        }

        public double getBodyFfm() {
            return bodyFfm;
        }

        public double getSubfat() {
            return subfat;
        }

        public double getVisfat() {
            return visfat;
        }

        public double getFlesh() {
            return flesh;
        }

        public double getWater() {
            return water;
        }

        public double getHealthScore() {
            return healthScore;
        }
    }

    public static class AthleticsInfoListBean {
        /**
         * gid : null
         * status : null
         * createUser : null
         * createTime : null
         * updateUser : null
         * updateTime : null
         * userId : null
         * sex : null
         * age : null
         * birthday : null
         * height : null
         * athlDate : 1537372800000
         * planflag : null
         * athlDesc : null
         * calorie : 2220
         * duration : 10190
         * avgHeart : 149
         * minHeart : 122
         * maxHeart : 235
         * heartCount : null
         * stepNumber : 0
         * kilometers : 0
         * athlRecord : null
         */

        private Object gid;
        private Object status;
        private Object createUser;
        private Object createTime;
        private Object updateUser;
        private Object updateTime;
        private Object userId;
        private Object sex;
        private Object age;
        private Object birthday;
        private Object height;
        private long athlDate;
        private Object planflag;
        private Object athlDesc;
        private int calorie;
        private int duration;
        private int avgHeart;
        private int minHeart;
        private int maxHeart;
        private Object heartCount;
        private int stepNumber;
        private int kilometers;
        private Object athlRecord;

        public Object getGid() {
            return gid;
        }

        public void setGid(Object gid) {
            this.gid = gid;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public Object getAge() {
            return age;
        }

        public void setAge(Object age) {
            this.age = age;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public Object getHeight() {
            return height;
        }

        public void setHeight(Object height) {
            this.height = height;
        }

        public long getAthlDate() {
            return athlDate;
        }

        public void setAthlDate(long athlDate) {
            this.athlDate = athlDate;
        }

        public Object getPlanflag() {
            return planflag;
        }

        public void setPlanflag(Object planflag) {
            this.planflag = planflag;
        }

        public Object getAthlDesc() {
            return athlDesc;
        }

        public void setAthlDesc(Object athlDesc) {
            this.athlDesc = athlDesc;
        }

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getAvgHeart() {
            return avgHeart;
        }

        public void setAvgHeart(int avgHeart) {
            this.avgHeart = avgHeart;
        }

        public int getMinHeart() {
            return minHeart;
        }

        public void setMinHeart(int minHeart) {
            this.minHeart = minHeart;
        }

        public int getMaxHeart() {
            return maxHeart;
        }

        public void setMaxHeart(int maxHeart) {
            this.maxHeart = maxHeart;
        }

        public Object getHeartCount() {
            return heartCount;
        }

        public void setHeartCount(Object heartCount) {
            this.heartCount = heartCount;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public void setStepNumber(int stepNumber) {
            this.stepNumber = stepNumber;
        }

        public int getKilometers() {
            return kilometers;
        }

        public void setKilometers(int kilometers) {
            this.kilometers = kilometers;
        }

        public Object getAthlRecord() {
            return athlRecord;
        }

        public void setAthlRecord(Object athlRecord) {
            this.athlRecord = athlRecord;
        }
    }


    public static class DataListBean {
        /**
         * gid : 50a0fb34364d45c08554e01760dd0846
         * status : 101
         * createUser : e3e35aaff6b84cc29195f270ab7b95a1
         * createTime : 1540603577000
         * updateUser : e3e35aaff6b84cc29195f270ab7b95a1
         * updateTime : 1540622061000
         * userId : e3e35aaff6b84cc29195f270ab7b95a1
         * recordDate : 1540569600000
         * athlCount : 0
         * athlCalorie : 0
         * duration : 0
         * athlPlan : 0
         * heatCount : 0
         * basalCalorie : 0
         * heatCalorie : 0
         * dietPlan : 0
         * weightCount : 11
         * weight : 61.1
         */

        private String gid;
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private String userId;
        private long recordDate;
        private int athlCount;
        private int athlCalorie;
        private int duration;
        private int athlPlan;
        private int heatCount;
        private int basalCalorie;
        private int heatCalorie;
        private int dietPlan;
        private int weightCount;
        private double weight;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public long getRecordDate() {
            return recordDate;
        }

        public void setRecordDate(long recordDate) {
            this.recordDate = recordDate;
        }

        public int getAthlCount() {
            return athlCount;
        }

        public void setAthlCount(int athlCount) {
            this.athlCount = athlCount;
        }

        public int getAthlCalorie() {
            return athlCalorie;
        }

        public void setAthlCalorie(int athlCalorie) {
            this.athlCalorie = athlCalorie;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getAthlPlan() {
            return athlPlan;
        }

        public void setAthlPlan(int athlPlan) {
            this.athlPlan = athlPlan;
        }

        public int getHeatCount() {
            return heatCount;
        }

        public void setHeatCount(int heatCount) {
            this.heatCount = heatCount;
        }

        public int getBasalCalorie() {
            return basalCalorie;
        }

        public void setBasalCalorie(int basalCalorie) {
            this.basalCalorie = basalCalorie;
        }

        public int getHeatCalorie() {
            return heatCalorie;
        }

        public void setHeatCalorie(int heatCalorie) {
            this.heatCalorie = heatCalorie;
        }

        public int getDietPlan() {
            return dietPlan;
        }

        public void setDietPlan(int dietPlan) {
            this.dietPlan = dietPlan;
        }

        public int getWeightCount() {
            return weightCount;
        }

        public void setWeightCount(int weightCount) {
            this.weightCount = weightCount;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public static class DietListBean {

        /**
         * calorie : 1
         * createTime : 1511248354000
         * createUser : 1
         * eatType : 1
         * foodCount : 1
         * foodId : string
         * foodImg : 1
         * foodName : string
         * gid : 1
         * heatDate : 2018-11-02T07:08:25.866Z
         * remark : string
         * status : 101
         * unit : string
         * unitCalorie : 1
         * unitCount : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * weightType : string
         */

        private int calorie;
        private long createTime;
        private int createUser;
        private int eatType;
        private int foodCount;
        private String foodId;
        private int foodImg;
        private String foodName;
        private int gid;
        private long heatDate;
        private String remark;
        private int status;
        private String unit;
        private int unitCalorie;
        private int unitCount;
        private long updateTime;
        private int updateUser;
        private String userId;
        private String weightType;

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

        public int getEatType() {
            return eatType;
        }

        public void setEatType(int eatType) {
            this.eatType = eatType;
        }

        public int getFoodCount() {
            return foodCount;
        }

        public void setFoodCount(int foodCount) {
            this.foodCount = foodCount;
        }

        public String getFoodId() {
            return foodId;
        }

        public void setFoodId(String foodId) {
            this.foodId = foodId;
        }

        public int getFoodImg() {
            return foodImg;
        }

        public void setFoodImg(int foodImg) {
            this.foodImg = foodImg;
        }

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public long getHeatDate() {
            return heatDate;
        }

        public void setHeatDate(long heatDate) {
            this.heatDate = heatDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getUnitCalorie() {
            return unitCalorie;
        }

        public void setUnitCalorie(int unitCalorie) {
            this.unitCalorie = unitCalorie;
        }

        public int getUnitCount() {
            return unitCount;
        }

        public void setUnitCount(int unitCount) {
            this.unitCount = unitCount;
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

        public String getWeightType() {
            return weightType;
        }

        public void setWeightType(String weightType) {
            this.weightType = weightType;
        }
    }

}
