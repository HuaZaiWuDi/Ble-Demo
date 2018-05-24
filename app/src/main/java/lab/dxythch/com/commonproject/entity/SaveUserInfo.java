package lab.dxythch.com.commonproject.entity;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jk on 2018/5/22.
 */
@EBean
public class SaveUserInfo implements Serializable {


    /**
     * createTokenTime : 0
     * failCount : 0
     * imgUrl : string
     * lockStatus : 0
     * loginCount : 0
     * loginFlag : 0
     * loginMacId : string
     * loginTime : 0
     * phoneCode : 0
     * qq : string
     * registerTime : 0
     * saltRandom : string
     * signature : string
     * token : string
     * userType : string
     * wechat : string
     * weibo : string
     * email : tom@qq.com
     * phone : 13366668888
     * userName : 13366668888
     * password : string
     * sex : 1
     * birthday : 2018-05-09T10:07:17.962Z
     * height : 170
     * targetWeight : 60
     * country : 中国
     * province : 广东
     * city : 深圳
     * avatar : /img/avatar.jpg
     */

    private int createTokenTime;
    private int failCount;
    private String imgUrl;
    private int lockStatus;
    private int loginCount;
    private int loginFlag;
    private String loginMacId;
    private int loginTime;
    private int phoneCode;
    private String qq;
    private int registerTime;
    private String saltRandom;
    private String signature;
    private String token;
    private String userType;
    private String wechat;
    private String weibo;
    private String email;
    private String phone;
    private String userName;
    private String password;
    private int sex = 1;
    private String birthday = new Date(1980, 0, 1).getTime() + "";
    private int height = 175;
    private int targetWeight = 60;
    private String country;
    private String province;
    private String city;
    private String avatar;

    public int getCreateTokenTime() {
        return createTokenTime;
    }

    public void setCreateTokenTime(int createTokenTime) {
        this.createTokenTime = createTokenTime;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public int getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(int loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getLoginMacId() {
        return loginMacId;
    }

    public void setLoginMacId(String loginMacId) {
        this.loginMacId = loginMacId;
    }

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(int registerTime) {
        this.registerTime = registerTime;
    }

    public String getSaltRandom() {
        return saltRandom;
    }

    public void setSaltRandom(String saltRandom) {
        this.saltRandom = saltRandom;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
