package com.smartclothing.module_wefit.bean;

import java.io.Serializable;

/**
 * Created by ZZP on 2018/5/20.
 */

public class UserCenterBean implements Serializable{

    /**
     * imgUrl : https://wesmart-image.oss-cn-shenzhen.aliyuncs.com/upload/user/2018/4/25/data/user/0/com.smartclothing.module_wefit/cache/cropped_1527244188024.jpg
     * userName : 哈哈哈哈
     * signature : 这可能是一段很长的签名这可能是一段很长的签名这可能是一段很长的签名hjnx
     * bindCount : 1
     * collectCount : 6
     * unreadCount : 1
     */

    private String imgUrl;
    private String userName;
    private String signature;
    private int bindCount;
    private int collectCount;
    private int unreadCount;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getBindCount() {
        return bindCount;
    }

    public void setBindCount(int bindCount) {
        this.bindCount = bindCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
