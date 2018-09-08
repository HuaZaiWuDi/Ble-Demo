package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.antishake.AntiShake;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.CollectBean;
import lab.wesmartclothing.wefit.flyso.rxbus.GoToFind;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

import static com.chad.library.adapter.base.BaseQuickAdapter.EMPTY_VIEW;

/**
 * Created by jk on 2018/8/10.
 */
public class CollectFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.rv_collect)
    SwipeMenuRecyclerView mRvCollect;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        pageNum = 1;
        initData();
    }

    private void initRecycler() {
        /*侧滑删除*/
        mRvCollect.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCollect.setSwipeItemClickListener(mItemClickListener);
        mRvCollect.setSwipeMenuCreator(mSwipeMenuCreator);
        mRvCollect.setSwipeMenuItemClickListener(mMenuItemClickListener);
        adapter = new BaseQuickAdapter<CollectBean.ListBean, BaseViewHolder>(R.layout.item_collect) {

            @Override
            protected void convert(BaseViewHolder helper, CollectBean.ListBean item) {
                MyAPP.getImageLoader().displayImage(mActivity, item.getCoverPicture(), (ImageView) helper.getView(R.id.iv_img));

                helper.setText(R.id.tv_title, item.getArticleName())
                        .setText(R.id.tv_content, item.getSummary())
//                        .setText(R.id.tv_readCount, "Timetofit\t\t\t" + "w\t次阅读")
                ;
            }
        };
        emptyView = View.inflate(mContext, R.layout.layout_no_data, null);
        emptyView.findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去发现
                RxBus.getInstance().post(new GoToFind());
                onBackPressed();
            }
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
        mQMUIAppBarLayout.setTitle("我的收藏");
    }


    /**
     * RecyclerView的Item中的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                deleteItemById(adapterPosition);
            }
        }
    };

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            if (viewType == EMPTY_VIEW) {
                return;
            }
            int width = RxUtils.dp2px(52);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem closeItem = new SwipeMenuItem(mActivity)
                    .setBackgroundColorResource(R.color.Gray)
                    .setImage(R.mipmap.icon_delete_write)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(closeItem); // 添加菜单到右侧。
        }
    };

    /**
     * RecyclerView的Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            RxLogUtils.d("收藏：" + position);
            if (AntiShake.getInstance().check()) return;
            CollectBean.ListBean bean = (CollectBean.ListBean) adapter.getData().get(position);
            Bundle bundle = new Bundle();
            //打开URL
            bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Detail + bean.getArticleId() + "&isgo=1");
            RxActivityUtils.skipActivity(mActivity, CollectWebActivity.class, bundle);
        }
    };


    private void deleteItemById(final int position) {
        String gid = ((CollectBean.ListBean) adapter.getData().get(position)).getGid();
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.removeCollection(gid))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        //如果成功，刷新列表，不加载数据
                        adapter.remove(position);
                        if (adapter.getData().size() == 0) {
                            adapter.setEmptyView(emptyView);
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    public void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.collectionList(pageNum, 10))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("collectionList", String.class, CacheStrategy.cacheAndRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        CollectBean collectBean = MyAPP.getGson().fromJson(s, CollectBean.class);
                        List<CollectBean.ListBean> list = collectBean.getList();
                        RxLogUtils.d("收藏信息：" + list.size());
                        RxLogUtils.d("页码：" + pageNum);
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
                        if (adapter.getData().size() == 0) {
                            adapter.setEmptyView(emptyView);
                        }
                    }

                    @Override
                    public void _onError(String e) {
                        RxToast.error(e);
                        if (smartRefreshLayout.isLoading())
                            smartRefreshLayout.finishLoadMore(false);
                        if (smartRefreshLayout.isRefreshing())
                            smartRefreshLayout.finishRefresh(false);
                    }
                });
    }

}
