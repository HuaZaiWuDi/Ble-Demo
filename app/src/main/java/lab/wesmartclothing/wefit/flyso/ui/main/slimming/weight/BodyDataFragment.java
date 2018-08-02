package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.adapter.ExpandableItemAdapter;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.WeightDetailsBean;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel0Bean;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel1Bean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.TipDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/7/27.
 */
public class BodyDataFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_bodyFat)
    TextView mTvBodyFat;
    @BindView(R.id.tv_weight)
    TextView mTvWeight;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    Unbinder unbinder;
    private List<MultiItemEntity> multiItermLists = new ArrayList<>();

    public static QMUIFragment getInstance() {
        return new BodyDataFragment();
    }

    private ExpandableItemAdapter adapter;
    private String gid;
    private int[] bodyImgs = {R.mipmap.man_1_1, R.mipmap.man_1_2, R.mipmap.man_1_3,
            R.mipmap.man_2_1, R.mipmap.man_2_2, R.mipmap.man_2_3,
            R.mipmap.man_3_1, R.mipmap.man_3_2, R.mipmap.man_3_2,};
    private int bodyIndex;
    private String[] bodys;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_body_data, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        bodys = getResources().getStringArray(R.array.bodyShape);
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvWeight.setTypeface(typeface);
        gid = getArguments() == null ? "" : getArguments().getString(Key.BUNDLE_WEIGHT_GID);
        initTopBar();
        initRecyclerView();
        initData();


    }

    private void initData() {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchWeightDetail(body))
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchWeightDetail" + gid, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        WeightDetailsBean detailsBean = new Gson().fromJson(s, WeightDetailsBean.class);
                        mTvDate.setText(RxFormat.setFormatDate(detailsBean.getWeightInfo().getWeightDate(), "yyyy年MM月dd日 HH:mm"));
                        mTvWeight.setText((float) detailsBean.getWeightInfo().getWeight() + "");

                        Drawable drawable = getResources().getDrawable(bodyImgs[(detailsBean.getBodyLevel() - 1) % 9]);
                        //一定要加这行！！！！！！！！！！！
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mTvBodyFat.setCompoundDrawables(null, drawable, null, null);
                        mTvBodyFat.setText(bodys[(detailsBean.getBodyLevel() - 1) % 9]);
                        WeightDetailsBean.WeightInfoBean weightInfo = detailsBean.getWeightInfo();
                        notifyData(weightInfo);
                        bodyIndex = detailsBean.getBodyLevel();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initRecyclerView() {
        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ExpandableItemAdapter(multiItermLists);
        mMRecyclerView.setAdapter(adapter);
    }


    private void notifyData(WeightDetailsBean.WeightInfoBean weightInfo) {
        String[] titles = getResources().getStringArray(R.array.weightDatas);
        String[] units = {"%", "", "级", "%", "kcal", "%", "%", "岁"};
        int[] imgs = {R.mipmap.icon_bodyfat, R.mipmap.icon_bmi, R.mipmap.icon_viscera, R.mipmap.icon_muscle,
                R.mipmap.icon_metabolic_rate, R.mipmap.icon_water, R.mipmap.icon_bone, R.mipmap.icon_body_age};
        double[] bodyDatas = new double[8];
        if (weightInfo != null) {
            bodyDatas[0] = weightInfo.getBodyFat();
            bodyDatas[1] = weightInfo.getBmi();
            bodyDatas[2] = weightInfo.getVisfat();
            bodyDatas[3] = weightInfo.getMuscle();
            bodyDatas[4] = weightInfo.getBmr();
            bodyDatas[5] = weightInfo.getWater();
            bodyDatas[6] = weightInfo.getBone();
            bodyDatas[7] = weightInfo.getBodyAge();

            View footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_body_data, null);
            LinearLayout layoutDelete = footerView.findViewById(R.id.layoutDelete);
            layoutDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteWeight();
                }
            });
            adapter.addFooterView(footerView);
        }

        multiItermLists.clear();
        for (int i = 0; i < titles.length; i++) {
            BodyLevel0Bean level0Bean = new BodyLevel0Bean(imgs[i], titles[i], units[i], bodyDatas[i]);
            BodyLevel1Bean bodyLevel1Bean = new BodyLevel1Bean((float) bodyDatas[i]);
            level0Bean.addSubItem(bodyLevel1Bean);
            multiItermLists.add(level0Bean);
        }
    }

    private void deleteWeight() {
        JsonObject object = new JsonObject();
        object.addProperty("gid", gid);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.removeWeightInfo(body))
                .compose(RxComposeUtils.<String>showDialog(new TipDialog(mContext)))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.normal("删除成功");
                        popBackStack();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("身体数据");
    }

    @OnClick(R.id.tv_bodyFat)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putInt(Key.BUNDLE_BODY_INDEX, bodyIndex);
        QMUIFragment instance = BodyFatFragment.getInstance();
        instance.setArguments(bundle);
        startFragment(instance);
    }
}
