package me.shaohui.shareutil.login.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
import me.shaohui.shareutil.login.result.WxToken;
import me.shaohui.shareutil.login.result.WxUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/12/1.
 */

public class WxLoginInstance extends LoginInstance {

    public static final String SCOPE_USER_INFO = "snsapi_userinfo";
    private static final String SCOPE_BASE = "snsapi_base";

    private static final String BASE_URL = "https://api.weixin.qq.com/sns/";

    private IWXAPI mIWXAPI;

    private LoginListener mLoginListener;

    private OkHttpClient mClient;

    private boolean fetchUserInfo;

    public WxLoginInstance(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        super(activity, listener, fetchUserInfo);
        mLoginListener = listener;
        mIWXAPI = WXAPIFactory.createWXAPI(activity, ShareManager.CONFIG.getWxId());
        mClient = new OkHttpClient();
        this.fetchUserInfo = fetchUserInfo;
    }

    @Override
    public void doLogin(Activity activity, LoginListener listener, boolean fetchUserInfo) {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = fetchUserInfo ? SCOPE_USER_INFO : SCOPE_BASE;
        req.state = String.valueOf(System.currentTimeMillis());
        mIWXAPI.sendReq(req);
    }

    private void getToken(final String code) {
        Observable.create(new ObservableOnSubscribe<WxToken>() {
            @Override
            public void subscribe(ObservableEmitter<WxToken> emitter) throws Exception {
                Request request = new Request.Builder().url(buildTokenUrl(code)).build();
                try {
                    Response response = mClient.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    WxToken token = WxToken.parse(jsonObject);
                    emitter.onNext(token);
                    Log.d("微信登录(token):", jsonObject.toString());
                } catch (IOException | JSONException e) {
                    ShareLogger.e("token error");
                    emitter.onError(e);
                }
            }
        }).compose(RxThreadUtils.<WxToken>rxThreadHelper())
                .subscribe(new Observer<WxToken>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WxToken wxToken) {
                        if (fetchUserInfo) {
                            mLoginListener.beforeFetchUserInfo(wxToken);
                            fetchUserInfo(wxToken);
                        } else {
                            mLoginListener.loginSuccess(new LoginResult(LoginPlatform.WX, wxToken));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginListener.loginFailure(new Exception(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void fetchUserInfo(final BaseToken token) {
        Observable.create(new ObservableOnSubscribe<WxUser>() {
            @Override
            public void subscribe(ObservableEmitter<WxUser> emitter) throws Exception {
                Request request = new Request.Builder().url(buildUserInfoUrl(token)).build();
                try {
                    Response response = mClient.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    WxUser user = WxUser.parse(jsonObject);
                    emitter.onNext(user);
                    Log.d("微信登录(WxUser):", jsonObject.toString());
                } catch (IOException | JSONException e) {
                    ShareLogger.e(INFO.FETCH_USER_INOF_ERROR);
                    emitter.onError(e);
                }
            }
        }).compose(RxThreadUtils.<WxUser>rxThreadHelper())
                .subscribe(new Observer<WxUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WxUser wxUser) {
                        mLoginListener.loginSuccess(
                                new LoginResult(LoginPlatform.WX, token, wxUser));
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

    @Override
    public void handleResult(int requestCode, int resultCode, Intent data) {
        mIWXAPI.handleIntent(data, new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {
                Log.d("微信登录(onReq):", baseReq.toString());
            }

            @Override
            public void onResp(BaseResp baseResp) {
                if (baseResp instanceof SendAuth.Resp && baseResp.getType() == 1) {
                    SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                    Log.d("微信登录(handleResult):", resp.toString());
                    switch (resp.errCode) {
                        case BaseResp.ErrCode.ERR_OK:
                            getToken(resp.code);
                            break;
                        case BaseResp.ErrCode.ERR_USER_CANCEL:
                            mLoginListener.loginCancel();
                            break;
                        case BaseResp.ErrCode.ERR_SENT_FAILED:
                            mLoginListener.loginFailure(new Exception(INFO.WX_ERR_SENT_FAILED));
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            mLoginListener.loginFailure(new Exception(INFO.WX_ERR_UNSUPPORT));
                            break;
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            mLoginListener.loginFailure(new Exception(INFO.WX_ERR_AUTH_DENIED));
                            break;
                        default:
                            mLoginListener.loginFailure(new Exception(INFO.WX_ERR_AUTH_ERROR));
                    }
                }
            }
        });
    }

    @Override
    public boolean isInstall(Context context) {
        return mIWXAPI.isWXAppInstalled();
    }

    @Override
    public void recycle() {
        if (mIWXAPI != null) {
            mIWXAPI.detach();
        }
    }

    private String buildTokenUrl(String code) {
        return BASE_URL
                + "oauth2/access_token?appid="
                + ShareManager.CONFIG.getWxId()
                + "&secret="
                + ShareManager.CONFIG.getWxSecret()
                + "&code="
                + code
                + "&grant_type=authorization_code";
    }

    private String buildUserInfoUrl(BaseToken token) {
        return BASE_URL
                + "userinfo?access_token="
                + token.getAccessToken()
                + "&openid="
                + token.getOpenid();
    }
}
