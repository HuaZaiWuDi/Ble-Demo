package lab.wesmartclothing.wefit.flyso.entity;

public class ListBean {
    /**
     * gid : 159a406c8b5247eebb6d34b677d6cf57
     * status : 101
     * updateUser : 123
     * createUser : 123
     * updateTime : 1525835728804
     * createTime : 1525835728804
     * pageNum : null
     * pageSize : null
     * foodName : 米饭
     * foodImg :
     * typeId : 6cec5aef380f4374af8cb6a2ef453a01
     * typeName : 谷薯芋、杂豆、主食
     * heat : 100
     * unitCount : 1
     * unit : 碗
     * description :
     * remark : 一碗米饭约100卡
     * sort : 0
     * userId : null
     */

    private String gid;
    private int status;
    private String updateUser;
    private String createUser;
    private long updateTime;
    private long createTime;
    private Object pageNum;
    private Object pageSize;
    private String foodName;
    private String foodImg;
    private String typeId;
    private String typeName;
    private int calorie;
    private int foodCount;
    private String unit;
    private String description;
    private String remark;
    private int sort;
    private Object userId;
    private String foodId;
    private int eatType;
    private long heatDate;
    private String mGid;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMGid() {
        return mGid;
    }

    public void setMGid(String mGid) {
        this.mGid = mGid;
    }


    public long getHeatDate() {
        return heatDate;
    }

    public void setHeatDate(long heatDate) {
        this.heatDate = heatDate;
    }

    public int getEatType() {
        return eatType;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getPageNum() {
        return pageNum;
    }

    public void setPageNum(Object pageNum) {
        this.pageNum = pageNum;
    }

    public Object getPageSize() {
        return pageSize;
    }

    public void setPageSize(Object pageSize) {
        this.pageSize = pageSize;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }


    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public String getFoodUnit() {
        return unit;
    }

    public void setFoodUnit(String foodUnit) {
        this.unit = foodUnit;
    }


    @Override
    public String toString() {
        return "ListBean{" +
                "gid='" + gid + '\'' +
                ", status=" + status +
                ", updateUser='" + updateUser + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", foodName='" + foodName + '\'' +
                ", foodImg='" + foodImg + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", calorie=" + calorie +
                ", foodCount=" + foodCount +
                ", unit='" + unit + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", sort=" + sort +
                ", userId=" + userId +
                ", foodId='" + foodId + '\'' +
                ", eatType=" + eatType +
                ", heatDate='" + heatDate + '\'' +
                '}';
    }
}