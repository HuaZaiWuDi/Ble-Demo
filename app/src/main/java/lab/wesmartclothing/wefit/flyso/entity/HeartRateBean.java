package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.entity.sql.HeartRateTab;

/**
 * Created icon_hide_password jk on 2018/6/11.
 */
public class HeartRateBean {


    /**
     * athlDate : 2018-11-07T06:56:21.290Z
     * athlDesc : string
     * athlScore : 1
     * heartList : [{"heartRate":120,"heartTime":"2018-11-07T06:56:21.290Z","stepTime":2}]
     * planFlag : 0
     * stepNumber : 1
     */

    private String athlDate;
    private String athlDesc;
    private double athlScore;
    private int planFlag;//定制课程：1
    private int stepNumber;
    private List<HeartRateTab> heartList;

    public String getAthlDate() {
        return athlDate;
    }

    public void setAthlDate(String athlDate) {
        this.athlDate = athlDate;
    }

    public String getAthlDesc() {
        return athlDesc;
    }

    public void setAthlDesc(String athlDesc) {
        this.athlDesc = athlDesc;
    }

    public double getAthlScore() {
        return athlScore;
    }

    public void setAthlScore(double athlScore) {
        this.athlScore = athlScore;
    }

    public int getPlanFlag() {
        return planFlag;
    }

    public void setPlanFlag(int planFlag) {
        this.planFlag = planFlag;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public List<HeartRateTab> getHeartList() {
        return heartList;
    }

    public void setHeartList(List<HeartRateTab> heartList) {
        this.heartList = heartList;
    }


}
