package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.tool.RxQRCode;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.chart.bar.BarVerticalChart;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseShareActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.entity.SportingDetailBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created by jk on 2018/7/19.
 */
public class SportsDetailsFragment extends BaseShareActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_avPace)
    TextView mTvAvPace;
    @BindView(R.id.tv_maxPace)
    TextView mTvMaxPace;
    @BindView(R.id.tv_sportsTime)
    TextView mTvSportsTime;
    @BindView(R.id.tv_sportskcal)
    TextView mTvSportskcal;
    @BindView(R.id.layout_sportingKcal)
    LinearLayout mLayoutSportingKcal;
    @BindView(R.id.layout_maxPace)
    LinearLayout mLayoutMaxPace;
    @BindView(R.id.bar_calm)
    BarVerticalChart mBarCalm;
    @BindView(R.id.tv_calm)
    TextView mTvCalm;
    @BindView(R.id.bar_warm)
    BarVerticalChart mBarWarm;
    @BindView(R.id.tv_warm)
    TextView mTvWarm;
    @BindView(R.id.bar_grease)
    BarVerticalChart mBarGrease;
    @BindView(R.id.tv_grease)
    TextView mTvGrease;
    @BindView(R.id.bar_aerobic)
    BarVerticalChart mBarAerobic;
    @BindView(R.id.tv_aerobic)
    TextView mTvAerobic;
    @BindView(R.id.bar_anaerobic)
    BarVerticalChart mBarAnaerobic;
    @BindView(R.id.tv_anaerobic)
    TextView mTvAnaerobic;
    @BindView(R.id.bar_limit)
    BarVerticalChart mBarLimit;
    @BindView(R.id.tv_limit)
    TextView mTvLimit;
    @BindView(R.id.tv_heartCount)
    TextView mTvHeartCount;
    @BindView(R.id.tv_expectKcal)
    TextView mTvExpectKcal;
    @BindView(R.id.layout_legend)
    RelativeLayout mLayoutLegend;
    @BindView(R.id.chart_HeartRate)
    LineChart mChartHeartRate;
    @BindView(R.id.layout_sporting)
    RxRelativeLayout mLayoutSporting;
    @BindView(R.id.pro_limit)
    RxRoundProgressBar mProLimit;
    @BindView(R.id.tv_limit_time)
    TextView mTvLimitTime;
    @BindView(R.id.pro_anaerobic)
    RxRoundProgressBar mProAnaerobic;
    @BindView(R.id.tv_anaerobic_time)
    TextView mTvAnaerobicTime;
    @BindView(R.id.pro_aerobic)
    RxRoundProgressBar mProAerobic;
    @BindView(R.id.tv_aerobic_time)
    TextView mTvAerobicTime;
    @BindView(R.id.pro_grease)
    RxRoundProgressBar mProGrease;
    @BindView(R.id.tv_grease_time)
    TextView mTvGreaseTime;
    @BindView(R.id.pro_warm)
    RxRoundProgressBar mProWarm;
    @BindView(R.id.tv_warm_time)
    TextView mTvWarmTime;
    @BindView(R.id.tv_detailTitle)
    TextView mTvDetailTitle;
    @BindView(R.id.pro_calm)
    RxRoundProgressBar mProCalm;
    @BindView(R.id.tv_calm_time)
    TextView mTvCalmTime;
    @BindView(R.id.img_userImg)
    RxImageView mImgUserImg;
    @BindView(R.id.layout_shareTitle)
    RelativeLayout mLayoutShareTitle;
    @BindView(R.id.img_QRcode)
    ImageView mImgQRcode;
    @BindView(R.id.tv_appVersion)
    TextView mTvAppVersion;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.layout_QRcode)
    RelativeLayout mLayoutQRcode;
    @BindView(R.id.layout_sportingTimeDetail)
    LinearLayout mLayoutSportingTimeDetail;
    @BindView(R.id.layout_content)
    LinearLayout mLayoutContent;
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.tv_currentTime)
    TextView mTvCurrentTime;
    @BindView(R.id.tv_stepFrequency)
    TextView mTvStepFrequency;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;

    int warmTime = 0;
    int greaseTime = 0;
    int aerobicTime = 0;
    int anaerobicTime = 0;
    int limitTime = 0;
    int calmTime = 0;

    private HeartLineChartUtils lineChartUtils;
    private boolean isFreeSporting = true;//是否是自由运动
    private boolean goBack = false;

    @Override
    protected int layoutId() {
        return R.layout.fragment_sport_details;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initTypeface();
        lineChartUtils = new HeartLineChartUtils(mChartHeartRate);
        lineChartUtils.setPlanLineColor(Color.parseColor("#3F3943"), Color.parseColor("#FFFFFF"));


    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle("");
        QMUIAlphaImageButton imageButton = mQMUIAppBarLayout.addRightImageButton(R.mipmap.icon_share, R.id.img_share);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
        }
        imageButton.setOnClickListener(v -> share(true));
    }

    private void share(final boolean startShare) {
        mQMUIAppBarLayout.setVisibility(startShare ? View.GONE : View.VISIBLE);
        mLayoutShareTitle.setVisibility(startShare ? View.VISIBLE : View.GONE);
        mLayoutSportingTimeDetail.setVisibility(startShare ? View.GONE : View.VISIBLE);
        mLayoutQRcode.setVisibility(startShare ? View.VISIBLE : View.GONE);

        if (startShare) {

            //延迟500毫秒，需要等到控件显示
            mLayoutQRcode.postDelayed(() -> {
                //控件转图片
                Bitmap bitmap = RxImageUtils.view2Bitmap(mLayoutContent, ContextCompat.getColor(mContext, R.color.Gray));
                //微信分享图片最大尺寸32KB
//                    bitmap = RxImageUtils.compressByScale(bitmap, 0.5f, 0.5f);
                shareBitmap(bitmap);
                share(false);
            }, 500);
        }
    }

    private void initTypeface() {
        Typeface typeface = MyAPP.typeface;
        mTvKcal.setTypeface(typeface);
        mTvAvPace.setTypeface(typeface);
        mTvMaxPace.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
        mTvLimitTime.setTypeface(typeface);
        mTvAnaerobicTime.setTypeface(typeface);
        mTvAerobicTime.setTypeface(typeface);
        mTvGreaseTime.setTypeface(typeface);
        mTvWarmTime.setTypeface(typeface);
        mTvUserName.setTypeface(typeface);
        mTvDate.setTypeface(typeface);
        mTvCalmTime.setTypeface(typeface);

        RxTextUtils.getBuilder(getString(R.string.HealthSlim) + "\n")
                .append(getString(R.string.appName) + " v" + RxDeviceUtils.getAppVersionName())
                .setProportion(0.5f)
                .into(mTvAppVersion);

        UserInfo info = MyAPP.getgUserInfo();
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg);
        RxTextUtils.getBuilder(info.getUserName() + "\n")
                .append(getString(R.string.appDays, getString(R.string.appName), info.getRegisterTime())).setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvUserName);
        mTvDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd"));

        RxQRCode.builder(ServiceAPI.APP_DOWN_LOAD_URL)
                .codeSide(800)
                .logoBitmap(R.mipmap.icon_app_white, getResources())
                .into(mImgQRcode);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        isFreeSporting = !bundle.getBoolean(Key.BUNDLE_SPORTING_PLAN);
        goBack = bundle.getBoolean(Key.BUNDLE_GO_BCAK);

        RxTextUtils.getBuilder(isFreeSporting ? "0" : "0.0")
                .append(isFreeSporting ? "kcal" : getString(R.string.min)).setProportion(0.1f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvKcal);

        RxTextUtils.getBuilder("0")
                .append("\tbpm").setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvAvPace);

        if (isFreeSporting) {
            mTvDetailTitle.setText(R.string.consumeCalorie);
            mLayoutLegend.setVisibility(View.GONE);
        } else {
            mTvDetailTitle.setText(R.string.sportsScore);
            mLayoutSportingKcal.setVisibility(View.VISIBLE);
        }

        String gid = bundle.getString(Key.BUNDLE_DATA_GID);
        initData(gid);
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }


    private void initData(String gid) {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .singleAthlDetail(NetManager.fetchRequest(object.toString())))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("athleticsDetail" + gid, String.class,
                        CacheStrategy.firstCache()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        SportingDetailBean heartRateBean = JSON.parseObject(s, SportingDetailBean.class);
                        updateUI(heartRateBean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    private void updateUI(SportingDetailBean heartRateBean) {
        if (isFreeSporting) {
            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(heartRateBean.getCalorie(), 1))
                    .append("kcal").setProportion(0.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvKcal);

        } else {
            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(heartRateBean.getAthlScore(), 1))
                    .append(getString(R.string.min)).setProportion(0.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvKcal);

            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(heartRateBean.getCalorie(), 1))
                    .append("\tkcal").setProportion(0.5f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvSportskcal);
        }

        mTvMaxPace.setText(RxFormat.setSec2MS(heartRateBean.getMaxPace()));
        mTvAvPace.setText(RxFormat.setSec2MS((int) heartRateBean.getAvgPace()));

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(heartRateBean.getCadence(), 1))
                .append("\t" + getString(R.string.StrideUnit)).setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvStepFrequency);

        RxTextUtils.getBuilder(heartRateBean.getKilometers() + "")
                .append("\tkm").setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvDistance);

        int sunTime = 0;
        for (AthlPlanListBean bean : heartRateBean.getPlanAthlList()) {
            sunTime += bean.getDuration();
            bean.setTime(sunTime);
        }
        mQMUIAppBarLayout.setTitle(RxFormat.setFormatDate(heartRateBean.getAthlDate(), RxFormat.Date_CH));
        mTvExpectKcal.setText(getString(R.string.expectKcal, sunTime, heartRateBean.getPlanTotalDeplete()));

        mTvHeartCount.setText(heartRateBean.getAthlDesc());

        heartRateStatistics(heartRateBean.getRealAthlList());
        updateBar(heartRateBean);

        if (!isFreeSporting) {
            lineChartUtils.setPlanLineData(heartRateBean.getPlanAthlList());
        }
    }

    private void updateBar(SportingDetailBean heartRateBean) {
        int restCalorie = heartRateBean.getRestData();
        int warmCalorie = heartRateBean.getWarmData();
        int greaseCalorie = heartRateBean.getGreaseData();
        int aerobicCalorie = heartRateBean.getAerobicData();
        int anaerobicCalorie = heartRateBean.getAnaerobicData();
        int limitCalorie = heartRateBean.getLimitData();
        int totalKcal = warmCalorie + greaseCalorie + aerobicCalorie + anaerobicCalorie + limitCalorie;

        if (totalKcal == 0) return;
//        mBarCalm.setProgress(restCalorie * 100 / totalKcal);
//        if (restCalorie * 100 / totalKcal > 0)
//            mTvCalm.setText((int) (restCalorie * 100 / totalKcal) + "%");

        mBarWarm.setProgress(warmCalorie * 100 / totalKcal);
        if (warmCalorie * 100 / totalKcal > 0)
            mTvWarm.setText(RxFormatValue.fromat4S5R(warmCalorie * 100f / totalKcal, 0) + "%");

        mBarGrease.setProgress(greaseCalorie * 100 / totalKcal);
        if (greaseCalorie * 100 / totalKcal > 0)
            mTvGrease.setText(RxFormatValue.fromat4S5R(greaseCalorie * 100f / totalKcal, 0) + "%");

        mBarAerobic.setProgress(aerobicCalorie * 100 / totalKcal);
        if (aerobicCalorie * 100 / totalKcal > 0)
            mTvAerobic.setText(RxFormatValue.fromat4S5R(aerobicCalorie * 100f / totalKcal, 0) + "%");

        mBarAnaerobic.setProgress(anaerobicCalorie * 100 / totalKcal);
        if (anaerobicCalorie * 100 / totalKcal > 0)
            mTvAnaerobic.setText(RxFormatValue.fromat4S5R(anaerobicCalorie * 100f / totalKcal, 0) + "%");

        mBarLimit.setProgress(limitCalorie * 100 / totalKcal);
        if (limitCalorie * 100 / totalKcal > 0)
            mTvLimit.setText(RxFormatValue.fromat4S5R(limitCalorie * 100f / totalKcal, 0) + "%");
    }

    private void heartRateStatistics(List<HeartRateItemBean> realAthlList) {
        RxLogUtils.d("心率数据个数：" + realAthlList.size());
        int totalTime = 0;
        List<Integer> heartLists = new ArrayList<>();
        for (int i = 0; i < realAthlList.size(); i++) {
            int reversePace = HeartRateUtil.reversePace(realAthlList.get(i).getPace());
            heartLists.add(reversePace);
            checkHeartRate(reversePace, realAthlList.get(i).getStepTime());
            totalTime += realAthlList.get(i).getStepTime();
        }
        lineChartUtils.setRealTimeData(heartLists);
        mChartHeartRate.animateX(1000);

        if (totalTime == 0) return;
        mProCalm.setProgress(calmTime * 100 / totalTime);
        mProWarm.setProgress((warmTime * 100) / totalTime);
        mProGrease.setProgress(greaseTime * 100 / totalTime);
        mProAerobic.setProgress(aerobicTime * 100 / totalTime);
        mProAnaerobic.setProgress(anaerobicTime * 100 / totalTime);
        mProLimit.setProgress(limitTime * 100 / totalTime);

        mTvCurrentTime.setText(RxFormat.setSec2MS(totalTime));
        mTvSportsTime.setText(RxFormat.setSec2MS(totalTime));
        mTvWarmTime.setText(RxFormat.setSec2MS(warmTime));
        mTvGreaseTime.setText(RxFormat.setSec2MS(greaseTime));
        mTvAerobicTime.setText(RxFormat.setSec2MS(aerobicTime));
        mTvAnaerobicTime.setText(RxFormat.setSec2MS(anaerobicTime));
        mTvLimitTime.setText(RxFormat.setSec2MS(limitTime));
        mTvCalmTime.setText(RxFormat.setSec2MS(calmTime));
    }


    private void checkHeartRate(int reversePace, int stepTime) {
        int[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0];
        int heart_1 = heartRates[1];
        int heart_2 = heartRates[2];
        int heart_3 = heartRates[3];
        int heart_4 = heartRates[4];
        int heart_5 = heartRates[5];
        int heart_6 = heartRates[6];

        if (reversePace < heart_1) {
            calmTime += stepTime;
        } else if (reversePace <= heart_2) {
            warmTime += stepTime;
        } else if (reversePace <= heart_3) {
            greaseTime += stepTime;
        } else if (reversePace <= heart_4) {
            aerobicTime += stepTime;
        } else if (reversePace <= heart_5) {
            anaerobicTime += stepTime;
        } else {
            limitTime += stepTime;
        }
    }


    @Override
    public void onBackPressed() {
        if (goBack) {
            super.onBackPressed();
        } else {
            RxActivityUtils.skipActivity(mContext, MainActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
