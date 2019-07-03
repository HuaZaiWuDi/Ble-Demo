package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.OverlapLayoutManager;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodInfoItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodTypeBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AddOrUpdateFoodDialog;


/**
 * Created by jk on 2018/8/3.
 */
public class FoodDetailsFragment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.recyclerAddFoods)
    RecyclerView mRecyclerAddFoods;
    @BindView(R.id.btn_mark)
    QMUIRoundButton mBtnMark;
    @BindView(R.id.iv_complete)
    ImageView mIvComplete;
    @BindView(R.id.layout_addFoods)
    RelativeLayout mLayoutAddFoods;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private int foodType = 0;
    private long currentTime = System.currentTimeMillis();
    private int pageNum = 0;//页码
    private boolean SlimmingPage = false;//是否是从首页跳转
    private Bundle bundle;
    private BaseQuickAdapter adapter;
    private BaseQuickAdapter adapterAddFoods;
    private AddOrUpdateFoodDialog dialog = new AddOrUpdateFoodDialog();
    public static List<FoodListBean> addedLists = new ArrayList<>();//已经添加的食物列表
    private String typeId = "";

    @Override
    protected int layoutId() {
        return R.layout.fragment_food_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initRecyclerView();
        initAddFoodRecyclerView();

        dialog.setLifecycleSubject(lifecycleSubject);
        dialog.setAddOrUpdateFoodListener(new AddOrUpdateFoodDialog.AddOrUpdateFoodListener() {
            @Override
            public void complete(FoodListBean listBean) {
                int index = dialog.isExist(addedLists, listBean);
                int index2 = adapterAddFoods.getData().indexOf(listBean.getFoodImg());
                RxLogUtils.d("添加的下标：" + index);
                if (index >= 0 && index2 >= 0) {
                    addedLists.remove(index % addedLists.size());
                    adapterAddFoods.remove(index2 % adapterAddFoods.getData().size());
                }
                addedLists.add(listBean);
                adapterAddFoods.addData(listBean.getFoodImg());
                mLayoutAddFoods.setVisibility(View.VISIBLE);
                if (addedLists.size() <= 10) {
                    mBtnMark.setVisibility(View.INVISIBLE);
                } else {
                    mBtnMark.setVisibility(View.VISIBLE);
                    mBtnMark.setText(addedLists.size() + "");
//                    adapterAddFoods.setData(0, R.mipmap.icon_ellipsis);
                    if (index2 < 0)
                        adapterAddFoods.remove(0);
                }

                adapterAddFoods.notifyDataSetChanged();
            }
        });
    }

    private void initTabLayout() {
        //默认加载全部
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("全部");
        tab.setTag("");
        mTabLayout.addTab(tab);

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().getFoodType())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("getFoodType", String.class,
                        CacheStrategy.firstCacheTimeout(Key.HOURS_6)))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        List<FoodTypeBean> list = JSON.parseObject(s, new TypeToken<List<FoodTypeBean>>() {
                        }.getType());
                        for (int i = 0; i < list.size(); i++) {
                            FoodTypeBean bean = list.get(i);
                            TabLayout.Tab tab = mTabLayout.newTab();
                            tab.setText(bean.getTypeName());
                            tab.setTag(bean.getGid());
                            mTabLayout.addTab(tab);
                        }
                    }
                });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RxLogUtils.d("Tab:" + tab.getText());
                typeId = (String) tab.getTag();
                pageNum = 1;
                initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (addedLists.size() > 0) {
            mLayoutAddFoods.setVisibility(View.VISIBLE);
            List<Object> addedFoods = new ArrayList<>();
            for (int i = 0; i < addedLists.size(); i++) {
                addedFoods.add(addedLists.get(i).getFoodImg());
            }
            mBtnMark.setVisibility(addedFoods.size() > 10 ? View.VISIBLE : View.GONE);
            if (addedFoods.size() > 10) {
                mBtnMark.setText(addedLists.size() + "");
                addedFoods = addedFoods.subList(addedFoods.size() - 10, addedFoods.size());
//                addedFoods.set(0, R.mipmap.icon_ellipsis);
            }

            adapterAddFoods.setNewData(addedFoods);
        }
    }


    private void initAddFoodRecyclerView() {
        mRecyclerAddFoods.setLayoutManager(new OverlapLayoutManager(mContext));
        adapterAddFoods = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyAPP.getImageLoader().displayImage(mActivity, item, (QMUIRadiusImageView) helper.getView(R.id.img_food));
            }
        };
        mRecyclerAddFoods.setAdapter(adapterAddFoods);
//        adapterAddFoods.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                QMUIFragment instance = AddedFoodFragment.getInstance();
//                bundle.putInt(Key.ADD_FOOD_TYPE, foodType);
//                bundle.putLong(Key.ADD_FOOD_DATE, currentTime);
//                instance.setArguments(bundle);
//                startFragment(instance);
//            }
//        });
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        this.bundle = bundle;
        foodType = bundle.getInt(Key.ADD_FOOD_TYPE);
        currentTime = bundle.getLong(Key.ADD_FOOD_DATE, System.currentTimeMillis());
        SlimmingPage = bundle.getBoolean(Key.ADD_FOOD_NAME, true);
        final String[] add_food = getResources().getStringArray(R.array.add_food);
        mQMUIAppBarLayout.setTitle(add_food[FOOD_TYPE(foodType)]);
    }


    public static int FOOD_TYPE(int type) {
        switch (type) {
            case Key.TYPE_BREAKFAST:
                return 0;
            case Key.TYPE_LUNCH:
                return 1;
            case Key.TYPE_DINNER:
                return 2;
            case Key.TYPED_MEAL:
                return 3;
        }
        return 0;
    }



    private void initRecyclerView() {
        adapter = new BaseQuickAdapter<FoodListBean, BaseViewHolder>(R.layout.item_add_food) {
            @Override
            protected void convert(BaseViewHolder helper, FoodListBean item) {
                QMUIRadiusImageView foodImg = helper.getView(R.id.iv_foodImg);
                MyAPP.getImageLoader().displayImage(mActivity, item.getFoodImg(), foodImg);
                helper.setText(R.id.tv_foodName, item.getFoodName());
                TextView foodKcal = helper.getView(R.id.tv_foodKcal);
                RxTextUtils.getBuilder(item.getUnitCalorie() + "")
                        .append("kcal/")
                        .setProportion(0.6f)
                        .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                        .append(RxFormat.setFormatNum(item.getUnitCount(), "0.0") + item.getUnit())
                        .setProportion(0.6f)
                        .setForegroundColor(getResources().getColor(R.color.GrayWrite))
                        .into(foodKcal);
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dialog.setFoodInfo(mActivity, false, foodType, currentTime, (FoodListBean) adapter.getItem(position));
            }
        });
        mMRecyclerView.setAdapter(adapter);
        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                pageNum++;
                initData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNum = 1;
                initData();
            }
        });

        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mQMUIAppBarLayout.addRightImageButton(R.mipmap.icon_search, R.id.id_search)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxActivityUtils.skipActivity(mActivity, SearchHistoryFragment.class, bundle);
                    }
                });
    }


    @Override
    protected void initNetData() {
        super.initNetData();
        initTabLayout();
        initData();
    }

    private void initData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().getFoodInfo(pageNum, 15, typeId))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable(
                        "getFoodInfo" + typeId + pageNum,
                        String.class,
                        CacheStrategy.firstCacheTimeout(Key.HOURS_6)))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {

                        FoodInfoItem item = JSON.parseObject(s, FoodInfoItem.class);
                        smartRefreshLayout.setEnableLoadMore(item.isHasNextPage());
                        smartRefreshLayout.finishLoadMore(true);
                        smartRefreshLayout.finishRefresh(true);

                        List<FoodListBean> beans = item.getList();
                        if (item.getPageNum() == 1) {
                            adapter.setNewData(beans);
                        } else {
                            adapter.addData(beans);
                        }
                    }

                    @Override
                    protected void _onError(String error, int errorCode) {
                        RxToast.normal(error);
                        smartRefreshLayout.finishLoadMore(false);
                        smartRefreshLayout.finishRefresh(false);
                    }
                });
    }

    //添加食物
    private void addFood() {
        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setAddDate(currentTime);
        foodItem.setEatType(foodType);

        List<AddFoodItem.intakeList> mIntakeLists = new ArrayList<>();

        for (int i = 0; i < addedLists.size(); i++) {
            FoodListBean foodListBean = addedLists.get(i);
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
            mIntakeLists.add(intakeList);
        }
        foodItem.setIntakeLists(mIntakeLists);

        String s = JSON.toJSONString(foodItem);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().addHeatInfo(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.success(getString(R.string.addSuccess));
                        addedLists.clear();
                        onBackPressed();
                        RxBus.getInstance().post(new RefreshSlimming());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    @OnClick({R.id.recyclerAddFoods, R.id.iv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recyclerAddFoods:
                RxActivityUtils.skipActivity(mContext, AddedFoodFragment.class);
                break;
            case R.id.iv_complete:
                //添加食物
                addFood();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (addedLists.size() == 0) {
            RxActivityUtils.finishActivity();
            return;
        }
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent(getString(R.string.addIncompleteAndBack))
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addedLists.clear();
                        RxActivityUtils.finishActivity();
                    }
                });
        rxDialog.setCanceledOnTouchOutside(false);
        rxDialog.show();
    }


}
