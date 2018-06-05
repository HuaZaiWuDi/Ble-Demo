package com.smartclothing.module_wefit.bean;

import java.io.Serializable;

/**
 * Created by ZZP on 2018/5/20.
 */

public class UserInfo implements Serializable {
    /*{"userImg":null,"userName":"哈哈哈哈","sex":1,"birthday":631123200000,"height":170,"targetWeight":50,"phone":"133*****8789"}*/
    private String birthday;
    private int height;
    private String phone;
    private int sex;
    private int targetWeight;
    private String userImg;
    private String userName;
    private String signature;

    public boolean isChange = false;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
        isChange = true;
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        isChange = true;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        isChange = true;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        isChange = true;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
        isChange = true;
    }

    public int getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight) {
        isChange = true;
        this.targetWeight = targetWeight;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        isChange = true;
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        isChange = true;
        this.userName = userName;
    }


}
