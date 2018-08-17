package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.vondear.rxtools.activity.RxActivityUtils;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.CollectBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.main.CollectWebActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

import static com.chad.library.adapter.base.BaseQuickAdapter.EMPTY_VIEW;

/**
 * Created by jk on 2018/8/10.
 */
public class CollectFragment extends BaseAcFragment {

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

    public static QMUIFragment getInstance() {
        return new CollectFragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_collect_, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        initRecycler();
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
                Glide.with(mActivity).load(item.getCoverPicture())
                        .asBitmap().placeholder(R.mipmap.icon_placeholder).into((ImageView) helper.getView(R.id.iv_img));
                helper.setText(R.id.tv_title, item.getArticleName())
                        .setText(R.id.tv_content, item.getSummary())
                        .setText(R.id.tv_readCount, "Timetofit\t\t\t" + "w\t次阅读");
            }
        };
        emptyView = View.inflate(mContext, R.layout.layout_no_data, null);
        emptyView.findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去发现
            }
        });

        adapter.setEmptyView(emptyView);
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

        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
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
            SwipeMenuItem closeItem = new SwipeMenuItem(getActivity())
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
            CollectBean.ListBean bean = (CollectBean.ListBean) adapter.getData().get(position);
            Bundle bundle = new Bundle();
            //打开URL
            bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Detail + bean.getArticleId() + "&isgo=1");
            RxActivityUtils.skipActivity(mActivity, CollectWebActivity.class, bundle);
        }
    };


    private void deleteItemById(final int position) {
        String gid = ((CollectBean.ListBean) adapter.getData().get(0)).getGid();
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
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        CollectBean collectBean = new Gson().fromJson(s, CollectBean.class);
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
