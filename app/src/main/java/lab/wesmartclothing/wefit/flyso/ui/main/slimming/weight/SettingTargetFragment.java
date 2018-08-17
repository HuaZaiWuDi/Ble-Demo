package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

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
    @BindView(R.id.tv_bestWeight)
    TextView tvBestWeight;
    @BindView(R.id.tv_targetDays)
    TextView tv_targetDays;
    @BindView(R.id.ruler_weight)
    DecimalScaleRulerView mWeightRulerView;
    @BindView(R.id.btn_nextStep)
    QMUIRoundButton mBtnNextStep;
    Unbinder unbinder;


    public static QMUIFragment getInstance() {
        return new SettingTargetFragment();
    }

    private float maxWeight, minWeight, settingWeight, initWeight = 0, stillNeed;
    private Bundle bundle;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_setting_target, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        bestWeight();
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvTargetWeight.setTypeface(typeface);
        mTvTips.setTypeface(typeface);
    }

    /**
     * 男性：(身高cm－80)×70﹪=标准体重
     * 女性：(身高cm－70)×60﹪=标准体重
     */
    private void bestWeight() {
        bundle = getArguments();
        if (bundle != null) {
            initWeight = (float) bundle.getDouble(Key.BUNDLE_INITIAL_WEIGHT);
            if (initWeight <= 0) initWeight = SPUtils.getFloat(SPKey.SP_realWeight);
        }
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo userInfo = new Gson().fromJson(string, UserInfo.class);
        float standardWeight = 0;
        if (userInfo.getSex() == 1) {
            standardWeight = (userInfo.getHeight() - 80) * 0.7f;
        } else {
            standardWeight = (userInfo.getHeight() - 70) * 0.6f;
        }
        initRuler(standardWeight);
        minWeight = standardWeight * 0.9f;
        maxWeight = standardWeight * 1.1f;

        settingWeight = standardWeight;
        stillNeed = initWeight - standardWeight;
        if (stillNeed <= 0) {
            //TODO 当前体重小于目标体重着
            tipDialog.showInfo("您当前体重小于标准体重，\n请设置目标体重~", 2000);
        }

        String tips = "需减重 " + RxFormatValue.fromat4S5R(stillNeed, 1) + " kg";
        SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .setLength(4, tips.length() - 3);

        mTvTips.setText(builder);
        tvBestWeight.setText(getString(R.string.bestWeight,
                RxFormatValue.fromat4S5R(minWeight, 1), RxFormatValue.fromat4S5R(maxWeight, 1)));
    }

    private void initRuler(float weight) {
        mTvTargetWeight.setText(RxFormatValue.fromat4S5R(weight, 1) + "");
        mWeightRulerView.setTextLabel("kg");
        mWeightRulerView.setColor(getResources().getColor(R.color.GrayWrite),
                getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.orange_FF7200));
        mWeightRulerView.setParam(DrawUtil.dip2px(10), DrawUtil.dip2px(60), DrawUtil.dip2px(40),
                DrawUtil.dip2px(25), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mWeightRulerView.initViewParam(weight, 35.0f, 90.0f, 1);
        mWeightRulerView.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                settingWeight = value;
                mTvTargetWeight.setText(RxFormatValue.fromat4S5R(value, 1));

                stillNeed = initWeight - settingWeight;
                String tips = (stillNeed < 0 ? "需增重" : "需减重 ") + RxFormatValue.fromat4S5R(stillNeed, 1) + " kg";
                SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                        .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                        .setProportion(1.4f)
                        .setLength(4, tips.length() - 3);
                mTvTips.setText(builder);

                if (settingWeight > maxWeight) {
                    RxToast.normal("您设定的体重目标有点高哦，\n可能会影响到身体健康～", 2000);
                } else if (settingWeight < minWeight) {
                    RxToast.normal("您设定的体重目标有点低哦，\n可能会影响到身体健康～", 2000);
                }
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

    private void nextStep() {
        if (settingWeight > initWeight) {
            tipDialog.showInfo("您设定的目标体重超过当前体重，\n请重新设置~", 2000);
            return;
        }

        Bundle mBundle = new Bundle(bundle);
        mBundle.putDouble(Key.BUNDLE_TARGET_WEIGHT, settingWeight);
        mBundle.putDouble(Key.BUNDLE_STILL_NEED, stillNeed);
        mBundle.putDouble(Key.BUNDLE_INITIAL_WEIGHT, initWeight);
        QMUIFragment instance = TargetDateFargment.getInstance();
        instance.setArguments(mBundle);
        startFragment(instance);
    }


    @OnClick(R.id.btn_nextStep)
    public void onViewClicked() {
        nextStep();
    }


}
