package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;

/**
 * Created by jk on 2018/7/26.
 */
public class SettingTargetFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.ruler_weight)
    DecimalScaleRulerView mWeightRulerView;
    @BindView(R.id.btn_nextStep)
    QMUIRoundButton mBtnNextStep;
    Unbinder unbinder;


    public static QMUIFragment getInstance() {
        return new SettingTargetFragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_setting_target, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        initRuler();

        String tips = "需减重 " + RxFormatValue.fromat4S5R(3, 1) + " kg";
        SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .setLength(4, tips.length() - 3);

        mTvTips.setText(builder);
    }

    private void initRuler() {
        mTvTargetWeight.setText(53 + "");
        mWeightRulerView.setTextLabel("kg");
        mWeightRulerView.setColor(getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.orange_FF7200));
        mWeightRulerView.setParam(DrawUtil.dip2px(10), DrawUtil.dip2px(60), DrawUtil.dip2px(40),
                DrawUtil.dip2px(25), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mWeightRulerView.initViewParam(53.0f, 20.0f, 200.0f, 1);
        mWeightRulerView.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTvTargetWeight.setText(RxFormatValue.fromat4S5R(value, 1));
            }
        });
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


    @OnClick(R.id.btn_nextStep)
    public void onViewClicked() {
        startFragment(TargetDateFargment.getInstance());
    }
}
