package lab.wesmartclothing.wefit.flyso.entity;

public class TargetInfoBean {
    /**
     * count : 0
     * initialWeight : 0
     * targetDate : 2018-10-26T07:50:16.184Z
     * targetWeight : 0
     * userId : string
     */

    private int count;
    private double initialWeight;
    private String targetDate;
    private double targetWeight;
    private String userId;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}