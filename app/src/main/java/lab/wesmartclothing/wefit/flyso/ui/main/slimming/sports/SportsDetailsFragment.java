package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.smartclothing.blelibrary.BleKey;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/7/19.
 */
public class SportsDetailsFragment extends BaseAcFragment {

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
    @BindView(R.id.layout_sports)
    LinearLayout mLayoutSports;
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
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new SportsDetailsFragment();
    }

    private long currentTime = 0;

    int warmTime = 0;
    int greaseTime = 0;
    int aerobicTime = 0;
    int anaerobicTime = 0;
    int limitTime = 0;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sport_details, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        JsonObject object = new JsonObject();
        object.addProperty("athlDate", currentTime + "");

        boolean isToday = RxFormat.setFormatDate(currentTime, RxFormat.Date).equals(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date));
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.athleticsDetail(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("athleticsDetail" + RxFormat.setFormatDate(currentTime, RxFormat.Date), String.class, isToday ? CacheStrategy.firstRemote() : CacheStrategy.firstCache()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        HeartRateBean heartRateBean = MyAPP.getGson().fromJson(s, HeartRateBean.class);
                        mTvKcal.setText(RxFormatValue.fromat4S5R(heartRateBean.getCalorie(), 1));
                        mTvAvHeartRate.setText(heartRateBean.getAvgHeart() + "");
                        mTvMaxHeartRate.setText(heartRateBean.getMaxHeart() + "");

                        heartRateStatistics(heartRateBean.getAthlList(), heartRateBean.getDuration());
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void heartRateStatistics(List<HeartRateBean.AthlList> athlList, int totalTime) {
        RxLogUtils.d("心率数据个数：" + athlList.size());
        for (int i = 0; i < athlList.size(); i++) {
            checkHeartRate(athlList.get(i));
        }

        mProWarm.setProgress((warmTime * 100) / totalTime);
        mProGrease.setProgress(greaseTime * 100 / totalTime);
        mProAerobic.setProgress(aerobicTime * 100 / totalTime);
        mProAnaerobic.setProgress(anaerobicTime * 100 / totalTime);
        mProLimit.setProgress(limitTime * 100 / totalTime);

        mTvSportsTime.setText(RxFormat.setSec2MS(totalTime));
        mTvWarmTime.setText(RxFormat.setSec2MS(warmTime));
        mTvGreaseTime.setText(RxFormat.setSec2MS(greaseTime));
        mTvAerobicTime.setText(RxFormat.setSec2MS(aerobicTime));
        mTvAnaerobicTime.setText(RxFormat.setSec2MS(anaerobicTime));
        mTvLimitTime.setText(RxFormat.setSec2MS(limitTime));

    }


    private void checkHeartRate(HeartRateBean.AthlList bean) {
        int heart = bean.getHeartRate();
        byte[] heartRates = BleKey.heartRates;
//        int heart_0 = heartRates[0] & 0xff;
        int heart_0 = 0;//TODO 测试暂时归纳小于100的心率算热身心率
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;

        if (heart >= heart_0 && heart <= heart_1) {
            warmTime += bean.getStepTime();
        } else if (heart >= heart_1 && heart < heart_2) {
            greaseTime += bean.getStepTime();
        } else if (heart >= heart_2 && heart < heart_3) {
            aerobicTime += bean.getStepTime();
        } else if (heart >= heart_3 && heart < heart_4) {
            anaerobicTime += bean.getStepTime();
        } else if (heart >= heart_4) {
            limitTime += bean.getStepTime();
        }
    }

    private void initView() {
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvKcal.setTypeface(typeface);
        mTvAvHeartRate.setTypeface(typeface);
        mTvMaxHeartRate.setTypeface(typeface);
        mTvSportsTime.setTypeface(typeface);
        mTvLimitTime.setTypeface(typeface);
        mTvAnaerobicTime.setTypeface(typeface);
        mTvAerobicTime.setTypeface(typeface);
        mTvGreaseTime.setTypeface(typeface);
        mTvWarmTime.setTypeface(typeface);

        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        Bundle args = getArguments();
        currentTime = args == null ? System.currentTimeMillis() : args.getLong(Key.BUNDLE_SPORTING_DATE);
        mQMUIAppBarLayout.setTitle(RxFormat.setFormatDate(currentTime, RxFormat.Date_CH));
    }

}
