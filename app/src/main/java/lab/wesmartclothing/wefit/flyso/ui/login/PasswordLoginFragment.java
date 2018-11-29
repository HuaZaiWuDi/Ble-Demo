package lab.wesmartclothing.wefit.flyso.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseFragment;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.rxbus.PasswordLoginBus;
import lab.wesmartclothing.wefit.flyso.view.PasswordView;

/**
 * Created icon_hide_password jk on 2018/7/2.
 */
public class PasswordLoginFragment extends BaseFragment {
    private String phone;
    private String password;

    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.mPasswordView)
    PasswordView mPasswordView;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.tv_forgetPassword)
    TextView mTvForgetPassword;
    Unbinder unbinder;


    public static Fragment getInstance() {
        return new PasswordLoginFragment();
    }


    @Override
    public void initData() {
        RxBus.getInstance().post(new PasswordLoginBus(phone, password));
    }

    @Override
    public void initView() {
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityUtils.skipActivity(mActivity, RegisterActivity.class);
            }
        });
        mTvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityUtils.skipActivity(mActivity, VerificationPhoneActivity.class);
            }
        });
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = s.toString();
                RxBus.getInstance().post(new PasswordLoginBus(phone, password));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordView.getPassword().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
                RxBus.getInstance().post(new PasswordLoginBus(phone, password));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login_password, null);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
