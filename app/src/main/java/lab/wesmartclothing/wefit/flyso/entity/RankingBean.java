package lab.wesmartclothing.wefit.flyso.entity;

import com.vondear.rxtools.utils.RxRegUtils;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName RankingBean
 * @Date 2019/6/29 14:29
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class RankingBean {


    /**
     * age : 0
     * avatar : string
     * city : string
     * loseWeight : 0
     * phone : string
     * sex : 0
     * totalDays : 0
     * userId : string
     * userName : string
     */

    private int age;
    private String avatar;
    private String city;
    private double loseWeight;
    private String phone;
    private int sex;
    private int totalDays;
    private String userId;
    private String userName;
    private String ranking;
    private int bgImg;
    private int textColor;
    private boolean isTitle;


    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLoseWeight() {
        return loseWeight;
    }

    public void setLoseWeight(double loseWeight) {
        this.loseWeight = loseWeight;
    }

    public String getPhone() {
        return phone.substring(phone.length() - 4);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return RxRegUtils.isMobileSimple(userName) ? userName.substring(userName.length() - 4) : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
