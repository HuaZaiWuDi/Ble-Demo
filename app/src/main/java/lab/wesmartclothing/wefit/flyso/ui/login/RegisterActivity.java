package lab.wesmartclothing.wefit.flyso.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxEncryptUtils;
import com.vondear.rxtools.utils.RxKeyboardUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.utils.LoginSuccessUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.view.PasswordView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private String phone;
    private String vCode;
    private String password;

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mMQMUIAppBarLayout;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_Vcode)
    EditText mEditVcode;
    @BindView(R.id.btn_getVCode)
    QMUIRoundButton mBtnGetVCode;
    @BindView(R.id.mPasswordView)
    PasswordView mPasswordView;
    @BindView(R.id.btn_register)
    QMUIRoundButton mBtnRegister;
    @BindView(R.id.tv_clause)
    TextView mTvClause;

    @OnClick(R.id.tv_clause)
    void tv_clause() {
        //服务协议
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Term_Service);
        bundle.putString(Key.BUNDLE_TITLE, getString(R.string.ServiceAgreement));
        RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.white))
                .setLightStatusBar(false)
                .process();
        initView();
    }

    public void initView() {


        initTopBar();
        SpannableStringBuilder spannableStringBuilder = RxTextUtils.getBuilder(mTvClause.getText())
                .setForegroundColor(getResources().getColor(R.color.red))
                .setUnderline()
                .setLength(9);
        mTvClause.setText(spannableStringBuilder);

        switchEnable(false);

        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = s.toString();
                switchEnable(!RxDataUtils.isNullString(phone));
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegister();
            }
        });
        mBtnGetVCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVCode();
            }
        });
        mPasswordView.getPassword().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegister();
            }
        });
        mEditVcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegister();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void initTopBar() {
        mMQMUIAppBarLayout.addLeftImageButton(R.mipmap.icon_back, R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册布局变化监听
        RxKeyboardUtils.registerSoftInputChangedListener(mActivity, new RxKeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                RxLogUtils.e("软键盘高度：" + height);
                if (height < 300) {
                    RxAnimationUtils.animateHeight(0, RxUtils.dp2px(100), mTvTitle);
                } else {
                    RxAnimationUtils.animateHeight(RxUtils.dp2px(100), 0, mTvTitle);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        RxKeyboardUtils.unregisterSoftInputChangedListener(mActivity);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getVCode() {
        String phone = mEditPhone.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.sendCode(phone, "register"))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxUtils.countDown(mBtnGetVCode, 60, 1, getString(R.string.getVCode));
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("验证码：" + s);
                        if (BuildConfig.DEBUG)
                            mEditVcode.setText(s);
                        RxToast.success(getString(R.string.SMSSended));
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void register() {
        String phone = mEditPhone.getText().toString();
        String vCode = mEditVcode.getText().toString();
        String password = mPasswordView.getPassword().getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(vCode)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        if (!RxRegUtils.isPassword(password)) {
            RxToast.warning(getString(R.string.passwordError));
            return;
        }

        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.register(phone, vCode, RxEncryptUtils.encryptMD5ToString(password)))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("验证码：" + s);
                        RxToast.success("注册成功");
                        new LoginSuccessUtils(mContext, s);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);
                    }
                });
    }

    private void switchEnable(boolean isEnable) {
        if (!mBtnGetVCode.getText().toString().equals(getString(R.string.getVCode))) return;
        mBtnGetVCode.setEnabled(isEnable);
        mBtnGetVCode.setAlpha(isEnable ? 1f : 0.5f);
    }

    private void checkRegister() {
        if (!RxDataUtils.isNullString(phone) && !RxDataUtils.isNullString(vCode) && !RxDataUtils.isNullString(password)) {
            ((QMUIRoundButtonDrawable) mBtnRegister.getBackground()).setColor(getResources().getColor(R.color.red));
            mBtnRegister.setEnabled(true);
        } else {
            ((QMUIRoundButtonDrawable) mBtnRegister.getBackground()).setColor(getResources().getColor(R.color.BrightGray));
            mBtnRegister.setEnabled(false);
        }
    }

}
