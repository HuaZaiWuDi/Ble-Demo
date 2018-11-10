package lab.wesmartclothing.wefit.flyso.ui.toolui;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;

public class Main2Activity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.img_sportingPlan)
    ImageView mImgSportingPlan;
    @BindView(R.id.container)
    LinearLayout mContainer;

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main2;
    }


    @Override
    protected void initViews() {
        super.initViews();
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        mTopBar.setTitle("Timetofit 健康报告");

        MyAPP.getImageLoader().displayImage(mActivity, R.drawable.pc_plan3, mImgSportingPlan);
    }

}
