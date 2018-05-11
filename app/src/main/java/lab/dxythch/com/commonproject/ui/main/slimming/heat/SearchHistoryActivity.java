package lab.dxythch.com.commonproject.ui.main.slimming.heat;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.entity.AddFoodItem;
import lab.dxythch.com.commonproject.entity.HotKeyItem;
import lab.dxythch.com.commonproject.entity.SearchListItem;
import lab.dxythch.com.commonproject.entity.sql.SearchWordTab;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;
import lab.dxythch.com.commonproject.view.DynamicTagFlowLayout;
import lab.dxythch.com.netlib.net.RetrofitService;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import lab.dxythch.com.netlib.utils.RxBus;
import okhttp3.RequestBody;


@EActivity(R.layout.activity_search_history)
public class SearchHistoryActivity extends BaseActivity {

    private List<String> latelyLists = new ArrayList<>();
    private List<String> hotLists = new ArrayList<>();
    private BaseQuickAdapter searchListAdapter;

    @ViewById
    DynamicTagFlowLayout tagFlowLayout_hot;
    @ViewById
    DynamicTagFlowLayout tagFlowLayout_lately;
    @ViewById
    SearchView mSearchView;
    @ViewById
    LinearLayout layoutHistory;
    @ViewById
    RecyclerView mRecyclerView;

    @Click
    void tv_cancel() {
        finish();
    }

    @Click
    void tv_delete() {
        SearchWordTab.deleteAll();
        latelyLists.clear();
        tagFlowLayout_lately.setTags(latelyLists);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
    }


    @Override
    @AfterViews
    public void initView() {
        init();
        initRecyclerView();
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        searchListAdapter = new BaseQuickAdapter<SearchListItem.ListBean, BaseViewHolder>(R.layout.item_search) {
            @Override
            protected void convert(BaseViewHolder helper, SearchListItem.ListBean item) {
                helper.setText(R.id.tv_foodName, item.getFoodName());
                TextView view = helper.getView(R.id.tv_foodInfo);
                RxTextUtils.getBuilder("")
                        .append(item.getHeat() + getString(R.string.unit_kcal)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                        .append(getString(R.string.unit_heat))
                        .into(view);
            }
        };
        searchListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mRecyclerView.setVisibility(View.GONE);
                SearchListItem.ListBean listBean = (SearchListItem.ListBean) adapter.getData().get(position);
                String foodName = listBean.getFoodName();
                if (!SearchWordTab.isExist(foodName)) {
                    SearchWordTab keyTab = new SearchWordTab(System.currentTimeMillis(), foodName);
                    keyTab.save();
                    latelyLists.add(foodName);
                    tagFlowLayout_lately.setTags(latelyLists);
                }
                showAddFoodDialog(listBean);
            }
        });
        mRecyclerView.setAdapter(searchListAdapter);
    }

    private void init() {
        initLateLyData();
        initHotData();

        tagFlowLayout_hot.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setQuery(((TextView) v).getText().toString(), true);
            }
        });

        tagFlowLayout_lately.setOnTagItemClickListener(new DynamicTagFlowLayout.OnTagItemClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setQuery(((TextView) v).getText().toString(), true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showAddFoodDialog(final SearchListItem.ListBean item) {
        View view = View.inflate(mContext, R.layout.dialogfragment_add_food, null);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .show();

        ImageView img_food = view.findViewById(R.id.img_food);
        TextView tv_foodName = view.findViewById(R.id.tv_foodName);
        TextView tv_foodInfo = view.findViewById(R.id.tv_info);
        TextView tv_heat = view.findViewById(R.id.tv_heat);
        final EditText et_food_g = view.findViewById(R.id.et_food_g);
        TextView tv_unit = view.findViewById(R.id.tv_unit);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView ok = view.findViewById(R.id.ok);

        loadCricle(item.getFoodImg(), img_food);
        tv_foodName.setText(item.getFoodName());
        tv_foodInfo.setText(item.getDescription());
        tv_unit.setText(item.getUnit());

        et_food_g.setText(item.getUnitCount() + "");

        RxTextUtils.getBuilder("")
                .append(item.getHeat() + getString(R.string.unit_kcal)).setForegroundColor(getResources().getColor(R.color.colorTheme))
                .append(getString(R.string.unit_heat))
                .into(tv_heat);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setUnitCount(Integer.parseInt(et_food_g.getText().toString()));
                AddFoodItem.intakeList intakeList = new AddFoodItem.intakeList();
                intakeList.setFoodId(item.getGid());
                intakeList.setFoodName(item.getFoodName());
                intakeList.setFoodCount(item.getUnitCount());
                intakeList.setFoodUnit(item.getUnit());
                //暂时不用传
                intakeList.setWeight("");
                intakeList.setWeightType("");

                dialog.dismiss();
                RxBus.getInstance().post(intakeList);
                onBackPressed();
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

        //修改搜索框的字体颜色及大小
        EditText textView = mSearchView
                .findViewById(
                        android.support.v7.appcompat.R.id.search_src_text
                );
        textView.setTextColor(getResources().getColor(R.color.white));//字体颜色
        textView.setTextSize(15);//字体、提示字体大小
        textView.setHintTextColor(Color.WHITE);//提示字体颜色**

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO 处理Text的。防止输入特殊文字
                if (!RxDataUtils.isNullString(query) && query.length() <= 20)
                    initSearchData(query);
                else RxToast.warning(getString(R.string.inputRightFoodName));

                if (!SearchWordTab.isExist(query)) {
                    SearchWordTab keyTab = new SearchWordTab(System.currentTimeMillis(), query);
                    keyTab.save();
                    latelyLists.add(query);
                    tagFlowLayout_lately.setTags(latelyLists);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO 处理Text的。防止输入特殊文字
                if (!RxDataUtils.isNullString(newText) && newText.length() <= 20)
                    initSearchData(newText);
                else mRecyclerView.setVisibility(View.GONE);
                return false;
            }
        });

        List<SearchWordTab> all = SearchWordTab.soft(SearchWordTab.getAll());

        latelyLists.clear();
        for (int i = 0; i < all.size(); i++) {
            latelyLists.add(all.get(i).searchKey);
        }
        tagFlowLayout_lately.setTags(latelyLists);

    }


    private void initSearchData(String key) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("foodName", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.searchFoodInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SearchListItem item = new Gson().fromJson(s, SearchListItem.class);
                        List<SearchListItem.ListBean> beans = item.getList();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        searchListAdapter.setNewData(beans);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initHotData() {
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getKeyWord(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxLogUtils.d("doOnSubscribe：");
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        RxLogUtils.d("结束：");
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        HotKeyItem item = new Gson().fromJson(s, HotKeyItem.class);
                        hotLists.clear();
                        List<HotKeyItem.ListBean> list = item.getList();
                        for (HotKeyItem.ListBean bean : list) {
                            hotLists.add(bean.getKeyWord());
                        }
                        tagFlowLayout_hot.setTags(hotLists);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });

    }


}
