package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

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
    }

    private void initData() {
        mTvDistanceTarget.setText("3.0");
        mTvTargetWeight.setText("53.0");
        mTvTargetDays.setText("37");
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
        startFragment(SettingTargetFragment.getInstance());
    }
}
