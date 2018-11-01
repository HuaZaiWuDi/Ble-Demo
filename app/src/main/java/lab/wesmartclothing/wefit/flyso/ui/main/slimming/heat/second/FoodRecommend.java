package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.section.HeatFoodSection;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;

/**
 * Created by jk on 2018/8/23.
 */
public class FoodRecommend extends BaseActivity {


    Unbinder unbinder;

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
    CardView mLayoutBreakfast;
    @BindView(R.id.layout_lunch)
    CardView mLayoutLunch;
    @BindView(R.id.layout_dinner)
    CardView mLayoutDinner;
    @BindView(R.id.layout_meal)
    CardView mLayoutMeal;
    @BindView(R.id.layout_addFood)
    LinearLayout mLayoutAddFood;


    private BaseSectionQuickAdapter sectionQuickAdapter;
    private List<HeatFoodSection> mBeans = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_recommend);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {

        initTopBar();
//        initRecycler();
        initData();
    }

    private void initData() {
//        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
//        RxManager.getInstance().doNetSubscribe(dxyService.fetchFoodPlan())
//                .compose(RxComposeUtils.<String>showDialog(tipDialog))
//                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
//                .compose(MyAPP.getRxCache().<String>transformObservable("fetchFoodPlan" +
//                        RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date), String.class, CacheStrategy.firstRemote()))
//                .map(new CacheResult.MapFunc<String>())
//                .subscribe(new RxNetSubscriber<String>() {
//                    @Override
//                    protected void _onNext(String s) {
//                        notifyData(s);
//                    }
//
//                    @Override
//                    protected void _onError(String error) {
//                        RxToast.error(error);
//                    }
//                });
    }

    private void notifyData(String s) {
//        FoodRecommendBean recommendBean = MyAPP.getGson().fromJson(s, FoodRecommendBean.class);
//        List<FoodListBean> breakfastList = recommendBean.getBreakfastList();
//        List<FoodListBean> lunchList = recommendBean.getLunchList();
//        List<FoodListBean> dinnerList = recommendBean.getDinnerList();
//        mBeans.clear();
//        int total = 0;
//        int breakFast = getSectionTotal(breakfastList);
//        int lunch = getSectionTotal(lunchList);
//        int dinner = getSectionTotal(dinnerList);
//        total = breakFast + lunch + dinner;
//
//        mBeans.add(new HeatFoodSection(true, getString(R.string.breakfast), getSectionTotal(breakfastList), R.mipmap.icon_breakfast));
//        for (int i = 0; i < breakfastList.size(); i++) {
//            mBeans.add(new HeatFoodSection(breakfastList.get(i), getString(R.string.breakfast)));
//        }
//        mBeans.add(new HeatFoodSection(true, getString(R.string.lunch), getSectionTotal(lunchList), R.mipmap.icon_lunch));
//        for (int i = 0; i < lunchList.size(); i++) {
//            mBeans.add(new HeatFoodSection(lunchList.get(i), getString(R.string.lunch)));
//        }
//        mBeans.add(new HeatFoodSection(true, getString(R.string.dinner), getSectionTotal(dinnerList), R.mipmap.icon_dinner));
//        for (int i = 0; i < dinnerList.size(); i++) {
//            mBeans.add(new HeatFoodSection(dinnerList.get(i), getString(R.string.dinner)));
//        }
//        sectionQuickAdapter.setNewData(mBeans);

//        RxTextUtils.getBuilder("icon_record_title：\t\t")
//                .append("" + total)
//                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
//                .append(" kcal")
//                .setProportion(0.6f)
//                .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
//                .into(mTvFoodTotal);
    }


    private int getSectionTotal(List<FoodListBean> list) {
        int total = 0;
        for (FoodListBean bean : list) {
            total += bean.getUnitCalorie();
        }
        return total;
    }

//    private void initRecycler() {
//        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        sectionQuickAdapter = new BaseSectionQuickAdapter<HeatFoodSection, BaseViewHolder>(R.layout.heat_item, R.layout.heat_item_title, mBeans) {
//            @Override
//            protected void convert(BaseViewHolder helper, HeatFoodSection item) {
//
//                MyAPP.getImageLoader().displayImage(mActivity, item.t.getFoodImg(), (QMUIRadiusImageView) helper.getView(R.id.img_food));
//
//                helper.setText(R.id.tv_foodName, item.t.getFoodName());
//                RxTextUtils.getBuilder(item.t.getUnitCalorie() + "")
//                        .append("kcal/")
//                        .setProportion(0.6f)
//                        .append(RxFormat.setFormatNum(item.t.getUnitCount(), "0.0") + item.t.getUnit())
//                        .setProportion(0.6f)
//                        .setForegroundColor(getResources().getColor(R.color.GrayWrite))
//                        .into((TextView) helper.getView(R.id.tv_kcal));
//                helper.setTypeface(R.id.tv_kcal, MyAPP.typeface);
//            }
//
//            @Override
//            protected void convertHead(BaseViewHolder helper, HeatFoodSection item) {
//                helper.setImageResource(R.id.img, item.getHeadImg());
//                helper.setText(R.id.tv_title, item.header);
//                RxTextUtils.getBuilder("" + item.getIntake())
//                        .append(" kcal").setProportion(0.6f).into((TextView) helper.getView(R.id.tv_kcal));
//                helper.setTypeface(R.id.tv_kcal, MyAPP.typeface);
//            }
//        };
//        mMRecyclerView.setAdapter(sectionQuickAdapter);
//    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTopBar.setTitle("食材推荐");
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
}
