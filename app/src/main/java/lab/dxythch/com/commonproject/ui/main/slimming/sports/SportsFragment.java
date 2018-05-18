package lab.dxythch.com.commonproject.ui.main.slimming.sports;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.out.QNScaleData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseFragment;
import lab.dxythch.com.commonproject.entity.SportsListBean;
import lab.dxythch.com.commonproject.entity.WeightInfoItem;
import lab.dxythch.com.commonproject.utils.MyXFormatter;
import lab.dxythch.com.commonproject.view.MyMarkerView;
import lab.dxythch.com.netlib.net.RetrofitService;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;
import me.dkzwm.widget.srl.utils.PixelUtl;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_sports)
public class SportsFragment extends BaseFragment {


    public static SportsFragment getInstance() {
        return new SportsFragment_();
    }

    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    TextView tv_weight_date;
    @ViewById
    SmoothRefreshLayout mRefresh;
    @ViewById
    LineChart mLineChart;

    @Click
    void tv_details() {

    }

    private BaseQuickAdapter adapter;
    private List<QNScaleData> QNDatas = new ArrayList<>();
    private List<WeightInfoItem> weightLists = new ArrayList<>();

    @Override
    public void initData() {
        setData(null, null, null);
        getSportsData();
    }

    @AfterViews
    public void initView() {
        initRecycler();
        initWeightInfo();
        initChart();
        initHRefresh();
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new BaseQuickAdapter<WeightInfoItem, BaseViewHolder>(R.layout.item_sports_body_info) {
            @Override
            protected void convert(BaseViewHolder helper, WeightInfoItem item) {
                helper.setText(R.id.tv_left, item.getData_left());
                helper.setText(R.id.tv_right, item.getData_right());
                helper.setText(R.id.title_left, item.getTitle_left())
                        .setTextColor(R.id.title_left, getResources().getColor(R.color.textColor));
                helper.setText(R.id.title_right, item.getTitle_right())
                        .setTextColor(R.id.title_right, getResources().getColor(R.color.textColor));
                helper.setImageResource(R.id.img_left, item.getImg_left());
                helper.setImageResource(R.id.img_right, item.getImg_right());
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    private void initWeightInfo() {
        QNDatas.clear();
        String[] sports_title = getResources().getStringArray(R.array.sports_title);
        int[] img_icons = {R.mipmap.time_icon, R.mipmap.energy_iocn, R.mipmap.average_icon, R.mipmap.max_icon};

        String[] data = {"--h--min", "--" + getString(R.string.unit_kcal), "--bpm", "--bpm"};

        for (int i = 0; i < sports_title.length; i = i + 2) {
            WeightInfoItem item = new WeightInfoItem();
            item.setTitle_left(sports_title[i]);
            item.setTitle_right(sports_title[i + 1]);
            item.setImg_left(img_icons[i]);
            item.setImg_right(img_icons[i + 1]);
            item.setData_left(data[i]);
            item.setData_right(data[i + 1]);
            weightLists.add(item);
        }
        adapter.setNewData(weightLists);
        tv_weight_date.setText(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date));
    }


    private void getSportsData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", "testuser");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getAthleticsList(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取体重数据：" + s);

                        SportsListBean bean = new Gson().fromJson(s, SportsListBean.class);
                        List<SportsListBean.ListBean> list = bean.getList();
                        if (list.size() > 0) {
                            syncChart(list);
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void syncChart(final List<SportsListBean.ListBean> list) {
        List<String> days = new ArrayList<>();
        synWeightData(list.get(list.size() - 1));

//        for (int i = 0; i < list.size(); i++) {
//            yVals.add(new Entry(i, (float) list.get(i).getWeight()));
//            days.add(RxFormat.setFormatDate(list.get(i).getWeightDate(), "dd"));
//        }
//
//        setData(yVals, days);

        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                RxLogUtils.e("---->", e.getX() + "  " + e.getY());
                synWeightData(list.get((int) e.getX()));
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void synWeightData(SportsListBean.ListBean bean) {

        if (bean == null) return;

        int min = bean.getDuration() / 60;

        weightLists.get(0).setData_left(min / 60 + "h" + min % 60 + "min");
        weightLists.get(0).setData_right(bean.getCalorie() + getString(R.string.unit_kcal));
        weightLists.get(1).setData_left(bean.getAvgHeart() + "bpm");
        weightLists.get(1).setData_right(bean.getMaxHeart() + "bpm");

        adapter.setNewData(weightLists);

        tv_weight_date.setText(RxFormat.setFormatDate(bean.getAthlDate(), RxFormat.Date));
    }


    //初始化图表
    private void initChart() {
        // no description text
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(true);//可以点击
        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);//X，Y轴缩放
        mLineChart.setViewPortOffsets(0, 80, 0, 50);
        mLineChart.getAxisRight().setEnabled(false);
        Legend legend = mLineChart.getLegend();//关闭图例
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setFormSize(10f); //图例大小
        legend.setForm(Legend.LegendForm.LINE); //图例形状
        //设置说明文字的大小和颜色
        legend.setTextSize(12f);
        legend.setTextColor(Color.WHITE);
        legend.setXOffset(30f);

        mLineChart.setAutoScaleMinMaxEnabled(false);
        mLineChart.setNoDataText("");//没有数据时显示

        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv); // Set the marker to the chart

        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();

    }


    private void setData(ArrayList<Entry> heats, ArrayList<Entry> heatRate, List<String> days) {

        heats = new ArrayList<>();
        heatRate = new ArrayList<>();
        days = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float random = (float) Math.random();
            heats.add(new Entry(i, random * 100 + 20));
            heatRate.add(new Entry(i, (1 - random) * 100 + 20));
            days.add("05/0" + i);
        }

        XAxis x = mLineChart.getXAxis();
        x.setLabelCount(7, false);
        x.setTextColor(Color.WHITE);
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setGridColor(getResources().getColor(R.color.lineColor));
        x.setAxisLineColor(Color.WHITE);
        x.setDrawAxisLine(true);
        x.setDrawLabels(true);
        x.setValueFormatter(new MyXFormatter(days));

        YAxis y = mLineChart.getAxisLeft();
        y.setDrawLimitLinesBehindData(true);
        y.setLabelCount(13, false);
        y.setTextColor(Color.WHITE);
        y.setDrawGridLines(true);
        y.setGridColor(getResources().getColor(R.color.lineColor));
        y.setAxisLineColor(Color.WHITE);
//        y.setGranularity(2f);// //设置最小间隔，防止当放大时出现重复标签
        y.setDrawAxisLine(false);
        y.setDrawLabels(false);
        y.setAxisMaximum(150f);
        y.setAxisMinimum(20f);
//        y.setSpaceTop(100f);
//        y.setSpaceBottom(100f);

        //热量
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(heats, getString(R.string.sportsTime));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setLineWidth(3f);
        set1.setDrawCircles(false);//是否显示节点圆心
        set1.setColor(Color.WHITE);
        set1.setDrawVerticalHighlightIndicator(false);
        set1.setDrawHorizontalHighlightIndicator(false);


        //心率
        LineDataSet set2 = new LineDataSet(heatRate, getString(R.string.heatRate));
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.enableDashedLine(20f, 10f, 20f); //设置线条为虚线 1.线条宽度2.间隔宽度3.角度
        set2.setLineWidth(3f);
        set2.setColor(Color.WHITE);
        set2.setDrawCircles(false);//是否显示节点圆心
        set2.setDrawVerticalHighlightIndicator(false);
        set2.setDrawHorizontalHighlightIndicator(false);
        set2.setHighlightEnabled(true);


        // create a data object with the datasets
        LineData data = new LineData(set1, set2);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        // set data
        mLineChart.setData(data);
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();

        // do not forget to refresh the chart
        mLineChart.animateX(500);

//        mLineChart.centerViewToY(50f, YAxis.AxisDependency.LEFT);

        mLineChart.moveViewToX(heats.size() - 1);
        mLineChart.setVisibleXRangeMaximum(7);

    }

    private void initHRefresh() {
        MaterialHeader header = new MaterialHeader(mActivity);
        header.setColorSchemeColors(new int[]{Color.parseColor("#00C5CC"), Color.parseColor("#62DFBA")});
        header.setPadding(PixelUtl.dp2px(mActivity, 25), 0, PixelUtl.dp2px(mActivity, 25), 0);
        mRefresh.setHeaderView(header);

        mRefresh.setDisableRefresh(true);
        mRefresh.setDisableLoadMore(true);
        mRefresh.setEnableOverScroll(false);
        mRefresh.setNestedScrollingEnabled(false);
        mRefresh.setEnablePinContentView(true);
        mRefresh.setEnablePinRefreshViewWhileLoading(true);

        mRefresh.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                mRefresh.refreshComplete();
            }
        });

    }

}
