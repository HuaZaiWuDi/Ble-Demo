package lab.wesmartclothing.wefit.flyso.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DietPlanBean;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created icon_hide_password jk on 2017/7/7.
 */

public class DateChoose extends RelativeLayout {

    public static final int TYPE_RECIPES = 0;
    public static final int TYPE_FOOD_RECORD = 1;

    //自定义注解
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DateChoose.TYPE_RECIPES, DateChoose.TYPE_FOOD_RECORD})
    @interface theme {

    }


    ImageView mTvLast;
    ImageView mTvNext;
    QMUIRoundButton mTvDate;
    AlertDialog.Builder Builder;
    RxTextView reToday;
    private MaterialCalendarView mCalendarView;
    private int month;
    private Calendar calendar = Calendar.getInstance();
    private int year;
    private int day;
    private boolean isToday = true;
    private Context mContext;
    List<CalendarDay> calendarDays = new ArrayList<>();
    private int Theme;

    public DateChoose(Context context) {
        this(context, null);
    }

    public DateChoose(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_date_choose, this, true);
        mContext = context;
        mTvLast = inflate.findViewById(R.id.iv_last);
        mTvNext = inflate.findViewById(R.id.iv_next);
        mTvDate = inflate.findViewById(R.id.btn_date);


        mTvDate.setOnClickListener(new OnClickListener() {
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
                notifyDate();
            }
        });

        mTvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, +1);
                notifyDate();
                isToday = isToday();

                nextIsEnable(isToday);

            }
        });

        notifyDate();
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
        reToday = view.findViewById(R.id.reToday);
        if (isToday) {
            reToday.setEnabled(false);
            reToday.setTextColor(ContextCompat.getColor(mContext, R.color.GrayWrite));
            reToday.getHelper().setBackgroundColorNormal(ContextCompat.getColor(mContext, R.color.white))
                    .setBorderColorNormal(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .setBorderWidthNormal(RxUtils.dp2px(1));
        } else {
            reToday.setEnabled(true);
            reToday.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            reToday.getHelper().setBackgroundColorNormal(ContextCompat.getColor(mContext, Theme == TYPE_FOOD_RECORD ? R.color.orange_FF7200 : R.color.black_312B36));
        }

        mCalendarView = view.findViewById(R.id.mCalendarView);
//        mCalendarView.state().edit().setMaximumDate(System.currentTimeMillis()).commit();
        mCalendarView.setCurrentDate(calendar);
        mCalendarView.setDateSelected(calendar, true);
        mCalendarView.setSelectionColor(Color.parseColor(Theme == TYPE_FOOD_RECORD ? "#FF7200" : "#312C35"));

        mCalendarView.addDecorator(new EventDecorator(Color.parseColor(Theme == TYPE_FOOD_RECORD ? "#FF7200" : "#E4CA9F"), calendarDays));
        mCalendarView.invalidateDecorators();

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (date.getCalendar().getTimeInMillis() > System.currentTimeMillis()) {
                    RxLogUtils.d("超过今天");
                    RxToast.normal("不能选择未来的日期");
                } else {
                    calendar = date.getCalendar();
                    isToday = isToday();
                    nextIsEnable(isToday);
                    notifyDate();
                    show.dismiss();
                }
            }
        });

        fetchPlanDate(calendar.getTimeInMillis());
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                fetchPlanDate(calendarDay.getCalendar().getTimeInMillis());
            }
        });

        reToday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isToday = true;
                nextIsEnable(isToday);
                mCalendarView.setCurrentDate(System.currentTimeMillis());
                calendar.setTimeInMillis(System.currentTimeMillis());
                notifyDate();
                show.dismiss();
            }
        });
    }

    private boolean isToday() {
        String date1 = RxFormat.setFormatDate(calendar.getTimeInMillis(), RxFormat.Date_Month_Day);
        String date2 = RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_Month_Day);

        return date1.equals(date2);
    }


    private void notifyDate() {
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


    public void setTheme(@theme int theme) {
        this.Theme = theme;
    }

    public void setRecipesDates(@NonNull List<Long> calendars) {
        for (Long time : calendars) {
            calendarDays.add(CalendarDay.from(time));
        }
    }


    public void setCalendarMillis(long millis) {
        calendar.setTimeInMillis(millis);
        notifyDate();
        nextIsEnable(isToday);
    }


    public Calendar getCalendar() {
        return calendar;
    }

    private OnDateChangeListener onDateChangeListener;

    public interface OnDateChangeListener {
        void onDateChangeListener(int year, int month, int day, long millis);
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 接口 查询定制计划的日期
    ///////////////////////////////////////////////////////////////////////////
    private void fetchPlanDate(long time) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(new DietPlanBean(time)));
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(Theme == TYPE_FOOD_RECORD ? dxyService.fetchPlanDate(body) : dxyService.fetchDietRecordDate(body))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchPlanDate" + time, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if (mContext == null) return;
                        List<Long> dates = MyAPP.getGson().fromJson(s, new TypeToken<List<Long>>() {
                        }.getType());
                        setRecipesDates(dates);
                        mCalendarView.addDecorator(new EventDecorator(Color.parseColor(Theme == TYPE_FOOD_RECORD ? "#FF7200" : "#E4CA9F"), calendarDays));
                        mCalendarView.invalidateDecorators();
                    }
                });
    }

}
