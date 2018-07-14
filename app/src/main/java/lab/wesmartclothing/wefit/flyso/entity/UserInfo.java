package lab.wesmartclothing.wefit.flyso.entity;

/**
 * Created by jk on 2018/7/13.
 */
public class UserInfo {


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

    private String birthday;
    private String clothesMacAddr;
    private int height;
    private String phone;
    private String scalesMacAddr;
    private int sex;
    private String signature;
    private int targetWeight;
    private String userImg;
    private String userName;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getClothesMacAddr() {
        return clothesMacAddr;
    }

    public void setClothesMacAddr(String clothesMacAddr) {
        this.clothesMacAddr = clothesMacAddr;
    }

    public int getHeight() {
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
}
