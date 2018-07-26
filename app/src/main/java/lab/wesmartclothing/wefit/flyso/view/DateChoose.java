package lab.wesmartclothing.wefit.flyso.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.vondear.rxtools.dateUtils.RxFormat;

import java.util.Calendar;

import lab.wesmartclothing.wefit.flyso.R;


/**
 * Created icon_hide_password jk on 2017/7/7.
 */

public class DateChoose extends RelativeLayout {
    ImageView mTvLast;
    ImageView mTvNext;
    TextView mTvDate;
    LinearLayout mLlDateContainer;
    AlertDialog.Builder Builder;

    private int month;
    private Calendar calendar;
    private int year;
    private int day;
    private boolean isToday = true;
    private Context mContext;

    public DateChoose(Context context) {
        this(context, null);
    }

    public DateChoose(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_date_choose, this, true);
        mContext = context;
        mTvLast = inflate.findViewById(R.id.iv_last);
        mTvNext = inflate.findViewById(R.id.iv_next);
        mTvDate = inflate.findViewById(R.id.tv_date);
        mLlDateContainer = findViewById(R.id.ll_date_container);


        mLlDateContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        mTvLast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isToday = false;
                nextIsEnable(isToday);
                calendar.add(Calendar.DATE, -1);
                notidyDate();
            }
        });

        mTvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, +1);
                notidyDate();
                isToday = isToday();

                nextIsEnable(isToday);

            }
        });

        calendar = Calendar.getInstance();

        notidyDate();
        nextIsEnable(isToday);
    }


    private void nextIsEnable(boolean isToday) {
        mTvNext.setEnabled(!isToday);
        mTvNext.setAlpha(isToday ? 0.5f : 1f);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showCalendar() {

        Builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_calendar, null);

        Builder.setView(view);

        final AlertDialog show = Builder.show();

        final MaterialCalendarView mCalendarView = view.findViewById(R.id.mCalendarView);
        mCalendarView.state().edit().setMaximumDate(System.currentTimeMillis()).commit();
        mCalendarView.setCurrentDate(calendar);
        mCalendarView.setDateSelected(calendar, true);
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                calendar.set(date.getYear(), date.getMonth(), date.getDay());
                isToday = isToday();
                nextIsEnable(isToday);
                notidyDate();
                show.dismiss();
            }
        });

        TextView reToday = view.findViewById(R.id.reToday);

        reToday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isToday = true;
                nextIsEnable(isToday);
                mCalendarView.setCurrentDate(System.currentTimeMillis());
                calendar.setTimeInMillis(System.currentTimeMillis());
                notidyDate();
                show.dismiss();
            }
        });
    }

    private boolean isToday() {
        String date1 = RxFormat.setFormatDate(calendar.getTimeInMillis(), RxFormat.Date_Month_Day);
        String date2 = RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_Month_Day);

        return date1.equals(date2);
    }


    private void notidyDate() {
        String date = RxFormat.setFormatDate(calendar.getTimeInMillis(), RxFormat.Date_Month_Day);
        mTvDate.setText(date);

        if (onDateChangeListener != null) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            onDateChangeListener.onDateChangeListener(year, month, day, calendar.getTimeInMillis());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    private OnDateChangeListener onDateChangeListener;

    public interface OnDateChangeListener {
        void onDateChangeListener(int year, int month, int day, long millis);
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }
}
