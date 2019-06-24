package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FetchHeatInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AddOrUpdateFoodDialog;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;

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
    @BindView(R.id.circleProgressBar)
    RoundProgressBar mCircleProgressBar;
    @BindView(R.id.tv_totalKcal)
    TextView mTvTotalKcal;

    private long currentTime = System.currentTimeMillis();
    private BaseQuickAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, mealAdapter;
    private AddOrUpdateFoodDialog dialog = new AddOrUpdateFoodDialog();

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
        mChooseDate.setOnDateChangeListener((year, month, day, millis) -> {
            currentTime = millis;
            foodRecord(millis);
        });

        dialog.setLifecycleSubject(lifecycleSubject);
        dialog.setDeleteFoodListener(listBean -> {
            foodRecord(currentTime);
            RxBus.getInstance().post(new RefreshSlimming());
        });
        dialog.setAddOrUpdateFoodListener(listBean -> {
            switch (listBean.getEatType()) {
                case Key.TYPE_BREAKFAST:
                    update(breakfastAdapter, listBean, listBean.getEatType());
                    break;
                case Key.TYPE_LUNCH:
                    update(lunchAdapter, listBean, listBean.getEatType());
                    break;
                case Key.TYPE_DINNER:
                    update(dinnerAdapter, listBean, listBean.getEatType());
                    break;
                case Key.TYPED_MEAL:
                    update(mealAdapter, listBean, listBean.getEatType());
                    break;
            }
        });

        RxTextUtils.getBuilder("摄入热量\n")
                .setProportion(0.4f).setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append("0")
                .append("\tkcal").setProportion(0.5f)
                .into(mTvTotalKcal);

    }


    private void update(BaseQuickAdapter adapter, FoodListBean listBean, int eatType) {
        int exist = dialog.isExist(adapter.getData(), listBean);
        if (exist >= 0) {
            adapter.setData(exist, listBean);
            updateFood(listBean, eatType);
        }
    }


    private void initRecycler() {
        UserInfo info = MyAPP.getgUserInfo();
        mTvEmpty.setText(getString(R.string.empty_record, info.getUserName()));
        mMRecyclerBreakfast.setLayoutManager(new LinearLayoutManager(mContext));
        mMRecyclerLunch.setLayoutManager(new LinearLayoutManager(mContext));
        mMRecyclerDinner.setLayoutManager(new LinearLayoutManager(mContext));
        mMRecyclerMeal.setLayoutManager(new LinearLayoutManager(mContext));

        breakfastAdapter = createAdapter();
        lunchAdapter = createAdapter();
        dinnerAdapter = createAdapter();
        mealAdapter = createAdapter();

        deleteOrUpdate(breakfastAdapter, Key.TYPE_BREAKFAST);
        deleteOrUpdate(lunchAdapter, Key.TYPE_LUNCH);
        deleteOrUpdate(dinnerAdapter, Key.TYPE_DINNER);
        deleteOrUpdate(mealAdapter, Key.TYPED_MEAL);

        mMRecyclerBreakfast.setAdapter(breakfastAdapter);
        mMRecyclerLunch.setAdapter(lunchAdapter);
        mMRecyclerDinner.setAdapter(dinnerAdapter);
        mMRecyclerMeal.setAdapter(mealAdapter);
    }

    @Override
    protected void initRxBus2() {
        super.initRxBus2();
        RxBus.getInstance().register2(RefreshSlimming.class)
                .compose(RxComposeUtils.<RefreshSlimming>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshSlimming>() {
                    @Override
                    protected void _onNext(RefreshSlimming refreshSlimming) {
                        initNetData();
                    }
                });
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
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .fetchOneDayHeatInfo(NetManager.fetchRequest(object.toString())))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(MyAPP.getRxCache().<String>transformObservable("getAthleticsInfo" + currentTime,
                        String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        FetchHeatInfoBean bean = JSON.parseObject(s, FetchHeatInfoBean.class);
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


        int totalKcal = bean.getBreakfast().getCalorie() + bean.getLunch().getCalorie() + bean.getDinner().getCalorie()
                + bean.getSnacks().getCalorie();
        RxTextUtils.getBuilder("摄入热量\n")
                .setProportion(0.4f).setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(totalKcal + "")
                .append("\tkcal").setProportion(0.5f)
                .into(mTvTotalKcal);

        mCircleProgressBar.setProgress((int) (bean.getIntakePercent() * 100));
    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mTopBar.setTitle("饮食记录");
    }

    @OnClick({R.id.layout_breakfast, R.id.layout_lunch, R.id.layout_dinner, R.id.layout_meal})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
        bundle.putBoolean(Key.ADD_FOOD_NAME, false);//是否是主页跳转的
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

    private void deleteOrUpdate(final BaseQuickAdapter adapter, final int eatType) {
        //点击更改
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dialog.setFoodInfo(mContext, true, eatType, currentTime, (FoodListBean) adapter.getData().get(position));
            }
        });

        //长按删除
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                //显示删除
                RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                        .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                        .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                        .setContent("确定要删除此条记录么？")
                        .setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RxLogUtils.d("今日食品类型：" + eatType);
                                FoodListBean item = (FoodListBean) adapter.getItem(position);
                                item.setEatType(eatType);
                                dialog.deleteData(mContext, item);
                            }
                        });
                rxDialog.show();
                return true;
            }
        });
    }


    private void updateFood(FoodListBean foodListBean, int eatType) {
        AddFoodItem.intakeList intakeList = new AddFoodItem.intakeList();
        intakeList.setFoodId(foodListBean.getFoodId());
        intakeList.setFoodName(foodListBean.getFoodName());
        intakeList.setFoodCount(foodListBean.getFoodCount());
        intakeList.setUnit(foodListBean.getUnit());
        intakeList.setGid(foodListBean.getGid());
        intakeList.setUnitCount(foodListBean.getUnitCount());
        intakeList.setFoodImg(foodListBean.getFoodImg());
        intakeList.setRemark(foodListBean.getRemark());
        intakeList.setCalorie(foodListBean.getCalorie());
        intakeList.setHeatDate(currentTime);
        intakeList.setUnitCalorie(foodListBean.getUnitCalorie());

        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setAddDate(currentTime);
        foodItem.setEatType(eatType);
        ArrayList<AddFoodItem.intakeList> lists = new ArrayList<>();
        lists.add(intakeList);
        foodItem.setIntakeLists(lists);
        String s = JSON.toJSONString(foodItem);

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().addHeatInfo(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("修改食物成功");
                        foodRecord(currentTime);
                        RxBus.getInstance().post(new RefreshSlimming());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

}
