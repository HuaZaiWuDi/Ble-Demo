package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;

public class PlanMatterActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_Title)
    TextView mTvTitle;
    @BindView(R.id.tv_unAgree)
    RxTextView mTvUnAgree;
    @BindView(R.id.tv_Agree)
    RxTextView mTvAgree;
    @BindView(R.id.tv_tip)
    TextView mTvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slimming_plan);
        StatusBarUtils.from(mActivity)
                .setLightStatusBar(true)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.white))
                .process();
        ButterKnife.bind(this);

        initTopBar();

        mTvTip.setText(getString(R.string.plan, getString(R.string.appName)));
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(v -> onBackPressed());

    }

    @OnClick({R.id.tv_unAgree, R.id.tv_Agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_unAgree:
                onBackPressed();
                RecordInfoActivity.mSubmitInfoFrom = null;
                break;
            case R.id.tv_Agree:
                RxActivityUtils.skipActivity(mActivity, RecordInfoActivity.class);
                break;
        }
    }
}
