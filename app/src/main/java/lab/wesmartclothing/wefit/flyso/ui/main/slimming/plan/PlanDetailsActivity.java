package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
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
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;

public class PlanDetailsActivity extends BaseActivity {


    public static final int STATE_UNCOMMITTED = 0;//未参加
    public static final int STATE_UNALLOCATED = 1;//待分配
    public static final int STATE_UNEXAMINE = 2;//待审核
    public static final int STATE_EXAMINE = 3;//已审核

    private boolean canBack = false;//是否能返回

    @BindView(R.id.tv_plan)
    TextView mTvPlan;
    @BindView(R.id.img_submit)
    ImageView mImgSubmit;
    @BindView(R.id.line_submit)
    View mLineSubmit;
    @BindView(R.id.line_examine_left)
    View mLineExamineLeft;
    @BindView(R.id.img_examine)
    ImageView mImgExamine;
    @BindView(R.id.line_examine_right)
    View mLineExamineRight;
    @BindView(R.id.line_plan)
    View mLinePlan;
    @BindView(R.id.img_plan)
    ImageView mImgPlan;
    @BindView(R.id.tv_ok)
    RxTextView mTvOk;
    @BindView(R.id.topBar)
    QMUITopBar mTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();


        Bundle bundle = getIntent().getExtras();
        canBack = bundle != null;
        //状态为待审核
        if (bundle != null && bundle.getInt(Key.BUNDLE_PLAN_STATUS) == STATE_UNEXAMINE) {
            mImgExamine.setImageResource(R.mipmap.icon_step_cur);
            mLineExamineLeft.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
            mLineExamineRight.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
        }
        initTopBar();
    }

    private void initTopBar() {
        if (canBack) {
            mTopBar.addLeftBackImageButton()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
            mTvOk.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_ok)
    public void onViewClicked() {
        RxActivityUtils.skipActivity(mContext, MainActivity.class);
    }


}
