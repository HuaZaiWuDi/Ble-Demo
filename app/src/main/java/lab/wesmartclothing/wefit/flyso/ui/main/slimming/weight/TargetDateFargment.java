package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.TargetInfoBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanDetailsActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.RecordInfoActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created by jk on 2018/7/27.
 */
public class TargetDateFargment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.ruler_weight)
    DecimalScaleRulerView mRulerWeight;
    @BindView(R.id.tv_targetDays)
    TextView mTvTargetDays;
    @BindView(R.id.btn_confirm)
    RxTextView mBtnConfirm;
    @BindView(R.id.tv_tips)
    TextView mTvTips;


    private float stillNeed = 1;
    private int weeks;
    private Bundle bundle;
    private TargetInfoBean mInfoBean = new TargetInfoBean();

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_target_date;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
    }

    private void initView() {
        initTopBar();
        initRuler();
        Typeface typeface = MyAPP.typeface;
        mTvTargetWeight.setTypeface(typeface);
        mTvTips.setTypeface(typeface);
        if (SPUtils.getBoolean(SPKey.SP_SET_PLAN)) {
            mBtnConfirm.setText("确认");
        } else {
            mBtnConfirm.setText("下一步");
        }
    }

    private void initRuler() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            RxLogUtils.d("BUNDLE：" + bundle);
            stillNeed = (float) bundle.getDouble(Key.BUNDLE_STILL_NEED);
        }

        weeks = (int) (stillNeed / 0.5f);
        weeks = (weeks < 1 ? 1 : weeks);
        mTvTargetDays.setText((weeks * 7) + "天");
        String tips = "每周减重目标 " + 0.5 + " kg";
        SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .setLength(7, tips.length() - 3);
        mTvTips.setText(builder);

        mTvTargetWeight.setText((int) (weeks < 1 ? 1 : weeks) + "");
        mRulerWeight.setTextLabel("周");
        mRulerWeight.setDecimal(false);
        mRulerWeight.setColor(getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.orange_FF7200));
        mRulerWeight.setParam(DrawUtil.dip2px(50), DrawUtil.dip2px(60), DrawUtil.dip2px(60),
                DrawUtil.dip2px(60), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mRulerWeight.initViewParam((int) (weeks < 1 ? 1 : weeks), 1f, (stillNeed / 0.28f < 1 ? 1 : stillNeed / 0.28f), 10);
        mRulerWeight.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                weeks = (int) (value < 1 ? 1 : value);
                mTvTargetWeight.setText(weeks + "");

                String tips = "每周减重目标 " + RxFormatValue.fromat4S5R((stillNeed / weeks), 1) + " kg";
                SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                        .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                        .setProportion(1.4f)
                        .setLength(7, tips.length() - 3);
                mTvTips.setText(builder);
                mTvTargetDays.setText((weeks * 7) + "天");
                mTvTargetDays.setCompoundDrawables(null, null, null, null);

                if ((stillNeed / weeks) >= 1) {
                    RxToast.normal("每周减重过多可能会导致健康问题，建议您健康减重哦～", 2000);
                }
            }
        });
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("目标设置");
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        double initWeight = bundle.getDouble(Key.BUNDLE_INITIAL_WEIGHT);
        double targetWeight = bundle.getDouble(Key.BUNDLE_TARGET_WEIGHT);
        mInfoBean.setCount(weeks);
        mInfoBean.setInitialWeight(RxFormatValue.format4S5R(initWeight, 1));
        mInfoBean.setTargetWeight(targetWeight);


        RxLogUtils.d("mSubmitInfoFrom：" + RecordInfoActivity.mSubmitInfoFrom);
        if (RecordInfoActivity.mSubmitInfoFrom == null) {
            settingTarget();
        } else {
            submitPlan();
        }
    }

    private void submitPlan() {
        RecordInfoActivity.mSubmitInfoFrom.setTargetInfo(mInfoBean);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .submitInform(NetManager.fetchRequest(JSON.toJSONString(RecordInfoActivity.mSubmitInfoFrom))))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RecordInfoActivity.mSubmitInfoFrom = null;
                        //关闭之前的设置目标体重和目标周期的界面
                        RxBus.getInstance().post(new RefreshSlimming());
                        //直接跳转到指定的Fragment（同时清栈）
                        RxActivityUtils.skipActivity(mContext, PlanDetailsActivity.class);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    private void settingTarget() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .setTargetWeight(NetManager.fetchRequest(JSON.toJSONString(mInfoBean))))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        //关闭之前的设置目标体重和目标周期的界面
                        RxBus.getInstance().post(new RefreshSlimming());
                        //直接跳转到指定的Fragment（同时清栈）
                        RxActivityUtils.skipActivity(mContext, MainActivity.class);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

}
