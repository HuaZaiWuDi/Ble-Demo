package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundRelativeLayout;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.OverlapLayoutManager;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.FetchHeatInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/8/2.
 */
public class HeatDetailFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mDateChoose)
    DateChoose mMDateChoose;
    @BindView(R.id.tv_title_Ingestion)
    TextView mTvTitleIngestion;
    @BindView(R.id.tv_Ingestion)
    TextView mTvIngestion;
    @BindView(R.id.pro_heat)
    QMUIProgressBar mProHeat;
    @BindView(R.id.tv_heat_title)
    TextView mTvHeatTitle;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_heatUnit)
    TextView mTvHeatUnit;
    @BindView(R.id.tv_title_consume)
    TextView mTvTitleConsume;
    @BindView(R.id.tv_consume)
    TextView mTvConsume;
    @BindView(R.id.iv_breakfast)
    ImageView mIvBreakfast;
    @BindView(R.id.tv_breakfast)
    TextView mTvBreakfast;
    @BindView(R.id.tv_breakfast_kcal)
    TextView mTvBreakfastKcal;
    @BindView(R.id.layout_breakfast)
    QMUIRoundRelativeLayout mLayoutBreakfast;
    @BindView(R.id.iv_lunch)
    ImageView mIvLunch;
    @BindView(R.id.tv_lunch)
    TextView mTvLunch;
    @BindView(R.id.tv_lunch_kcal)
    TextView mTvLunchKcal;
    @BindView(R.id.layout_lunch)
    QMUIRoundRelativeLayout mLayoutLunch;
    @BindView(R.id.iv_dinner)
    ImageView mIvDinner;
    @BindView(R.id.tv_dinner)
    TextView mTvDinner;
    @BindView(R.id.tv_dinner_kcal)
    TextView mTvDinnerKcal;
    @BindView(R.id.layout_dinner)
    QMUIRoundRelativeLayout mLayoutDinner;
    @BindView(R.id.iv_meal)
    ImageView mIvMeal;
    @BindView(R.id.tv_meal)
    TextView mTvMeal;
    @BindView(R.id.tv_meal_kcal)
    TextView mTvMealKcal;
    @BindView(R.id.layout_meal)
    QMUIRoundRelativeLayout mLayoutMeal;
    Unbinder unbinder;
    @BindView(R.id.recycler_breakfast)
    RecyclerView mRecyclerBreakfast;
    @BindView(R.id.recycler_lunch)
    RecyclerView mRecyclerLunch;
    @BindView(R.id.recycler_dinner)
    RecyclerView mRecyclerDinner;
    @BindView(R.id.recycler_meal)
    RecyclerView mRecyclerMeal;

    public static QMUIFragment getInstance() {
        return new HeatDetailFragment();
    }

    private BaseQuickAdapter adapterBreakfast, adapterLunch, adapterDinner, adapterMeal;

    public static final int TYPE_BREAKFAST = 1;
    public static final int TYPE_LUNCH = 2;
    public static final int TYPE_DINNER = 3;
    public static final int TYPED_MEAL = 5;
    private long currentTime = System.currentTimeMillis();
    private String heatData = "";
    private Typeface typeface;
    Bundle bundle = new Bundle();

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_heat_detail, null);
        unbinder = ButterKnife.bind(this, view);
        initView();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("热量记录");
    }


    private void initData() {
        JsonObject object = new JsonObject();
        object.addProperty("heatDate", currentTime);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchOneDayHeatInfo(body))
                .compose(MyAPP.getRxCache().<String>transformObservable("getAthleticsInfo", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("加载数据：" + s);
                        heatData = s;
                        FetchHeatInfoBean bean = new Gson().fromJson(s, FetchHeatInfoBean.class);
                        refreshData(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void refreshData(FetchHeatInfoBean bean) {
        mTvIngestion.setText(bean.getIntake() + "");
        mTvConsume.setText(bean.getDepletion() + "");
        mTvKcal.setText(Math.abs(bean.getAbleIntake()) + "");
        mTvHeatTitle.setText(bean.getAbleIntake() >= 0 ? R.string.can_eatHeat : R.string.EatMore);
        mProHeat.setProgress((int) (bean.getIntakePercent() * 100) + 1);
        mTvKcal.setTextColor(getResources().getColor(bean.isWarning() ? R.color.orange_FF7200 : R.color.green_61D97F));
        mTvHeatUnit.setTextColor(getResources().getColor(bean.isWarning() ? R.color.orange_FF7200 : R.color.green_61D97F));

        //早餐
        FetchHeatInfoBean.BreakfastBean breakfast = bean.getBreakfast();
        mTvBreakfastKcal.setText(breakfast.getCalorie() + "");
        List<FoodListBean> listBreakfast = breakfast.getFoodList();
        List<Object> imgsBreakfast = food2Img(listBreakfast);
        adapterBreakfast.setNewData(imgsBreakfast);
        //午餐
        FetchHeatInfoBean.LunchBean lunch = bean.getLunch();
        mTvLunchKcal.setText(lunch.getCalorie() + "");
        List<FoodListBean> listLunch = lunch.getFoodList();
        List<Object> imgsLunch = food2Img(listLunch);
        adapterLunch.setNewData(imgsLunch);
        //晚餐
        FetchHeatInfoBean.DinnerBean dinner = bean.getDinner();
        mTvDinnerKcal.setText(dinner.getCalorie() + "");
        List<FoodListBean> listDinner = dinner.getFoodList();
        List<Object> imgsDinner = food2Img(listDinner);
        adapterDinner.setNewData(imgsDinner);
        //加餐
        FetchHeatInfoBean.SnacksBean meal = bean.getSnacks();
        mTvMealKcal.setText(meal.getCalorie() + "");
        List<FoodListBean> listMeal = meal.getFoodList();
        List<Object> imgsMeal = food2Img(listMeal);
        adapterMeal.setNewData(imgsMeal);

    }

    private List<Object> food2Img(List<FoodListBean> listBreakfast) {
        List<Object> imgs = new ArrayList<>();
        for (int i = (listBreakfast.size() > 4 ? 4 : listBreakfast.size()) - 1; i >= 0; i--) {
            imgs.add(listBreakfast.get(i).getFoodImg());
        }
        if (listBreakfast.size() > 4) {
            imgs.set(0, R.mipmap.icon_ellipsis);
        }
        RxLogUtils.d("添加食物图片：" + Arrays.asList(imgs));
        return imgs;
    }

    private void initView() {
        initTypeface();
        initTopBar();
        initRecycler();
        initDataChoose();
    }

    private void initTypeface() {
        typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvIngestion.setTypeface(typeface);
        mTvConsume.setTypeface(typeface);
        mTvKcal.setTypeface(typeface);
        mTvBreakfastKcal.setTypeface(typeface);
        mTvLunchKcal.setTypeface(typeface);
        mTvDinnerKcal.setTypeface(typeface);
        mTvMealKcal.setTypeface(typeface);

    }

    private void initDataChoose() {
        mMDateChoose.setOnDateChangeListener(new DateChoose.OnDateChangeListener() {
            @Override
            public void onDateChangeListener(int year, int month, int day, long millis) {
                currentTime = millis;
                initData();
            }
        });
    }

    private void initRecycler() {
        mRecyclerBreakfast.setLayoutManager(new OverlapLayoutManager(mContext));
        mRecyclerLunch.setLayoutManager(new OverlapLayoutManager(mContext));
        mRecyclerDinner.setLayoutManager(new OverlapLayoutManager(mContext));
        mRecyclerMeal.setLayoutManager(new OverlapLayoutManager(mContext));
        adapterBreakfast = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                QMUIRadiusImageView view = helper.getView(R.id.img_food);
                Glide.with(mActivity).load(item).asBitmap().placeholder(R.mipmap.group15).into(view);
            }
        };
        adapterBreakfast.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QMUIFragment instance = AddedFoodFragment.getInstance();
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_BREAKFAST);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
                instance.setArguments(bundle);
                startFragment(instance);
            }
        });

        adapterLunch = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                QMUIRadiusImageView view = helper.getView(R.id.img_food);
                Glide.with(mActivity).load(item).asBitmap().placeholder(R.mipmap.group15).into(view);
            }
        };
        adapterLunch.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QMUIFragment instance = AddedFoodFragment.getInstance();
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_LUNCH);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
                instance.setArguments(bundle);
                startFragment(instance);
            }
        });

        adapterDinner = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                QMUIRadiusImageView view = helper.getView(R.id.img_food);
                Glide.with(mActivity).load(item).asBitmap().placeholder(R.mipmap.group15).into(view);
            }
        };
        adapterDinner.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QMUIFragment instance = AddedFoodFragment.getInstance();
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_DINNER);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
                instance.setArguments(bundle);
                startFragment(instance);
            }
        });
        adapterMeal = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                QMUIRadiusImageView view = helper.getView(R.id.img_food);
                Glide.with(mActivity).load(item).asBitmap().placeholder(R.mipmap.group15).into(view);
            }
        };
        adapterMeal.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QMUIFragment instance = AddedFoodFragment.getInstance();
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPED_MEAL);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
                instance.setArguments(bundle);
                startFragment(instance);
            }
        });

        mRecyclerBreakfast.setAdapter(adapterBreakfast);
        mRecyclerLunch.setAdapter(adapterLunch);
        mRecyclerDinner.setAdapter(adapterDinner);
        mRecyclerMeal.setAdapter(adapterMeal);
    }

    @OnClick({R.id.recycler_breakfast, R.id.layout_breakfast, R.id.recycler_lunch, R.id.layout_lunch, R.id.recycler_dinner, R.id.layout_dinner, R.id.recycler_meal, R.id.layout_meal})
    public void onViewClicked(View view) {

        QMUIFragment instance = null;
        switch (view.getId()) {
            case R.id.recycler_breakfast://跳转食物详情

                break;
            case R.id.layout_breakfast://跳转添加食物
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_BREAKFAST);
                instance = FoodDetailsFragment.getInstance();
                break;
            case R.id.recycler_lunch:
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_LUNCH);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                instance = AddedFoodFragment.getInstance();
                break;
            case R.id.layout_lunch:
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_LUNCH);
                instance = FoodDetailsFragment.getInstance();
                break;
            case R.id.recycler_dinner:
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_DINNER);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                instance = AddedFoodFragment.getInstance();
                break;
            case R.id.layout_dinner:
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPE_DINNER);
                instance = FoodDetailsFragment.getInstance();
                break;
            case R.id.recycler_meal:
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPED_MEAL);
                bundle.putString(Key.ADD_FOOD_INFO, heatData);
                instance = AddedFoodFragment.getInstance();
                break;
            case R.id.layout_meal:
                bundle.putInt(Key.ADD_FOOD_TYPE, TYPED_MEAL);
                instance = FoodDetailsFragment.getInstance();
                break;
            default:
                break;
        }
        bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
        instance.setArguments(bundle);
        startFragment(instance);
    }

    public static int FOOD_TYPE(int type) {
        switch (type) {
            case TYPE_BREAKFAST:
                return 0;
            case TYPE_LUNCH:
                return 1;
            case TYPE_DINNER:
                return 2;
            case TYPED_MEAL:
                return 3;
        }
        return 0;
    }


}
