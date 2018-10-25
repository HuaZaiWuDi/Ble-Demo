package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;

public class PlanDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_plan)
    TextView mTvPlan;
    @BindView(R.id.img_submit)
    ImageView mImgSubmit;
    @BindView(R.id.line_submit)
    View mLineSubmit;
    @BindView(R.id.tv_date_submit)
    TextView mTvDateSubmit;
    @BindView(R.id.line_examine_left)
    View mLineExamineLeft;
    @BindView(R.id.img_examine)
    ImageView mImgExamine;
    @BindView(R.id.line_examine_right)
    View mLineExamineRight;
    @BindView(R.id.tv_date_examine)
    TextView mTvDateExamine;
    @BindView(R.id.line_plan)
    View mLinePlan;
    @BindView(R.id.img_plan)
    ImageView mImgPlan;
    @BindView(R.id.tv_date_plan)
    TextView mTvDatePlan;
    @BindView(R.id.tv_ok)
    RxTextView mTvOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();
    }

    @OnClick(R.id.tv_ok)
    public void onViewClicked() {
    }
}
