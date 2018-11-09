package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.FetchHeatInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/8/23.
 */
public class FoodRecommend extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.chooseDate)
    DateChoose mChooseDate;
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
    @BindView(R.id.tv_MealTitle)
    TextView mTvMealTitle;
    @BindView(R.id.tv_MealKcal)
    TextView mTvMealKcal;
    @BindView(R.id.mRecycler_Meal)
    RecyclerView mMRecyclerMeal;
    @BindView(R.id.layout_foodList)
    LinearLayout mLayoutFoodList;
    @BindView(R.id.layout_empty)
    LinearLayout mLayoutEmpty;
    @BindView(R.id.layout_breakfast)
    RxLinearLayout mLayoutBreakfast;
    @BindView(R.id.layout_lunch)
    RxLinearLayout mLayoutLunch;
    @BindView(R.id.layout_dinner)
    RxLinearLayout mLayoutDinner;
    @BindView(R.id.layout_meal)
    RxLinearLayout mLayoutMeal;
    @BindView(R.id.layout_addFood)
    LinearLayout mLayoutAddFood;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    private long currentTime = System.currentTimeMillis();
    private BaseQuickAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, mealAdapter;

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_food_recommend;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initRecycler();
        mChooseDate.setTheme(DateChoose.TYPE_FOOD_RECORD);
        mChooseDate.setOnDateChangeListener(new DateChoose.OnDateChangeListener() {
            @Override
            public void onDateChangeListener(int year, int month, int day, long millis) {
                foodRecord(millis);
            }
        });
    }

    private void initRecycler() {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        UserInfo info = MyAPP.getGson().fromJson(string, UserInfo.class);
        mTvEmpty.setText(getString(R.string.empty_record, info.getUserName()));
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
        foodRecord(currentTime);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (bundle != null) {
            currentTime = bundle.getLong(Key.BUNDLE_DATE_TIME, System.currentTimeMillis());
            mChooseDate.setCalendarMillis(currentTime);
        }
    }

    private void foodRecord(long currentTime) {
        JsonObject object = new JsonObject();
        object.addProperty("heatDate", currentTime);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchOneDayHeatInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(MyAPP.getRxCache().<String>transformObservable("getAthleticsInfo" + currentTime, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        FetchHeatInfoBean bean = MyAPP.getGson().fromJson(s, FetchHeatInfoBean.class);
                        updateUI(bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void updateUI(FetchHeatInfoBean bean) {
        boolean isEmtpy = bean.getBreakfast().getFoodList().isEmpty()
                && bean.getLunch().getFoodList().isEmpty()
                && bean.getDinner().getFoodList().isEmpty()
                && bean.getSnacks().getFoodList().isEmpty();

        mLayoutFoodList.setVisibility(isEmtpy ? View.GONE : View.VISIBLE);
        mLayoutEmpty.setVisibility(isEmtpy ? View.VISIBLE : View.GONE);

        breakfastAdapter.setNewData(bean.getBreakfast().getFoodList());
        lunchAdapter.setNewData(bean.getLunch().getFoodList());
        dinnerAdapter.setNewData(bean.getDinner().getFoodList());
        mealAdapter.setNewData(bean.getSnacks().getFoodList());

        RxTextUtils.getBuilder(bean.getBreakfast().getCalorie() + "")
                .append("kcal").setProportion(0.5f)
                .into(mTvBreakfastkcal);
        RxTextUtils.getBuilder(bean.getLunch().getCalorie() + "")
                .append("kcal").setProportion(0.5f)
                .into(mTvLunchKcal);
        RxTextUtils.getBuilder(bean.getDinner().getCalorie() + "")
                .append("kcal").setProportion(0.5f)
                .into(mTvDinnerKcal);
        RxTextUtils.getBuilder(bean.getSnacks().getCalorie() + "")
                .append("kcal").setProportion(0.5f)
                .into(mTvMealKcal);

    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTopBar.setTitle("饮食记录");
    }

    @OnClick({R.id.layout_breakfast, R.id.layout_lunch, R.id.layout_dinner, R.id.layout_meal})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.layout_breakfast:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_BREAKFAST);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.layout_lunch:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_LUNCH);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.layout_dinner:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPE_DINNER);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
            case R.id.layout_meal:
                bundle.putInt(Key.ADD_FOOD_TYPE, HeatDetailFragment.TYPED_MEAL);
                RxActivityUtils.skipActivity(mActivity, FoodDetailsFragment.class, bundle);
                break;
        }
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

}
