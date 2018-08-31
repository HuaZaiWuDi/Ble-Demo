package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

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
    QMUIRoundButton mBtnConfirm;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    Unbinder unbinder;


    private float stillNeed = 1, weeks;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_target_date);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initTopBar();
        initRuler();
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvTargetWeight.setTypeface(typeface);
        mTvTips.setTypeface(typeface);
    }

    private void initRuler() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            RxLogUtils.d("BUNDLE：" + bundle);
            stillNeed = (float) bundle.getDouble(Key.BUNDLE_STILL_NEED);
        }

        weeks = stillNeed / 0.5f;
        mTvTargetDays.setText((int) (weeks * 7) + "");
        String tips = "每周减重目标 " + 0.5 + " kg";
        SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                .setProportion(1.4f)
                .setLength(7, tips.length() - 3);
        mTvTips.setText(builder);

        mTvTargetWeight.setText(RxFormatValue.fromat4S5R(weeks, 0));
        mRulerWeight.setTextLabel("周");
        mRulerWeight.setDecimal(false);
        mRulerWeight.setColor(getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.GrayWrite), getResources().getColor(R.color.orange_FF7200));
        mRulerWeight.setParam(DrawUtil.dip2px(50), DrawUtil.dip2px(60), DrawUtil.dip2px(60),
                DrawUtil.dip2px(60), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mRulerWeight.initViewParam(weeks, 1f, stillNeed / 0.28f, 10);
        mRulerWeight.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                weeks = value;
                mTvTargetWeight.setText(RxFormatValue.fromat4S5R(value, 0));

                String tips = "每周减重目标 " + RxFormatValue.fromat4S5R((stillNeed / weeks), 1) + " kg";
                SpannableStringBuilder builder = RxTextUtils.getBuilder(tips)
                        .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                        .setProportion(1.4f)
                        .setLength(7, tips.length() - 3);
                mTvTips.setText(builder);
                mTvTargetDays.setText((int) (weeks * 7) + "");
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
        settingTarget();
    }


    private void settingTarget() {
        double aDouble = bundle.getDouble(Key.BUNDLE_LAST_WEIGHT);
        double aDouble1 = bundle.getDouble(Key.BUNDLE_TARGET_WEIGHT);

        JsonObject object = new JsonObject();
        object.addProperty("count", weeks);
        object.addProperty("initialWeight", aDouble);
        object.addProperty("targetWeight", aDouble1);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.setTargetWeight(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        //TODO 这里跳转目标不详，先跳转到体重首页
                        //关闭之前的设置目标体重和目标周期的界面
                        //直接跳转到指定的Fragment（同时清栈）
//                        getBaseFragmentActivity().popBackStack(WeightRecordFragment_.class);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
