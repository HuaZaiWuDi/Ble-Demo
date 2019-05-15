package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/8/23.
 */
public class FoodRecommendBean {


    /**
     * dateList : ["2018-10-30T10:17:20.599Z"]
     * foodPlan : {"breakfastList":[{"createTime":1511248354000,"createUser":1,"description":"string","foodImg":"string","foodName":"string","gid":1,"remark":"string","showImg":"string","sort":1,"status":101,"typeId":"string","typeName":"string","unit":"string","unitCalorie":1,"unitCount":1,"updateTime":1511248354000,"updateUser":1}],"dinnerList":[{"createTime":1511248354000,"createUser":1,"description":"string","foodImg":"string","foodName":"string","gid":1,"remark":"string","showImg":"string","sort":1,"status":101,"typeId":"string","typeName":"string","unit":"string","unitCalorie":1,"unitCount":1,"updateTime":1511248354000,"updateUser":1}],"lunchList":[{"createTime":1511248354000,"createUser":1,"description":"string","foodImg":"string","foodName":"string","gid":1,"remark":"string","showImg":"string","sort":1,"status":101,"typeId":"string","typeName":"string","unit":"string","unitCalorie":1,"unitCount":1,"updateTime":1511248354000,"updateUser":1}],"snackList":[{"createTime":1511248354000,"createUser":1,"description":"string","foodImg":"string","foodName":"string","gid":1,"remark":"string","showImg":"string","sort":1,"status":101,"typeId":"string","typeName":"string","unit":"string","unitCalorie":1,"unitCount":1,"updateTime":1511248354000,"updateUser":1}]}
     * hasFoodPlan : true
     */

    private FoodPlanBean foodPlan;
    private boolean hasFoodPlan;
    private List<Long> dateList;
    private String planName;
    private String planAdvice;//饮食建议

    public String getPlanAdvice() {
        return planAdvice;
    }

    public void setPlanAdvice(String planAdvice) {
        this.planAdvice = planAdvice;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public FoodPlanBean getFoodPlan() {
        return foodPlan;
    }

    public void setFoodPlan(FoodPlanBean foodPlan) {
        this.foodPlan = foodPlan;
    }

    public boolean isHasFoodPlan() {
        return hasFoodPlan;
    }

    public void setHasFoodPlan(boolean hasFoodPlan) {
        this.hasFoodPlan = hasFoodPlan;
    }

    public List<Long> getDateList() {
        return dateList;
    }

    public void setDateList(List<Long> dateList) {
        this.dateList = dateList;
    }

    public static class FoodPlanBean {
        private List<FoodListBean> breakfastList;
        private List<FoodListBean> dinnerList;
        private List<FoodListBean> lunchList;
        private List<FoodListBean> snackList;

        public List<FoodListBean> getBreakfastList() {
            return breakfastList;
        }

        public void setBreakfastList(List<FoodListBean> breakfastList) {
            this.breakfastList = breakfastList;
        }

        public List<FoodListBean> getDinnerList() {
            return dinnerList;
        }

        public void setDinnerList(List<FoodListBean> dinnerList) {
            this.dinnerList = dinnerList;
        }

        public List<FoodListBean> getLunchList() {
            return lunchList;
        }

        public void setLunchList(List<FoodListBean> lunchList) {
            this.lunchList = lunchList;
        }

        public List<FoodListBean> getSnackList() {
            return snackList;
        }

        public void setSnackList(List<FoodListBean> snackList) {
            this.snackList = snackList;
        }

    }
}
