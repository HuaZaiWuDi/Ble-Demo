package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;

/**
 * Created icon_hide_password jk on 2018/5/16.
 */
public class WeightAddBean implements Serializable {
    /**
     * userId	string		用户ID
     * measureTime	datetime		测量时间
     * weight	double		当前体重。单位：kg
     * bmi	double		身体质量指数
     * subfat	double		皮下脂肪率
     * visfat	double		内脏脂肪等级
     * bmr	double		基础代谢量
     * flesh	double		肌肉。单位：kg
     * bodyAge	int		身体年龄
     * bodyfat	double		体脂。单位：百分比
     * bodyType	int		体型。1-瘦，2-正常，3-微胖，4-肥胖
     * bone	double		骨骼。单位：kg
     * muscle	double		骨骼肌率
     * protein	double		蛋白质
     * flesh	double		肌肉。单位：kg
     * water	double		水分。单位：百分比
     */

    String userId;
    String measureTime;
    double weight;
    double bmi;
    double subfat;
    double visfat;
    double bmr;
    double flesh;
    int bodyAge;
    double bodyFat;
    String bodyType;
    double bone;
    double muscle;
    double protein;
    double water;
    double bodyFfm;
    double healthScore;
    double sinew;
    int bodyTypeIndex = 0;

    public int getBodyTypeIndex() {
        return bodyTypeIndex;
    }

    public void setBodyTypeIndex(int bodyTypeIndex) {
        this.bodyTypeIndex = bodyTypeIndex;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public double getBodyFfm() {
        return bodyFfm;
    }

    public void setBodyFfm(double bodyFfm) {
        this.bodyFfm = bodyFfm;
    }

    public double getSinew() {
        return sinew;
    }

    public void setSinew(double sinew) {
        this.sinew = sinew;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getSubfat() {
        return subfat;
    }

    public void setSubfat(double subfat) {
        this.subfat = subfat;
    }

    public double getVisfat() {
        return visfat;
    }

    public void setVisfat(double visfat) {
        this.visfat = visfat;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getFlesh() {
        return flesh;
    }

    public void setFlesh(double flesh) {
        this.flesh = flesh;
    }

    public int getBodyAge() {
        return bodyAge;
    }

    public void setBodyAge(int bodyAge) {
        this.bodyAge = bodyAge;
    }

    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public double getBone() {
        return bone;
    }

    public void setBone(double bone) {
        this.bone = bone;
    }

    public double getMuscle() {
        return muscle;
    }

    public void setMuscle(double muscle) {
        this.muscle = muscle;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }


    @Override
    public String toString() {
        return "WeightAddBean{" +
                "userId='" + userId + '\'' +
                ", measureTime='" + measureTime + '\'' +
                ", weight=" + weight +
                ", bmi=" + bmi +
                ", subfat=" + subfat +
                ", visfat=" + visfat +
                ", bmr=" + bmr +
                ", flesh=" + flesh +
                ", bodyAge=" + bodyAge +
                ", bodyfat=" + bodyFat +
                ", bodyType=" + bodyType +
                ", bone=" + bone +
                ", muscle=" + muscle +
                ", protein=" + protein +
                ", water=" + water +
                '}';
    }
}
