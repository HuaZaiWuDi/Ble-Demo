package com.smartclothing.module_wefit.bean;

import java.io.Serializable;

public class Message implements Serializable {


    /**
     * gid : c82e9e7612a447358c11
     * status : 101
     * createUser : c82e9e7612a447358c2a82ef437f3dc2
     * createTime : 1525859580000
     * updateUser : c82e9e7612a447358c2a82ef437f3d11
     * updateTime : 1527307030000
     * userId : c82e9e7612a447358c2a82ef437f3d11
     * msgId : b9bdc848cab44740b467530ae50eb207
     * title : Luis Kane
     * msgType : 2
     * pushTime : 1525795200000
     * readState : 1
     * readTime : 2018
     * content : 提醒内容3333333333333333333
     */

    private String gid;
    private int status;
    private String createUser;
    private long createTime;
    private String updateUser;
    private long updateTime;
    private String userId;
    private String msgId;
    private String title;
    private int msgType;
    private long pushTime;
    private int readState;
    private int readTime;
    private String content;

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

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public int getReadTime() {
        return readTime;
    }

    public void setReadTime(int readTime) {
        this.readTime = readTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
