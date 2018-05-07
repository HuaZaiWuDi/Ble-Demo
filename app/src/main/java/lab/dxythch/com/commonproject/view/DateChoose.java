package lab.dxythch.com.commonproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.dateUtils.RxFormat;

import lab.dxythch.com.commonproject.R;


/**
 * Created by jk on 2017/7/7.
 */

public class DateChoose extends RelativeLayout {
    ImageView mTvLast;
    ImageView mTvNext;
    TextView mTvDate;
    LinearLayout mLlDateContainer;


    private int month;
    private int year;
    private boolean showYearOnly;
    private Context mContext;

    public DateChoose(Context context) {
        this(context, null);
    }

    public DateChoose(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_date_choose, this, true);
//        ButterKnife.bind(inflate, this);
        mContext = context;
        mTvLast = findViewById(R.id.iv_last);
        mTvNext = findViewById(R.id.iv_next);
        mTvDate = findViewById(R.id.tv_date);
        mLlDateContainer = findViewById(R.id.ll_date_container);

        initDate();

    }


    private void initDate() {
        String date = RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date);
        String[] split = date.split("-");
        year = Integer.valueOf(split[0]);
        month = Integer.valueOf(split[1]);

    }

    /**
     * 设置初始化日期
     */
    public void setInitDate(int year, int month) {
        this.year = year;
        this.month = month;
        setDate();
    }

    public void setInitDate(String year, String month) {
        this.year = Integer.valueOf(year);
        this.month = Integer.valueOf(month);
        if (this.month < 10) {
            String m = "";
            m = "0" + this.month;

            mTvDate.setText(showYearOnly ? year + mContext.getString(R.string.Year) : year + mContext.getString(R.string.Year) + m + mContext.getString(R.string.Month));
        } else {
            mTvDate.setText(showYearOnly ? year + mContext.getString(R.string.Year) : year + mContext.getString(R.string.Year) + this.month + mContext.getString(R.string.Month));
        }
    }

    /**
     * 只显示年
     *
     * @param showYearOnly
     */
    public void showYearOnly(boolean showYearOnly) {
        this.showYearOnly = showYearOnly;
        if (month < 10) {
            String m = "";
            m = "0" + month;

            mTvDate.setText(showYearOnly ? year + mContext.getString(R.string.Year) : year + mContext.getString(R.string.Year) + m + mContext.getString(R.string.Month));
        } else {
            mTvDate.setText(showYearOnly ? year + mContext.getString(R.string.Year) : year + mContext.getString(R.string.Year) + month + mContext.getString(R.string.Month));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 点击上一个月
             */
            case R.id.iv_last:
                if (showYearOnly) {
                    year -= 1;
                } else {
                    month -= 1;
                }

                if (month == 0) {
                    month = 12;
                    year -= 1;
                }
                setDate();
//                onDateChangeListener.onDateChangeListener(year,month);

//                onDateChangeListener.onDateChangeListener(year,month);
                break;
            /**
             * 点击下一个月
             */
            case R.id.iv_next:

                if (showYearOnly) {
                    year += 1;
                } else {
                    month += 1;
                }

                if (month == 13) {
                    month = 1;
                    year += 1;
                }
                setDate();
//                onDateChangeListener.onDateChangeListener(year,month);
                break;

            case R.id.ll_date_container:

                break;

        }

    }


    private void setDate() {
        if (month < 10) {
            String m = "";
            m = "0" + month;

            mTvDate.setText(showYearOnly ? year + mContext.getString(R.string.Year) : year + mContext.getString(R.string.Year) + m + mContext.getString(R.string.Month));
        } else {
            mTvDate.setText(showYearOnly ? year + mContext.getString(R.string.Year) : year + mContext.getString(R.string.Year) + month + mContext.getString(R.string.Month));
        }
        if (onDateChangeListener != null) {
            onDateChangeListener.onDateChangeListener(year, month);
        }
    }

    private OnDateChangeListener onDateChangeListener;

    public interface OnDateChangeListener {
        void onDateChangeListener(int year, int month);
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
//        setDate();
    }
}
