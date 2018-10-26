package lab.wesmartclothing.wefit.flyso.ui.main.record;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.chart.bar.BarGroupChart;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.VerticalProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.view.CountDownView;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.record
 * @FileName SlimmingFragment
 * @Date 2018/10/22 16:50
 * @Author JACK
 * @Describe TODO记录界面
 * @Project Android_WeFit_2.0
 */
public class SlimmingFragment extends BaseAcFragment {


    @BindView(R.id.iv_userImg)
    RxImageView mIvUserImg;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.iv_notify)
    ImageView mIvNotify;
    @BindView(R.id.layout_notify)
    LinearLayout mLayoutNotify;
    @BindView(R.id.img_seeRecord)
    RxTextView mImgSeeRecord;
    @BindView(R.id.img_planMark)
    RxImageView mImgPlanMark;
    @BindView(R.id.mCountDownView)
    CountDownView mMCountDownView;
    @BindView(R.id.resetTarget)
    RxTextView mResetTarget;
    @BindView(R.id.tv_weight_title)
    TextView mTvWeightTitle;
    @BindView(R.id.tv_currentWeight)
    TextView mTvCurrentWeight;
    @BindView(R.id.tv_initWeight)
    TextView mTvInitWeight;
    @BindView(R.id.pro_target)
    RxRoundProgressBar mProTarget;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.layout_slimmingTerget)
    LinearLayout mLayoutSlimmingTerget;
    @BindView(R.id.img_recipes)
    RxImageView mImgRecipes;
    @BindView(R.id.tv_IngestionHeat)
    TextView mTvIngestionHeat;
    @BindView(R.id.circleProgressBar)
    QMUIProgressBar mCircleProgressBar;
    @BindView(R.id.line_vertical)
    View mLineVertical;
    @BindView(R.id.breakfastProgress)
    VerticalProgress mBreakfastProgress;
    @BindView(R.id.lunchProgress)
    VerticalProgress mLunchProgress;
    @BindView(R.id.dinnerProgress)
    VerticalProgress mDinnerProgress;
    @BindView(R.id.mealProgress)
    VerticalProgress mMealProgress;
    @BindView(R.id.layout_breakfast)
    CardView mLayoutBreakfast;
    @BindView(R.id.layout_lunch)
    CardView mLayoutLunch;
    @BindView(R.id.layout_dinner)
    CardView mLayoutDinner;
    @BindView(R.id.layout_meal)
    CardView mLayoutMeal;
    @BindView(R.id.layout_heat)
    LinearLayout mLayoutHeat;
    @BindView(R.id.layout_sportTitle)
    LinearLayout mLayoutSportTitle;
    @BindView(R.id.tv_chartTitle)
    TextView mTvChartTitle;
    @BindView(R.id.lineChart)
    LineChart mLineChart;
    @BindView(R.id.tv_sportingTime)
    TextView mTvSportingTime;
    @BindView(R.id.tv_sportingKcal)
    TextView mTvSportingKcal;
    @BindView(R.id.tv_freeSporting)
    RxTextView mTvFreeSporting;
    @BindView(R.id.tv_curriculumSporting)
    RxTextView mTvCurriculumSporting;
    @BindView(R.id.layout_sporting)
    RelativeLayout mLayoutSporting;
    @BindView(R.id.layout_weightTitle)
    LinearLayout mLayoutWeightTitle;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.tv_weight_status)
    RxTextView mTvWeightStatus;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.tv_bmi)
    TextView mTvBmi;
    @BindView(R.id.tv_bmr)
    TextView mTvBmr;
    @BindView(R.id.reWeigh)
    RxTextView mReWeigh;
    @BindView(R.id.layout_weight)
    LinearLayout mLayoutWeight;
    @BindView(R.id.layout_energyTitle)
    LinearLayout mLayoutEnergyTitle;
    @BindView(R.id.pro_diet_plan)
    BarGroupChart mProDietPlan;
    @BindView(R.id.pro_diet_real)
    BarGroupChart mProDietReal;
    @BindView(R.id.pro_sport_plan)
    BarGroupChart mProSportPlan;
    @BindView(R.id.pro_sport_real)
    BarGroupChart mProSportReal;
    @BindView(R.id.pro_heat_plan)
    BarGroupChart mProHeatPlan;
    @BindView(R.id.pro_heat_real)
    BarGroupChart mProHeatReal;
    @BindView(R.id.barChart)
    LinearLayout mBarChart;
    @BindView(R.id.line_chart)
    View mLineView;
    @BindView(R.id.layout_energy)
    LinearLayout mLayoutEnergy;
    Unbinder unbinder;

    public static SlimmingFragment newInstance() {
        Bundle args = new Bundle();
        SlimmingFragment fragment = new SlimmingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_slimming, null);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mTvCurrentWeight.setTypeface(MyAPP.typeface);
        mTvInitWeight.setTypeface(MyAPP.typeface);
        mTvTargetWeight.setTypeface(MyAPP.typeface);
        mTvIngestionHeat.setTypeface(MyAPP.typeface);
        mTvSportingTime.setTypeface(MyAPP.typeface);
        mTvSportingKcal.setTypeface(MyAPP.typeface);
        mTvWeight.setTypeface(MyAPP.typeface);
        mTvBodyFat.setTypeface(MyAPP.typeface);
        mTvBmi.setTypeface(MyAPP.typeface);
        mTvBmr.setTypeface(MyAPP.typeface);


        RxTextUtils.getBuilder("66.6")
                .append("\tkg").setProportion(0.5f).into(mTvCurrentWeight);

        RxTextUtils.getBuilder("起始体重\n")
                .append("60.6").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvInitWeight);

        RxTextUtils.getBuilder("目标体重\n")
                .append("60.6").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvTargetWeight);


        RxTextUtils.getBuilder("多摄入热量\n")
                .append("60.6").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("kcal").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .into(mTvIngestionHeat);


        RxTextUtils.getBuilder("100‘06“")
                .append("本次运动时间")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvSportingTime);

        RxTextUtils.getBuilder("1666")
                .append("预计消耗热量(kcal)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvSportingKcal);

        RxTextUtils.getBuilder("56.6")
                .append("体重(kg)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.5f).into(mTvWeight);


        RxTextUtils.getBuilder("22.80")
                .append(" 体脂率(%)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvBodyFat);
        RxTextUtils.getBuilder("23.60")
                .append(" BMI").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvBmi);
        RxTextUtils.getBuilder("1226")
                .append(" 基础代谢率(kcal)").setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setProportion(0.8f)
                .into(mTvBmr);


        mBreakfastProgress.setProgress(50);
        mLunchProgress.setProgress(70);
        mDinnerProgress.setProgress(80);
        mMealProgress.setProgress(100);
        mMealProgress.setProgressColor(ContextCompat.getColor(mContext, R.color.red));

        //饮食摄入预计
        mProDietPlan.setProgress(0);
        mProDietPlan.setTopValue(1200);
        mProDietPlan.setColor(ContextCompat.getColor(mContext, R.color.BrightGray));

        //饮食摄入实际
        mProDietReal.setProgress(100);
        mProDietReal.setTopValue(800);
        mProDietReal.setColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00));

        //运动预计
        mProSportPlan.setProgress(70);
        mProSportPlan.setTopValue(800);
        mProSportPlan.setColor(ContextCompat.getColor(mContext, R.color.BrightGray));

        //运动实际
        mProSportReal.setProgress(40);
        mProSportReal.setTopValue(800);
        mProSportReal.setColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00));

        //盈余预计
        mProHeatPlan.setProgress(80);
        mProHeatPlan.setTopValue(800);
        mProHeatPlan.setColor(ContextCompat.getColor(mContext, R.color.BrightGray));

        //盈余实际
        mProHeatReal.setProgress(60);
        mProHeatReal.setTopValue(800);
        mProHeatReal.setColor(ContextCompat.getColor(mContext, R.color.yellow_FFBC00));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_userImg, R.id.layout_notify, R.id.img_seeRecord, R.id.img_planMark, R.id.resetTarget, R.id.img_recipes, R.id.layout_breakfast, R.id.layout_lunch, R.id.layout_dinner, R.id.layout_meal, R.id.tv_freeSporting, R.id.tv_curriculumSporting, R.id.reWeigh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_userImg:
                break;
            case R.id.layout_notify:
                break;
            case R.id.img_seeRecord:
                break;
            case R.id.img_planMark:
                break;
            case R.id.resetTarget:
                break;
            case R.id.img_recipes:
                break;
            case R.id.layout_breakfast:
                break;
            case R.id.layout_lunch:
                break;
            case R.id.layout_dinner:
                break;
            case R.id.layout_meal:
                break;
            case R.id.tv_freeSporting:
                break;
            case R.id.tv_curriculumSporting:
                break;
            case R.id.reWeigh:
                break;
        }
    }
}
