package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxTimeUtils;
import com.vondear.rxtools.utils.RxConstUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.ticker.RxTickerUtils;
import com.vondear.rxtools.view.ticker.RxTickerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.TargetDetailsFragment;

/**
 * @Package lab.wesmartclothing.wefit.flyso.view
 * @FileName CountDownView
 * @Date 2018/10/25 18:18
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class CountDownView extends LinearLayout {

    private static final char[] NUMBER_LIST = RxTickerUtils.getDefaultNumberList();

    @BindView(R.id.ticker_days)
    RxTickerView mTickerDays;
    @BindView(R.id.ticker_hour)
    RxTickerView mTickerHour;
    @BindView(R.id.ticker_min)
    RxTickerView mTickerMin;
    @BindView(R.id.ticker_sec)
    RxTickerView mTickerSec;
    private Context mContext;

    long time2 = System.currentTimeMillis();
    private CountDownTimer countDownTimer;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_count_down, this, true);
        ButterKnife.bind(this, inflate);
        mTickerDays.setCharacterList(NUMBER_LIST);
        mTickerHour.setCharacterList(NUMBER_LIST);
        mTickerMin.setCharacterList(NUMBER_LIST);
        mTickerSec.setCharacterList(NUMBER_LIST);
        mTickerDays.setTypeface(MyAPP.typeface);
        mTickerHour.setTypeface(MyAPP.typeface);
        mTickerMin.setTypeface(MyAPP.typeface);
        mTickerSec.setTypeface(MyAPP.typeface);

//        setCountDownDays(time);
    }


    public void setCountDownDays(final long time) {
        long day = RxTimeUtils.getIntervalByNow(time, RxConstUtils.TimeUnit.DAY);
        mTickerDays.setText(day + "", true);
        mTickerHour.setText("0", true);
        mTickerMin.setText("0", true);
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long day = RxTimeUtils.getIntervalTime(millisUntilFinished, time2, RxConstUtils.TimeUnit.DAY);
                long hour = RxTimeUtils.getIntervalTime(millisUntilFinished, time2, RxConstUtils.TimeUnit.HOUR) % 24;
                long min = RxTimeUtils.getIntervalTime(millisUntilFinished, time2, RxConstUtils.TimeUnit.MIN) % 60;
                long sec = RxTimeUtils.getIntervalTime(millisUntilFinished, time2, RxConstUtils.TimeUnit.SEC) % 60;

                if (!String.valueOf(day).equals(mTickerDays.getText())) {
                    mTickerDays.setText(day + "", true);
                }
                if (!String.valueOf(hour).equals(mTickerHour.getText())) {
                    mTickerHour.setText(hour + "", true);
                }
                if (!String.valueOf(min).equals(mTickerMin.getText())) {
                    mTickerMin.setText(min + "", true);
                }
                mTickerSec.setText(sec + "", true);


                if (System.currentTimeMillis() >= time) {
                    onFinish();
                }

            }

            @Override
            public void onFinish() {
                cancel();
                mTickerSec.setText("0", true);
                RxLogUtils.d("结束");
                float realWeight = SPUtils.getFloat(SPKey.SP_realWeight);
                UserInfo info = MyAPP.getGson().fromJson(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
                boolean isComplete = realWeight <= info.getTargetWeight();

                //目标已完成
                new RxDialogSure(mContext)
                        .setTitle("提示")
                        .setContent(isComplete ? "恭喜您的瘦身目标已达成" : "很遗憾，您的目标未完成")
                        .setSure(isComplete ? "开启新的目标" : "重置目标")
                        .setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RxActivityUtils.skipActivity(mContext, TargetDetailsFragment.class);
                            }
                        }).show();

            }
        }.start();

    }

}
