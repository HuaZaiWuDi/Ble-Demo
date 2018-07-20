package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.androidannotations.annotations.EFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/7/18.
 */
@EFragment
public class SmartClothingFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.layout_StrongTip)
    RelativeLayout mLayoutStrongTip;
    @BindView(R.id.iv_sports)
    ImageView mIvSports;
    @BindView(R.id.layout_sports)
    RelativeLayout mLayoutSports;
    @BindView(R.id.iv_heatKacl)
    ImageView mIvHeatKacl;
    @BindView(R.id.tv_heatTitlle)
    TextView mTvHeatTitlle;
    @BindView(R.id.tv_Heat_Kcal)
    TextView mTvHeatKcal;
    @BindView(R.id.iv_sports_time)
    ImageView mIvSportsTime;
    @BindView(R.id.tv_sports_time_title)
    TextView mTvSportsTimeTitle;
    @BindView(R.id.tv_Sports_Time)
    TextView mTvSportsTime;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.line)
    View mLine;
    Unbinder unbinder;
    @BindView(R.id.btn_strongTip)
    QMUIRoundButton mBtnStrongTip;

    private Button btn_Connect;

    public static QMUIFragment getInstance() {
        return new SmartClothingFragment_();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_smart_clothing, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();

    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("运动记录");
        btn_Connect = mQMUIAppBarLayout.addRightTextButton("未连接", R.id.tv_connect);
        btn_Connect.setTextColor(Color.WHITE);
        btn_Connect.setTextSize(13);
    }


    @OnClick({R.id.layout_sports, R.id.btn_strongTip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_sports:
                Bundle args = new Bundle();
                args.putLong(Key.BUNDLE_SPORTING_DATE, System.currentTimeMillis());
                QMUIFragment fragment = SportsDetailsFragment.getInstance();
                fragment.setArguments(args);
                startFragment(fragment);
                break;
            case R.id.btn_strongTip:
//                mLayoutStrongTip.setVisibility(View.GONE);
                startFragment(SportingFragment.getInstance());
                break;
        }
    }

}
