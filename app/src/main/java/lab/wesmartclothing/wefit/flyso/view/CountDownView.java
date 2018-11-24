package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.vondear.rxtools.dateUtils.RxTimeUtils;
import com.vondear.rxtools.utils.RxConstUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.ticker.RxTickerUtils;
import com.vondear.rxtools.view.ticker.RxTickerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;

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

        defaultState();
        long day = RxTimeUtils.getIntervalByNow(time, RxConstUtils.TimeUnit.DAY);
        mTickerDays.setText(day + "", true);

        if (System.currentTimeMillis() >= time) {
            return;
        }

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
                defaultState();
                RxLogUtils.d("结束");


                if (mCountDownFinishCallBack != null) {
                    mCountDownFinishCallBack.finish();
                }
            }
        }.start();
    }

    private void defaultState() {
        mTickerDays.setText("0", false);
        mTickerHour.setText("0", false);
        mTickerMin.setText("0", false);
        mTickerSec.setText("0", false);
    }

    public interface CountDownFinishCallBack {
        void finish();
    }

    private CountDownFinishCallBack mCountDownFinishCallBack;

    public void setCountDownFinishCallBack(CountDownFinishCallBack countDownFinishCallBack) {
        mCountDownFinishCallBack = countDownFinishCallBack;
    }
}
