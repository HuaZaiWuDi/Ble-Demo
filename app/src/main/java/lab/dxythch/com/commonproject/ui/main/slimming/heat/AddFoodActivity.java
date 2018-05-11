package lab.dxythch.com.commonproject.ui.main.slimming.heat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.entity.AddFoodItem;
import lab.dxythch.com.commonproject.entity.FoodInfoItem;
import lab.dxythch.com.commonproject.prefs.Prefs_;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;
import lab.dxythch.com.netlib.net.RetrofitService;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import lab.dxythch.com.netlib.utils.RxBus;
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
    String ADD_FOOD_DATE;
    @Extra
    int ADD_FOOD_TYPE;

    @Pref
    Prefs_ mPrefs;

    private BaseQuickAdapter adapter;
    private List<AddFoodItem.intakeList> mIntakeLists = new ArrayList<>();


    @Click
    void back() {
        onBackPressed();
    }

    @Click
    void tv_mSearchView() {
        RxActivityUtils.skipActivity(mContext, SearchHistoryActivity_.class);
    }

    @Click
    void tv_complete() {
        if (mIntakeLists.size() > 0) {
            addFood();
        }
        finish();
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
        initRecycler();
    }


    private void initRecycler() {
        initData();
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<FoodInfoItem.ListBean, BaseViewHolder>(R.layout.item_add_foods) {
            @Override
            protected void convert(BaseViewHolder helper, FoodInfoItem.ListBean item) {
                helper.setText(R.id.tv_food, item.getFoodName());
                helper.setText(R.id.tv_foodWeight, RxFormat.setFormatNum(item.getUnitCount(), "0.0") + item.getUnit());
                helper.setText(R.id.tv_foodKcal, item.getHeat() + getString(R.string.unit_kcal));
                loadCricle(item.getFoodImg(), (ImageView) helper.getView(R.id.img_food));
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showAddFoodDialog((FoodInfoItem.ListBean) adapter.getData().get(position));
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    private void showAddFoodDialog(final FoodInfoItem.ListBean item) {
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

                mIntakeLists.add(intakeList);
                mark.setVisibility(View.VISIBLE);
                tv_mark.setText(mIntakeLists.size() + "");
                dialog.dismiss();
            }
        });
    }

    private void addFood() {
        String userId = mPrefs.UserId().getOr("testuser");
        RxLogUtils.d("用户ID" + userId);
        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setUserId(userId);
        foodItem.setAddDate(ADD_FOOD_DATE);
        foodItem.setEatType(ADD_FOOD_TYPE);
        foodItem.setIntakeLists(mIntakeLists);
        String s = new Gson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxBus.getInstance().post(mIntakeLists.size() > 0);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initData() {
        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getFoodInfo(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        FoodInfoItem item = new Gson().fromJson(s, FoodInfoItem.class);
                        List<FoodInfoItem.ListBean> beans = item.getList();
                        adapter.setNewData(beans);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
