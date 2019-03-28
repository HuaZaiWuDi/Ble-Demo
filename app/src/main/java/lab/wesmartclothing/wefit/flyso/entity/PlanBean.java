package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName PlanBean
 * @Date 2018/10/30 19:22
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class PlanBean {


    /**
     * clothesMacAddr : FD:A9:C8:0C:F5:3E
     * scalesMacAddr : C2:46:D9:0E:8C:D9
     * planState : 3
     * unreadCount : 0
     * hasTodayWeight : false
     * minNormalWeight : 50.37
     * maxNormalWeight : 65.07
     * weightChangeVO : {"weight":{"gid":"4745b7f8c347463a9b978f8c9bd3e4a3","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540621751000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540622061000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1540569600000,"measureTime":1540622060000,"weight":61.1,"initialFlag":1,"basalHeat":1537,"bmi":22.44,"bmr":0,"bmiLevel":"S","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0},"hasInitialWeight":true,"weightChange":0,"bmiChange":0,"bmrChange":0,"bodyFatChange":0,"basalHeatChange":0,"sinewChange":0,"bodyAgeChange":0}
     * targetInfo : {"gid":"b223317959b84c8c9e8ce19b1ec3e7e1","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1534320257000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540630572000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","count":4,"targetDate":1542988800000,"initialWeight":59,"targetWeight":56.9}
     * complete : -1
     * hasDays : 24
     * heatInfoVO : {"basalCalorie":1537,"ableIntake":1537,"intake":0,"warning":false,"breakfast":{"calorie":0,"foodList":[]},"lunch":{"calorie":0,"foodList":[]},"dinner":{"calorie":0,"foodList":[]},"snacks":{"calorie":0,"foodList":[]},"intakePercent":0,"depletion":0}
     * totalTime : 60
     * totalDeplete : 1860
     * athlData : {"gid":"000652bfcce34fd6ae545d5e50d28cf9","status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"athlDate":1540828800000,"planFlag":null,"athlScore":null,"athlDesc":"","calorie":7469,"duration":68146,"avgHeart":106,"minHeart":117,"maxHeart":140,"heartCount":null,"stepNumber":0,"kilometers":0,"athlRecord":null}
     * athlPlanList : [{"range":1,"duration":5},{"range":2,"duration":5},{"range":3,"duration":5},{"range":4,"duration":5}]
     * todayHeatVO : {"planIntake":0,"realIntake":0,"planDeplete":1860,"realDeplete":0,"planSurplus":1860,"realSurplus":0}
     */

    private String athlPlanBelongUser;
    private String dietPlanBelongUser;
    private String clothesMacAddr;
    private String scalesMacAddr;
    private int planState;
    private int unreadCount;
    private boolean hasTodayWeight;
    private double minNormalWeight;
    private double maxNormalWeight;
    private WeightChangeVOBean weightChangeVO;
    private TargetInfoBean targetInfo;
    private double complete;
    private int hasDays;
    private HeatInfoVOBean heatInfoVO;
    private int totalTime;
    private int totalDeplete;
    private AthlDataBean athlData;
    private TodayHeatVOBean todayHeatVO;
    private List<AthlPlanListBean> athlPlanList;
    private String informGid;

    public String getInformGid() {
        return informGid;
    }

    public void setInformGid(String informGid) {
        this.informGid = informGid;
    }

    public void setAthlPlanBelongUser(String athlPlanBelongUser) {
        this.athlPlanBelongUser = athlPlanBelongUser;
    }

    public void setDietPlanBelongUser(String dietPlanBelongUser) {
        this.dietPlanBelongUser = dietPlanBelongUser;
    }

    public String getAthlPlanBelongUser() {
        return athlPlanBelongUser;
    }

    public String getDietPlanBelongUser() {
        return dietPlanBelongUser;
    }

    public String getClothesMacAddr() {
        return clothesMacAddr;
    }

    public void setClothesMacAddr(String clothesMacAddr) {
        this.clothesMacAddr = clothesMacAddr;
    }

    public String getScalesMacAddr() {
        return scalesMacAddr;
    }

    public void setScalesMacAddr(String scalesMacAddr) {
        this.scalesMacAddr = scalesMacAddr;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isHasTodayWeight() {
        return hasTodayWeight;
    }

    public void setHasTodayWeight(boolean hasTodayWeight) {
        this.hasTodayWeight = hasTodayWeight;
    }

    public double getMinNormalWeight() {
        return minNormalWeight;
    }

    public void setMinNormalWeight(double minNormalWeight) {
        this.minNormalWeight = minNormalWeight;
    }

    public double getMaxNormalWeight() {
        return maxNormalWeight;
    }

    public void setMaxNormalWeight(double maxNormalWeight) {
        this.maxNormalWeight = maxNormalWeight;
    }

    public WeightChangeVOBean getWeightChangeVO() {
        return weightChangeVO;
    }

    public void setWeightChangeVO(WeightChangeVOBean weightChangeVO) {
        this.weightChangeVO = weightChangeVO;
    }

    public TargetInfoBean getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(TargetInfoBean targetInfo) {
        this.targetInfo = targetInfo;
    }

    public double getComplete() {
        return complete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }

    public int getHasDays() {
        return hasDays;
    }

    public void setHasDays(int hasDays) {
        this.hasDays = hasDays;
    }

    public HeatInfoVOBean getHeatInfoVO() {
        return heatInfoVO;
    }

    public void setHeatInfoVO(HeatInfoVOBean heatInfoVO) {
        this.heatInfoVO = heatInfoVO;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalDeplete() {
        return totalDeplete;
    }

    public void setTotalDeplete(int totalDeplete) {
        this.totalDeplete = totalDeplete;
    }

    public AthlDataBean getAthlData() {
        return athlData;
    }

    public void setAthlData(AthlDataBean athlData) {
        this.athlData = athlData;
    }

    public TodayHeatVOBean getTodayHeatVO() {
        return todayHeatVO;
    }

    public void setTodayHeatVO(TodayHeatVOBean todayHeatVO) {
        this.todayHeatVO = todayHeatVO;
    }

    public List<AthlPlanListBean> getAthlPlanList() {
        return athlPlanList;
    }

    public void setAthlPlanList(List<AthlPlanListBean> athlPlanList) {
        this.athlPlanList = athlPlanList;
    }

    public static class WeightChangeVOBean {
        /**
         * weight : {"gid":"4745b7f8c347463a9b978f8c9bd3e4a3","status":101,"createUser":"e3e35aaff6b84cc29195f270ab7b95a1","createTime":1540621751000,"updateUser":"e3e35aaff6b84cc29195f270ab7b95a1","updateTime":1540622061000,"userId":"e3e35aaff6b84cc29195f270ab7b95a1","sex":null,"age":null,"birthday":null,"height":null,"weightDate":1540569600000,"measureTime":1540622060000,"weight":61.1,"initialFlag":1,"basalHeat":1537,"bmi":22.44,"bmr":0,"bmiLevel":"S","bodyLevel":4,"bodyType":"缺乏运动","bodyAge":0,"bone":0,"muscle":0,"sinew":0,"protein":0,"bodyFat":0,"bodyFfm":0,"subfat":0,"visfat":0,"flesh":0,"water":0,"healthScore":0}
         * hasInitialWeight : true
         * weightChange : 0
         * bmiChange : 0
         * bmrChange : 0
         * bodyFatChange : 0
         * basalHeatChange : 0
         * sinewChange : 0
         * bodyAgeChange : 0
         */

        private WeightBean weight;
        private boolean hasInitialWeight;
        private double weightChange;
        private double bmiChange;
        private double bmrChange;
        private double bodyFatChange;
        private double basalHeatChange;
        private double sinewChange;
        private double bodyAgeChange;

        public WeightBean getWeight() {
            return weight;
        }

        public void setWeight(WeightBean weight) {
            this.weight = weight;
        }

        public boolean isHasInitialWeight() {
            return hasInitialWeight;
        }

        public void setHasInitialWeight(boolean hasInitialWeight) {
            this.hasInitialWeight = hasInitialWeight;
        }

        public double getWeightChange() {
            return weightChange;
        }

        public double getBmiChange() {
            return bmiChange;
        }

        public double getBmrChange() {
            return bmrChange;
        }

        public double getBodyFatChange() {
            return bodyFatChange;
        }

        public double getBasalHeatChange() {
            return basalHeatChange;
        }

        public double getSinewChange() {
            return sinewChange;
        }

        public double getBodyAgeChange() {
            return bodyAgeChange;
        }


        public void setWeightChange(double weightChange) {
            this.weightChange = weightChange;
        }

        public void setBmiChange(double bmiChange) {
            this.bmiChange = bmiChange;
        }

        public void setBmrChange(double bmrChange) {
            this.bmrChange = bmrChange;
        }

        public void setBodyFatChange(double bodyFatChange) {
            this.bodyFatChange = bodyFatChange;
        }

        public void setBasalHeatChange(double basalHeatChange) {
            this.basalHeatChange = basalHeatChange;
        }

        public void setSinewChange(double sinewChange) {
            this.sinewChange = sinewChange;
        }

        public void setBodyAgeChange(double bodyAgeChange) {
            this.bodyAgeChange = bodyAgeChange;
        }

        public static class WeightBean {
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
            private int bmr;
            private String bmiLevel;
            private int bodyLevel;
            private String bodyType;
            private int bodyAge;
            private int bone;
            private int muscle;
            private int sinew;
            private int protein;
            private int bodyFat;
            private int bodyFfm;
            private int subfat;
            private int visfat;
            private int flesh;
            private int water;
            private double healthScore;

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

            public long getWeightDate() {
                return weightDate;
            }

            public void setWeightDate(long weightDate) {
                this.weightDate = weightDate;
            }

            public long getMeasureTime() {
                return measureTime;
            }

            public void setMeasureTime(long measureTime) {
                this.measureTime = measureTime;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public int getInitialFlag() {
                return initialFlag;
            }

            public void setInitialFlag(int initialFlag) {
                this.initialFlag = initialFlag;
            }

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

            public int getBmr() {
                return bmr;
            }

            public void setBmr(int bmr) {
                this.bmr = bmr;
            }

            public String getBmiLevel() {
                return bmiLevel;
            }

            public void setBmiLevel(String bmiLevel) {
                this.bmiLevel = bmiLevel;
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

            public int getBodyAge() {
                return bodyAge;
            }

            public void setBodyAge(int bodyAge) {
                this.bodyAge = bodyAge;
            }

            public int getBone() {
                return bone;
            }

            public void setBone(int bone) {
                this.bone = bone;
            }

            public int getMuscle() {
                return muscle;
            }

            public void setMuscle(int muscle) {
                this.muscle = muscle;
            }

            public int getSinew() {
                return sinew;
            }

            public void setSinew(int sinew) {
                this.sinew = sinew;
            }

            public int getProtein() {
                return protein;
            }

            public void setProtein(int protein) {
                this.protein = protein;
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

            public int getSubfat() {
                return subfat;
            }

            public void setSubfat(int subfat) {
                this.subfat = subfat;
            }

            public int getVisfat() {
                return visfat;
            }

            public void setVisfat(int visfat) {
                this.visfat = visfat;
            }

            public int getFlesh() {
                return flesh;
            }

            public void setFlesh(int flesh) {
                this.flesh = flesh;
            }

            public int getWater() {
                return water;
            }

            public void setWater(int water) {
                this.water = water;
            }

            public double getHealthScore() {
                return healthScore;
            }

            public void setHealthScore(int healthScore) {
                this.healthScore = healthScore;
            }
        }


        @Override
        public String toString() {
            return "WeightChangeVOBean{" +
                    "weight=" + weight +
                    ", hasInitialWeight=" + hasInitialWeight +
                    ", weightChange=" + weightChange +
                    ", bmiChange=" + bmiChange +
                    ", bmrChange=" + bmrChange +
                    ", bodyFatChange=" + bodyFatChange +
                    ", basalHeatChange=" + basalHeatChange +
                    ", sinewChange=" + sinewChange +
                    ", bodyAgeChange=" + bodyAgeChange +
                    '}';
        }
    }

    public static class TargetInfoBean {
        /**
         * gid : b223317959b84c8c9e8ce19b1ec3e7e1
         * status : 101
         * createUser : e3e35aaff6b84cc29195f270ab7b95a1
         * createTime : 1534320257000
         * updateUser : e3e35aaff6b84cc29195f270ab7b95a1
         * updateTime : 1540630572000
         * userId : e3e35aaff6b84cc29195f270ab7b95a1
         * count : 4
         * targetDate : 1542988800000
         * initialWeight : 59
         * targetWeight : 56.9
         */

        private String gid;
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private String userId;
        private int count;
        private long targetDate;
        private double initialWeight;
        private double targetWeight;

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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getTargetDate() {
            return targetDate;
        }

        public void setTargetDate(long targetDate) {
            this.targetDate = targetDate;
        }


        public double getInitialWeight() {
            return initialWeight;
        }

        public void setInitialWeight(double initialWeight) {
            this.initialWeight = initialWeight;
        }

        public double getTargetWeight() {
            return targetWeight;
        }

        public void setTargetWeight(double targetWeight) {
            this.targetWeight = targetWeight;
        }
    }

    public static class HeatInfoVOBean {
        /**
         * basalCalorie : 1537
         * ableIntake : 1537
         * intake : 0
         * warning : false
         * breakfast : {"calorie":0,"foodList":[]}
         * lunch : {"calorie":0,"foodList":[]}
         * dinner : {"calorie":0,"foodList":[]}
         * snacks : {"calorie":0,"foodList":[]}
         * intakePercent : 0
         * depletion : 0
         */

        private int basalCalorie;
        private int ableIntake;
        private int intake;
        private boolean warning;
        private BreakfastBean breakfast;
        private LunchBean lunch;
        private DinnerBean dinner;
        private SnacksBean snacks;
        private double intakePercent;
        private int depletion;

        public int getBasalCalorie() {
            return basalCalorie;
        }

        public void setBasalCalorie(int basalCalorie) {
            this.basalCalorie = basalCalorie;
        }

        public int getAbleIntake() {
            return ableIntake;
        }

        public void setAbleIntake(int ableIntake) {
            this.ableIntake = ableIntake;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public boolean isWarning() {
            return warning;
        }

        public void setWarning(boolean warning) {
            this.warning = warning;
        }

        public BreakfastBean getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(BreakfastBean breakfast) {
            this.breakfast = breakfast;
        }

        public LunchBean getLunch() {
            return lunch;
        }

        public void setLunch(LunchBean lunch) {
            this.lunch = lunch;
        }

        public DinnerBean getDinner() {
            return dinner;
        }

        public void setDinner(DinnerBean dinner) {
            this.dinner = dinner;
        }

        public SnacksBean getSnacks() {
            return snacks;
        }

        public void setSnacks(SnacksBean snacks) {
            this.snacks = snacks;
        }

        public double getIntakePercent() {
            return intakePercent;
        }

        public void setIntakePercent(double intakePercent) {
            this.intakePercent = intakePercent;
        }

        public int getDepletion() {
            return depletion;
        }

        public void setDepletion(int depletion) {
            this.depletion = depletion;
        }

        public static class BreakfastBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;
            private List<?> foodList;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

            public List<?> getFoodList() {
                return foodList;
            }

            public void setFoodList(List<?> foodList) {
                this.foodList = foodList;
            }
        }

        public static class LunchBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;
            private List<?> foodList;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

            public List<?> getFoodList() {
                return foodList;
            }

            public void setFoodList(List<?> foodList) {
                this.foodList = foodList;
            }
        }

        public static class DinnerBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;
            private List<?> foodList;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

            public List<?> getFoodList() {
                return foodList;
            }

            public void setFoodList(List<?> foodList) {
                this.foodList = foodList;
            }
        }

        public static class SnacksBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;
            private List<?> foodList;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

            public List<?> getFoodList() {
                return foodList;
            }

            public void setFoodList(List<?> foodList) {
                this.foodList = foodList;
            }
        }
    }

    public static class AthlDataBean {
        /**
         * gid : 000652bfcce34fd6ae545d5e50d28cf9
         * status : null
         * createUser : null
         * createTime : null
         * updateUser : null
         * updateTime : null
         * userId : e3e35aaff6b84cc29195f270ab7b95a1
         * sex : null
         * age : null
         * birthday : null
         * height : null
         * athlDate : 1540828800000
         * planFlag : null
         * athlScore : null
         * athlDesc :
         * calorie : 7469
         * duration : 68146
         * avgHeart : 106
         * minHeart : 117
         * maxHeart : 140
         * heartCount : null
         * stepNumber : 0
         * kilometers : 0
         * athlRecord : null
         */

        private String gid;
        private Object status;
        private Object createUser;
        private Object createTime;
        private Object updateUser;
        private Object updateTime;
        private String userId;
        private Object sex;
        private Object age;
        private Object birthday;
        private Object height;
        private long athlDate;
        private Object planFlag;
        private Object athlScore;
        private String athlDesc;
        private int calorie;
        private int duration;
        private int avgHeart;
        private int minHeart;
        private int maxHeart;
        private Object heartCount;
        private int stepNumber;
        private int kilometers;
        private Object athlRecord;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
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

        public Object getPlanFlag() {
            return planFlag;
        }

        public void setPlanFlag(Object planFlag) {
            this.planFlag = planFlag;
        }

        public Object getAthlScore() {
            return athlScore;
        }

        public void setAthlScore(Object athlScore) {
            this.athlScore = athlScore;
        }

        public String getAthlDesc() {
            return athlDesc;
        }

        public void setAthlDesc(String athlDesc) {
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

    public static class TodayHeatVOBean {
        /**
         * planIntake : 0
         * realIntake : 0
         * planDeplete : 1860
         * realDeplete : 0
         * planSurplus : 1860
         * realSurplus : 0
         */

        private int planIntake;
        private int realIntake;
        private int planDeplete;
        private int realDeplete;
        private int planSurplus;
        private int realSurplus;

        public int getPlanIntake() {
            return planIntake;
        }

        public void setPlanIntake(int planIntake) {
            this.planIntake = planIntake;
        }

        public int getRealIntake() {
            return realIntake;
        }

        public void setRealIntake(int realIntake) {
            this.realIntake = realIntake;
        }

        public int getPlanDeplete() {
            return planDeplete;
        }

        public void setPlanDeplete(int planDeplete) {
            this.planDeplete = planDeplete;
        }

        public int getRealDeplete() {
            return realDeplete;
        }

        public void setRealDeplete(int realDeplete) {
            this.realDeplete = realDeplete;
        }

        public int getPlanSurplus() {
            return planSurplus;
        }

        public void setPlanSurplus(int planSurplus) {
            this.planSurplus = planSurplus;
        }

        public int getRealSurplus() {
            return realSurplus;
        }

        public void setRealSurplus(int realSurplus) {
            this.realSurplus = realSurplus;
        }
    }


}
