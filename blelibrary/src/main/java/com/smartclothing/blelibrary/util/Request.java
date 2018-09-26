package com.smartclothing.blelibrary.util;

/**
 * Created by jk on 2018/9/6.
 */
public class Request {


    private byte[] mBytes;

    public Type type;
    private boolean enable;

    public enum Type {
        WRITE_NO,
        WRITE,
        READ,
        ENABLE_NOTIFICATIONS,
        ENABLE_INDICATIONS
    }

    public Request(byte[] bytes) {
        mBytes = bytes;
        this.type = Type.WRITE;
    }

    public Request(Type type) {
        this.type = type;
    }

    public Request(byte[] bytes, Type type) {
        mBytes = bytes;
        this.type = type;
    }

    public Request() {
        this.type = Type.ENABLE_NOTIFICATIONS;
    }

    public byte[] getBytes() {
        return mBytes;
    }

    public void setBytes(byte[] bytes) {
        mBytes = bytes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
