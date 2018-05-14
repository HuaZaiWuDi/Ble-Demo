package lab.dxythch.com.commonproject.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.dateUtils.RxFormat;

import java.util.Calendar;

import lab.dxythch.com.commonproject.R;


/**
 * Created by jk on 2017/7/7.
 */

public class DateChoose extends RelativeLayout {
    ImageView mTvLast;
    ImageView mTvNext;
    TextView mTvDate;
    LinearLayout mLlDateContainer;
    AlertDialog.Builder Builder;

    private Calendar calendar;
    private int month;
    private int year;
    private int day;
    private boolean isToday;
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
                calendar.add(Calendar.DATE, -1);
                notidyDate();
            }
        });

        mTvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, +1);
                notidyDate();
            }
        });

        calendar = Calendar.getInstance();

        notidyDate();

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showCalendar() {

        Builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_calendar, null);

        Builder.setView(view);

        final AlertDialog show = Builder.show();

        final CalendarView mCalendarView = view.findViewById(R.id.mCalendarView);
        mCalendarView.setDate(calendar.getTimeInMillis(), true, true);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                notidyDate();
                show.dismiss();
            }
        });

        TextView reToday = view.findViewById(R.id.reToday);

        reToday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setDate(System.currentTimeMillis(), true, true);
                calendar.setTimeInMillis(System.currentTimeMillis());
                notidyDate();
                show.dismiss();
            }
        });
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
        Builder.create().dismiss();
    }


    private OnDateChangeListener onDateChangeListener;

    public interface OnDateChangeListener {
        void onDateChangeListener(int year, int month, int day, long millis);
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }
}
