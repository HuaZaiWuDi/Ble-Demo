package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.SportsListBean;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
import lab.wesmartclothing.wefit.flyso.view.HeartRateView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_sports_details)
public class SportsDetailsActivity extends BaseActivity {


    @ViewById
    TextView tv_heat;
    @ViewById
    TextView tv_curHeart;
    @ViewById
    TextView tv_maxHeart;
    @ViewById
    TextView tv_sportsTime;
    @ViewById
    TextView tv_Running;
    @ViewById
    TextView tv_connectDevice;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_state_title;
    @ViewById
    TextView tv_state;
    @ViewById
    TextView tv_1;
    @ViewById
    TextView tv_2;
    @ViewById
    TextView tv_3;
    @ViewById
    HeartRateView mHeartRateView;

    @Bean
    HeartRateToKcal mHeartRateToKcal;


    //监听瘦身衣连接情况
    @Receiver(actions = Key.ACTION_CLOTHING_CONNECT)
    void clothingConnectStatus(@Receiver.Extra(Key.EXTRA_CLOTHING_CONNECT) boolean state) {
        if (state) {
            tv_connectDevice.setText(R.string.connected);
            mTimer.startTimer();
        } else {
            tv_connectDevice.setText(R.string.disConnected);
            if (!isSports) return;
            mTimer.stopTimer();
            showDialog();
        }
    }

    //监听系统蓝牙开启
    @Receiver(actions = Key.ACTION_CLOTHING_STOP)
    void CLOTHING_STOP() {
        if (!isSports) return;
        mTimer.stopTimer();
        showDialog();
    }


    @Extra
    SportsListBean.ListBean BUNDLE_SPORTS_INFO;

    @Bean
    HeartRateBean heartRateBean;

    @Click
    void back() {
        finish();
    }

    //是否运动界面
    private boolean isSports = false;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕沉浸
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
    }

    @Override
    @AfterViews
    public void initView() {
        isSports = BUNDLE_SPORTS_INFO == null;

        if (isSports) {
            showStartSports();
        } else {
            showSportsInfo();
        }
        initText();
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }

    int duration = 0;

    MyTimer mTimer = new MyTimer(1000, new MyTimerListener() {
        @Override
        public void enterTimer() {
            duration++;
            tv_sportsTime.setText(RxFormat.setFormatDateG(duration * 1000, RxFormat.Date_Time));
        }
    });

    private void showStartSports() {
        tv_state.setText("开始运动");
        tv_connectDevice.setText(BleTools.getInstance().isConnect() ? "已连接" : "未连接");
        initRxBus();
    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(SportsDataTab.class, new Consumer<SportsDataTab>() {
            @Override
            public void accept(SportsDataTab sportsDataTab) throws Exception {
                if (isFirst) {
                    mHeartRateView.offileData(sportsDataTab.getAthlRecord_2(), sportsDataTab.getDuration());
                    isFirst = false;
                    duration = sportsDataTab.getDuration();
                }
                mTimer.startTimer();
                if (dialog != null)
                    dialog.dismiss();

                tv_curHeart.setText(sportsDataTab.getCurHeart() + "bpm");
                tv_maxHeart.setText(sportsDataTab.getMaxHeart() + "bpm");
                double calorie = sportsDataTab.getKcal();
                if (calorie < 1) {
                    tv_heat.setText(RxFormatValue.fromatUp(calorie * 1000, 0) + getString(R.string.unit_k));
                } else
                    tv_heat.setText((int) calorie + getString(R.string.unit_kcal));
                mHeartRateView.checkHeartRate(sportsDataTab.getCurHeart());
                tv_Running.setText(sportsDataTab.getSteps() + "步");
            }
        });
        RxBus.getInstance().addSubscription(this, register);
    }

    private void initText() {
        tv_1.setText(isSports ? R.string.curHeart : R.string.avgHeart);
        tv_2.setText(R.string.maxHeart);
        tv_3.setText(isSports ? R.string.heat : R.string.consumeHeat);
        tv_state_title.setVisibility(isSports ? View.VISIBLE : View.INVISIBLE);
        tv_state.setTextColor(getResources().getColor(isSports ? R.color.colorTheme : R.color.textColor));
    }


    private void showSportsInfo() {
        tv_state.setText(getString(R.string.sports) + getString(R.string.sportsTime));
        tv_curHeart.setText(BUNDLE_SPORTS_INFO.getAvgHeart() + "bpm");
        tv_maxHeart.setText(BUNDLE_SPORTS_INFO.getMaxHeart() + "bpm");
        tv_heat.setText(BUNDLE_SPORTS_INFO.getCalorie() + getString(R.string.unit_k));
        tv_title.setText(RxFormat.setFormatDate(BUNDLE_SPORTS_INFO.getAthlDate(), RxFormat.Date_CH));
        tv_sportsTime.setText(RxFormat.setFormatDate(BUNDLE_SPORTS_INFO.getDuration() * 1000 - 8 * 3600000, RxFormat.Date_Time));
        tv_connectDevice.setVisibility(View.GONE);
        getHistoryData();
    }


    private void getHistoryData() {
        if (BUNDLE_SPORTS_INFO == null) return;
        JsonObject object = new JsonObject();
        object.addProperty("athlDate", BUNDLE_SPORTS_INFO.getAthlDate());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.athleticsDetail(body))
                .compose(MyAPP.getRxCache().<String>transformObservable("athleticsDetail", String.class, CacheStrategy.cacheAndRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加心率：" + s);
                        HeartRateBean heartRateBean = new Gson().fromJson(s, HeartRateBean.class);
                        mHeartRateView.offileData(heartRateBean.getAthlList(), heartRateBean.getDuration());
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    RxDialogSure dialog;

    private void showDialog() {
        if (dialog == null) {
            dialog = new RxDialogSure(mActivity);
            dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
            dialog.getTvContent().setText("运动已经结束！！");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setSure("确定");
            dialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
        dialog.show();
    }

}
