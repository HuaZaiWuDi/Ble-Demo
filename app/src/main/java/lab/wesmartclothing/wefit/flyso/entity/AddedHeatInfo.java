package lab.wesmartclothing.wefit.flyso.entity;

/**
 * Created by jk on 2018/5/28.
 */
public class AddedHeatInfo {


    /**
     * calorie : 0
     * eatType : 0
     * foodCount : 0
     * foodId : string
     * foodName : string
     * foodUnit : string
     * heatDate : 2018-05-28T09:29:36.413Z
     * remark : string
     * userId : string
     * weightType : string
     */

    private int calorie;
    private int eatType;
    private int foodCount;
    private String foodId;
    private String foodName;
    private String unit;
    private String heatDate;
    private String remark;
    private String userId;
    private String weightType;
    private String gid;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String foodUnit) {
        this.unit = foodUnit;
    }

    public String getHeatDate() {
        return heatDate;
    }

    public void setHeatDate(String heatDate) {
        this.heatDate = heatDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
