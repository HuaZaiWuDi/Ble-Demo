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
import com.vondear.rxtools.utils.RxEncryptUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.ExplainException;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxHttpsException;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.LoginSuccessUtils;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class ForgetPasswordActivity extends BaseActivity {
    private String pwFirst;
    private String pwSecond;

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.edit_phone)
    EditText mEditFirst;
    @BindView(R.id.edit_Vcode)
    EditText mEditSecond;
    @BindView(R.id.btn_register)
    QMUIRoundButton mBtnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
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
        mEditFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwFirst = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtn();
            }
        });
        mEditSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwSecond = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtn();
            }
        });
    }

    private void checkBtn() {
        if (!RxDataUtils.isNullString(pwFirst) && !RxDataUtils.isNullString(pwSecond)) {
            ((QMUIRoundButtonDrawable) mBtnComplete.getBackground()).setColor(getResources().getColor(R.color.red));
            mBtnComplete.setEnabled(true);
        } else {
            ((QMUIRoundButtonDrawable) mBtnComplete.getBackground()).setColor(getResources().getColor(R.color.BrightGray));
            mBtnComplete.setEnabled(false);
        }

        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restPassWord();
            }
        });
    }


    private void restPassWord() {
        pwFirst = mEditFirst.getText().toString();
        pwSecond = mEditSecond.getText().toString();
        if (!RxRegUtils.isPassword(pwFirst) || !RxRegUtils.isPassword(pwSecond)) {
            RxToast.warning(getString(R.string.passwordError));
            return;
        }
        if (!pwFirst.equals(pwSecond)) {
            RxToast.warning(getString(R.string.password_not_same));
            return;
        }
        String phone = getIntent().getStringExtra(SPKey.SP_phone);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().resetPassword(phone,
                RxEncryptUtils.encryptMD5ToString(pwFirst), getIntent().getStringExtra("VCODE")))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("修改密码成功：" + s);
                        RxToast.success(getString(R.string.settingPasswordSuccess));
                        new LoginSuccessUtils(mContext, s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof ExplainException) {
                            RxToast.normal(((ExplainException) e).getMsg());
                        } else {
                            RxToast.normal(new RxHttpsException().handleResponseError(e));
                        }
                    }
                });
    }

}
