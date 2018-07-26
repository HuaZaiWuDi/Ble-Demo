package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodHistoryItem;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.entity.ListBean;
import lab.wesmartclothing.wefit.flyso.entity.section.HeatFoodSection;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AddOrUpdateFoodDialog;
import lab.wesmartclothing.wefit.flyso.view.CircleMenuView;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;
import lab.wesmartclothing.wefit.flyso.view.RoundView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.config.Constants;
import okhttp3.RequestBody;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
@EFragment()
public class HeatFragment extends BaseAcFragment {

    @ViewById
    SwipeMenuRecyclerView mRecyclerView;
    @ViewById
    SmoothRefreshLayout refresh;
    @ViewById
    CircleMenuView menu;
    @ViewById
    DateChoose mDateChoose;
    @ViewById
    RelativeLayout parent;
    @ViewById
    LinearLayout head_bg;
    @ViewById
    TextView tv_consume;
    @ViewById
    TextView tv_intake;
    @ViewById
    RoundView mRoundView;


    @Bean
    AddOrUpdateFoodDialog mAddOrUpdateFoodDialog;

    private int[] foodTypeRes_green = {R.mipmap.breakfast_green_icon, R.mipmap.lunch_green_icon,
            R.mipmap.dinner_green_icon, R.mipmap.midnight_snack_green_icon, R.mipmap.snack_green_icon};
    private int[] foodTypeRes_red = {R.mipmap.breakfast_red_icon, R.mipmap.lunch_red_icon,
            R.mipmap.dinner_red_icon, R.mipmap.midnight_snack_red_icon, R.mipmap.snack_red_icon};


    public boolean isComplete = false;
    private String date;


    public static HeatFragment getInstance() {
        return new HeatFragment_();
    }


    @AfterViews
    public void initView() {
        date = System.currentTimeMillis() + "";
        initDate();
        initRecycler();
        initMenu();
        initRxBus();
    }

    private void initRxBus() {
        Disposable register = RxBus.getInstance().register(Boolean.class, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean)
                    notifyData(date);
            }
        });
        RxBus.getInstance().addSubscription(this, register);
    }

    private void initMenu() {
        final String[] add_food = getResources().getStringArray(R.array.add_food);
        menu.setOnMenuClickListener(new CircleMenuView.OnMenuClickListener() {
            @Override
            public void menuClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Key.ADD_FOOD_NAME, add_food[position]);
                bundle.putInt(Key.ADD_FOOD_TYPE, position + 1);
                bundle.putString(Key.ADD_FOOD_DATE, date);
                RxActivityUtils.skipActivity(mActivity, AddFoodActivity_.class, bundle);
            }

            @Override
            public void menuOpen(boolean isOpen) {
            }
        });
    }

    private void initDate() {
        mDateChoose.setOnDateChangeListener(new DateChoose.OnDateChangeListener() {
            @Override
            public void onDateChangeListener(int year, int month, int day, long millis) {
                notifyData(millis + "");
            }
        });
    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
        notifyData(date);
    }

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_heat, null);
        return rootView;
    }

    private void deleteData(String gid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("userId", SPUtils.getString(SPKey.SP_UserId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.removeHeatInfo(body))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        notifyData(date);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void notifyData(String date) {
        this.date = date;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("heatDate", date);
        RxLogUtils.d("时间：" + date);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getHeatHistory(body))
                .compose(MyAPP.getRxCache().<String>transformObservable("getHeatHistory", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("网络请求：" + s);
                        handleData(s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }

                });
    }

    private void handleData(String s) {
        FoodHistoryItem foodHistoryItem = new Gson().fromJson(s, FoodHistoryItem.class);
        isComplete = !foodHistoryItem.isWarning();
        tv_intake.setText("" + foodHistoryItem.getIntake());
        tv_consume.setText("" + foodHistoryItem.getDepletion());

        int ableIntake = foodHistoryItem.getAbleIntake() + foodHistoryItem.getIntake();
        mRoundView.setCentreText(foodHistoryItem.getAbleIntake(), getString(R.string.can_eatHeat), getString(R.string.unit_kcal2));
        int angle = (int) ((float) foodHistoryItem.getIntake() / (float) ableIntake * 360);

        mRoundView.setSweepAngle(angle);
        switchTheme();
        mBeans.clear();
        List<FoodListBean> foodList_1 = foodHistoryItem.get_$1().getFoodList();
        if (foodList_1.size() > 0) {
            mBeans.add(new HeatFoodSection(true, getString(R.string.breakfast), foodHistoryItem.get_$1().getIntake(), !isComplete ? foodTypeRes_green[0] : foodTypeRes_red[0]));
            for (FoodListBean bean : foodList_1) {
                mBeans.add(new HeatFoodSection(bean, getString(R.string.breakfast)));
            }
        }
        List<FoodListBean> foodList_2 = foodHistoryItem.get_$2().getFoodList();
        if (foodList_2.size() > 0) {
            mBeans.add(new HeatFoodSection(true, getString(R.string.lunch), foodHistoryItem.get_$2().getIntake(), !isComplete ? foodTypeRes_green[1] : foodTypeRes_red[1]));
            for (FoodListBean bean : foodList_2) {
                mBeans.add(new HeatFoodSection(bean, getString(R.string.lunch)));
            }
        }
        List<FoodListBean> foodList_3 = foodHistoryItem.get_$3().getFoodList();
        if (foodList_3.size() > 0) {
            mBeans.add(new HeatFoodSection(true, getString(R.string.dinner), foodHistoryItem.get_$3().getIntake(), !isComplete ? foodTypeRes_green[2] : foodTypeRes_red[2]));
            for (FoodListBean bean : foodList_3) {
                mBeans.add(new HeatFoodSection(bean, getString(R.string.dinner)));
            }
        }
        List<FoodListBean> foodList_4 = foodHistoryItem.get_$4().getFoodList();
        if (foodList_4.size() > 0) {
            mBeans.add(new HeatFoodSection(true, getString(R.string.midnight), foodHistoryItem.get_$4().getIntake(), !isComplete ? foodTypeRes_green[3] : foodTypeRes_red[3]));
            for (FoodListBean bean : foodList_4) {
                mBeans.add(new HeatFoodSection(bean, getString(R.string.midnight)));
            }
        }
        List<FoodListBean> foodList_5 = foodHistoryItem.get_$5().getFoodList();
        if (foodList_5.size() > 0) {
            mBeans.add(new HeatFoodSection(true, getString(R.string.snack), foodHistoryItem.get_$5().getIntake(), !isComplete ? foodTypeRes_green[4] : foodTypeRes_red[4]));
            for (FoodListBean bean : foodList_5) {
                mBeans.add(new HeatFoodSection(bean, getString(R.string.snack)));
            }
        }
        if (mBeans.size() > 0) {
            refresh.setState(Constants.STATE_CONTENT);
        } else {
            refresh.setState(Constants.STATE_EMPTY);
        }
        sectionQuickAdapter.setNewData(mBeans);
    }

    private void switchTheme() {
        menu.setComplete(isComplete);
        head_bg.setBackgroundResource(isComplete ? R.mipmap.energy_red_bg_down : R.drawable.energy_green_bg_down);
        B.broadUpdate(mActivity, Key.ACTION_SWITCH_THEME, Key.EXTRA_SWITCH_THEME, isComplete);
    }

    private List<HeatFoodSection> mBeans = new ArrayList<>();
    private BaseSectionQuickAdapter sectionQuickAdapter;

    private void initRecycler() {
        //滑动冲突处理
//        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    //允许ScrollView截断点击事件，ScrollView可滑动
//                    mScrollView.requestDisallowInterceptTouchEvent(false);
//                } else {
//                    //不允许ScrollView截断点击事件，点击事件由子View处理
//                    mScrollView.requestDisallowInterceptTouchEvent(true);
//                }
//                return false;
//            }
//        });

        refresh.setEnableOverScroll(true);
        refresh.setEnablePullToRefresh(false);
        refresh.setState(Constants.STATE_EMPTY);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        sectionQuickAdapter = new BaseSectionQuickAdapter<HeatFoodSection, BaseViewHolder>(R.layout.heat_item, R.layout.heat_item_title, mBeans) {
            @Override
            protected void convert(BaseViewHolder helper, HeatFoodSection item) {
                Glide.with(mContext)
                        .load(item.t.getFoodImg())
                        .bitmapTransform(new CropCircleTransformation(mContext))//圆角图片
                        .error(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) helper.getView(R.id.img_food));
                helper.setText(R.id.tv_foodName, item.t.getFoodName());
                helper.setText(R.id.tv_foodWeight, item.t.getFoodCount() + "" + item.t.getUnit());
                helper.setText(R.id.tv_kcal, item.t.getCalorie() + getString(R.string.unit_kcal));
            }

            @Override
            protected void convertHead(BaseViewHolder helper, HeatFoodSection item) {
                helper.setImageResource(R.id.img, item.getHeadImg());
                helper.setText(R.id.tv_title, item.header);
                helper.setText(R.id.tv_kcal, item.getIntake() + getString(R.string.unit_kcal));
                helper.setTextColor(R.id.tv_kcal, getResources().getColor(isComplete ? R.color.colorRed : R.color.colorTheme));
            }
        };

        sectionQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (RxUtils.isFastClick(1000)) return;
                if (mBeans.get(position).isHeader) return;
                ListBean bean = new ListBean();
                final FoodListBean foodListBean = mBeans.get(position).t;
                bean.setFoodImg(foodListBean.getFoodImg());
                bean.setFoodName(foodListBean.getFoodName());
                bean.setFoodUnit(foodListBean.getUnit());
                bean.setFoodCount(foodListBean.getFoodCount());
                bean.setGid(foodListBean.getGid());
                bean.setFoodId(foodListBean.getFoodId());
                bean.setHeatDate(date);


                mAddOrUpdateFoodDialog.setFoodInfo(mActivity, false, bean, new AddOrUpdateFoodDialog.AddOrUpdateFoodListener() {
                    @Override
                    public void complete(AddFoodItem.intakeList item) {
                        updateFood(foodListBean, item);
                    }
                });

            }
        });

        sectionQuickAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mBeans.get(position).isHeader) return true;
                showDeleteDialog(position);
                return true;
            }
        });

        mRecyclerView.setAdapter(sectionQuickAdapter);
    }

    private void showDeleteDialog(final int position) {
        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
        dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
        dialog.getTvContent().setText("是否删除？");
        dialog.setCancel("删除");
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HeatFoodSection section = mBeans.get(position);
                deleteData(section.t.getGid());
            }
        });
        dialog.setSure("取消");
        dialog.show();
        dialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateFood(FoodListBean foodListBean, AddFoodItem.intakeList item) {
        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setUserId(SPUtils.getString(SPKey.SP_UserId));
        foodItem.setAddDate(date);
        foodItem.setEatType(foodListBean.getEatType());
        ArrayList<AddFoodItem.intakeList> lists = new ArrayList<>();
        lists.add(item);
        foodItem.setIntakeLists(lists);
        String s = new Gson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        notifyData(date);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }
}
