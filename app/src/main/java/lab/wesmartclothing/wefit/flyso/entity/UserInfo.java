package lab.wesmartclothing.wefit.flyso.entity;

import com.vondear.rxtools.utils.dateUtils.RxTimeUtils;
import com.vondear.rxtools.utils.RxConstUtils;

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
    private int sex = 2;//1男2女0未知（未录入）
    private String signature;
    private String imgUrl;
    private String userName;
    private String city;
    private String country;
    private String province;
    private boolean isChange = false;
    private long registerTime;
    private int planState;
    private int age;
    private boolean hasInviteCode;
    private String invitationCode;

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public boolean isHasInviteCode() {
        return hasInviteCode;
    }

    public void setHasInviteCode(boolean hasInviteCode) {
        this.hasInviteCode = hasInviteCode;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }

    public long getRegisterTime() {
        return RxTimeUtils.getIntervalTime(System.currentTimeMillis(), registerTime, RxConstUtils.TimeUnit.DAY) + 1;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public void setCity(String city) {
        this.city = city;
        isChange = true;
    }

    public void setCountry(String country) {
        this.country = country;
        isChange = true;
    }

    public void setProvince(String province) {
        this.province = province;
        isChange = true;
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
        isChange = true;
    }

    public String getClothesMacAddr() {
        return clothesMacAddr;
    }

    public void setClothesMacAddr(String clothesMacAddr) {
        this.clothesMacAddr = clothesMacAddr;
    }

    public int getHeight() {
        if (this.height == 0)
            this.height = 165;
        return height;
    }

    public void setHeight(int height) {
        isChange = true;
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
        isChange = true;
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        isChange = true;
        this.signature = signature;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        isChange = true;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        isChange = true;
        this.userName = userName;
    }


    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
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
                ", userImg='" + imgUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
