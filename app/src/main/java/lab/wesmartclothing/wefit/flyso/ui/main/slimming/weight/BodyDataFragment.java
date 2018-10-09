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
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import lab.wesmartclothing.wefit.flyso.rxbus.OpenAddWeight;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
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
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/7/27.
 */
public class BodyDataFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.tv_healthScore)
    TextView mTvHealthScore;
    @BindView(R.id.tv_Details)
    TextView mTvDetails;
    private List<MultiItemEntity> multiItermLists = new ArrayList<>();


    private ExpandableItemAdapter adapter;
    private String gid;
    private int[] bodyImgs = {R.mipmap.man_1_1, R.mipmap.man_1_2, R.mipmap.man_1_3,
            R.mipmap.man_2_1, R.mipmap.man_2_2, R.mipmap.man_2_3,
            R.mipmap.man_3_1, R.mipmap.man_3_2, R.mipmap.man_3_2,};
    private int bodyIndex;
    private String[] bodys;
    private List<Healthy> mHealthyList = new ArrayList<>();

    private BodyDataUtil bodyDataUtil;
    private double[] bodyValue = new double[13];


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
        Typeface typeface = MyAPP.typeface;
        mTvHealthScore.setTypeface(typeface);

        gid = getIntent().getExtras().getString(Key.BUNDLE_WEIGHT_GID);

        initTopBar();
        initRecyclerView();
        initData();
        initRxBus();
    }


    public void initRxBus() {
        RxBus.getInstance().register2(OpenAddWeight.class)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<OpenAddWeight>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<OpenAddWeight>() {
                    @Override
                    protected void _onNext(OpenAddWeight integer) {
                        RxLogUtils.d("显示：WeightRecordFragment");
                        RxActivityUtils.finishActivity();
                    }
                });
    }

    //TODO 个人信息资料标准的限定需要两套，根据性别来判定
    private void initWeightData(WeightDetailsBean.WeightInfoBean weightInfo) {
        mHealthyList.clear();

        //体重
        Healthy healthy0 = new Healthy();
        mHealthyList.add(healthy0);

        //体脂率
        Healthy healthy = new Healthy();
        healthy.setSections(new double[]{11, 21, 26});
        healthy.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy.setSectionLabels(new String[]{"11.0%", "21.0%", "26.0%"});
        healthy.setLabels(new String[]{"偏低", "标准", "偏高", "严重偏高"});
        mHealthyList.add(healthy);
        //BMI
        Healthy healthy2 = new Healthy();
        healthy2.setSections(new double[]{18.5, 25});
        healthy2.setSectionLabels(new String[]{"18.5", "25.0"});
        healthy2.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00")});
        healthy2.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyList.add(healthy2);

        //内脏脂肪等级
        Healthy healthy3 = new Healthy();
        healthy3.setSections(new double[]{9, 14});
        healthy3.setSectionLabels(new String[]{"9", "14"});
        healthy3.setColors(new int[]{Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy3.setLabels(new String[]{"标准", "偏高", "严重偏高"});
        mHealthyList.add(healthy3);

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
        healthy4.setSectionLabels(new String[]{RxFormatValue.fromat4S5R(value1, 1) + "kg", RxFormatValue.fromat4S5R(value2, 1) + "kg"});
        healthy4.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
        healthy4.setLabels(new String[]{"偏低", "标准", "充足"});
        mHealthyList.add(healthy4);

        //基础代谢
        float bmr = SPUtils.getFloat(SPKey.SP_BMR);
        bmr = bmr == 0 ? 903 : bmr;
        Healthy healthy5 = new Healthy();
        healthy5.setSections(new double[]{bmr});
        healthy5.setSectionLabels(new String[]{bmr + "kcal"});
        healthy5.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F")});
        healthy5.setLabels(new String[]{"未达标", "达标"});
        mHealthyList.add(healthy5);

        //水分
        Healthy healthy6 = new Healthy();
        healthy6.setSections(new double[]{55, 65});
        healthy6.setSectionLabels(new String[]{"55.0%", "65.0%"});
        healthy6.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
        healthy6.setLabels(new String[]{"偏低", "标准", "充足"});
        mHealthyList.add(healthy6);

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
        mHealthyList.add(healthy7);

        //皮下脂肪率
        Healthy healthy10 = new Healthy();
        healthy10.setSections(new double[]{18.5, 26.7});
        healthy10.setSectionLabels(new String[]{"18.5%", "26.7%"});
        healthy10.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00")});
        healthy10.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyList.add(healthy10);

        //骨骼肌率
        Healthy healthy11 = new Healthy();
        healthy11.setSections(new double[]{40, 50});
        healthy11.setSectionLabels(new String[]{"40.0%", "50.0%"});
        healthy11.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00")});
        healthy11.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyList.add(healthy11);

        //蛋白质
        Healthy healthy12 = new Healthy();
        healthy12.setSections(new double[]{14.0f, 16.0});
        healthy12.setSectionLabels(new String[]{"14.0%", "16.0%"});
        healthy12.setColors(new int[]{Color.parseColor("#FF7200"), Color.parseColor("#61D97F"),
                Color.parseColor("#17BD4F")});
        healthy12.setLabels(new String[]{"不足", "标准", "优秀"});
        mHealthyList.add(healthy12);

        //身体年龄
        mHealthyList.add(healthy12);

        //去脂体重
        mHealthyList.add(healthy12);

        bodyDataUtil = new BodyDataUtil(mHealthyList);

    }

    private void initData() {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
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
                        mTvHealthScore.setText(detailsBean.getWeightInfo().getHealthScore() + "");
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
        String[] units = {"kg", "%", "", "级", "kg", "kcal", "%", "kg", "%", "%", "%", "岁", "kg"};
        int[] imgs = {R.mipmap.icon_data_weight, R.mipmap.icon_bodyfat, R.mipmap.icon_bmi, R.mipmap.icon_viscera, R.mipmap.icon_muscle,
                R.mipmap.icon_metabolic_rate, R.mipmap.icon_water, R.mipmap.icon_bone, R.mipmap.icon_subcutaneous_fat,
                R.mipmap.icon_muscle_rate, R.mipmap.icon_protein, R.mipmap.icon_body_age, R.mipmap.icon_lean_body_weight};

        if (weightInfo != null) {
            bodyValue[0] = weightInfo.getWeight();
            bodyValue[1] = weightInfo.getBodyFat();
            bodyValue[2] = weightInfo.getBmi();
            bodyValue[3] = weightInfo.getVisfat();
            bodyValue[4] = weightInfo.getSinew();
            bodyValue[5] = weightInfo.getBmr();
            bodyValue[6] = weightInfo.getWater();
            bodyValue[7] = weightInfo.getBone();
            bodyValue[8] = weightInfo.getSubfat();
            bodyValue[9] = weightInfo.getMuscle();
            bodyValue[10] = weightInfo.getProtein();
            bodyValue[11] = weightInfo.getBodyAge();
            bodyValue[12] = weightInfo.getBodyFfm();

            View footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_delete_data, null);
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
            if (i == titles.length - 1 || i == titles.length - 2 || i == 0) {
                level0Bean.setStatus("标准");
                level0Bean.setStatusColor(Color.parseColor("#61D97F"));
                level0Bean.setCanExpanded(false);
            } else {

                BodyLevel1Bean bodyLevel1Bean = new BodyLevel1Bean(bodyDataUtil.transformation(i, level0Bean.getBodyValue()));
                Healthy healthy = mHealthyList.get(i % mHealthyList.size());
                bodyLevel1Bean.setSectionLabels(healthy.getSectionLabels());
                bodyLevel1Bean.setLabels(healthy.getLabels());
                bodyLevel1Bean.setColors(healthy.getColors());
                level0Bean.addSubItem(bodyLevel1Bean);

                level0Bean.setStatus((String) bodyDataUtil.checkStatus(level0Bean.getBodyValue(), i)[0]);
                level0Bean.setStatusColor((int) bodyDataUtil.checkStatus(level0Bean.getBodyValue(), i)[1]);
            }

            multiItermLists.add(level0Bean);
        }
        int index = 1;
        //排列下顺序，有问题的显示在前面，判断标准：状态颜色为 #61D97F、#17BD4F
        for (int i = 0; i < multiItermLists.size(); i++) {
            BodyLevel0Bean entity = (BodyLevel0Bean) multiItermLists.get(i);
            if (entity.getStatusColor() != Color.parseColor("#61D97F")
                    && entity.getStatusColor() != Color.parseColor("#17BD4F")) {
//                entity.setExpanded(true);
                swap(multiItermLists, index, i);
                index++;
            }
        }
        adapter.setNewData(multiItermLists);
    }

    public static void swap(List<?> list, int i, int j) {
        final List l = list;
        l.set(i, l.set(j, l.get(i)));
    }


    private void deleteWeight() {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.removeWeightInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(new TipDialog(mContext)))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
//                        RxToast.normal("删除成功");
                        //刷新数据
                        //刷新数据
                        RxBus.getInstance().post(new RefreshSlimming());
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        RxActivityUtils.skipActivityAndFinish(mContext, WeightRecordFragment_.class);
    }
}
