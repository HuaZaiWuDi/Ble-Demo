package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/8/3.
 */
public class FetchHeatInfoBean {


    /**
     * ableIntake : 0
     * breakfast : {"calorie":0,"foodList":[{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]}
     * depletion : 0
     * dinner : {"calorie":0,"foodList":[{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]}
     * intake : 0
     * intakePercent : 0
     * lunch : {"calorie":0,"foodList":[{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]}
     * snacks : {"calorie":0,"foodList":[{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]}
     * warning : true
     */

    private int ableIntake;
    private BreakfastBean breakfast;
    private int depletion;
    private DinnerBean dinner;
    private int intake;
    private double intakePercent;
    private LunchBean lunch;
    private SnacksBean snacks;
    private boolean warning;

    public int getAbleIntake() {
        return ableIntake;
    }

    public void setAbleIntake(int ableIntake) {
        this.ableIntake = ableIntake;
    }

    public BreakfastBean getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(BreakfastBean breakfast) {
        this.breakfast = breakfast;
    }

    public int getDepletion() {
        return depletion;
    }

    public void setDepletion(int depletion) {
        this.depletion = depletion;
    }

    public DinnerBean getDinner() {
        return dinner;
    }

    public void setDinner(DinnerBean dinner) {
        this.dinner = dinner;
    }

    public int getIntake() {
        return intake;
    }

    public void setIntake(int intake) {
        this.intake = intake;
    }

    public double getIntakePercent() {
        return intakePercent;
    }

    public void setIntakePercent(double intakePercent) {
        this.intakePercent = intakePercent;
    }

    public void setLunch(LunchBean lunch) {
        this.lunch = lunch;
    }

    public LunchBean getLunch() {
        return lunch;
    }

    public SnacksBean getSnacks() {
        return snacks;
    }

    public void setSnacks(SnacksBean snacks) {
        this.snacks = snacks;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public static class BreakfastBean {
        /**
         * calorie : 0
         * foodList : [{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]
         */

        private int calorie;
        private List<FoodListBean> foodList;

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }


    }

    public static class DinnerBean {
        /**
         * calorie : 0
         * foodList : [{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]
         */

        private int calorie;
        private List<FoodListBean> foodList;

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }


    }

    public static class LunchBean {
        /**
         * calorie : 0
         * foodList : [{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]
         */

        private int calorie;
        private List<FoodListBean> foodList;

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }

    public static class SnacksBean {
        /**
         * calorie : 0
         * foodList : [{"calorie":1,"createTime":1511248354000,"createUser":1,"eatType":1,"foodCount":1,"foodId":"string","foodImg":1,"foodName":"string","gid":1,"heatDate":"2018-08-03T06:51:22.746Z","remark":"string","status":101,"unit":"string","updateTime":1511248354000,"updateUser":1,"userId":"string","weightType":"string"}]
         */

        private int calorie;
        private List<FoodListBean> foodList;

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }
}
