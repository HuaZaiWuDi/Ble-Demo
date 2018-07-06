package me.shaohui.shareutil.login;

import java.io.Serializable;

import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.BaseUser;

/**
 * Created by shaohui on 2016/12/3.
 */

public class LoginResult implements Serializable {

    private BaseToken mToken;

    private BaseUser mUserInfo;

    private int mPlatform;

    public LoginResult(int platform, BaseToken token) {
        mPlatform = platform;
        mToken = token;
    }

    public LoginResult(int platform, BaseToken token, BaseUser userInfo) {
        mPlatform = platform;
        mToken = token;
        mUserInfo = userInfo;
    }



    public int getPlatform() {
        return mPlatform;
    }

    public void setPlatform(int platform) {
        this.mPlatform = platform;
    }

    public BaseToken getToken() {
        return mToken;
    }

    public void setToken(BaseToken token) {
        mToken = token;
    }

    public BaseUser getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(BaseUser userInfo) {
        mUserInfo = userInfo;
    }


    @Override
    public String toString() {
        return "LoginResult{" +
                "mToken=" + mToken.toString() +
                ", mUserInfo=" + mUserInfo.toString() +
                ", mPlatform=" + mPlatform +
                '}';
    }

}