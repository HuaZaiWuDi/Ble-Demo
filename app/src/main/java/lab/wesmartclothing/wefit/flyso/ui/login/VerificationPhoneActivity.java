package lab.wesmartclothing.wefit.flyso.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

public class VerificationPhoneActivity extends BaseActivity {
    private String phone;
    private String vCode;

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_Vcode)
    EditText mEditVcode;
    @BindView(R.id.btn_getVCode)
    QMUIRoundButton mBtnGetVCode;
    @BindView(R.id.btn_register)
    QMUIRoundButton mBtnNextWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);
        ButterKnife.bind(this);
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.white))
                .setLightStatusBar(false)
                .process();
        initView();
    }

    public void initView() {
        mQMUIAppBarLayout.addLeftImageButton(R.mipmap.icon_back, R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityUtils.finishActivity();
            }
        });

        switchEnable(false);

        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switchEnable(!RxDataUtils.isNullString(s.toString()));
                phone = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtn();
            }
        });
        mEditVcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtn();
            }
        });
        mBtnGetVCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVCode();
            }
        });
        //下一步
        mBtnNextWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRestPassword();
            }
        });

    }

    private void switchEnable(boolean isEnable) {
        if (!mBtnGetVCode.getText().toString().equals(getString(R.string.getVCode))) return;
        mBtnGetVCode.setEnabled(isEnable);
        mBtnGetVCode.setAlpha(isEnable ? 1f : 0.5f);
    }

    private void getVCode() {
        String phone = mEditPhone.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.sendCode(phone, "resetPassword"))
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

    private void toRestPassword() {
        final String phone = mEditPhone.getText().toString();
        final String vCode = mEditVcode.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(vCode)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.toResetPassword(phone, vCode))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("验证码：" + s);
                        RxToast.success(getString(R.string.SMSSended));
                        Bundle bundle = new Bundle();
                        bundle.putString(SPKey.SP_phone, phone);
                        bundle.putString("VCODE", vCode);
                        RxActivityUtils.skipActivityAndFinish(mContext, ForgetPasswordActivity.class, bundle);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void checkBtn() {
        if (!RxDataUtils.isNullString(phone) && !RxDataUtils.isNullString(vCode)) {
            ((QMUIRoundButtonDrawable) mBtnNextWay.getBackground()).setColor(getResources().getColor(R.color.red));
            mBtnNextWay.setEnabled(true);
        } else {
            ((QMUIRoundButtonDrawable) mBtnNextWay.getBackground()).setColor(getResources().getColor(R.color.BrightGray));
            mBtnNextWay.setEnabled(false);
        }
    }

}
