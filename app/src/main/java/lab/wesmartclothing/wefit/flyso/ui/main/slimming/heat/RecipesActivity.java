package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DietPlanBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodRecommendBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RecipesActivity extends BaseActivity {


    @BindView(R.id.tv_MealTitle)
    TextView mTvMealTitle;
    @BindView(R.id.tv_MealKcal)
    TextView mTvMealKcal;
    @BindView(R.id.mRecycler_Meal)
    RecyclerView mMRecyclerMeal;
    @BindView(R.id.card_meal)
    CardView mCardMeal;
    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.chooseDate)
    DateChoose mChooseDate;
    @BindView(R.id.tv_totalKcal)
    TextView mTvTotalKcal;
    @BindView(R.id.layout_totalKcal)
    RelativeLayout mLayoutTotalKcal;
    @BindView(R.id.tv_breakfastTitle)
    TextView mTvBreakfastTitle;
    @BindView(R.id.tv_breakfastkcal)
    TextView mTvBreakfastkcal;
    @BindView(R.id.mRecycler_Breakfast)
    RecyclerView mMRecyclerBreakfast;
    @BindView(R.id.tv_lunchTitle)
    TextView mTvLunchTitle;
    @BindView(R.id.tv_lunchKcal)
    TextView mTvLunchKcal;
    @BindView(R.id.mRecycler_Lunch)
    RecyclerView mMRecyclerLunch;
    @BindView(R.id.tv_dinnerTitle)
    TextView mTvDinnerTitle;
    @BindView(R.id.tv_dinnerKcal)
    TextView mTvDinnerKcal;
    @BindView(R.id.mRecycler_Dinner)
    RecyclerView mMRecyclerDinner;
    @BindView(R.id.layout_foodList)
    LinearLayout mLayoutFoodList;
    @BindView(R.id.tv_dietitianName)
    TextView mTvDietitianName;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.card_dietitian_Tip)
    CardView mCardDietitianTip;
    @BindView(R.id.layout_empty)
    LinearLayout mLayoutEmpty;

    private BaseQuickAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, mealAdapter;
    private int totalKcal = 0;


    @Override
    protected int layoutId() {
        return R.layout.activity_recipes_activity;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initRecycler();
        mTvTotalKcal.setTypeface(MyAPP.typeface);
        mChooseDate.setTheme(DateChoose.TYPE_RECIPES);
        mChooseDate.setOnDateChangeListener(new DateChoose.OnDateChangeListener() {
            @Override
            public void onDateChangeListener(int year, int month, int day, long millis) {
                foodRecipes(millis);
            }
        });
    }

    private void initRecycler() {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = MyAPP.getGson().fromJson(string, UserInfo.class);
        mTvEmpty.setText(getString(R.string.empty_recipes, info.getUserName()));
        mTvTip.setText(getString(R.string.DietitianTip, info.getUserName(), "推荐食材"));
        mMRecyclerBreakfast.setLayoutManager(new LinearLayoutManager(mContext));
        mMRecyclerLunch.setLayoutManager(new LinearLayoutManager(mContext));
        mMRecyclerDinner.setLayoutManager(new LinearLayoutManager(mContext));
        mMRecyclerMeal.setLayoutManager(new LinearLayoutManager(mContext));

        breakfastAdapter = createAdapter();
        lunchAdapter = createAdapter();
        dinnerAdapter = createAdapter();
        mealAdapter = createAdapter();

        mMRecyclerBreakfast.setAdapter(breakfastAdapter);
        mMRecyclerLunch.setAdapter(lunchAdapter);
        mMRecyclerDinner.setAdapter(dinnerAdapter);
        mMRecyclerMeal.setAdapter(mealAdapter);

    }


    @Override
    protected void initNetData() {
        super.initNetData();

        foodRecipes(System.currentTimeMillis());

    }

    private void foodRecipes(long foodTime) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(new DietPlanBean(foodTime)));
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchDietPlan(body))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchFoodPlan" +
                        RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date), String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        FoodRecommendBean recommendBean = MyAPP.getGson().fromJson(s, FoodRecommendBean.class);
                        if (recommendBean.isHasFoodPlan()) {
                            breakfastAdapter.setNewData(recommendBean.getFoodPlan().getBreakfastList());
                            lunchAdapter.setNewData(recommendBean.getFoodPlan().getLunchList());
                            dinnerAdapter.setNewData(recommendBean.getFoodPlan().getDinnerList());
                            mealAdapter.setNewData(recommendBean.getFoodPlan().getSnackList());
                            getSectionTotal(recommendBean.getFoodPlan().getBreakfastList(), mTvBreakfastkcal);
                            getSectionTotal(recommendBean.getFoodPlan().getLunchList(), mTvLunchKcal);
                            getSectionTotal(recommendBean.getFoodPlan().getDinnerList(), mTvDinnerKcal);
                            getSectionTotal(recommendBean.getFoodPlan().getSnackList(), mTvMealKcal);

                            RxTextUtils.getBuilder("今日总计：\t\t")
                                    .append(totalKcal + "").setProportion(1.5f)
                                    .append("kcal")
                                    .into(mTvTotalKcal);
                        }
                        mLayoutFoodList.setVisibility(!recommendBean.isHasFoodPlan() ? View.GONE : View.VISIBLE);
                        mLayoutEmpty.setVisibility(!recommendBean.isHasFoodPlan() ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });

    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        mTopBar.setTitle("定制食谱");
    }


    private BaseQuickAdapter createAdapter() {
        return new BaseQuickAdapter<FoodListBean, BaseViewHolder>(R.layout.heat_item) {
            @Override
            protected void convert(BaseViewHolder helper, FoodListBean item) {
                MyAPP.getImageLoader().displayImage(mActivity, item.getFoodImg(), (QMUIRadiusImageView) helper.getView(R.id.img_food));

                helper.setText(R.id.tv_foodName, item.getFoodName());
                RxTextUtils.getBuilder(item.getUnitCalorie() + "")
                        .append("kcal/")
                        .setProportion(0.6f)
                        .append(RxFormat.setFormatNum(item.getUnitCount(), "0.0") + item.getUnit())
                        .setProportion(0.6f)
                        .setForegroundColor(getResources().getColor(R.color.GrayWrite))
                        .into((TextView) helper.getView(R.id.tv_kcal));
                helper.setTypeface(R.id.tv_kcal, MyAPP.typeface);
            }
        };
    }


    private void getSectionTotal(List<FoodListBean> list, TextView tv) {
        int total = 0;
        for (FoodListBean bean : list) {
            total += bean.getUnitCalorie();
        }
        totalKcal += total;
        RxTextUtils.getBuilder(total + "")
                .append("kcal").setProportion(0.5f)
                .into(tv);
    }

}
