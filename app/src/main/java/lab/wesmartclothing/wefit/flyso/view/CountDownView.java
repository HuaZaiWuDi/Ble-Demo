package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.vondear.rxtools.view.ticker.RxTickerUtils;
import com.vondear.rxtools.view.ticker.RxTickerView;

import java.util.Calendar;

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
    private Calendar mCalendar;

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

        setCountDownDays(22);
    }


    public void setCountDownDays(int days) {
        mTickerDays.setText(days + "", true);
        mCountDownTimer.start();
    }


    CountDownTimer mCountDownTimer = new CountDownTimer(System.currentTimeMillis(), 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mCalendar = Calendar.getInstance();
            int hour = 24 - mCalendar.get(Calendar.HOUR_OF_DAY);
            int min = 60 - mCalendar.get(Calendar.MINUTE);
            int sec = 60 - mCalendar.get(Calendar.SECOND);
            if (!String.valueOf(hour).equals(mTickerHour.getText())) {
                mTickerHour.setText(hour + "", true);
            }
            if (!String.valueOf(min).equals(mTickerMin.getText())) {
                mTickerMin.setText(min + "", true);
            }
            mTickerSec.setText(sec + "", true);
        }

        @Override
        public void onFinish() {

        }
    };


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCountDownTimer != null) mCountDownTimer.cancel();
    }
}
