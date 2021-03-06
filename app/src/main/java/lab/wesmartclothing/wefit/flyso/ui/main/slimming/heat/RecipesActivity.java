package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.os.Bundle;
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
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.dateUtils.RxTimeUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.SwitchView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.base.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.entity.DietPlanBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodRecommendBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.TextSpeakUtils;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;

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
    @BindView(R.id.tv_changedDiet)
    RxTextView mTvChangedDiet;
    @BindView(R.id.sw_speak)
    SwitchView mSwSpeak;

    private BaseQuickAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, mealAdapter;
    private int totalKcal = 0;
    private UserInfo info;
    private boolean speechFlag = false;
    private long currentTime = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_recipes_activity;
    }

    @Override
    protected void initViews() {
        super.initViews();

        initTopBar();
        initSwitch();
        initRecycler();
        mTvTotalKcal.setTypeface(MyAPP.typeface);
        mChooseDate.setTheme(DateChoose.TYPE_RECIPES);
        mChooseDate.setOnDateChangeListener((year, month, day, millis) -> {
            foodRecipes(millis);
            if (RxTimeUtils.isToday(millis)) {
                mTvChangedDiet.setVisibility(View.VISIBLE);
            } else {
                mTvChangedDiet.setVisibility(View.GONE);
            }
        });
    }


    private void initSwitch() {
        mSwSpeak.setOpened(SPUtils.getBoolean(SPKey.SP_VoiceTip, true));

        mSwSpeak.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView switchView) {
                mSwSpeak.setOpened(true);
                SPUtils.put(SPKey.SP_VoiceTip, true);
            }

            @Override
            public void toggleToOff(SwitchView switchView) {
                mSwSpeak.setOpened(false);
                SPUtils.put(SPKey.SP_VoiceTip, false);
                TextSpeakUtils.stop();
            }
        });
    }

    private void initRecycler() {
        info = MyAPP.getgUserInfo();
        mTvEmpty.setText(getString(R.string.empty_recipes, info.getUserName()));

        mTvDietitianName.setText("营养师 " + SPUtils.getString(SPKey.SP_DIET_PLAN_USER, ""));
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
        currentTime = foodTime;
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .fetchDietPlan(NetManager.fetchRequest(JSON.toJSONString(new DietPlanBean(foodTime)))))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("fetchFoodPlan" +
                                RxFormat.setFormatDate(foodTime, RxFormat.Date), String.class,
                        RxTimeUtils.isToday(foodTime) ? CacheStrategy.firstRemote() : CacheStrategy.firstCache()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        updateUI(s);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    private void updateUI(String json) {
        FoodRecommendBean recommendBean = JSON.parseObject(json, FoodRecommendBean.class);
        if (recommendBean.isHasFoodPlan()) {
            totalKcal = 0;
            breakfastAdapter.setNewData(recommendBean.getFoodPlan().getBreakfastList());
            lunchAdapter.setNewData(recommendBean.getFoodPlan().getLunchList());
            dinnerAdapter.setNewData(recommendBean.getFoodPlan().getDinnerList());
            mealAdapter.setNewData(recommendBean.getFoodPlan().getSnackList());
            getSectionTotal(recommendBean.getFoodPlan().getBreakfastList(), mTvBreakfastkcal);
            getSectionTotal(recommendBean.getFoodPlan().getLunchList(), mTvLunchKcal);
            getSectionTotal(recommendBean.getFoodPlan().getDinnerList(), mTvDinnerKcal);
            getSectionTotal(recommendBean.getFoodPlan().getSnackList(), mTvMealKcal);

            RxTextUtils.getBuilder(getString(R.string.todayTotal))
                    .append(totalKcal + "").setProportion(1.5f)
                    .append("kcal")
                    .into(mTvTotalKcal);
            mTvTip.setText(recommendBean.getPlanAdvice());
            //只播放今天的
            if (!speechFlag && RxTimeUtils.isToday(currentTime) && mSwSpeak.isOpened()) {
                speechFlag = true;
                TextSpeakUtils.speakAdd(getString(R.string.speech_recipes, SPUtils.getString(SPKey.SP_DIET_PLAN_USER, ""),
                        recommendBean.getPlanAdvice()
                ));
            }
        }

        mLayoutFoodList.setVisibility(!recommendBean.isHasFoodPlan() ? View.GONE : View.VISIBLE);
        mLayoutEmpty.setVisibility(!recommendBean.isHasFoodPlan() ? View.VISIBLE : View.GONE);
    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(v -> onBackPressed());
        mTopBar.setTitle(R.string.customizedRecipes);
        mTopBar.addRightImageButton(R.mipmap.ic_shipu, R.id.iv_right)
                .setOnClickListener(view -> {
                    //TODO 跳转
                    WebTitleActivity.startWebActivity(mActivity, "", ServiceAPI.RECIPES_URL);
                });
    }


    private BaseQuickAdapter createAdapter() {
        return new BaseQuickAdapter<FoodListBean, BaseViewHolder>(R.layout.heat_item) {
            @Override
            protected void convert(BaseViewHolder helper, FoodListBean item) {
                MyAPP.getImageLoader().displayImage(mActivity, item.getFoodImg(), helper.getView(R.id.img_food));

                helper.setText(R.id.tv_foodName, item.getFoodName());
                RxTextUtils.getBuilder(item.getCalorie() + "")
                        .append("kcal/")
                        .setProportion(0.6f)
                        .append(RxFormat.setFormatNum(item.getFoodCount(), "0.0") + item.getUnit())
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
            total += bean.getCalorie();
        }
        totalKcal += total;
        RxTextUtils.getBuilder(total + "")
                .append("kcal").setProportion(0.5f)
                .into(tv);
    }


    @Override
    protected void onDestroy() {
        TextSpeakUtils.stop();
        super.onDestroy();
    }

    @OnClick(R.id.tv_changedDiet)
    public void onViewClicked() {
        changedDiet();
    }

    private void changedDiet() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pushDate", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .changeDietPlan(NetManager.fetchRequest(jsonObject.toString())))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("fetchFoodPlan" +
                                RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date), String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        updateUI(s);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
