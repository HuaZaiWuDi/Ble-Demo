package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/7/27.
 */
public class TargetDetailsFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_DistanceTarget)
    TextView mTvDistanceTarget;
    @BindView(R.id.iv_target)
    ImageView mIvTarget;
    @BindView(R.id.tv_targetTitle)
    TextView mTvTargetTitle;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.tv_targetDays)
    TextView mTvTargetDays;
    @BindView(R.id.btn_reSet)
    QMUIRoundButton mBtnReSet;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new TargetDetailsFragment();
    }

    private Bundle bundle;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_target_details, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        initData();
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvTargetDays.setTypeface(typeface);
        mTvTargetWeight.setTypeface(typeface);
        mTvDistanceTarget.setTypeface(typeface);
    }

    private void initData() {
        bundle = getArguments();
        if (bundle != null) {
            int hasDays = bundle.getInt(Key.BUNDLE_HAS_DAYS);
            double aDouble = bundle.getDouble(Key.BUNDLE_TARGET_WEIGHT);
            double still = bundle.getDouble(Key.BUNDLE_STILL_NEED);
            mTvTargetDays.setText(hasDays + "");
            mTvTargetWeight.setText((float) aDouble + "");
            mTvDistanceTarget.setText((float) still + "");
        }
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("目标设置");
    }

    @OnClick(R.id.btn_reSet)
    public void onViewClicked() {
        //传递初始体重信息
        QMUIFragment fragment = SettingTargetFragment.getInstance();
        fragment.setArguments(bundle);
        startFragmentAndDestroyCurrent(fragment);
    }
}
