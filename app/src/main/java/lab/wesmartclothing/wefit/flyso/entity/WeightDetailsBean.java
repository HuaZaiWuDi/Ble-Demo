package lab.wesmartclothing.wefit.flyso.entity;

/**
 * Created by jk on 2018/7/30.
 */
public class WeightDetailsBean {


    /**
     * bodyType : string
     * levelDesc : string
     * sickLevel : string
     * weightInfo : {"bmi":0,"bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyType":"string","bone":0,"createTime":1511248354000,"createUser":1,"flesh":0,"gid":1,"healthScore":0,"measureTime":"2018-07-30T09:35:52.213Z","muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":0,"water":0,"weight":0,"weightDate":"2018-07-30T09:35:52.213Z"}
     */
    private int bodyLevel;//1-9表示体型标准等级，0表示未获取
    private String bodyType;
    private String levelDesc;
    private String sickLevel;
    private HealthyInfoBean weightInfo;


    public int getBodyLevel() {
        return bodyLevel;
    }

    public void setBodyLevel(int bodyLevel) {
        this.bodyLevel = bodyLevel;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public String getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(String sickLevel) {
        this.sickLevel = sickLevel;
    }

    public HealthyInfoBean getWeightInfo() {
        return weightInfo;
    }

    public void setWeightInfo(HealthyInfoBean weightInfo) {
        this.weightInfo = weightInfo;
    }

}
