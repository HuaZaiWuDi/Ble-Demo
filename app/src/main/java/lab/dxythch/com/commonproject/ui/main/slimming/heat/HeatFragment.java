package lab.dxythch.com.commonproject.ui.main.slimming.heat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseFragment;
import lab.dxythch.com.commonproject.entity.AddFoodItem;
import lab.dxythch.com.commonproject.entity.FoodHistoryItem;
import lab.dxythch.com.commonproject.entity.FoodListBean;
import lab.dxythch.com.commonproject.entity.ListBean;
import lab.dxythch.com.commonproject.entity.section.HeatFoodSection;
import lab.dxythch.com.commonproject.netserivce.RetrofitService;
import lab.dxythch.com.commonproject.prefs.Prefs_;
import lab.dxythch.com.commonproject.tools.Key;
import lab.dxythch.com.commonproject.view.AddOrUpdateFoodDialog;
import lab.dxythch.com.commonproject.view.CircleMenuView;
import lab.dxythch.com.commonproject.view.DateChoose;
import lab.dxythch.com.commonproject.view.RoundView;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import lab.dxythch.com.netlib.utils.RxBus;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.config.Constants;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_heat)
public class HeatFragment extends BaseFragment {

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

    @Pref
    Prefs_ mPrefs;

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
        initDialog();
        initDate();
        initRecycler();
        notifyData(RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date));
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

    private void initDialog() {
        tipDialog = new QMUITipDialog.Builder(mActivity)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
    }

    private void initDate() {
        mDateChoose.setOnDateChangeListener(new DateChoose.OnDateChangeListener() {
            @Override
            public void onDateChangeListener(int year, int month, int day, long millis) {
                notifyData(RxFormat.setFormatDate(millis, RxFormat.Date));
            }
        });
    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }

    @Override
    public void initData() {

    }

    private void deleteData(String gid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gid", gid);
            jsonObject.put("userId", mPrefs.UserId().getOr("testuser"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.removeHeatInfo(body))
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
                        RxLogUtils.d("结束：" + s);
//                        removeItem(mBeans, position);
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
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", "testuser");
//            jsonObject.put("heatDate", "2018-05-04");
            jsonObject.put("heatDate", date);
//            jsonObject.put("pageNum", 1);
//            jsonObject.put("pageSize", 10);

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
            RxManager.getInstance().doNetSubscribe(dxyService.getHeatHistory(body))
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
                            RxLogUtils.d("网络请求：" + s);
                            handleData(s);
                        }

                        @Override
                        protected void _onError(String error) {
                            RxToast.error(error);
                        }

                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleData(String s) {
        FoodHistoryItem foodHistoryItem = new Gson().fromJson(s, FoodHistoryItem.class);
        isComplete = !foodHistoryItem.isWarning();
        tv_intake.setText("" + foodHistoryItem.getIntake());
        tv_consume.setText("" + foodHistoryItem.getDepletion());
//        mPrefs.UserId().put(foodHistoryItem.getUserId());

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
                helper.setText(R.id.tv_foodWeight, item.t.getFoodCount() + "" + item.t.getFoodUnit());
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

//        // 创建菜单：
//        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
//            @Override
//            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
//                if (viewType != 0) return;//是headLayout不添加
//                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
//                deleteItem.setBackgroundColor(getResources().getColor(R.color.color_material_Red_600));
//                deleteItem.setText(R.string.delete);
//                deleteItem.setTextColor(getResources().getColor(R.color.white));
//                deleteItem.setTextSize(20);
//                deleteItem.setHeight(LinearLayout.MarginLayoutParams.MATCH_PARENT);
//                deleteItem.setWidth(200);
//                // 各种文字和图标属性设置。
//                rightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
//            }
//        };
//        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);

        // 菜单点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                HeatFoodSection section = mBeans.get(adapterPosition);
                deleteData(section.t.getGid());

//                menu.setVisibility(View.GONE);
//                menuBridge.closeMenu();
            }
        });
        sectionQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ListBean bean = new ListBean();
                final FoodListBean foodListBean = mBeans.get(position).t;
                bean.setFoodImg(foodListBean.getFoodImg());
                bean.setFoodName(foodListBean.getFoodName());
                bean.setUnit(foodListBean.getFoodUnit());
                bean.setUnitCount(foodListBean.getFoodCount());
                bean.setTypeId(foodListBean.getFoodId());
                bean.setGid(foodListBean.getGid());
                bean.setFoodId(foodListBean.getFoodId());
                new AddOrUpdateFoodDialog(mActivity, false, bean, new AddOrUpdateFoodDialog.AddOrUpdateFoodListener() {
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
                showDeleteDialog(position);
                return true;
            }
        });

        mRecyclerView.setAdapter(sectionQuickAdapter);
    }

    private void showDeleteDialog(final int position) {
        QMUIDialog dialog = new QMUIDialog.MessageDialogBuilder(mActivity)
                .setTitle("提示")
                .setMessage("是否删除")
                .addAction(R.string.cancel, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(R.string.delete, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        HeatFoodSection section = mBeans.get(position);
                        deleteData(section.t.getGid());
                    }
                }).show();

    }

    private void updateFood(FoodListBean foodListBean, AddFoodItem.intakeList item) {
        String userId = mPrefs.UserId().getOr("testuser");
        RxLogUtils.d("用户ID" + userId);
        AddFoodItem foodItem = new AddFoodItem();
        foodItem.setUserId(userId);
        foodItem.setAddDate(date);
        foodItem.setEatType(foodListBean.getEatType());
        ArrayList<AddFoodItem.intakeList> lists = new ArrayList<>();
        lists.add(item);
        foodItem.setIntakeLists(lists);
        String s = new Gson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
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
