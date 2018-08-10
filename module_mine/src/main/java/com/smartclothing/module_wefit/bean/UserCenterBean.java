package com.smartclothing.module_wefit.bean;

import java.io.Serializable;

/**
 * Created by ZZP on 2018/5/20.
 */

public class UserCenterBean implements Serializable{


    /**
     * athlDays : 0
     * bindCount : 0
     * calorie : 0
     * collectCount : 0
     * duration : 0
     * imgUrl : string
     * maxHeart : 0
     * signature : string
     * unreadCount : 0
     * userName : string
     */

    private int athlDays;
    private int bindCount;
    private int calorie;
    private int collectCount;
    private int duration;
    private String imgUrl;
    private int maxHeart;
    private String signature;
    private int unreadCount;
    private String userName;

    public int getAthlDays() {
        return athlDays;
    }

    public void setAthlDays(int athlDays) {
        this.athlDays = athlDays;
    }

    public int getBindCount() {
        return bindCount;
    }

    public void setBindCount(int bindCount) {
        this.bindCount = bindCount;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getMaxHeart() {
        return maxHeart;
    }

    public void setMaxHeart(int maxHeart) {
        this.maxHeart = maxHeart;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
