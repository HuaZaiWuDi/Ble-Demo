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
 * Created by jk on 2018/7/27.
 */
public class TargetDateFargment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.ruler_weight)
    DecimalScaleRulerView mRulerWeight;
    @BindView(R.id.tv_targetDays)
    TextView mTvTargetDays;
    @BindView(R.id.btn_confirm)
    QMUIRoundButton mBtnConfirm;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new TargetDateFargment();
    }


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_target_date, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        initRuler();

        String tips = "每周减重目标 " + RxFormatValue.fromat4S5R(5, 1) + " kg";
        SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .setLength(7, tips.length() - 3);

        mTvTips.setText(builder);
    }

    private void initRuler() {
        mRulerWeight.setTextLabel("周");
        mRulerWeight.setDecimal(false);
        mRulerWeight.setColor(getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.orange_FF7200));
        mRulerWeight.setParam(DrawUtil.dip2px(50), DrawUtil.dip2px(60), DrawUtil.dip2px(60),
                DrawUtil.dip2px(60), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mRulerWeight.initViewParam(100f, 0f, 100f, 10);
        mRulerWeight.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTvTargetWeight.setText(RxFormatValue.fromat4S5R(value, 0));
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

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        startFragment(TargetDetailsFragment.getInstance());
    }
}
