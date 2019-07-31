package lab.wesmartclothing.wefit.flyso.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName SubmitInfoFrom
 * @Date 2018/10/26 17:55
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class SubmitInfoFrom {


    /**
     * answer : string
     * targetInfo : {"count":0,"initialWeight":0,"targetDate":"2018-10-26T07:50:16.184Z","targetWeight":0,"userId":"string"}
     * weightInfo : {"age":18,"basalHeat":1,"birthday":"2018-10-26T07:50:16.184Z","bmi":1,"bmiLevel":1,"bmr":1,"bodyAge":1,"bodyFat":1,"bodyFfm":1,"bodyLevel":1,"bodyType":"string","bone":1,"flesh":1,"healthScore":1,"height":170,"measureTime":"2018-10-26T07:50:16.184Z","muscle":1,"protein":1,"sex":1,"sinew":1,"subfat":1,"userId":"string","visfat":1,"water":1,"weight":60,"weightDate":"2018-10-26T07:50:16.184Z"}
     */

    private String answer;
    private TargetInfoBean targetInfo;
    private HealthyInfoBean weightInfo;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public TargetInfoBean getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(TargetInfoBean targetInfo) {
        this.targetInfo = targetInfo;
    }

    public HealthyInfoBean getWeightInfo() {
        return weightInfo;
    }

    public void setWeightInfo(HealthyInfoBean weightInfo) {
        this.weightInfo = weightInfo;
    }


}
