package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smartclothing.blelibrary.BleTools;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.SportsListBean;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
import lab.wesmartclothing.wefit.flyso.view.HeartRateView;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
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
        } else {
            tv_connectDevice.setText(R.string.disConnected);
            sportsTime.stopTimer();
        }
    }


    //心率
    @Receiver(actions = Key.ACTION_HEART_RATE_CHANGED)
    void myHeartRate(@Receiver.Extra(Key.EXTRA_HEART_RATE_CHANGED) byte[] data) {
        if (!isSports) return;
        int heartRate = data[8] & 0xff;
        sportsTime.startTimer();
        mHeartRateView.checkHeartRate(heartRate);

        maxHeart = heartRate > maxHeart ? heartRate : maxHeart;
        minHeart = heartRate < minHeart ? heartRate : minHeart;
        tv_curHeart.setText(heartRate + "bpm");
        tv_maxHeart.setText(maxHeart + "bpm");


        double hour = duration / 3600000f;

        double calorie = mHeartRateToKcal.getCalorie(heartRate, hour);
        RxLogUtils.d("calorie：" + calorie);
        if (calorie < 1) {
            tv_heat.setText(RxFormatValue.fromatUp(calorie * 1000, 0) + getString(R.string.unit_k));
        } else
            tv_heat.setText((int) calorie + getString(R.string.unit_kcal));
        tv_Running.setText(ByteUtil.bytesToIntD2(new byte[]{data[12], data[13]}) + "步");
    }


    @Extra
    SportsListBean.ListBean BUNDLE_SPORTS_INFO;

    @Bean
    HeartRateBean heartRateBean;

    @Click
    void back() {
        onBackPressed();
    }

    //是否运动界面
    private boolean isSports = false;


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

//        RxLogUtils.d("男：" + HeartRateToKcal.getCalorie(true, 150, 60, 20, 1));
//        RxLogUtils.d("女：" + HeartRateToKcal.getCalorie(false, 150, 60, 20, 1));
    }

    private int maxHeart = 0;//最大心率
    private int minHeart = 0;//最小心率
    private int duration = 0;//运动持续时间


    //运动计时
    MyTimer sportsTime = new MyTimer(1000, new MyTimerListener() {
        @Override
        public void enterTimer() {
            duration += 1000;
            tv_sportsTime.setText(RxFormat.setFormatDateG(duration, RxFormat.Date_Time));
        }
    });

//
//    //定时改变心率
//    MyTimer timer = new MyTimer(30000, 30000, new MyTimerListener() {
//        @Override
//        public void enterTimer() {
//            int heart = (int) (Math.random() * 100 + 100);
//            RxLogUtils.d("心率数据：" + heart);
//            BleAPI.syncSetting(40, heart, 0x00, new BleChartChangeCallBack() {
//                @Override
//                public void callBack(byte[] data) {
//
//                }
//            });
//        }
//    });

    @Override
    protected void onDestroy() {
        sportsTime.stopTimer();
        super.onDestroy();
    }

    private void showStartSports() {
        tv_state.setText("开始运动");
        tv_connectDevice.setText(BleTools.getInstance().isConnect() ? "已连接" : "未连接");
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
        JSONObject object = new JSONObject();
        try {
            object.put("athlDate", BUNDLE_SPORTS_INFO.getAthlDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.athleticsDetail(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加心率：" + s);
                        HeartRateBean heartRateBean = new Gson().fromJson(s, HeartRateBean.class);
                        mHeartRateView.offileData(heartRateBean);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
