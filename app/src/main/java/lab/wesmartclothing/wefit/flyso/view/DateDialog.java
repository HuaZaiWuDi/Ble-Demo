package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vondear.rxtools.dateUtils.RxTimeUtils;
import com.vondear.rxtools.recyclerview.picker.PickerLayoutManager;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.dialog.RxDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/8/8.
 */
public class DateDialog extends RxDialog {

    @BindView(R.id.recyclerYear)
    RecyclerView mRecyclerYear;
    @BindView(R.id.recyclerMonth)
    RecyclerView mRecyclerMonth;
    @BindView(R.id.recyclerDay)
    RecyclerView mRecyclerDay;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_sure)
    TextView mTvSure;

    BaseQuickAdapter adapterYear, adapterMoth, adapterDay;
    private String months[] = new String[]{"01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String days[] = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    private String[] years = {};

    private Calendar calendar = Calendar.getInstance();
    private int beginYear, endYear, year, month, day;
    private Context context;

    public DateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public DateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public DateDialog(Context context) {
        super(context);
        this.context = context;
    }

    public DateDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        this.context = context;
    }

    public DateDialog setYear(int beginYear) {
        this.beginYear = beginYear;
        this.endYear = calendar.get(Calendar.YEAR);
        return this;
    }

    public DateDialog setYear(int beginYear, int endYear) {
        this.beginYear = beginYear;
        this.endYear = endYear;
        return this;
    }

    public DateDialog setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    public DateDialog createView() {
        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DIN-Regular.ttf");
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_date, null);
        ButterKnife.bind(this, dialogView);

        mRecyclerYear.setLayoutManager(new PickerLayoutManager(context, mRecyclerYear, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true));
        mRecyclerMonth.setLayoutManager(new PickerLayoutManager(context, mRecyclerMonth, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true));
        mRecyclerDay.setLayoutManager(new PickerLayoutManager(context, mRecyclerDay, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true));
//        adapterDay = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_picker) {
//            @Override
//            protected void convert(BaseViewHolder helper, String item) {
//                helper.setText(R.id.tv_text, item)
//                        .setTypeface(R.id.tv_text, typeface)
//                        .setTextColor(R.id.tv_text, context.getResources().getColor(R.color.Gray));
//            }
//        };
//        adapterMoth = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_picker) {
//            @Override
//            protected void convert(BaseViewHolder helper, String item) {
//                helper.setText(R.id.tv_text, item)
//                        .setTypeface(R.id.tv_text, typeface)
//                        .setTextColor(R.id.tv_text, context.getResources().getColor(R.color.Gray));
//            }
//        };
//        adapterYear = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_picker) {
//            @Override
//            protected void convert(BaseViewHolder helper, String item) {
//                helper.setText(R.id.tv_text, item)
//                        .setTypeface(R.id.tv_text, typeface)
//                        .setTextColor(R.id.tv_text, context.getResources().getColor(R.color.Gray));
//            }
//        };

        years = new String[endYear - beginYear];
        int temp = beginYear;
        for (int i = 0; i < years.length; i++) {
            years[i] = temp + "";
            temp++;
        }
        mRecyclerYear.setAdapter(new MyAdapter(Arrays.asList(years)));
        mRecyclerMonth.setAdapter(new MyAdapter(Arrays.asList(months)));
        mRecyclerDay.setAdapter(new MyAdapter(Arrays.asList(updateDays())));

//        adapterYear.setNewData(Arrays.asList(years));
//        adapterMoth.setNewData(Arrays.asList(months));
//        adapterDay.setNewData(Arrays.asList(updateDays()));
//        adapterYear.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                year = beginYear + position;
//                updateDays();
//                adapterDay.setNewData(Arrays.asList(updateDays()));
//            }
//        });
//        adapterMoth.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                month = position + 1;
//                updateDays();
//                adapterDay.setNewData(Arrays.asList(updateDays()));
//            }
//        });
//        adapterDay.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                day = position + 1;
//                updateDays();
//            }
//        });

        getLayoutParams().gravity = Gravity.BOTTOM;
        setFullScreenWidth();
        setContentView(dialogView);

//        mRecyclerYear.scrollToPosition(year - beginYear + 1);
//        mRecyclerMonth.scrollToPosition(month - 1);
//        mRecyclerDay.scrollToPosition(day - 1);

        return this;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] mColors = {Color.YELLOW, Color.RED};
        private List<String> mList;

        public MyAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvText.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvText;

            public ViewHolder(View itemView) {
                super(itemView);
                tvText = itemView.findViewById(R.id.tv_text);
            }
        }
    }


    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    private String[] updateDays() {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
//        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxDays = RxTimeUtils.getDaysByYearMonth(beginYear + year, month);
        RxLogUtils.e("最大的天数：" + maxDays);

        String[] days = new String[maxDays];
        System.arraycopy(this.days, 0, days, 0, maxDays);
        return days;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                dismiss();
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.chooseDate(year, month, day, calendar.getTimeInMillis());
                }
                break;
        }
    }

    private onDateChooseListener mOnDateChooseListener;

    public void setOnDateChooseListener(onDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }

    public interface onDateChooseListener {
        void chooseDate(int year, int month, int day, long millis);
    }

}
