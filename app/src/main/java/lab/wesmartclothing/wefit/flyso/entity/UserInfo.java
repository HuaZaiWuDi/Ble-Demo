package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;

/**
 * Created by jk on 2018/7/13.
 */
public class UserInfo implements Serializable {


    /**
     * birthday : 2018-07-13T09:28:00.844Z
     * clothesMacAddr : string
     * height : 0
     * phone : string
     * scalesMacAddr : string
     * sex : 0
     * signature : string
     * targetWeight : 0
     * userImg : string
     * userName : string
     */

    private long birthday = Long.parseLong("631123200000");
    private String clothesMacAddr;
    private int height = 175;
    private String phone;
    private String scalesMacAddr;
    private int sex = 1;
    private String signature;
    private int targetWeight;
    private String userImg;
    private String userName;
    private String city;
    private String country;
    private String province;


    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getClothesMacAddr() {
        return clothesMacAddr;
    }

    public void setClothesMacAddr(String clothesMacAddr) {
        this.clothesMacAddr = clothesMacAddr;
    }

    public int getHeight() {
        if (this.height == 0)
            this.height = 175;
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getScalesMacAddr() {
        return scalesMacAddr;
    }

    public void setScalesMacAddr(String scalesMacAddr) {
        this.scalesMacAddr = scalesMacAddr;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "birthday=" + birthday +
                ", clothesMacAddr='" + clothesMacAddr + '\'' +
                ", height=" + height +
                ", phone='" + phone + '\'' +
                ", scalesMacAddr='" + scalesMacAddr + '\'' +
                ", sex=" + sex +
                ", signature='" + signature + '\'' +
                ", targetWeight=" + targetWeight +
                ", userImg='" + userImg + '\'' +
                ", userName='" + userName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
