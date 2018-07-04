package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;

/**
 * Created icon_hide_password Jack on 2018/5/22.
 */
public class BindDeviceBean implements Serializable{

    /**
     * 0:体重秤
     * 1：瘦身衣
     */

    int deivceType;
    String deivceName;
    boolean isBind;
    String mac;

    public BindDeviceBean() {
    }

    public BindDeviceBean(int deivceType, String deivceName, boolean isBind, String mac) {
        this.deivceType = deivceType;
        this.deivceName = deivceName;
        this.isBind = isBind;
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public int getDeivceType() {
        return deivceType;
    }

    public void setDeivceType(int deivceType) {
        this.deivceType = deivceType;
    }

    public String getDeivceName() {
        return deivceName;
    }

    public void setDeivceName(String deivceName) {
        this.deivceName = deivceName;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

}
