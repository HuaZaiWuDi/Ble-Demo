package lab.wesmartclothing.wefit.flyso.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName DietPlanBean
 * @Date 2018/10/30 18:18
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class DietPlanBean {


    /**
     * belongUser : string
     * dietComment : string
     * editDate : 2018-10-30T10:17:20.525Z
     * foodList : string
     * planName : string
     * pushDate : 2018-10-30T10:17:20.525Z
     * suitablePeople : string
     * totalHeat : 1
     */

    private String belongUser;
    private String dietComment;
    private String editDate;
    private String foodList;
    private String planName;
    private long pushDate;
    private String suitablePeople;
    private int totalHeat;


    public DietPlanBean(long pushDate) {
        this.pushDate = pushDate;
    }

    public String getBelongUser() {
        return belongUser;
    }

    public void setBelongUser(String belongUser) {
        this.belongUser = belongUser;
    }

    public String getDietComment() {
        return dietComment;
    }

    public void setDietComment(String dietComment) {
        this.dietComment = dietComment;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getFoodList() {
        return foodList;
    }

    public void setFoodList(String foodList) {
        this.foodList = foodList;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public long getPushDate() {
        return pushDate;
    }

    public void setPushDate(long pushDate) {
        this.pushDate = pushDate;
    }

    public String getSuitablePeople() {
        return suitablePeople;
    }

    public void setSuitablePeople(String suitablePeople) {
        this.suitablePeople = suitablePeople;
    }

    public int getTotalHeat() {
        return totalHeat;
    }

    public void setTotalHeat(int totalHeat) {
        this.totalHeat = totalHeat;
    }
}
