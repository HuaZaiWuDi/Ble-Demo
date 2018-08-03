package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.OverlapLayoutManager;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.view.DateChoose;

/**
 * Created by jk on 2018/8/2.
 */
public class HeatDetailFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mDateChoose)
    DateChoose mMDateChoose;
    @BindView(R.id.tv_title_Ingestion)
    TextView mTvTitleIngestion;
    @BindView(R.id.tv_Ingestion)
    TextView mTvIngestion;
    @BindView(R.id.pro_heat)
    QMUIProgressBar mProHeat;
    @BindView(R.id.tv_heat_title)
    TextView mTvHeatTitle;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_heatUnit)
    TextView mTvHeatUnit;
    @BindView(R.id.tv_title_consume)
    TextView mTvTitleConsume;
    @BindView(R.id.tv_consume)
    TextView mTvConsume;
    @BindView(R.id.iv_breakfast)
    ImageView mIvBreakfast;
    @BindView(R.id.tv_breakfast)
    TextView mTvBreakfast;
    @BindView(R.id.tv_breakfast_kcal)
    TextView mTvBreakfastKcal;
    @BindView(R.id.layout_breakfast)
    QMUIRoundRelativeLayout mLayoutBreakfast;
    @BindView(R.id.iv_lunch)
    ImageView mIvLunch;
    @BindView(R.id.tv_lunch)
    TextView mTvLunch;
    @BindView(R.id.tv_lunch_kcal)
    TextView mTvLunchKcal;
    @BindView(R.id.layout_lunch)
    QMUIRoundRelativeLayout mLayoutLunch;
    @BindView(R.id.iv_dinner)
    ImageView mIvDinner;
    @BindView(R.id.tv_dinner)
    TextView mTvDinner;
    @BindView(R.id.tv_dinner_kcal)
    TextView mTvDinnerKcal;
    @BindView(R.id.layout_dinner)
    QMUIRoundRelativeLayout mLayoutDinner;
    @BindView(R.id.iv_meal)
    ImageView mIvMeal;
    @BindView(R.id.tv_meal)
    TextView mTvMeal;
    @BindView(R.id.tv_meal_kcal)
    TextView mTvMealKcal;
    @BindView(R.id.layout_meal)
    QMUIRoundRelativeLayout mLayoutMeal;
    Unbinder unbinder;
    @BindView(R.id.recycler_breakfast)
    RecyclerView mRecyclerBreakfast;
    @BindView(R.id.recycler_lunch)
    RecyclerView mRecyclerLunch;
    @BindView(R.id.recycler_dinner)
    RecyclerView mRecyclerDinner;
    @BindView(R.id.recycler_meal)
    RecyclerView mRecyclerMeal;

    public static QMUIFragment getInstance() {
        return new HeatDetailFragment();
    }

    private BaseQuickAdapter adapterBreakfast, adapterLunch, adapterDinner, adapterMeal;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_heat_detail, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void initData() {
        adapterBreakfast.addData(R.drawable.bigen);
        adapterBreakfast.addData(R.drawable.bing);
        adapterBreakfast.addData(R.drawable.eg);
        adapterBreakfast.addData(R.drawable.tc);

        adapterLunch.addData(R.drawable.bigen);
        adapterLunch.addData(R.drawable.bing);
        adapterLunch.addData(R.drawable.eg);
        adapterLunch.addData(R.drawable.tc);

        adapterDinner.addData(R.drawable.bigen);
        adapterDinner.addData(R.drawable.bing);
        adapterDinner.addData(R.drawable.eg);
        adapterDinner.addData(R.drawable.tc);

        adapterMeal.addData(R.drawable.bigen);
        adapterMeal.addData(R.drawable.bing);
        adapterMeal.addData(R.drawable.eg);
        adapterMeal.addData(R.drawable.tc);

    }

    private void initView() {
        mRecyclerBreakfast.setLayoutManager(new OverlapLayoutManager(mContext));
        mRecyclerLunch.setLayoutManager(new OverlapLayoutManager(mContext));
        mRecyclerDinner.setLayoutManager(new OverlapLayoutManager(mContext));
        mRecyclerMeal.setLayoutManager(new OverlapLayoutManager(mContext));
        adapterBreakfast = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.img_food, item);
            }
        };
        adapterLunch = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.img_food, item);
            }
        };
        adapterDinner = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.img_food, item);
            }
        };
        adapterMeal = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_food_img) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.img_food, item);
            }
        };
        mRecyclerBreakfast.setAdapter(adapterBreakfast);
        mRecyclerLunch.setAdapter(adapterLunch);
        mRecyclerDinner.setAdapter(adapterDinner);
        mRecyclerMeal.setAdapter(adapterMeal);
    }


    @OnClick({R.id.recycler_breakfast, R.id.layout_breakfast, R.id.recycler_lunch, R.id.layout_lunch, R.id.recycler_dinner, R.id.layout_dinner, R.id.recycler_meal, R.id.layout_meal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recycler_breakfast:
                break;
            case R.id.layout_breakfast:
                break;
            case R.id.recycler_lunch:
                break;
            case R.id.layout_lunch:
                break;
            case R.id.recycler_dinner:
                break;
            case R.id.layout_dinner:
                break;
            case R.id.recycler_meal:
                break;
            case R.id.layout_meal:
                break;
        }
    }
}
