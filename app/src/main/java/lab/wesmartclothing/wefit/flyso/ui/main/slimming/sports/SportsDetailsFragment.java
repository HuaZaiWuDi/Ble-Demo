package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
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
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AthlPlanListBean;
import lab.wesmartclothing.wefit.flyso.entity.SportingDetailBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.utils.HeartLineChartUtils;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created by jk on 2018/7/19.
 */
public class SportsDetailsFragment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_avHeartRate)
    TextView mTvAvHeartRate;
    @BindView(R.id.tv_maxHeartRate)
    TextView mTvMaxHeartRate;
    @BindView(R.id.tv_sportsTime)
    TextView mTvSportsTime;
    @BindView(R.id.tv_sportskcal)
    TextView mTvSportskcal;
    @BindView(R.id.layout_sportingKcal)
    LinearLayout mLayoutSportingKcal;
    @BindView(R.id.layout_maxHeart)
    LinearLayout mLayoutMaxHeart;
    @BindView(R.id.layout_sports)
    LinearLayout mLayoutSports;
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
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("");
        QMUIAlphaImageButton imageButton = mQMUIAppBarLayout.addRightImageButton(R.mipmap.icon_share, R.id.img_share);
        imageButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(true);
            }
        });
    }

    private void share(final boolean startShare) {
        mQMUIAppBarLayout.setVisibility(startShare ? View.GONE : View.VISIBLE);
        mLayoutShareTitle.setVisibility(startShare ? View.VISIBLE : View.GONE);
        mLayoutSportingTimeDetail.setVisibility(startShare ? View.GONE : View.VISIBLE);
        mLayoutQRcode.setVisibility(startShare ? View.VISIBLE : View.GONE);

        if (startShare) {

            RxQRCode.builder(ServiceAPI.APP_DOWN_LOAD_URL)
                    .codeSide(800)
                    .logoBitmap(R.mipmap.icon_app_round, getResources())
                    .into(mImgQRcode);

            //延迟500毫秒，需要等到控件显示
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //控件转图片
                    Bitmap bitmap = RxImageUtils.view2Bitmap(mLayoutContent, ContextCompat.getColor(mContext, R.color.Gray));
                    //微信分享图片最大尺寸32KB
//                    bitmap = RxImageUtils.compressByScale(bitmap, 0.5f, 0.5f);
                    showSimpleBottomSheetGrid(bitmap);
                    share(false);
                }
            }, 500);
        }
    }

    private void initTypeface() {
        Typeface typeface = MyAPP.typeface;
        mTvKcal.setTypeface(typeface);
        mTvAvHeartRate.setTypeface(typeface);
        mTvMaxHeartRate.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
        mTvLimitTime.setTypeface(typeface);
        mTvAnaerobicTime.setTypeface(typeface);
        mTvAerobicTime.setTypeface(typeface);
        mTvGreaseTime.setTypeface(typeface);
        mTvWarmTime.setTypeface(typeface);
        mTvUserName.setTypeface(typeface);
        mTvDate.setTypeface(typeface);
        mTvCalmTime.setTypeface(typeface);


        RxTextUtils.getBuilder("让 你 健 康 瘦\n")
                .append(getString(R.string.appName) + " v" + RxDeviceUtils.getAppVersionName())
                .setProportion(0.5f)
                .into(mTvAppVersion);

        UserInfo info = MyAPP.gUserInfo;
        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mImgUserImg);
        RxTextUtils.getBuilder(info.getUserName() + "\n")
                .append(getString(R.string.appDays, getString(R.string.appName), info.getRegisterTime())).setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvUserName);
        mTvDate.setText(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd"));
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        isFreeSporting = !bundle.getBoolean(Key.BUNDLE_SPORTING_PLAN);
        goBack = bundle.getBoolean(Key.BUNDLE_GO_BCAK);

        RxTextUtils.getBuilder(isFreeSporting ? "0" : "0.0")
                .append(isFreeSporting ? "kcal" : "分").setProportion(0.1f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvKcal);

        RxTextUtils.getBuilder("0")
                .append("\tbpm").setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvAvHeartRate);

        if (isFreeSporting) {
            mTvDetailTitle.setText("消耗热量");
            mLayoutLegend.setVisibility(View.GONE);

            RxTextUtils.getBuilder("0")
                    .append("\tbpm").setProportion(0.5f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvMaxHeartRate);

        } else {
            mTvDetailTitle.setText("运动评分");
            mLayoutSportingKcal.setVisibility(View.VISIBLE);
            mLayoutMaxHeart.setVisibility(View.GONE);

            RxTextUtils.getBuilder("0")
                    .append("\tkcal").setProportion(0.5f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvSportskcal);
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
                .compose(MyAPP.getRxCache().<String>transformObservable("athleticsDetail" + gid, String.class, CacheStrategy.firstCache()))
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

            RxTextUtils.getBuilder(heartRateBean.getMaxHeart() + "")
                    .append("\tbpm").setProportion(0.5f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvMaxHeartRate);

        } else {
            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(heartRateBean.getAthlScore(), 1))
                    .append("分").setProportion(0.3f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvKcal);

            RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(heartRateBean.getCalorie(), 1))
                    .append("\tkcal").setProportion(0.5f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(mTvSportskcal);
        }


        RxTextUtils.getBuilder(heartRateBean.getAvgHeart() + "")
                .append("\tbpm").setProportion(0.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(mTvAvHeartRate);


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
        int restCalorie = heartRateBean.getRestCalorie();
        int warmCalorie = heartRateBean.getWarmCalorie();
        int greaseCalorie = heartRateBean.getGreaseCalorie();
        int aerobicCalorie = heartRateBean.getAerobicCalorie();
        int anaerobicCalorie = heartRateBean.getAnaerobicCalorie();
        int limitCalorie = heartRateBean.getLimitCalorie();
        int totalKcal = restCalorie + warmCalorie + greaseCalorie + aerobicCalorie + anaerobicCalorie + limitCalorie;

        if (totalKcal == 0) return;
        mBarCalm.setProgress(restCalorie * 100 / totalKcal);
        if (restCalorie * 100 / totalKcal > 0)
            mTvCalm.setText((int) (restCalorie * 100 / totalKcal) + "%");

        mBarWarm.setProgress(warmCalorie * 100 / totalKcal);
        if (warmCalorie * 100 / totalKcal > 0)
            mTvWarm.setText((int) (warmCalorie * 100 / totalKcal) + "%");

        mBarGrease.setProgress(greaseCalorie * 100 / totalKcal);
        if (greaseCalorie * 100 / totalKcal > 0)
            mTvGrease.setText(((int) greaseCalorie * 100 / totalKcal) + "%");

        mBarAerobic.setProgress(aerobicCalorie * 100 / totalKcal);
        if (aerobicCalorie * 100 / totalKcal > 0)
            mTvAerobic.setText(((int) aerobicCalorie * 100 / totalKcal) + "%");

        mBarAnaerobic.setProgress(anaerobicCalorie * 100 / totalKcal);
        if (anaerobicCalorie * 100 / totalKcal > 0)
            mTvAnaerobic.setText(((int) anaerobicCalorie * 100 / totalKcal) + "%");

        mBarLimit.setProgress(limitCalorie * 100 / totalKcal);
        if (limitCalorie * 100 / totalKcal > 0)
            mTvLimit.setText(((int) limitCalorie * 100 / totalKcal) + "%");
    }

    private void heartRateStatistics(List<SportingDetailBean.RealAthlListBean> realAthlList) {
        RxLogUtils.d("心率数据个数：" + realAthlList.size());
        int totalTime = 0;
        List<Integer> heartLists = new ArrayList<>();
        for (int i = 0; i < realAthlList.size(); i++) {
            heartLists.add(realAthlList.get(i).getHeartRate());
            checkHeartRate(realAthlList.get(i));
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


    private void checkHeartRate(SportingDetailBean.RealAthlListBean bean) {
        int heart = bean.getHeartRate();
        int[] heartRates = Key.HRART_SECTION;
        int heart_0 = heartRates[0] ;
        int heart_1 = heartRates[1] ;
        int heart_2 = heartRates[2] ;
        int heart_3 = heartRates[3] ;
        int heart_4 = heartRates[4] ;
        int heart_5 = heartRates[5] ;
        int heart_6 = heartRates[6] ;

        if (heart >= heart_1 && heart <= heart_2) {
            warmTime += bean.getStepTime();
        } else if (heart >= heart_2 && heart < heart_3) {
            greaseTime += bean.getStepTime();
        } else if (heart >= heart_3 && heart < heart_4) {
            aerobicTime += bean.getStepTime();
        } else if (heart >= heart_4 && heart < heart_5) {
            anaerobicTime += bean.getStepTime();
        } else if (heart >= heart_5) {
            limitTime += bean.getStepTime();
        } else if (heart < heart_1) {
            calmTime += bean.getStepTime();
        }
    }


    private void showSimpleBottomSheetGrid(final Bitmap imgUrl) {

        UMImage image = new UMImage(this, imgUrl);//网络图片

//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享压缩格式设置

        UMImage thumb = new UMImage(this, imgUrl);
        image.setThumb(thumb);
        new ShareAction(mActivity)
                .withMedia(image)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(mUMShareListener).open();

    }

    UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            RxLogUtils.d("开始分享");
            tipDialog.show("正在分享...", 3000);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            RxLogUtils.d("分享成功");
            RxToast.normal("分享成功");
            tipDialog.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            RxLogUtils.d("分享失败");
            RxToast.normal("分享失败");
            tipDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            RxLogUtils.d("分享关闭");
            RxToast.normal("分享关闭");
            tipDialog.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (goBack) {
            super.onBackPressed();
        } else {
            RxActivityUtils.skipActivity(mContext, MainActivity.class);
        }
    }
}
