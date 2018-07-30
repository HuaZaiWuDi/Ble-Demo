package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

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

    private String gid;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_body_data, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
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
                .compose(MyAPP.getRxCache().<String>transformObservable("fetchWeightDetail" + gid, String.class, CacheStrategy.firstCache()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("心率数据：" + s);
                        WeightDetailsBean detailsBean = new Gson().fromJson(s, WeightDetailsBean.class);
                        mTvDate.setText(RxFormat.setFormatDate(detailsBean.getWeightInfo().getWeightDate(), "yyyy年MM月dd日 HH:mm"));
                        mTvWeight.setText((float) detailsBean.getWeightInfo().getWeight() + "");
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void initRecyclerView() {
        notifyData();

        mMRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(multiItermLists);
        View footerView = LayoutInflater.from(mContext).inflate( R.layout.footer_body_data, null);
        LinearLayout layoutDelete = footerView.findViewById(R.id.layoutDelete);
        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWeight();
            }
        });
        adapter.addFooterView(footerView);
        mMRecyclerView.setAdapter(adapter);
    }

    private void notifyData() {
        String[] titles = getResources().getStringArray(R.array.weightDatas);
        int[] imgs = {R.mipmap.icon_bodyfat, R.mipmap.icon_bmi, R.mipmap.icon_viscera, R.mipmap.icon_muscle,
                R.mipmap.icon_metabolic_rate, R.mipmap.icon_water, R.mipmap.icon_bone, R.mipmap.icon_body_age};

        for (int i = 0; i < titles.length; i++) {
            BodyLevel0Bean level0Bean = new BodyLevel0Bean(imgs[i], titles[i], "", 0f, i == 3);
            BodyLevel1Bean bodyLevel1Bean = new BodyLevel1Bean(0);
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
                        RxLogUtils.d("心率数据：" + s);
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
        startFragment(BodyFatFragment.getInstance());
    }
}
