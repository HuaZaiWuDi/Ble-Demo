package me.shaohui.shareutil.login.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.login.result.BaseToken;
import me.shaohui.shareutil.login.result.QQToken;
import me.shaohui.shareutil.login.result.QQUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/12/1.
 */

public class QQLoginInstance extends LoginInstance implements IUiListener {

    private static final String SCOPE = "get_simple_userinfo";

    private static final String URL = "https://graph.qq.com/user/get_user_info";

    private Tencent mTencent;


    private LoginListener mLoginListener;
    private boolean fetchUserInfo;

    public QQLoginInstance(Activity activity, final LoginListener listener,
                           final boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        mTencent = Tencent.createInstance(ShareManager.CONFIG.getQqId(),
                activity.getApplicationContext());
        Log.d("QQ登录：", mTencent.toString());
        this.fetchUserInfo = fetchUserInfo;
        mLoginListener = listener;

    }

    @Override
    public void doLogin(Activity activity, final LoginListener listener, boolean fetchUserInfo) {

        mTencent.login(activity, SCOPE, this);
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {

        Observable.create(new ObservableOnSubscribe<QQUser>() {
            @Override
            public void subscribe(ObservableEmitter<QQUser> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(buildUserInfoUrl(token, URL)).build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    QQUser user = QQUser.parse(token.getOpenid(), jsonObject);
                    emitter.onNext(user);
                } catch (IOException | JSONException e) {
                    ShareLogger.e(INFO.FETCH_USER_INOF_ERROR);
                    emitter.onError(e);
                }
            }
        }).compose(RxThreadUtils.<QQUser>rxThreadHelper())
                .subscribe(new Observer<QQUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QQUser qqUser) {
                        mLoginListener.loginSuccess(
                                new LoginResult(LoginPlatform.QQ, token, qqUser));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginListener.loginFailure(new Exception(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String buildUserInfoUrl(BaseToken token, String base) {
        return base
                + "?access_token="
                + token.getAccessToken()
                + "&oauth_consumer_key="
                + ShareManager.CONFIG.getQqId()
                + "&openid="
                + token.getOpenid();
    }

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        Tencent.handleResultData(data, this);
    }

    @Override
    public boolean isInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {
//        mTencent.releaseResource();
        mLoginListener = null;
        mTencent = null;
    }

    @Override
    public void onComplete(Object o) {
        ShareLogger.i(INFO.QQ_AUTH_SUCCESS + ((JSONObject) o).toString());
        try {
            QQToken token = QQToken.parse((JSONObject) o);
            if (fetchUserInfo) {
                mLoginListener.beforeFetchUserInfo(token);
                fetchUserInfo(token);
            } else {
                mLoginListener.loginSuccess(new LoginResult(LoginPlatform.QQ, token));
            }

        } catch (JSONException e) {
            ShareLogger.i(INFO.ILLEGAL_TOKEN);
            mLoginListener.loginFailure(e);
        }
    }

    @Override
    public void onError(UiError uiError) {
        ShareLogger.i(INFO.QQ_LOGIN_ERROR);
        mLoginListener.loginFailure(
                new Exception("QQError: " + uiError.errorCode + uiError.errorDetail));
    }

    @Override
    public void onCancel() {
        ShareLogger.i(INFO.AUTH_CANCEL);
        mLoginListener.loginCancel();
    }
}
