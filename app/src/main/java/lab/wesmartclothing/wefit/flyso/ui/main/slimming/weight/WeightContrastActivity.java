package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.dateUtils.RxTimeUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HealthyInfoBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.BarView;
import lab.wesmartclothing.wefit.flyso.view.LineView;
import lab.wesmartclothing.wefit.flyso.view.picker.CustomDatePicker;

public class WeightContrastActivity extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_startDate)
    RxTextView mTvStartDate;
    @BindView(R.id.tv_to)
    TextView mTvTo;
    @BindView(R.id.tv_endDate)
    RxTextView mTvEndDate;
    @BindView(R.id.tv_diffWeight)
    TextView mTvDiffWeight;
    @BindView(R.id.tv_startWeight)
    TextView mTvStartWeight;
    @BindView(R.id.layout_startWeight)
    LinearLayout mLayoutStartWeight;
    @BindView(R.id.tv_endWeight)
    TextView mTvEndWeight;
    @BindView(R.id.layout_endWeight)
    LinearLayout mLayoutEndWeight;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_maxAndMin)
    TextView mTvMaxAndMin;
    @BindView(R.id.img_line)
    RxImageView mImgLine;
    @BindView(R.id.img_bar)
    RxImageView mImgBar;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.tv_weightDiffDec)
    TextView mTvWeightDiffDec;
    @BindView(R.id.lineView)
    LineView mLineView;
    @BindView(R.id.barView)
    BarView mBarView;
    @BindView(R.id.HScrollView)
    HorizontalScrollView mHScrollView;
    @BindView(R.id.tv_noData)
    TextView mTvNoData;
    @BindView(R.id.tv_yLabel_1)
    TextView mTvYLabel1;
    @BindView(R.id.tv_yLabel_2)
    TextView mTvYLabel2;
    @BindView(R.id.tv_yLabel_3)
    TextView mTvYLabel3;
    @BindView(R.id.tv_yLabel_4)
    TextView mTvYLabel4;
    @BindView(R.id.tv_yLabel_5)
    TextView mTvYLabel5;
    @BindView(R.id.tv_yLabel_6)
    TextView mTvYLabel6;


    private Long startDate, endDate;
    private boolean isLine = true;
    ArrayList<Float> valueEntrys = new ArrayList<>();
    ArrayList<String> dateEntrys = new ArrayList<>();
    private List<HealthyInfoBean> mInfoBeans;
    private String unit = "%";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_weight_contrast;
    }

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.green_61D97F);
    }

    @Override
    protected void initViews() {
        super.initViews();

        Calendar instance = Calendar.getInstance();
        endDate = instance.getTimeInMillis();
        instance.set(Calendar.DAY_OF_MONTH, -30);
        startDate = instance.getTimeInMillis();

        mTvStartDate.setText(RxFormat.setFormatDate(startDate, RxFormat.Date));
        mTvEndDate.setText(RxFormat.setFormatDate(endDate, RxFormat.Date));

        initTitle();
        initTabLayout();
        initTextUtils(0, 0);

        mImgLine.setEnabled(false);
    }


    private void initTitle() {
        mQMUIAppBarLayout.setTitle("对比");
        mQMUIAppBarLayout.addLeftBackImageButton()
                .setOnClickListener(view -> onBackPressed());
    }


    private void initTextUtils(double startWeight, double endWeight) {
        //增重
        if (startWeight < endWeight) {
            mTvWeightDiffDec.setText("周期内减重失败");
            mTvDiffWeight.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(Math.abs(startWeight - endWeight), 1))
                .append("\tkg")
                .setProportion(0.5f)
                .into(mTvDiffWeight);

        RxTextUtils.getBuilder("起始体重\n")
                .append(RxFormatValue.fromat4S5R(startWeight, 1)).setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(1.3f)
                .append("\tkg").setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(0.5f)
                .into(mTvStartWeight);
        RxTextUtils.getBuilder("最终体重\n")
                .append(RxFormatValue.fromat4S5R(endWeight, 1)).setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(1.3f)
                .append("\tkg").setForegroundColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setProportion(0.5f)
                .into(mTvEndWeight);
    }


    private void initTabLayout() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (RxDataUtils.isEmpty(mInfoBeans))
                    getWeightContrast();
                else {
                    updateUI(mInfoBeans);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String[] tabs = {"体脂率", "BMI", "内脏脂肪等级", "肌肉量", "基础代谢率",
                "水分", "骨量", "身体年龄", "去脂体重", "皮下脂肪率", "骨骼肌率", "蛋白质"};

        for (String s : tabs) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(s);
            mTabLayout.addTab(tab);
        }
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }


    private void showDateDialog(final boolean isStart) {
        Calendar nowCalendar = Calendar.getInstance();
        CustomDatePicker datePicker = new CustomDatePicker(mActivity);
        datePicker.setRangeStart(1940, 01, 01);
        datePicker.setRangeEnd(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH) + 1, nowCalendar.get(Calendar.DAY_OF_MONTH));
        nowCalendar.setTimeInMillis(isStart ? startDate : endDate);
        datePicker.setTextColor(getResources().getColor(R.color.Gray));
        datePicker.setSelectedItem(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH) + 1, nowCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
            RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);

            Date date = RxTimeUtils.string2Date(year + "-" + month + "-" + day, RxFormat.Date);
            if (isStart) {
                if (date.getTime() >= endDate) {
                    RxToast.warning("开始时间不能早于结束时间");
                    return;
                }
                mTvStartDate.setText(year + "-" + month + "-" + day);
                startDate = date.getTime();
            } else {
                if (date.getTime() >= startDate) {
                    RxToast.warning("结束时间不能晚于开始时间");
                    return;
                }
                mTvEndDate.setText(year + "-" + month + "-" + day);
                endDate = date.getTime();
            }
            getWeightContrast();

        });
        datePicker.show();
    }


    private void getWeightContrast() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("startTime", startDate);
            jsonObject.put("endTime", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //当日多次体重详情
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().weightCompare(
                        NetManager.fetchRequest(jsonObject.toString())))
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().transformObservable("weightCompare" + startDate + endDate, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        Type type = new TypeToken<List<HealthyInfoBean>>() {
                        }.getType();

                        mInfoBeans = JSON.parseObject(s, type);
                        updateUI(mInfoBeans);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void updateUI(List<HealthyInfoBean> weightInfoLists) {
        valueEntrys.clear();
        dateEntrys.clear();
        RxLogUtils.d("数据个数：" + weightInfoLists.size());
        if (RxDataUtils.isEmpty(weightInfoLists)) {
            initTextUtils(0, 0);
            mTvNoData.setVisibility(View.VISIBLE);
            return;
        } else {
            mTvNoData.setVisibility(View.GONE);
            initTextUtils(weightInfoLists.get(0).getWeight(),
                    weightInfoLists.get(weightInfoLists.size() - 1).getWeight());
        }

        for (int i = 0; i < weightInfoLists.size(); i++) {
            HealthyInfoBean bean = weightInfoLists.get(i);
            //只添加有效数据
            if ((float) getHealthyValue(bean) != 0) {
                valueEntrys.add((float) RxFormatValue.format4S5R(getHealthyValue(bean), 1));
                dateEntrys.add(RxFormat.setFormatDate(bean.getWeightDate(), "MM/dd"));
            }
        }

        float max = 0, min = 0, startValue = 0, endValue = 0;
        if (valueEntrys.size() > 1) {
            startValue = valueEntrys.get(0);
            endValue = valueEntrys.get(valueEntrys.size() - 1);
            max = Collections.max(valueEntrys);
            min = Collections.min(valueEntrys);
        } else if (valueEntrys.size() == 1) {
            startValue = endValue = max = min = valueEntrys.get(0);
        }


        if (mTabLayout.getSelectedTabPosition() == 2
                || mTabLayout.getSelectedTabPosition() == 4
                || mTabLayout.getSelectedTabPosition() == 7) {

            mTvYLabel1.setText((int) max + "");
            mTvYLabel2.setText((int) (max * 0.8f) + "");
            mTvYLabel3.setText((int) (max * 0.6f) + "");
            mTvYLabel4.setText((int) (max * 0.4f) + "");
            mTvYLabel5.setText((int) (max * 0.2f) + "");
            mTvYLabel6.setText("0");

            mTvMaxAndMin.setText((int) Math.abs(startValue - endValue) + unit);
        } else {
            mTvYLabel1.setText(RxFormatValue.fromat4S5R(max, 1));
            mTvYLabel2.setText(RxFormatValue.fromat4S5R((max * 0.8f), 1));
            mTvYLabel3.setText(RxFormatValue.fromat4S5R((max * 0.6f), 1));
            mTvYLabel4.setText(RxFormatValue.fromat4S5R((max * 0.4f), 1));
            mTvYLabel5.setText(RxFormatValue.fromat4S5R((max * 0.2f), 1));
            mTvYLabel6.setText("0");
            mTvMaxAndMin.setText(RxFormatValue.fromat4S5R(Math.abs(startValue - endValue), 1) + unit);
        }

        Drawable drawable1 = null;
        if (startValue - endValue > 0) {
            drawable1 = ContextCompat.getDrawable(mContext, R.mipmap.ic_contrast_down);
        } else {
            drawable1 = ContextCompat.getDrawable(mContext, R.mipmap.ic_contrast_up);
        }

        mTvMaxAndMin.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, null, null);


        mTvUnit.setText(unit);
        if (isLine) {
            mLineView.setVisibility(View.VISIBLE);
            mBarView.setVisibility(View.GONE);
            mLineView.setBottomTextList(dateEntrys);
            mLineView.setColorArray(new int[]{
                    Color.parseColor("#FF62D981")
            });
            mLineView.setDrawDotLine(true);
            mLineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
            mLineView.setFloatDataList(valueEntrys);
        } else {
            mBarView.setVisibility(View.VISIBLE);
            mLineView.setVisibility(View.GONE);
            mBarView.setBottomTextList(dateEntrys);
            mBarView.setDataList(valueEntrys, max);
        }

        new Handler()
                .postDelayed(() -> {
                    if (mHScrollView != null)
                        mHScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }, 100);

    }


    private double getHealthyValue(HealthyInfoBean weightInfoBean) {
        double value = 0;
        unit = "%";
        switch (mTabLayout.getSelectedTabPosition()) {
            //体脂率
            case 0:
                value = weightInfoBean.getBodyFat();
                break;
            //BMI
            case 1:
                unit = "";
                value = weightInfoBean.getBmi();
                break;
            //内脏脂肪等级
            case 2:
                unit = "";
                value = weightInfoBean.getVisfat();
                break;
            //肌肉量
            case 3:
                unit = "kg";
                value = weightInfoBean.getSinew();
                break;
            //基础代谢率
            case 4:
                unit = "kcal";
                value = weightInfoBean.getBmr();
                break;
            //水分
            case 5:
                value = weightInfoBean.getWater();
                break;
            //骨量
            case 6:
                value = weightInfoBean.getBone();
                unit = "kg";
                break;
            //身体年龄
            case 7:
                unit = "岁";
                value = weightInfoBean.getBodyAge();
                break;
            //去脂体重
            case 8:
                unit = "kg";
                value = weightInfoBean.getBodyFfm();
                break;
            //皮下脂肪率
            case 9:
                value = weightInfoBean.getSubfat();
                break;
            //骨骼肌率
            case 10:
                value = weightInfoBean.getMuscle();
                break;
            //蛋白质
            case 11:
                value = weightInfoBean.getProtein();
                break;
        }
        return value;
    }

    @OnClick({R.id.tv_startDate, R.id.tv_endDate, R.id.img_line, R.id.img_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_startDate:
                showDateDialog(true);
                break;
            case R.id.tv_endDate:
                showDateDialog(false);
                break;
            case R.id.img_line:
                isLine = true;
                mImgLine.setEnabled(false);
                mImgBar.setEnabled(true);
                updateUI(mInfoBeans);
                break;
            case R.id.img_bar:
                isLine = false;
                mImgBar.setEnabled(false);
                mImgLine.setEnabled(true);
                updateUI(mInfoBeans);
                break;
        }
    }
}
