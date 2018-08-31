package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.ExpandableItemAdapter;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.Healthy;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.entity.WeightDetailsBean;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel0Bean;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel1Bean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.BodyDataUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/7/27.
 */
public class BodyDataFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    Unbinder unbinder;
    private List<MultiItemEntity> multiItermLists = new ArrayList<>();


    private ExpandableItemAdapter adapter;
    private String gid;
    private int[] bodyImgs = {R.mipmap.man_1_1, R.mipmap.man_1_2, R.mipmap.man_1_3,
            R.mipmap.man_2_1, R.mipmap.man_2_2, R.mipmap.man_2_3,
            R.mipmap.man_3_1, R.mipmap.man_3_2, R.mipmap.man_3_2,};
    private int bodyIndex;
    private String[] bodys;
    public Map<Integer, Healthy> mHealthyMaps = new HashMap<>();

    private BodyDataUtil bodyDataUtil;
    private double[] bodyValue = new double[8];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_body_data);
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.white))
                .setLightStatusBar(true)
                .process();
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        bodys = getResources().getStringArray(R.array.bodyShape);
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvWeight.setTypeface(typeface);
        gid = getIntent().getExtras() == null ? "" : getIntent().getExtras().getString(Key.BUNDLE_WEIGHT_GID);
        initTopBar();
        initRecyclerView();
        initData();

    }

    private void initWeightData(WeightDetailsBean.WeightInfoBean weightInfo) {
        Healthy healthy = new Healthy();
        healthy.setSections(new double[]{11, 21, 26});
        healthy.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy.setSectionLabels(new String[]{"11.0%", "21.0%", "26.0%"});
        healthy.setLabels(new String[]{"偏低", "标准", "偏高", "严重偏高"});
        mHealthyMaps.put(0, healthy);

        Healthy healthy2 = new Healthy();
        healthy2.setSections(new double[]{18.5, 25});
        healthy2.setSectionLabels(new String[]{"18.5%", "25.0%"});
        healthy2.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00")});
        healthy2.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyMaps.put(1, healthy2);

        Healthy healthy3 = new Healthy();
        healthy3.setSections(new double[]{9, 14});
        healthy3.setSectionLabels(new String[]{"9", "14"});
        healthy3.setColors(new int[]{Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy3.setLabels(new String[]{"标准", "偏高", "严重偏高"});
        mHealthyMaps.put(2, healthy3);

        /**
         * 肌肉量
         * 偏低=小于标准体重*67%
         标准=在标准体重*67%-标准体重*80%之间
         充足=大于标准体重*80%
         *
         * */
        UserInfo userInfo = MyAPP.getGson().fromJson(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
        int standardWeight = 0;
        if (userInfo.getSex() == 1) {
            standardWeight = (int) ((userInfo.getHeight() - 80) * 0.7);
        } else if (userInfo.getSex() == 2)
            standardWeight = (int) ((userInfo.getHeight() - 70) * 0.6);
        float value1 = standardWeight * 0.67f;
        float value2 = standardWeight * 0.80f;

        Healthy healthy4 = new Healthy();
        healthy4.setSections(new double[]{value1, value2});
        healthy4.setSectionLabels(new String[]{value1 + "kg", value2 + "kg"});
        healthy4.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
        healthy4.setLabels(new String[]{"偏低", "标准", "充足"});
        mHealthyMaps.put(3, healthy4);

        Healthy healthy5 = new Healthy();
        healthy5.setSections(new double[]{903});
        healthy5.setSectionLabels(new String[]{"903kcal"});
        healthy5.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F")});
        healthy5.setLabels(new String[]{"未达标", "达标"});
        mHealthyMaps.put(4, healthy5);

        Healthy healthy6 = new Healthy();
        healthy6.setSections(new double[]{55, 65});
        healthy6.setSectionLabels(new String[]{"55.0%", "65.0%"});
        healthy6.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
        healthy6.setLabels(new String[]{"偏低", "标准", "充足"});
        mHealthyMaps.put(5, healthy6);

        /**
         * 骨量
         偏低=小于体重*3.7%
         标准=在体重*3.7%-体重*4.2%之间
         偏高=大于体重*4.2%
         * */
        double start = (weightInfo.getWeight() * 0.037f);
        double end = (weightInfo.getWeight() * 0.042f);

        Healthy healthy7 = new Healthy();
        healthy7.setSections(new double[]{start, end});
        healthy7.setSectionLabels(new String[]{RxFormatValue.fromat4S5R(start, 1) + "kg", RxFormatValue.fromat4S5R(end, 1) + "kg"});
        healthy7.setColors(new int[]{Color.parseColor("#5A7BEE"),
                Color.parseColor("#61D97F"), Color.parseColor("#FFBC00")});
        healthy7.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyMaps.put(6, healthy7);

        Healthy healthy8 = new Healthy();
        healthy8.setSections(new double[]{20, 30, 40});
        healthy8.setSectionLabels(new String[]{"20", "30", "40"});
        healthy8.setColors(new int[]{Color.parseColor("#CFED00"), Color.parseColor("#61D97F"),
                Color.parseColor("#17BD4F"), Color.parseColor("#FFBC00")});
        healthy8.setLabels(new String[]{"青少年", "青年", "中年", "中老年"});
        mHealthyMaps.put(7, healthy8);
        bodyDataUtil = new BodyDataUtil(mHealthyMaps);

    }

    private void initData() {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchWeightDetail(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchWeightDetail" + gid, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        WeightDetailsBean detailsBean = MyAPP.getGson().fromJson(s, WeightDetailsBean.class);
                        mTvDate.setText(RxFormat.setFormatDate(detailsBean.getWeightInfo().getMeasureTime(), "yyyy年MM月dd日 HH:mm"));
                        mTvWeight.setText((float) detailsBean.getWeightInfo().getWeight() + "");

                        Drawable drawable = getResources().getDrawable(bodyImgs[(detailsBean.getBodyLevel() - 1) % 9]);
                        //一定要加这行！！！！！！！！！！！
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mTvBodyFat.setCompoundDrawables(null, drawable, null, null);
                        mTvBodyFat.setText(bodys[(detailsBean.getBodyLevel() - 1) % 9]);
                        WeightDetailsBean.WeightInfoBean weightInfo = detailsBean.getWeightInfo();
                        notifyData(weightInfo);
                        bodyIndex = detailsBean.getBodyLevel();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initRecyclerView() {
        adapter = new ExpandableItemAdapter(multiItermLists);
        mMRecyclerView.setAdapter(adapter);
        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }


    private void notifyData(WeightDetailsBean.WeightInfoBean weightInfo) {
        initWeightData(weightInfo);
        String[] titles = getResources().getStringArray(R.array.weightDatas);
        String[] units = {"%", "", "级", "kg", "kcal", "%", "kg", "岁"};
        int[] imgs = {R.mipmap.icon_bodyfat, R.mipmap.icon_bmi, R.mipmap.icon_viscera, R.mipmap.icon_muscle,
                R.mipmap.icon_metabolic_rate, R.mipmap.icon_water, R.mipmap.icon_bone, R.mipmap.icon_body_age};

        if (weightInfo != null) {
            bodyValue[0] = weightInfo.getBodyFat();
            bodyValue[1] = weightInfo.getBmi();
            bodyValue[2] = weightInfo.getVisfat();
            bodyValue[3] = weightInfo.getMuscle();
            bodyValue[4] = weightInfo.getBmr();
            bodyValue[5] = weightInfo.getWater();
            bodyValue[6] = weightInfo.getBone();
            bodyValue[7] = weightInfo.getBodyAge();

            View footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_body_data, null);
            LinearLayout layoutDelete = footerView.findViewById(R.id.layoutDelete);
            layoutDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteWeight();
                }
            });
            adapter.addFooterView(footerView);
        }

        multiItermLists.clear();
        for (int i = 0; i < titles.length; i++) {
            BodyLevel0Bean level0Bean = new BodyLevel0Bean();
            level0Bean.setUnit(units[i]);
            level0Bean.setBodyValue(bodyValue[i]);
            level0Bean.setBodyData(titles[i]);
            level0Bean.setBodyDataImg(imgs[i]);
            level0Bean.setStatus((String) bodyDataUtil.checkStatus(level0Bean.getBodyValue(), i)[0]);
            level0Bean.setStatusColor((int) bodyDataUtil.checkStatus(level0Bean.getBodyValue(), i)[1]);


            BodyLevel1Bean bodyLevel1Bean = new BodyLevel1Bean(bodyDataUtil.transformation(i, (float) bodyValue[i]));
            Healthy healthy = mHealthyMaps.get(i % mHealthyMaps.size());
            bodyLevel1Bean.setSectionLabels(healthy.getSectionLabels());
            bodyLevel1Bean.setLabels(healthy.getLabels());
            bodyLevel1Bean.setColors(healthy.getColors());

            level0Bean.addSubItem(bodyLevel1Bean);
            multiItermLists.add(level0Bean);
        }
        adapter.setNewData(multiItermLists);
//        adapter.expandAll();
    }


    private void deleteWeight() {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.removeWeightInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(new TipDialog(mContext)))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.normal("删除成功");
                        onBackPressed();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("身体数据");
    }

    @OnClick(R.id.tv_bodyFat)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putInt(Key.BUNDLE_BODY_INDEX, bodyIndex);
        RxActivityUtils.skipActivity(mActivity, BodyFatFragment.class, bundle);
    }
}
