package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.OverlapLayoutManager;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.HotKeyItem;
import lab.wesmartclothing.wefit.flyso.entity.SearchListItem;
import lab.wesmartclothing.wefit.flyso.entity.sql.SearchWordTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.FoodDetailsFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.HeatDetailFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AddOrUpdateFoodDialog;
import lab.wesmartclothing.wefit.flyso.view.DynamicTagFlowLayout;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class SearchHistoryFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mSearchView)
    SearchView mSearchView;
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
    private boolean isStorage = false;
    private Disposable subscribe;
    private AddOrUpdateFoodDialog dialog = new AddOrUpdateFoodDialog();

    private int foodType = 0;
    private long currentTime = 0;
    private Bundle bundle;

    public static QMUIFragment getInstance() {
        return new SearchHistoryFragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_history, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
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
            if (addedFoods.size() > 10) {
                mBtnMark.setVisibility(View.VISIBLE);
                mBtnMark.setText(FoodDetailsFragment.addedLists.size() + "");
                addedFoods = addedFoods.subList(0, 10);
                addedFoods.set(0, R.mipmap.icon_ellipsis);
            }
            adapterAddFoods.setNewData(addedFoods);
            adapterAddFoods.notifyDataSetChanged();
        }
    }

    public void initView() {
        subscribe = new RxPermissions(mActivity)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        isStorage = permission.granted;
                    }
                });
        initBundle();
        init();
        initTopBar();
        initRecyclerView();
        initAddFoodRecyclerView();
    }

    private void initBundle() {
        bundle = getArguments();
        if (bundle != null) {
            foodType = bundle.getInt(Key.ADD_FOOD_TYPE);
            currentTime = bundle.getLong(Key.ADD_FOOD_DATE);
        }
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    private void initAddFoodRecyclerView() {
        mRecyclerAddFoods.setLayoutManager(new OverlapLayoutManager(mContext));
        adapterAddFoods = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                if (item instanceof String) {
                    QMUIRadiusImageView view = helper.getView(R.id.img_food);
                    Glide.with(mActivity).load(item).asBitmap().placeholder(R.mipmap.group15).into(view);
                } else if (item instanceof Integer) {
                    helper.setImageResource(R.id.img_food, (Integer) item);
                }
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
                Glide.with(mContext).load(item.getFoodImg()).asBitmap().into(foodImg);
                helper.setText(R.id.tv_foodName, item.getFoodName());
                TextView foodKcal = helper.getView(R.id.tv_foodKcal);
                RxTextUtils.getBuilder(item.getUnitCalorie() + "")
                        .append("kacl/")
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
                if (RxUtils.isFastClick(1000)) return;
                FoodListBean listBean = (FoodListBean) adapter.getData().get(position);
                String foodName = listBean.getFoodName();

                addSearchKey(foodName);
                showAddFoodDialog(listBean);
            }
        });
        searchListAdapter.bindToRecyclerView(mMRecyclerView);
        searchListAdapter.setEmptyView(R.layout.layout_search_no_data);

    }

    private void init() {
        initLateLyData();
        initHotData();

        mTagFlowLayoutHot.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setQuery(((TextView) v).getText().toString(), true);
            }
        });

        mTagFlowLayoutLately.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setQuery(((TextView) v).getText().toString(), true);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (subscribe != null) subscribe.dispose();
        subscribe = null;
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
                if (index >= 0) {
                    FoodDetailsFragment.addedLists.remove(index);
                    adapterAddFoods.remove(index);
                }
                FoodDetailsFragment.addedLists.add(listBean);
                mLayoutAddFoods.setVisibility(View.VISIBLE);
                if (FoodDetailsFragment.addedLists.size() <= 10) {
                    mBtnMark.setVisibility(View.INVISIBLE);
                    adapterAddFoods.addData(listBean.getFoodImg());
                } else {
                    mBtnMark.setVisibility(View.VISIBLE);
                    mBtnMark.setText(FoodDetailsFragment.addedLists.size() + "");
                    adapterAddFoods.remove(1);
                    adapterAddFoods.addData(listBean.getFoodImg());
                    adapterAddFoods.setData(0, R.mipmap.icon_ellipsis);
                }
                adapterAddFoods.notifyDataSetChanged();
            }
        });
    }


    private void initLateLyData() {
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
//        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
//        mSearchView.setIconified(false);
//        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
//        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.onActionViewExpanded();
        //修改搜索框底部的横线
        mSearchView.findViewById(R.id.search_plate).setBackgroundColor(getResources().getColor(R.color.GrayWrite));
        //修改搜索框的字体颜色及大小
        EditText textView = mSearchView
                .findViewById(
                        android.support.v7.appcompat.R.id.search_src_text
                );
        textView.setTextColor(Color.WHITE);//字体颜色
        textView.setTextSize(15);//字体、提示字体大小
        textView.setHintTextColor(Color.WHITE);//提示字体颜色**

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO 处理Text的。防止输入特殊文字
                if (!RxDataUtils.isNullString(query) && query.length() <= 20) {

                    addSearchKey(query);

                    initSearchData(query);
                } else RxToast.warning(getString(R.string.inputRightFoodName));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO 处理Text的。防止输入特殊文字
                if (!RxDataUtils.isNullString(newText) && newText.length() <= 20)
                    initSearchData(newText);
                else mlayoutSearchData.setVisibility(View.GONE);
                return false;
            }
        });

        if (!isStorage) return;

        notifySearchKey();
    }

    private void addSearchKey(String query) {
        if (isStorage) {
            RxLogUtils.d("是否包含：" + query + "---" + SearchWordTab.getKey(query));
            if (SearchWordTab.getKey(query) == null) {
                SearchWordTab keyTab = new SearchWordTab(System.currentTimeMillis(), query);
                keyTab.save();
            } else {
                SearchWordTab.update(System.currentTimeMillis(), query);
            }
        }

        notifySearchKey();
    }

    private void notifySearchKey() {
        List<SearchWordTab> all = SearchWordTab.soft(SearchWordTab.getAll());
        List<String> list = new ArrayList<>();

        for (int i = 0; i < all.size(); i++) {
            list.add(all.get(i).searchKey);
        }
        mTagFlowLayoutLately.setTags(list);
        mlayoutSearchKey.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
    }


    private void initSearchData(final String key) {
        mlayoutSearchData.setVisibility(View.VISIBLE);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.searchFoodInfo(key))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SearchListItem item = new Gson().fromJson(s, SearchListItem.class);
                        List<FoodListBean> beans = item.getList();
                        searchListAdapter.setNewData(beans);
                        if (beans.size() == 0) {
                            TextView noData = searchListAdapter.getEmptyView().findViewById(R.id.tv_noData);
                            noData.setText(getString(R.string.search_noData, key));
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initHotData() {
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getKeyWord(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("getKeyWord", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        HotKeyItem item = new Gson().fromJson(s, HotKeyItem.class);
                        hotLists.clear();
                        List<HotKeyItem.ListBean> list = item.getList();
                        for (HotKeyItem.ListBean bean : list) {
                            hotLists.add(bean.getKeyWord());
                        }
                        mTagFlowLayoutHot.setTags(hotLists);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
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

        String s = new Gson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.success("添加成功");
                        FoodDetailsFragment.addedLists.clear();
                        getBaseFragmentActivity().popBackStack(HeatDetailFragment.class);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    @OnClick(R.id.tv_delete)
    public void onViewClicked() {
        SearchWordTab.deleteAll();
        notifySearchKey();
    }

    @OnClick(R.id.iv_complete)
    public void iv_complete() {
        addFood();
    }

}
