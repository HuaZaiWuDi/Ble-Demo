package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/8/23.
 */
public class FoodRecommendBean {

    private List<FoodListBean> breakfastList;
    private List<FoodListBean> dinnerList;
    private List<FoodListBean> lunchList;

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


}
