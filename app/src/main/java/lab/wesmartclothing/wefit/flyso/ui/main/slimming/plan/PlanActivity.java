package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.SPUtils;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

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

        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = JSON.parseObject(string, UserInfo.class);
        mTvPlanTip.setText(getString(R.string.empty_plan, info.getUserName()));
    }

}
