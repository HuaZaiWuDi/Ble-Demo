package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created by jk on 2018/7/26.
 */
public class SettingTargetFragment extends BaseActivity {


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
    RxTextView mBtnNextStep;
    Unbinder unbinder;


    private float maxWeight, minWeight, targetWeight, initWeight = SPUtils.getFloat(SPKey.SP_realWeight), stillNeed;

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_setting_target;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        bestWeight();
        Typeface typeface = MyAPP.typeface;
        mTvTargetWeight.setTypeface(typeface);
        mTvTips.setTypeface(typeface);
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);

        targetWeight = (float) bundle.getDouble(Key.BUNDLE_TARGET_WEIGHT);
        //最新的体重
        initWeight = (float) bundle.getDouble(Key.BUNDLE_INITIAL_WEIGHT);
        bestWeight();
    }

    /**
     * 男性：(身高cm－80)×70﹪=标准体重
     * 女性：(身高cm－70)×60﹪=标准体重
     */
    private void bestWeight() {
        float standardWeight = 0;
        UserInfo userInfo = MyAPP.getgUserInfo();
        if (userInfo.getSex() == 1) {
            standardWeight = (userInfo.getHeight() - 80) * 0.7f;
        } else {
            standardWeight = (userInfo.getHeight() - 70) * 0.6f;
        }
        if (targetWeight == 0)
            targetWeight = standardWeight;
        initRuler(targetWeight);
        minWeight = standardWeight * 0.9f;
        maxWeight = standardWeight * 1.1f;

        stillNeed = initWeight - targetWeight;
        if (stillNeed < 0) {
            //TODO 当前体重小于目标体重着
            tipDialog.showInfo("您当前体重小于目标体重，\n请设置目标体重~", 2000);
        }

        String tips = (stillNeed < 0 ? "需增重：" : "需减重：") + RxFormatValue.fromat4S5R(Math.abs(stillNeed), 1) + " kg";
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
                targetWeight = value;
                mTvTargetWeight.setText(RxFormatValue.fromat4S5R(value, 1));

                stillNeed = initWeight - targetWeight;
                String tips = (stillNeed < 0 ? "需增重：" : "需减重：") + RxFormatValue.fromat4S5R(Math.abs(stillNeed), 1) + " kg";
                SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                        .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                        .setProportion(1.4f)
                        .setLength(4, tips.length() - 3);
                mTvTips.setText(builder);

                tv_targetDays.setCompoundDrawables(null, null, null, null);

                if (targetWeight > maxWeight) {
                    RxToast.normal("您设定的体重目标有点高哦，\n可能会影响到身体健康～");
                } else if (targetWeight < minWeight) {
                    RxToast.normal("您设定的体重目标有点低哦，\n可能会影响到身体健康～");
                }
            }
        });
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle("目标设置");
    }

    private void nextStep() {
        if (targetWeight >= initWeight) {
            tipDialog.showInfo("您设定的目标体重超过或等于当前体重，\n请重新设置~", 2000);
            return;
        }
        Bundle mBundle = new Bundle();
        mBundle.putDouble(Key.BUNDLE_TARGET_WEIGHT, RxFormatValue.format4S5R(targetWeight, 2));
        mBundle.putDouble(Key.BUNDLE_STILL_NEED, RxFormatValue.format4S5R(stillNeed, 2));
        mBundle.putDouble(Key.BUNDLE_INITIAL_WEIGHT, initWeight);
        RxActivityUtils.skipActivity(mContext, TargetDateFargment.class, mBundle);
    }


    @OnClick(R.id.btn_nextStep)
    public void onViewClicked() {
        nextStep();
    }


}
