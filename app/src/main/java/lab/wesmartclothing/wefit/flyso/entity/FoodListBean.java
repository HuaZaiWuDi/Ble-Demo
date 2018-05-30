package lab.wesmartclothing.wefit.flyso.entity;

public class FoodListBean {
    /**
     * gid : 03102d9e332c46aeb0bf46bec3d1c4c1
     * heatDate : 1525363200000
     * foodId : AR766369969034952000000000000000
     * updateUser : testuser
     * updateTime : 1524810000000
     * userId : testuser
     * weightType : 100
     * foodName : 嘉顿 生命面包 450g
     * foodCount : 100
     * eatType : 1
     * createTime : 1524810000000
     * foodImg : http://s2.boohee.cn/house/food_small/small_photo_201512618245916435.jpg
     * calorie : 116
     * createUser : testuser
     * foodUnit : 克
     */

    private String gid;
    private long heatDate;
    private String foodId;
    private String updateUser;
    private long updateTime;
    private String userId;
    private String weightType;
    private String foodName;
    private int foodCount;
    private int eatType;
    private long createTime;
    private String foodImg;
    private int calorie;
    private String createUser;
    private String unit;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public long getHeatDate() {
        return heatDate;
    }

    public void setHeatDate(long heatDate) {
        this.heatDate = heatDate;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
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

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public int getEatType() {
        return eatType;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String foodUnit) {
        this.unit = foodUnit;
    }
}