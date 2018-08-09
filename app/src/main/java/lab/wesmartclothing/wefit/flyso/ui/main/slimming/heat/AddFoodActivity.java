package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.ListBean;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_add_food)
public class AddFoodActivity extends BaseActivity {


    @ViewById
    TextView tv_mark;
    @ViewById
    RelativeLayout mark;
    @ViewById
    TextView tv_title;
    @ViewById
    RecyclerView mRecyclerView;

    @Extra
    String ADD_FOOD_NAME;
    @Extra
    long ADD_FOOD_DATE;
    @Extra
    int ADD_FOOD_TYPE;


    private BaseQuickAdapter adapter;
    private List<AddFoodItem.intakeList> mIntakeLists = new ArrayList<>();


    @Click
    void back() {
        RxActivityUtils.finishActivity();
    }

    @Click
    void tv_mSearchView() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(Key.ADD_FOOD_TYPE, ADD_FOOD_TYPE);
//        bundle.putLong(Key.ADD_FOOD_DATE, ADD_FOOD_DATE);
//        RxActivityUtils.skipActivity(mContext, SearchHistoryActivity_.class, bundle);
    }

    @Click
    void tv_complete() {
        if (mIntakeLists.size() > 0) {
            if (mIntakeLists.size() > 99) {
                RxToast.warning("最多添加99条食材记录");
                return;
            }
            addFood();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();

        Disposable disposable = RxBus.getInstance().register(AddFoodItem.intakeList.class, new Consumer<AddFoodItem.intakeList>() {
            @Override
            public void accept(AddFoodItem.intakeList intakeList) throws Exception {
                mIntakeLists.add(intakeList);
                mark.setVisibility(View.VISIBLE);
                tv_mark.setText(mIntakeLists.size() + "");
            }
        });
        RxBus.getInstance().addSubscription(this, disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
    }

    @Override
    @AfterViews
    public void initView() {
        tv_title.setText(ADD_FOOD_NAME);
        mark.setVisibility(View.GONE);
        initRecycler();
        initData();
    }


    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<ListBean, BaseViewHolder>(R.layout.item_add_foods) {
            @Override
            protected void convert(BaseViewHolder helper, ListBean item) {
                helper.setText(R.id.tv_food, item.getFoodName());
                helper.setText(R.id.tv_foodWeight, RxFormat.setFormatNum(item.getFoodCount(), "0.0") + item.getFoodUnit());
                loadCricle(item.getFoodImg(), (ImageView) helper.getView(R.id.img_food));
                helper.setText(R.id.tv_foodKcal, item.getCalorie() + getString(R.string.unit_kcal));
            }
        };

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!RxUtils.isFastClick(1000))
                    showAddFoodDialog((ListBean) adapter.getData().get(position));
            }
        });

        adapter.bindToRecyclerView(mRecyclerView);
        adapter.setEmptyView(R.layout.layout_not_data);
    }

    private void showAddFoodDialog(final ListBean item) {
        item.setEatType(ADD_FOOD_TYPE);
        item.setHeatDate(ADD_FOOD_DATE);
//        mAddOrUpdateFoodDialog.setFoodInfo(mContext, true, item, new AddOrUpdateFoodDialog.AddOrUpdateFoodListener() {
//            @Override
//            public void complete(AddFoodItem.intakeList item) {
//                mIntakeLists.add(item);
//                mark.setVisibility(View.VISIBLE);
//                tv_mark.setText(mIntakeLists.size() + "");
//            }
//        });
    }

    private void addFood() {
        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setAddDate(ADD_FOOD_DATE);
        foodItem.setEatType(ADD_FOOD_TYPE);
        foodItem.setIntakeLists(mIntakeLists);
        String s = new Gson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxBus.getInstance().post(mIntakeLists.size() > 0);
                        finish();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initData() {
//        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
//        RxManager.getInstance().doNetSubscribe(dxyService.getFoodInfo())
//                .compose(MyAPP.getRxCache().<String>transformObservable("getFoodInfo" + ADD_FOOD_TYPE, String.class, CacheStrategy.firstRemote()))
//                .map(new CacheResult.MapFunc<String>())
//                .subscribe(new RxNetSubscriber<String>() {
//                    @Override
//                    protected void _onNext(String s) {
//                        RxLogUtils.d("结束：" + s);
//                        FoodInfoItem item = new Gson().fromJson(s, FoodInfoItem.class);
//                        List<FoodListBean> beans = item.getList();
//                        adapter.setNewData(beans);
//                    }
//
//                    @Override
//                    protected void _onError(String error) {
//                        RxToast.normal(error);
//                    }
//                });
    }

}
