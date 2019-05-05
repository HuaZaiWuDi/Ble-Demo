package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;

public class PlanActivity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_planTip)
    TextView mTvPlanTip;
    @BindView(R.id.layout_empty)
    LinearLayout mLayoutEmpty;

    @Override
    protected int layoutId() {
        return R.layout.activity_plan;
    }

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.Gray);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mTopBar.setTitle("定制课程");
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        UserInfo info = MyAPP.gUserInfo;
        mTvPlanTip.setText(getString(R.string.empty_plan, info.getUserName()));
    }

}
