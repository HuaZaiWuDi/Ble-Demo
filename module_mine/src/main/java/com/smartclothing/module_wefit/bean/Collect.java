package com.smartclothing.module_wefit.bean;

import java.io.Serializable;

public class Collect implements Serializable {

    /**
     * gid : 3b51dee02a0e45749089721d57248732
     * status : 101
     * createUser : c82e9e7612a447358c2a82ef437f3d11
     * createTime : 1527236940000
     * updateUser : c82e9e7612a447358c2a82ef437f3d11
     * updateTime : 1527236940000
     * userId : c82e9e7612a447358c2a82ef437f3d11
     * articleId : 82bd011f0c9748d8894d6493562d6d541521682929808
     * articleName : 武松打虎啊啊啊
     * summary : 123
     * infoType : 1
     * sort : 0
     */

    private String gid;
    private int status;
    private String createUser;
    private long createTime;
    private String updateUser;
    private long updateTime;
    private String userId;
    private String articleId;
    private String articleName;
    private String summary;
    private String infoType;
    private int sort;
    private String coverPicture;


    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
