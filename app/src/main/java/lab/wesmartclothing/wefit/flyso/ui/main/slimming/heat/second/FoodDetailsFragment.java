package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.OverlapLayoutManager;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodInfoItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.MainFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.SearchHistoryFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AddOrUpdateFoodDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;


/**
 * Created by jk on 2018/8/3.
 */
public class FoodDetailsFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    Unbinder unbinder;
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

    public static QMUIFragment getInstance() {
        return new FoodDetailsFragment();
    }

    private int foodType = 0;
    private long currentTime = 0;
    private int pageNum = 0;//页码
    private boolean SlimmingPage = false;
    private Bundle bundle;
    private BaseQuickAdapter adapter;
    private BaseQuickAdapter adapterAddFoods;
    private AddOrUpdateFoodDialog dialog = new AddOrUpdateFoodDialog();
    public static List<FoodListBean> addedLists = new ArrayList<>();//已经添加的食物列表

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_food_detail, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
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

    private void initView() {
        initBundle();
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

    private void initBundle() {
        bundle = getArguments();
        if (bundle != null) {
            foodType = bundle.getInt(Key.ADD_FOOD_TYPE);
            currentTime = bundle.getLong(Key.ADD_FOOD_DATE);
            SlimmingPage = bundle.getBoolean(Key.ADD_FOOD_NAME);
        }
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


        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
    }


    private void initTopBar() {
        final String[] add_food = getResources().getStringArray(R.array.add_food);

        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle(add_food[HeatDetailFragment.FOOD_TYPE(foodType)]);
        mQMUIAppBarLayout.addRightImageButton(R.mipmap.icon_search, R.id.id_search)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIFragment instance = SearchHistoryFragment.getInstance();
                        instance.setArguments(bundle);
                        startFragment(instance);
                    }
                });
    }


    private void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getFoodInfo(pageNum, 20))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("getFoodInfo" + pageNum, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("历史数据：" + s);
                        FoodInfoItem item = MyAPP.getGson().fromJson(s, FoodInfoItem.class);
                        List<FoodListBean> beans = item.getList();
                        if (pageNum == 1) {
                            adapter.setNewData(beans);
                        } else {
                            adapter.addData(beans);
                        }
                        if (smartRefreshLayout.isLoading()) {
                            pageNum++;
                            smartRefreshLayout.finishLoadMore(true);
                        }
                        if (smartRefreshLayout.isRefreshing())
                            smartRefreshLayout.finishRefresh(true);
                        smartRefreshLayout.setEnableLoadMore(item.isHasNextPage());
                    }

                    @Override
                    protected void _onError(String error, int errorCode) {
                        RxToast.normal(error);
                        if (smartRefreshLayout.isLoading()) {
                            pageNum++;
                            smartRefreshLayout.finishLoadMore(false);
                        }
                        if (smartRefreshLayout.isRefreshing())
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

        String s = MyAPP.getGson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.success("添加成功");
                        addedLists.clear();

                        getBaseFragmentActivity().popBackStack(SlimmingPage ? MainFragment.class : HeatDetailFragment.class);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    @OnClick({R.id.recyclerAddFoods, R.id.iv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recyclerAddFoods:
                startFragment(AddedFoodFragment.getInstance());
                break;
            case R.id.iv_complete:
                //添加食物
                addFood();
                break;
        }
    }

    //监听返回按钮
    @Override
    protected void popBackStack() {
//        super.popBackStack();

        if (addedLists.size() == 0) {
            super.popBackStack();
            return;
        }

        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getTvTitle().setVisibility(View.GONE);
        dialog.setContent("已添加的食物还未确认，您确定要返回么？");
        dialog.getTvCancel().setBackgroundColor(getResources().getColor(R.color.orange_FF7200));
        dialog.setCancel("确认").setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addedLists.clear();
                getBaseFragmentActivity().popBackStack();
            }
        })
                .setSure("取消")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
