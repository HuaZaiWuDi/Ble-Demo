package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.FetchHeatInfoBean;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
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
public class AddedFoodFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.tv_addedNoData)
    TextView mTvAddedNoData;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new AddedFoodFragment();
    }

    private int foodType = 0;
    private BaseQuickAdapter adapter;
    private Bundle bundle;
    private AddOrUpdateFoodDialog dialog = new AddOrUpdateFoodDialog();
    private long currentTime = 0;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_added_food, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initRecyclerView();
        initData();
        initTopBar();
        dialog.setLifecycleSubject(lifecycleSubject);
        dialog.setDeleteFoodListener(new AddOrUpdateFoodDialog.DeleteFoodListener() {
            @Override
            public void deleteFood(FoodListBean listBean) {
                int exist = dialog.isExist(adapter.getData(), listBean);
                RxLogUtils.d("下标：" + exist);
                if (exist >= 0)
                    adapter.remove(exist);
                mTvAddedNoData.setVisibility(adapter.getData().isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        dialog.setAddOrUpdateFoodListener(new AddOrUpdateFoodDialog.AddOrUpdateFoodListener() {
            @Override
            public void complete(FoodListBean listBean) {
                int exist = dialog.isExist(adapter.getData(), listBean);
                if (exist >= 0) {
                    adapter.setData(exist, listBean);
                    updateFood(listBean);
                }
            }
        });
    }

    private void initData() {
        bundle = getArguments();
        if (bundle != null) {
            foodType = bundle.getInt(Key.ADD_FOOD_TYPE);
            currentTime = bundle.getLong(Key.ADD_FOOD_DATE);
            String heatData = bundle.getString(Key.ADD_FOOD_INFO);
            FetchHeatInfoBean bean = MyAPP.getGson().fromJson(heatData, FetchHeatInfoBean.class);
            RxLogUtils.d("加载食材：" + bean);
            if (bean != null) {
                List<FoodListBean> list = null;
                switch (foodType) {
                    case HeatDetailFragment.TYPE_BREAKFAST:
                        list = bean.getBreakfast().getFoodList();
                        break;
                    case HeatDetailFragment.TYPE_LUNCH:
                        list = bean.getLunch().getFoodList();
                        break;
                    case HeatDetailFragment.TYPE_DINNER:
                        list = bean.getDinner().getFoodList();
                        break;
                    case HeatDetailFragment.TYPED_MEAL:
                        list = bean.getSnacks().getFoodList();
                        break;
                }
                adapter.setNewData(list);
            }

            if (FoodDetailsFragment.addedLists.size() > 0) {
                if (adapter.getData().isEmpty()) {
                    adapter.setNewData(FoodDetailsFragment.addedLists);
                } else
                    adapter.addData(FoodDetailsFragment.addedLists);
            }

            mTvAddedNoData.setVisibility(adapter.getData().isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void initRecyclerView() {
        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<FoodListBean, BaseViewHolder>(R.layout.item_add_food) {
            @Override
            protected void convert(BaseViewHolder helper, FoodListBean item) {
                QMUIRadiusImageView foodImg = helper.getView(R.id.iv_foodImg);

                MyAPP.getImageLoader().displayImage(mActivity, item.getFoodImg(), foodImg);

                helper.setText(R.id.tv_foodName, item.getFoodName());
                TextView foodKcal = helper.getView(R.id.tv_foodKcal);
                RxTextUtils.getBuilder(item.getCalorie() + "")
                        .append("kcal/")
                        .setProportion(0.6f)
                        .setForegroundColor(getResources().getColor(R.color.orange_FF7200))
                        .append(RxFormat.setFormatNum(item.getFoodCount(), "0.0") + item.getUnit())
                        .setProportion(0.6f)
                        .setForegroundColor(getResources().getColor(R.color.GrayWrite))
                        .into(foodKcal);
            }
        };
        View footerView = View.inflate(mContext, R.layout.footer_added_food, null);
        footerView.findViewById(R.id.layout_addFood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转添加食物
                QMUIFragment instance = FoodDetailsFragment.getInstance();
                instance.setArguments(bundle);
                startFragment(instance);
            }
        });
        adapter.addFooterView(footerView);
        mMRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //显示删除
                dialog.setFoodInfo(mContext, true, foodType, currentTime, (FoodListBean) adapter.getData().get(position));
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                final RxDialogSureCancel deleteDialog = new RxDialogSureCancel(mActivity);
                deleteDialog.setCanceledOnTouchOutside(false);
                deleteDialog.getTvTitle().setVisibility(View.GONE);
                deleteDialog.setContent("确定要删除此条记录么？");
                deleteDialog.setCancel(getString(R.string.confirm)).setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                        dialog.deleteData(mContext, ((FoodListBean) adapter.getItem(position)));
                    }
                })
                        .setSure(getString(R.string.cancel))
                        .setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteDialog.dismiss();
                            }
                        }).show();

                return true;
            }
        });
    }


    private void initTopBar() {
        final String[] add_food = getResources().getStringArray(R.array.add_food);

        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle(add_food[HeatDetailFragment.FOOD_TYPE(foodType)].substring(2, 4));
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

    private void updateFood(FoodListBean foodListBean) {
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
        foodItem.setEatType(foodType);
        ArrayList<AddFoodItem.intakeList> lists = new ArrayList<>();
        lists.add(intakeList);
        foodItem.setIntakeLists(lists);
        String s = MyAPP.getGson().toJson(foodItem);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addHeatInfo(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("修改食物成功");
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.normal(error);
                    }
                });
    }

}
