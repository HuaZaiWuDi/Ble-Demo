package lab.wesmartclothing.wefit.flyso.ui.main.ranking;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.textview.RxTextviewVertical;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.RankingBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.ui.guide.SplashActivity;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.ranking
 * @FileName RankingFragment
 * @Date 2019/6/27 14:32
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class RankingFragment extends BaseAcFragment {

    @BindView(R.id.tv_ranking)
    TextView mTvRanking;
    @BindView(R.id.tv_reduceWeight)
    TextView mTvReduceWeight;
    Unbinder unbinder;
    @BindView(R.id.img_No1)
    RxImageView mImgNo1;
    @BindView(R.id.tv_no1)
    TextView mTvNo1;
    @BindView(R.id.recycler_ranking)
    RecyclerView mRecyclerRanking;
    @BindView(R.id.runText)
    RxTextviewVertical mRunText;

    public static RankingFragment getInstance() {
        return new RankingFragment();
    }

    private BaseQuickAdapter adapter;

    @Override
    protected int layoutId() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected void initViews() {
        super.initViews();

        initRecycler();
        initText();
//        RxTextUtils.getBuilder("参与用户 " + 2020 + "人\n你的排名\n")
//                .append("230").setProportion(3f)
//                .append("名").into(mTvRanking);
//
//        RxTextUtils.getBuilder("累计减重 " + 2020 + "KG\n成功减重\n")
//                .append("3").setProportion(3f)
//                .append("KG").into(mTvReduceWeight);
//
//        mTvNo1.setText("草帽\t\t\t\t\t\t\t0304\t\t\t\t\t\t\t10KG\t\t\t\t\t\t\t20天");
    }

    private void initText() {
        RxLogUtils.d("Tip" + SplashActivity.weightTipList);
        List<String> tips = JSON.parseObject(SplashActivity.weightTipList, new TypeToken<List<String>>() {
        }.getType());
        mRunText.setAnimTime(500);
        mRunText.setTextStillTime(2000);
        mRunText.setText(RxUtils.sp2px(13), RxUtils.dp2px(20), ContextCompat.getColor(mContext, R.color.GrayWrite));
        if (!RxDataUtils.isEmpty(tips))
            mRunText.setTextList(tips);
    }

    private void initRecycler() {
        mRecyclerRanking.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerRanking.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter = new BaseQuickAdapter<RankingBean, BaseViewHolder>(R.layout.item_ranking) {
            @Override
            protected void convert(BaseViewHolder helper, RankingBean item) {
                String position = item.getRanking();

                if ("01".equals(position)) {
                    helper.setBackgroundRes(R.id.parent, R.mipmap.bg_no1)
                            .setBackgroundRes(R.id.tv_ranking, R.mipmap.ic_ranking_no1);
                } else if ("02".equals(position)) {
                    helper.setBackgroundRes(R.id.parent, R.mipmap.bg_no2)
                            .setBackgroundRes(R.id.tv_ranking, R.mipmap.ic_ranking_no2);
                } else if ("03".equals(position)) {
                    helper.setBackgroundRes(R.id.parent, R.mipmap.bg_no3)
                            .setBackgroundRes(R.id.tv_ranking, R.mipmap.ic_ranking_no3);
                } else {
                    helper.setBackgroundColor(R.id.parent, 0)
                            .setBackgroundRes(R.id.tv_ranking, 0);
                }

                if (Arrays.asList("01", "02", "03").contains(position)) {
                    helper
                            .setTextColor(R.id.tv_nickname, ContextCompat.getColor(mContext, R.color.white))
                            .setTextColor(R.id.tv_phone, ContextCompat.getColor(mContext, R.color.white))
                            .setTextColor(R.id.tv_loseWeight, ContextCompat.getColor(mContext, R.color.white))
                            .setTextColor(R.id.tv_duration, ContextCompat.getColor(mContext, R.color.white))
                            .setText(R.id.tv_ranking, "");
                } else {
                    helper
                            .setTextColor(R.id.tv_nickname, ContextCompat.getColor(mContext, R.color.Gray))
                            .setTextColor(R.id.tv_phone, ContextCompat.getColor(mContext, R.color.Gray))
                            .setTextColor(R.id.tv_loseWeight, ContextCompat.getColor(mContext, R.color.Gray))
                            .setTextColor(R.id.tv_duration, ContextCompat.getColor(mContext, R.color.Gray))
                            .setText(R.id.tv_ranking, position);
                }

                if (!RxDataUtils.isNullString(position)) {
                    helper
                            .setText(R.id.tv_nickname, item.getUserName())
                            .setText(R.id.tv_phone, item.getPhone())
                            .setText(R.id.tv_loseWeight, String.format("%.1f", Math.abs(item.getLoseWeight() * 2)) + "斤")
                            .setText(R.id.tv_duration, item.getTotalDays() + "天");
                    helper.setTypeface(Typeface.DEFAULT,
                            R.id.tv_ranking, R.id.tv_nickname, R.id.tv_phone, R.id.tv_loseWeight, R.id.tv_duration);
                } else if (RxDataUtils.isNullString(position)) {
                    helper.setTypeface(Typeface.DEFAULT_BOLD,
                            R.id.tv_ranking, R.id.tv_nickname, R.id.tv_phone, R.id.tv_loseWeight, R.id.tv_duration);
                    helper.setText(R.id.tv_ranking, "排名")
                            .setText(R.id.tv_nickname, "昵称")
                            .setText(R.id.tv_phone, "手机尾号")
                            .setText(R.id.tv_loseWeight, "减重")
                            .setText(R.id.tv_duration, "耗时");
                }
            }
        };
        mRecyclerRanking.setAdapter(adapter);
    }

    @Override
    protected void initNetData() {
        super.initNetData();
        NetManager.getApiService().lastTopUser(10, 30)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("lastTopUser", String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        List<RankingBean> rankingBean = JSON.parseObject(s, new TypeToken<List<RankingBean>>() {
                        }.getType());
                        if (!RxDataUtils.isEmpty(rankingBean))
                            updateUI(rankingBean);
                    }
                });


        NetManager.getApiService().rankingSummary()
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("rankingSummary", String.class,
                        CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        /**
                         * 返回数组的含义分别是：参与用户、累计减重、我的排名、我的减重
                         * */
                        List<Double> rankingBean = JSON.parseObject(s, new TypeToken<List<Double>>() {
                        }.getType());

                        if (!RxDataUtils.isEmpty(rankingBean)) {
                            RxTextUtils.getBuilder("参与用户 " + rankingBean.get(0).intValue() + "人\n你的排名\n")
                                    .append(rankingBean.get(2).intValue() + "").setProportion(3f)
                                    .append("名").into(mTvRanking);

                            RxTextUtils.getBuilder("累计减重 " + String.format("%.1f", Math.abs(rankingBean.get(1))) + "KG\n成功减重\n")
                                    .append("" + String.format("%.1f", Math.abs(rankingBean.get(3)))).setProportion(3f)
                                    .append("KG").into(mTvReduceWeight);
                        }
                    }
                });
    }

    private void updateUI(List<RankingBean> rankingBean) {
        RankingBean rankingtitle = new RankingBean();
        rankingBean.add(0, rankingtitle);

        for (int i = 0; i < rankingBean.size(); i++) {
            RankingBean bean = rankingBean.get(i);
            if (i != 0)
                bean.setRanking(String.format("%02d", i));
        }

        adapter.setNewData(rankingBean);

        RankingBean no1Bean = rankingBean.get(1);
        GlideImageLoader.getInstance().displayImage(mActivity, no1Bean.getAvatar(), mImgNo1);

        String nickname = no1Bean.getUserName().length() > 4 ? no1Bean.getUserName().substring(no1Bean.getUserName().length() - 4) : no1Bean.getUserName();
        String phone = no1Bean.getPhone();

        mTvNo1.setText(nickname + "\t\t\t\t\t\t" + phone + "\t\t\t\t\t\t" +
                String.format("%.1f", Math.abs(no1Bean.getLoseWeight() * 2)) + "斤\t\t\t\t\t\t" + no1Bean.getTotalDays() + "天");

    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        mRunText.stopAutoScroll();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        mRunText.startAutoScroll();
    }
}
