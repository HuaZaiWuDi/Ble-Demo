package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName PlanIndexBean
 * @Date 2018/10/30 15:34
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class PlanIndexBean {


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
     * athlPlanList : [{"range":1,"duration":5}]
     * todayHeatVO : {"planIntake":0,"realIntake":0,"planDeplete":1860,"realDeplete":0,"planSurplus":1860,"realSurplus":0}
     */

    private String clothesMacAddr;
    private String scalesMacAddr;
    private int planState;
    private int unreadCount;
    private boolean hasTodayWeight;
    private double minNormalWeight;
    private double maxNormalWeight;
    private WeightChangeVOBean weightChangeVO;
    private TargetInfoBean targetInfo;
    private int complete;
    private int hasDays;
    private HeatInfoVOBean heatInfoVO;
    private int totalTime;
    private int totalDeplete;
    private AthlDataBean athlData;
    private TodayHeatVOBean todayHeatVO;
    private List<AthlPlanListBean> athlPlanList;

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

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
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

        public boolean isHasInitialWeight() {
            return hasInitialWeight;
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

            public int getBmr() {
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


            @Override
            public String toString() {
                return "WeightBean{" +
                        "weightDate=" + weightDate +
                        ", measureTime=" + measureTime +
                        ", weight=" + weight +
                        ", initialFlag=" + initialFlag +
                        ", basalHeat=" + basalHeat +
                        ", bmi=" + bmi +
                        ", bmr=" + bmr +
                        ", bmiLevel='" + bmiLevel + '\'' +
                        ", bodyLevel=" + bodyLevel +
                        ", bodyType='" + bodyType + '\'' +
                        ", bodyAge=" + bodyAge +
                        ", bone=" + bone +
                        ", muscle=" + muscle +
                        ", sinew=" + sinew +
                        ", protein=" + protein +
                        ", bodyFat=" + bodyFat +
                        ", bodyFfm=" + bodyFfm +
                        ", subfat=" + subfat +
                        ", visfat=" + visfat +
                        ", flesh=" + flesh +
                        ", water=" + water +
                        ", healthScore=" + healthScore +
                        '}';
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

        public int getCount() {
            return count;
        }

        public long getTargetDate() {
            return targetDate;
        }

        public double getInitialWeight() {
            return initialWeight;
        }

        public double getTargetWeight() {
            return targetWeight;
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
        private int intakePercent;
        private int depletion;

        public int getBasalCalorie() {
            return basalCalorie;
        }

        public int getAbleIntake() {
            return ableIntake;
        }

        public int getIntake() {
            return intake;
        }

        public boolean isWarning() {
            return warning;
        }

        public BreakfastBean getBreakfast() {
            return breakfast;
        }

        public LunchBean getLunch() {
            return lunch;
        }

        public DinnerBean getDinner() {
            return dinner;
        }

        public SnacksBean getSnacks() {
            return snacks;
        }

        public int getIntakePercent() {
            return intakePercent;
        }

        public int getDepletion() {
            return depletion;
        }

        public static class BreakfastBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }
        }

        public static class LunchBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

        }

        public static class DinnerBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

        }

        public static class SnacksBean {
            /**
             * calorie : 0
             * foodList : []
             */

            private int calorie;

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

        }

        @Override
        public String toString() {
            return "HeatInfoVOBean{" +
                    "basalCalorie=" + basalCalorie +
                    ", ableIntake=" + ableIntake +
                    ", intake=" + intake +
                    ", warning=" + warning +
                    ", breakfast=" + breakfast +
                    ", lunch=" + lunch +
                    ", dinner=" + dinner +
                    ", snacks=" + snacks +
                    ", intakePercent=" + intakePercent +
                    ", depletion=" + depletion +
                    '}';
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
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private String userId;
        private int sex;
        private int age;
        private long birthday;
        private int height;
        private long athlDate;
        private int planFlag;
        private int athlScore;
        private String athlDesc;
        private int calorie;
        private int duration;
        private int avgHeart;
        private int minHeart;
        private int maxHeart;
        private int heartCount;
        private int stepNumber;
        private int kilometers;
        private Object athlRecord;

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

        public int getSex() {
            return sex;
        }

        public int getAge() {
            return age;
        }

        public long getBirthday() {
            return birthday;
        }

        public int getHeight() {
            return height;
        }

        public long getAthlDate() {
            return athlDate;
        }

        public int getPlanFlag() {
            return planFlag;
        }

        public int getAthlScore() {
            return athlScore;
        }

        public String getAthlDesc() {
            return athlDesc;
        }

        public int getCalorie() {
            return calorie;
        }

        public int getDuration() {
            return duration;
        }

        public int getAvgHeart() {
            return avgHeart;
        }

        public int getMinHeart() {
            return minHeart;
        }

        public int getMaxHeart() {
            return maxHeart;
        }

        public int getHeartCount() {
            return heartCount;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public int getKilometers() {
            return kilometers;
        }

        public Object getAthlRecord() {
            return athlRecord;
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

    public static class AthlPlanListBean {
        /**
         * range : 1
         * duration : 5
         */

        private int range;
        private int duration;

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
