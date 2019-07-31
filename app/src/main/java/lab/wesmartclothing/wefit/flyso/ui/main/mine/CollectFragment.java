package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.antishake.AntiShake;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.CollectBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.GoToMainPage;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;


/**
 * Created by jk on 2018/8/10.
 */
public class CollectFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.rv_collect)
    RecyclerView mRvCollect;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;

    private BaseQuickAdapter adapter;
    private int pageNum = 1;
    private View emptyView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_collect_);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTopBar();
        initRecycler();
        pageNum = 1;
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initRecycler() {
        /*侧滑删除*/
        mRvCollect.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<CollectBean.ListBean, BaseViewHolder>(R.layout.item_collect) {

            @Override
            protected void convert(BaseViewHolder helper, CollectBean.ListBean item) {
                MyAPP.getImageLoader().displayImage(mActivity, item.getCoverPicture(), helper.getView(R.id.iv_img));

                helper.setText(R.id.tv_title, item.getArticleName())
                        .setText(R.id.tv_content, item.getSummary())
                        .addOnClickListener(R.id.img_delete);
            }
        };
        emptyView = View.inflate(mContext, R.layout.layout_no_data, null);
        emptyView.findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去发现
                RxBus.getInstance().post(new GoToMainPage(2));
                onBackPressed();
            }
        });

        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if (AntiShake.getInstance().check()) return;
            if (view.getId() == R.id.img_delete) {
                deleteItemById(position);
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            RxLogUtils.d("收藏：" + position);
            if (AntiShake.getInstance().check() || RxDataUtils.isEmpty(adapter.getData())) return;
            CollectBean.ListBean bean = (CollectBean.ListBean) adapter.getData().get(Math.max(0, Math.min(position, adapter.getData().size())));
            Bundle bundle = new Bundle();
            //打开URL
            bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Detail + bean.getArticleId() + "&isgo=1");
            bundle.putSerializable(Key.BUNDLE_DATA, bean);
            RxActivityUtils.skipActivity(mActivity, CollectWebActivity.class, bundle);
        });

        mRvCollect.setAdapter(adapter);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                initData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNum = 1;
                initData();
            }
        });

        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle(R.string.myCollect);
    }


    private void deleteItemById(final int position) {
        String gid = ((CollectBean.ListBean) adapter.getData().get(position)).getGid();
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().removeCollection(gid))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        //如果成功，刷新列表，不加载数据
                        adapter.remove(position);
                        if (adapter.getData().size() == 0) {
                            smartRefreshLayout.autoRefresh();
                        }
                        RxBus.getInstance().post(new RefreshMe());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    public void initData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().collectionList(pageNum, 10))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("collectionList" + pageNum, String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        CollectBean collectBean = JSON.parseObject(s, CollectBean.class);
                        List<CollectBean.ListBean> list = collectBean.getList();
                        if (pageNum == 1) {
                            adapter.setNewData(list);
                        } else {
                            adapter.addData(list);
                        }


                        if (smartRefreshLayout.isLoading()) {
                            pageNum++;
                            smartRefreshLayout.finishLoadMore(true);
                        }
                        if (smartRefreshLayout.isRefreshing())
                            smartRefreshLayout.finishRefresh(true);
                        smartRefreshLayout.setEnableLoadMore(collectBean.isHasNextPage());

                        if (collectBean.getTotal() == 0) {
                            adapter.setEmptyView(emptyView);
                        } else {
                            if (adapter.getData().size() == 0) {
                                smartRefreshLayout.autoRefresh();
                            }
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.normal(error, code);
                        if (smartRefreshLayout.isLoading())
                            smartRefreshLayout.finishLoadMore(false);
                        if (smartRefreshLayout.isRefreshing())
                            smartRefreshLayout.finishRefresh(false);
                    }

                });
    }

}
