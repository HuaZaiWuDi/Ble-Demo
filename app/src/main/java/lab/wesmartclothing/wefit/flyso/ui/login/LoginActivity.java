package lab.wesmartclothing.wefit.flyso.ui.login;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @ViewById
    Button btn_login;
    @ViewById
    EditText edit_phone;
    @ViewById
    EditText edit_VCode;
    @ViewById
    TextView tv_country;
    @ViewById
    TextView tv_about;
    @ViewById
    TextView tv_countDown;

    @Pref
    Prefs_ mPrefs;


    private String phone;
    private String VCode;


    @Click
    void btn_login() {
        VCode = edit_VCode.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning("手机号码号码不正确");
            return;
        }
        if (RxDataUtils.isNullString(VCode) || VCode.length() < 4 || !RxDataUtils.isNumber(VCode)) {
            RxToast.warning("验证码不正确");
            return;
        }
        login(phone, VCode);
    }

    @Click
    void tv_countDown() {
        phone = edit_phone.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning("手机号码号码不正确");
            return;
        }
        getVCode(phone);
        RxUtils.countDown(tv_countDown, 60, 1, getString(R.string.getVCode));
    }

    @Override
    @AfterViews
    public void initView() {
        String info_about = getString(R.string.login_clause);
        SpannableStringBuilder builder = RxTextUtils.getBuilder(info_about)
                .setForegroundColor(getResources().getColor(R.color.colorTheme))
                .setUnderline()
                .setLength(7, info_about.length());
        tv_about.setText(builder);
    }

    private void login(String phone, String code) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.login(phone, code))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxLogUtils.d("doOnSubscribe：");
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        RxLogUtils.d("结束：");
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String userId = jsonObject.getString("userId");
                            String token = jsonObject.getString("token");
                            mPrefs.UserId().put(userId);
                            mPrefs.token().put(token);
                            NetManager.getInstance().setUserIdToken(userId, token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RxActivityUtils.skipActivityAndFinish(mContext, UserInfoActivity_.class);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void getVCode(String phone) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.sendCode(phone))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        RxToast.success("短信已发送");
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
