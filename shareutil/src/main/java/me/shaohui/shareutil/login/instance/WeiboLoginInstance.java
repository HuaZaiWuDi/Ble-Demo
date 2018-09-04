package me.shaohui.shareutil.login.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.shaohui.shareutil.RxThreadUtils;
import me.shaohui.shareutil.ShareLogger;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.WeiboToken;
import me.shaohui.shareutil.login.result.WeiboUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/12/1.
 */

public class WeiboLoginInstance extends LoginInstance {

    private static final String USER_INFO = "https://api.weibo.com/2/users/show.json";

    private SsoHandler mSsoHandler;

    private LoginListener mLoginListener;

    public WeiboLoginInstance(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        mSsoHandler = new SsoHandler(activity);
        mLoginListener = listener;
    }

    @Override
    public void doLogin(Activity activity, final LoginListener listener,
                        final boolean fetchUserInfo) {
        mSsoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                WeiboToken weiboToken = WeiboToken.parse(oauth2AccessToken);
                if (fetchUserInfo) {
                    if (listener != null) {
                        listener.beforeFetchUserInfo(weiboToken);
                        fetchUserInfo(weiboToken);
                    }
                } else {
                    if (listener != null)
                        listener.loginSuccess(new LoginResult(LoginPlatform.WEIBO, weiboToken));
                }
            }

            @Override
            public void cancel() {
                ShareLogger.i(INFO.AUTH_CANCEL);
                if (listener != null)
                    listener.loginCancel();
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                ShareLogger.i(INFO.WEIBO_AUTH_ERROR);
                if (listener != null)
                    listener.loginFailure(new Exception(wbConnectErrorMessage.getErrorMessage()));
            }
        });
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {
        Observable.create(new ObservableOnSubscribe<WeiboUser>() {
            @Override
            public void subscribe(ObservableEmitter<WeiboUser> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request =
                        new Request.Builder().url(buildUserInfoUrl(token, USER_INFO)).build();
                try {
                    Response response = client.newCall(request).execute();
                    //响应的Body只能被使用一次，之后就会被销毁
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    WeiboUser user = WeiboUser.parse(jsonObject);
                    ShareLogger.e("WeiboUser：" + user.toString());
                    emitter.onNext(user);
                } catch (IOException | JSONException e) {
                    ShareLogger.e(INFO.FETCH_USER_INOF_ERROR);
                    emitter.onError(e);
                }
            }
        }).compose(RxThreadUtils.<WeiboUser>rxThreadHelper())
                .subscribe(new Observer<WeiboUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeiboUser weiboUser) {
                        if (mLoginListener != null)
                            mLoginListener.loginSuccess(
                                    new LoginResult(LoginPlatform.WEIBO, token, weiboUser));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ShareLogger.e("onError：" + e.toString());
                        if (mLoginListener != null)
                            mLoginListener.loginFailure(new Exception(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private String buildUserInfoUrl(BaseToken token, String baseUrl) {
        return baseUrl + "?access_token=" + token.getAccessToken() + "&uid=" + token.getOpenid();
    }

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null)
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    @Override
    public boolean isInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.sina.weibo")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {
        mSsoHandler = null;
//        mLoginListener = null;
    }
}
