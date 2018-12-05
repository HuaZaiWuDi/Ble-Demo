package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.UserInfoActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class InvitationCodeActivity extends BaseActivity {

    public static final String REGEX_CODE = "^[A-Za-z0-9]{5}$";

    @BindView(R.id.tv_invitation)
    TextView mTvInvitation;
    @BindView(R.id.edit_invitation)
    EditText mEditInvitation;
    @BindView(R.id.tv_start)
    RxTextView mTvStart;


    private UserInfo mUserInfo = MyAPP.getGson().fromJson(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_invitation_code;
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }

    @Override
    protected void initViews() {
        super.initViews();
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
        mEditInvitation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RxLogUtils.e("邀请码：" + s);
                if (RxRegUtils.isMatch(REGEX_CODE, s.toString())) {
                    mTvStart.getHelper().setBackgroundColorNormal(ContextCompat.getColor(mContext, R.color.red));
                    mTvStart.setEnabled(true);
                } else {
                    mTvStart.getHelper().setBackgroundColorNormal(ContextCompat.getColor(mContext, R.color.BrightGray));
                    mTvStart.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
    }


    @OnClick(R.id.tv_start)
    public void onViewClicked() {
        String invitation = mEditInvitation.getText().toString();
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .verifyInvitationCode(invitation))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if (Boolean.parseBoolean(s)) {
                            if (mUserInfo != null && mUserInfo.getSex() == 0) {
                                RxActivityUtils.skipActivityAndFinish(mActivity, UserInfoActivity.class);
                            } else {
                                RxActivityUtils.skipActivityAndFinish(mActivity, MainActivity.class);
                            }
                        } else {
                            RxToast.normal("验证码不合法");
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                });
    }
}
