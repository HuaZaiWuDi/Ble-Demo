package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.Healthy;
import lab.wesmartclothing.wefit.flyso.entity.HealthyInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.BodyDataUtil;
import lab.wesmartclothing.wefit.flyso.view.BodyAgeProgressView;
import lab.wesmartclothing.wefit.flyso.view.HealthLevelView;
import lab.wesmartclothing.wefit.flyso.view.HealthyProgressView;

public class HealthyAssessActivity extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.circleProgressBar)
    RoundProgressBar mCircleProgressBar;
    @BindView(R.id.tv_healthScore)
    TextView mTvHealthScore;
    @BindView(R.id.Healthy_level)
    HealthLevelView mHealthyLevel;
    @BindView(R.id.iv_icon_bodyFit)
    ImageView mIvIconBodyFit;
    @BindView(R.id.tv_title_bodyFit)
    TextView mTvTitleBodyFit;
    @BindView(R.id.tv_bodyValue_bodyFit)
    TextView mTvBodyValueBodyFit;
    @BindView(R.id.btn_status_bodyFit)
    RxTextView mBtnStatusBodyFit;
    @BindView(R.id.pro_bodyFit)
    HealthyProgressView mProBodyFit;
    @BindView(R.id.tv_bodyFat_tip)
    TextView mTvBodyFatTip;
    @BindView(R.id.iv_icon_visceralFat)
    ImageView mIvIconVisceralFat;
    @BindView(R.id.tv_title_visceralFat)
    TextView mTvTitleVisceralFat;
    @BindView(R.id.tv_bodyValue_visceralFat)
    TextView mTvBodyValueVisceralFat;
    @BindView(R.id.btn_status_visceralFat)
    RxTextView mBtnStatusVisceralFat;
    @BindView(R.id.pro_visceralFat)
    HealthyProgressView mProVisceralFat;
    @BindView(R.id.tv_visceralFat_tip)
    TextView mTvVisceralFatTip;
    @BindView(R.id.iv_icon_bodyAge)
    ImageView mIvIconBodyAge;
    @BindView(R.id.tv_title_bodyAge)
    TextView mTvTitleBodyAge;
    @BindView(R.id.tv_bodyValue_bodyAge)
    TextView mTvBodyValueBodyAge;
    @BindView(R.id.pro_bodyAge)
    BodyAgeProgressView mProBodyAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.green_61D97F);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_healthy_assess;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();


    }

    private void initTopBar() {
        mQMUIAppBarLayout.setTitle("健康评估");
        mQMUIAppBarLayout.addLeftBackImageButton()
                .setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        HealthyInfoBean bean = (HealthyInfoBean) bundle.getSerializable(Key.BUNDLE_DATA);
        if (bean == null) return;

        BodyDataUtil bodyDataUtil = new BodyDataUtil();

        mProBodyAge.setUpDownText(bean.getBodyAge() + "", new String[]{"20", "30", "40", "50"});
        mProBodyAge.setProgress(bean.getBodyAge());
        mTvBodyValueBodyAge.setText(bean.getBodyAge() + "岁");

        UserInfo userInfo = JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);

        /**
         * 体脂率
         * 男性 15-18-23
         * 女性 20 -25- 30
         *
         * */
        //体脂率
        Healthy healthy = new Healthy();
        healthy.setSections(userInfo.getSex() == 1 ? new double[]{15, 18, 23} : new double[]{20, 25, 30});
        healthy.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy.setSectionLabels(userInfo.getSex() == 1 ? new String[]{"15.0%", "18.0%", "23.0%"} : new String[]{"20.0%", "25.0%", "30.0%"});
        healthy.setLabels(new String[]{"偏低", "标准", "偏高", "严重偏高"});
        mProBodyFit.setUpDownText(healthy.getSectionLabels(), healthy.getLabels());
        mProBodyFit.setColors(healthy.getColors());
        mProBodyFit.setProgress(bodyDataUtil.transformation(healthy, bean.getBodyFat()));
        String statusStr = (String) bodyDataUtil.checkStatus(bean.getBodyFat(), healthy)[0];
        Integer stateColor = (Integer) bodyDataUtil.checkStatus(bean.getBodyFat(), healthy)[1];
        mTvBodyValueBodyFit.setText(bean.getBodyFat() + " %");
        mTvBodyValueBodyFit.setTextColor(stateColor);

        mBtnStatusBodyFit.setText(statusStr);
        mBtnStatusBodyFit.setTextColor(stateColor);
        mBtnStatusBodyFit.getHelper().setBorderColorNormal(stateColor);

        String healthyDetailStr = "";
        Drawable drawable = null;
        //异常
        switch (statusStr) {
            case "偏低":
                healthyDetailStr = "体脂率病理性降低会引起功能性失调。";
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_error_tip);
                break;
            case "标准":
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_normal_tip);
                healthyDetailStr = "您的体脂率处于标准范围，请继续保持哦！";
                break;
            case "偏高":
            case "严重偏高":
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_error_tip);
                healthyDetailStr = " 体脂率病理性增高会增加患高血压、肾病、糖尿病、痛风、月经异常的风险。";
                break;
        }
        mTvBodyFatTip.setText(healthyDetailStr);
        mTvBodyFatTip.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);


        //内脏脂肪等级
        Healthy healthy3 = new Healthy();
        healthy3.setSections(new double[]{9, 14});
        healthy3.setSectionLabels(new String[]{"9", "14"});
        healthy3.setColors(new int[]{Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy3.setLabels(new String[]{"标准", "偏高", "严重偏高"});
        mProVisceralFat.setUpDownText(healthy3.getSectionLabels(), healthy3.getLabels());
        mProVisceralFat.setColors(healthy3.getColors());
        mProVisceralFat.setProgress(bodyDataUtil.transformation(healthy3, bean.getVisfat()));
        statusStr = (String) bodyDataUtil.checkStatus(bean.getVisfat(), healthy3)[0];
        stateColor = (Integer) bodyDataUtil.checkStatus(bean.getVisfat(), healthy3)[1];
        mTvBodyValueVisceralFat.setText(bean.getVisfat() + " %");
        mTvBodyValueVisceralFat.setTextColor(stateColor);

        mBtnStatusVisceralFat.setText(statusStr);
        mBtnStatusVisceralFat.setTextColor(stateColor);
        mBtnStatusVisceralFat.getHelper().setBorderColorNormal(stateColor);


        //异常
        switch (statusStr) {
            case "标准":
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_normal_tip);
                healthyDetailStr = "您的内脏脂肪等级处于标准范围，请继续保持哦！";
                break;
            case "偏高":
            case "严重偏高":
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_error_tip);
                healthyDetailStr = "内脏脂肪等级增加会引发脂肪肝，扰乱新陈代谢，还可能致癌。";
                break;
        }
        mTvVisceralFatTip.setText(healthyDetailStr);
        mTvVisceralFatTip.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);


        //健康指数
        RxTextUtils.getBuilder("健康指数\n")
                .append(bean.getHealthScore() + "").setProportion(2f).setBold()
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                .into(mTvHealthScore);
        mCircleProgressBar.setProgress((int) bean.getHealthScore());


        //健康评级
        mHealthyLevel.switchLevel(bean.getBmiLevel());
    }

}
