package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;

public class InvitationCodeActivity extends BaseActivity {

    public static final String REGEX_CODE = "^[A-Za-z0-9]{5}$";

    @BindView(R.id.tv_invitation)
    TextView mTvInvitation;
    @BindView(R.id.edit_invitation)
    EditText mEditInvitation;
    @BindView(R.id.tv_start)
    RxTextView mTvStart;


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
        RxToast.normal("验证邀请码");
    }
}
