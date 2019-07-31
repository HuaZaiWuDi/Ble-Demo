package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName HealthyInfoBean
 * @Date 2019/3/6 15:13
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class HealthyInfoBean implements Serializable {


    /**
     * age : 28
     * basalHeat : 1606
     * birthday : 654710400000
     * bmi : 21.9
     * bmr : 1580.0
     * bodyAge : 24
     * bodyFat : 12.4
     * bodyFfm : 55.99
     * bodyLevel : 5
     * bodyType : 标准
     * bone : 2.8
     * createTime : 1538986549000
     * createUser : 9801eee7ffce4364bed693e9cca8b681
     * flesh : 0.0
     * gid : 8acd8c17582447f78b49dba05bd37d2d
     * healthScore : 94.2
     * height : 171.0
     * measureTime : 1538986547000
     * muscle : 56.6
     * protein : 20.01
     * sex : 1
     * sinew : 53.22
     * status : 101
     * subfat : 11.1
     * updateTime : 1538986549000
     * updateUser : 9801eee7ffce4364bed693e9cca8b681
     * userId : 9801eee7ffce4364bed693e9cca8b681
     * visfat : 4.0
     * water : 63.2
     * weight : 63.95
     * weightDate : 1538928000000
     */

    private int age;
    private int basalHeat;
    private long birthday;
    private double bmi;
    private double bmr;
    private int bodyAge;
    private double bodyFat;
    private double bodyFfm;
    private int bodyLevel;
    private String bodyType;
    private double bone;
    private long createTime;
    private String createUser;
    private double flesh;
    private String gid;
    private double healthScore;
    private double height;
    private long measureTime;
    private double muscle;
    private double protein;
    private int sex;
    private double sinew;
    private int status;
    private double subfat;
    private long updateTime;
    private String updateUser;
    private String userId;
    private double visfat;
    private double water;
    private double weight;
    private long weightDate;
    private String bmiLevel;
    private String hmac;

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getBmiLevel() {
        return bmiLevel;
    }

    public void setBmiLevel(String bmiLevel) {
        this.bmiLevel = bmiLevel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBasalHeat() {
        return basalHeat;
    }

    public void setBasalHeat(int basalHeat) {
        this.basalHeat = basalHeat;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
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

    public double getBodyFfm() {
        return bodyFfm;
    }

    public void setBodyFfm(double bodyFfm) {
        this.bodyFfm = bodyFfm;
    }

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

    public double getBone() {
        return bone;
    }

    public void setBone(double bone) {
        this.bone = bone;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public double getFlesh() {
        return flesh;
    }

    public void setFlesh(double flesh) {
        this.flesh = flesh;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public long getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(long measureTime) {
        this.measureTime = measureTime;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getSinew() {
        return sinew;
    }

    public void setSinew(double sinew) {
        this.sinew = sinew;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getSubfat() {
        return subfat;
    }

    public void setSubfat(double subfat) {
        this.subfat = subfat;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getVisfat() {
        return visfat;
    }

    public void setVisfat(double visfat) {
        this.visfat = visfat;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(long weightDate) {
        this.weightDate = weightDate;
    }
}
