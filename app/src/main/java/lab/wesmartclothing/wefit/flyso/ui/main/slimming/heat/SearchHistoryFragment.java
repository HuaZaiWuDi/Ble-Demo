package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextDrawable;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxEditText;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.OverlapLayoutManager;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.HotKeyItem;
import lab.wesmartclothing.wefit.flyso.entity.SearchListItem;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodDetailsFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AddOrUpdateFoodDialog;
import lab.wesmartclothing.wefit.flyso.view.DynamicTagFlowLayout;


public class SearchHistoryFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mSearchView)
    RxEditText mSearchView;
    @BindView(R.id.tagFlowLayout_lately)
    DynamicTagFlowLayout mTagFlowLayoutLately;
    @BindView(R.id.tv_delete)
    QMUIRoundButton mTvDelete;
    @BindView(R.id.tagFlowLayout_hot)
    DynamicTagFlowLayout mTagFlowLayoutHot;
    @BindView(R.id.layoutHistory)
    LinearLayout mLayoutHistory;
    @BindView(R.id.layout_searchData)
    LinearLayout mlayoutSearchData;
    @BindView(R.id.layout_searchKey)
    LinearLayout mlayoutSearchKey;
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

    private List<String> hotLists = new ArrayList<>();
    private BaseQuickAdapter searchListAdapter, adapterAddFoods;
    private AddOrUpdateFoodDialog dialog = new AddOrUpdateFoodDialog();
    private boolean SlimmingPage = false;
    private int foodType = 0;
    private long currentTime = 0;
    private List<String> searchKeyLists = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.activity_search_history;
    }


    @Override
    protected void initViews() {
        super.initViews();

        dialog.setLifecycleSubject(lifecycleSubject);
        new RxPermissions((FragmentActivity) mActivity)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxComposeUtils.<Permission>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Permission>() {
                    @Override
                    protected void _onNext(Permission permission) {
                    }
                });

        init();
        initTopBar();
        initRecyclerView();
        initAddFoodRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FoodDetailsFragment.addedLists.size() > 0) {
            mLayoutAddFoods.setVisibility(View.VISIBLE);
            List<Object> addedFoods = new ArrayList<>();
            for (int i = 0; i < FoodDetailsFragment.addedLists.size(); i++) {
                addedFoods.add(FoodDetailsFragment.addedLists.get(i).getFoodImg());
            }
            mBtnMark.setVisibility(addedFoods.size() > 10 ? View.VISIBLE : View.GONE);
            if (addedFoods.size() > 10) {
                mBtnMark.setText(FoodDetailsFragment.addedLists.size() + "");
                addedFoods = addedFoods.subList(0, 10);
                addedFoods.set(0, R.mipmap.icon_ellipsis);
            }
            adapterAddFoods.setNewData(addedFoods);
        }
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        foodType = bundle.getInt(Key.ADD_FOOD_TYPE);
        currentTime = bundle.getLong(Key.ADD_FOOD_DATE, System.currentTimeMillis());
        SlimmingPage = bundle.getBoolean(Key.ADD_FOOD_NAME, true);//是否是从首页跳转
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    }


    private void initRecyclerView() {
        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        searchListAdapter = new BaseQuickAdapter<FoodListBean, BaseViewHolder>(R.layout.item_add_food) {
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
        searchListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FoodListBean listBean = (FoodListBean) adapter.getData().get(position);
                String foodName = listBean.getFoodName();

                addSearchKey(foodName);
                showAddFoodDialog(listBean);

                mSearchView.clearFocus();
            }
        });
        searchListAdapter.bindToRecyclerView(mMRecyclerView);


    }

    private void init() {
        initLateLyData();
        initHotData();

        mTagFlowLayoutHot.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                String string = ((TextView) v).getText().toString();
                mSearchView.setText(string);
                mSearchView.setSelection(string.length());
            }
        });

        mTagFlowLayoutLately.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                String string = ((TextView) v).getText().toString();
                mSearchView.setText(string);
                mSearchView.setSelection(string.length());
            }
        });

        RxCache.getDefault().<List<String>>load(Key.CACHE_SEARCH_KEY, new TypeToken<List<String>>() {
        }.getType())
                .map(new CacheResult.MapFunc<List<String>>())
                .subscribe(new RxSubscriber<List<String>>() {
                    @Override
                    protected void _onNext(List<String> strings) {
                        if (!RxDataUtils.isEmpty(strings)) {
                            searchKeyLists = strings;
                            notifySearchKey();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showAddFoodDialog(final FoodListBean item) {
        item.setEatType(item.getEatType());
        item.setHeatDate(item.getHeatDate());

        dialog.setFoodInfo(mContext, false, foodType, currentTime, item);
        dialog.setAddOrUpdateFoodListener(new AddOrUpdateFoodDialog.AddOrUpdateFoodListener() {
            @Override
            public void complete(FoodListBean listBean) {
                int index = dialog.isExist(FoodDetailsFragment.addedLists, listBean);
                int index2 = adapterAddFoods.getData().indexOf(listBean.getFoodImg());
                if (index >= 0 && index2 >= 0) {
                    FoodDetailsFragment.addedLists.remove(index);
                    adapterAddFoods.remove(index2);
                }
                FoodDetailsFragment.addedLists.add(listBean);
                adapterAddFoods.addData(listBean.getFoodImg());
                mLayoutAddFoods.setVisibility(View.VISIBLE);
                if (FoodDetailsFragment.addedLists.size() <= 10) {
                    mBtnMark.setVisibility(View.INVISIBLE);
                } else {
                    mBtnMark.setVisibility(View.VISIBLE);
                    mBtnMark.setText(FoodDetailsFragment.addedLists.size() + "");
                    adapterAddFoods.addData(listBean.getFoodImg());
                    if (index2 < 0)
                        adapterAddFoods.remove(0);
//                    adapterAddFoods.setData(0, R.mipmap.icon_ellipsis);
                }
                adapterAddFoods.notifyDataSetChanged();
            }
        });
    }


    private void initLateLyData() {
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                RxLogUtils.e("newText：" + newText);
                //文字改变就会响应
                if (!RxDataUtils.isNullString(newText) && newText.length() <= 20)
                    initSearchData(newText);
                else mlayoutSearchData.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxTextDrawable.addTextDrawableListener(mSearchView, RxTextDrawable.O_RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });

        notifySearchKey();
    }

    private void addSearchKey(String query) {

        boolean remove = searchKeyLists.remove(query);
        RxLogUtils.d("是否存在：" + remove);

        searchKeyLists.add(0, query);

        RxLogUtils.d("最近搜索:" + Arrays.asList(searchKeyLists.toArray()));
        RxCache.getDefault().save(Key.CACHE_SEARCH_KEY, searchKeyLists)
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        RxLogUtils.d("最近搜索保存成功");
                    }
                });

        notifySearchKey();
    }


    private void notifySearchKey() {
        mTagFlowLayoutLately.setTags(searchKeyLists);
        mlayoutSearchKey.setVisibility(searchKeyLists.isEmpty() ? View.GONE : View.VISIBLE);
    }


    private void initSearchData(final String key) {

        mlayoutSearchData.setVisibility(View.VISIBLE);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().searchFoodInfo(key))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SearchListItem item = JSON.parseObject(s, SearchListItem.class);
                        List<FoodListBean> beans = item.getList();
                        searchListAdapter.setNewData(beans);
                        if (beans.size() == 0) {
                            searchListAdapter.setEmptyView(R.layout.layout_search_no_data);
                            TextView noData = searchListAdapter.getEmptyView().findViewById(R.id.tv_noData);
                            noData.setText(getString(R.string.search_noData, key));
                        } else {
                            addSearchKey(key);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    private void initHotData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .getKeyWord())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("getKeyWord", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        HotKeyItem item = JSON.parseObject(s, HotKeyItem.class);
                        hotLists.clear();
                        List<HotKeyItem.ListBean> list = item.getList();
                        for (HotKeyItem.ListBean bean : list) {
                            hotLists.add(bean.getKeyWord());
                        }
                        mTagFlowLayoutHot.setTags(hotLists);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    //添加食物
    private void addFood() {
        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setAddDate(currentTime);
        foodItem.setEatType(foodType);

        List<AddFoodItem.intakeList> mIntakeLists = new ArrayList<>();
        for (int i = 0; i < FoodDetailsFragment.addedLists.size(); i++) {
            FoodListBean foodListBean = FoodDetailsFragment.addedLists.get(i);
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
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addHeatInfo(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        FoodDetailsFragment.addedLists.clear();
                        //刷新数据
                        RxBus.getInstance().post(new RefreshSlimming());

                        if (SlimmingPage) {
                            RxActivityUtils.skipActivity(mContext, MainActivity.class);
                        } else {
                            RxActivityUtils.finishActivity();
                            RxActivityUtils.finishActivity();
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    @OnClick(R.id.tv_delete)
    public void onViewClicked() {
        RxCache.getDefault().remove(Key.CACHE_SEARCH_KEY);
        searchKeyLists.clear();
        notifySearchKey();
    }

    @OnClick(R.id.iv_complete)
    public void iv_complete() {
        addFood();
    }

}
