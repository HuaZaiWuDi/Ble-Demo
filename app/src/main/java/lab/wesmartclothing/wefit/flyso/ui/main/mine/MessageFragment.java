package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
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
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.base.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.entity.MessageBean;
import lab.wesmartclothing.wefit.flyso.entity.ReadedBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.MessageChangeBus;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver;

/**
 * Created by jk on 2018/8/10.
 */
public class MessageFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.rv_collect)
    RecyclerView mRvCollect;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    private BaseQuickAdapter adapter;
    private int pageNum = 1;
    private View emptyView;
    private boolean changeReadState = false;


    @Override
    protected int layoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initRecycler();
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);

    }

    @Override
    protected void initNetData() {
        super.initNetData();
        pageNum = 1;
        initData();
    }


    private void initRecycler() {
        /*侧滑删除*/
        mRvCollect.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<MessageBean.ListBean, BaseViewHolder>(R.layout.item_message) {

            @Override
            protected void convert(BaseViewHolder helper, MessageBean.ListBean item) {
                MyAPP.getImageLoader().displayImage(mActivity, R.mipmap.icon_app, helper.getView(R.id.iv_img));
                helper.setVisible(R.id.iv_redDot, item.getReadState() == 0)
                        .setText(R.id.tv_title, item.getTitle())
                        .setText(R.id.tv_date, RxFormat.setFormatDate(item.getPushTime(), RxFormat.Date_Date2))
                        .setText(R.id.tv_content, item.getContent())
                        .addOnClickListener(R.id.img_delete);
            }
        };

        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if (AntiShake.getInstance().check()) return;
            if (view.getId() == R.id.img_delete) {
                deleteItemById(position);
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (AntiShake.getInstance().check()) return;
            readed(position);
        });

        emptyView = View.inflate(mContext, R.layout.layout_no_message, null);
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

        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
        mQMUIAppBarLayout.setTitle(R.string.messageNotify);
        mQMUIAppBarLayout.addRightTextButton(R.string.readed, R.id.spread_inside)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readAllRequest();
                    }
                });
    }


    public void initData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().message(pageNum, 10))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<String>transformObservable("message", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        MessageBean bean = JSON.parseObject(s, MessageBean.class);
                        List<MessageBean.ListBean> list = bean.getList();
                        if (pageNum == 1) {
                            adapter.setNewData(list);
                        } else {
                            adapter.addData(list);
                        }

                        if (smartRefreshLayout.isLoading()) {
                            pageNum++;
                            smartRefreshLayout.finishLoadMore(true);
                        }
                        if (smartRefreshLayout.isRefreshing()) {
                            smartRefreshLayout.finishRefresh(true);
                        }
                        smartRefreshLayout.setEnableLoadMore(bean.isHasNextPage());
                    }

                    @Override
                    public void _onError(String e, int code) {
                        if (smartRefreshLayout.isLoading())
                            smartRefreshLayout.finishLoadMore(false);
                        if (smartRefreshLayout.isRefreshing())
                            smartRefreshLayout.finishRefresh(false);
                    }
                });
    }

    //是否含有未读
    private boolean hasUnRead() {
        if (adapter != null) {
            for (MessageBean.ListBean bean : (List<MessageBean.ListBean>) adapter.getData()) {
                if (bean.getReadState() != 1) {
                    return true;
                }
            }
        }
        return false;
    }


    private void readAllRequest() {
        if (!hasUnRead()) {
            RxToast.normal(getString(R.string.notunreadMessage));
            return;
        }
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().readedAll())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        List<MessageBean.ListBean> listBeans = adapter.getData();
                        for (MessageBean.ListBean bean : listBeans) {
                            bean.setReadState(1);
                        }
                        adapter.setNewData(listBeans);
                        changeReadState = true;
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    //查看了Message，更新
    private void readed(final int position) {
        final MessageBean.ListBean item = (MessageBean.ListBean) adapter.getItem(position);
        if (item == null) return;
        String gid = item.getGid();
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().readed(gid))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if (item.getReadState() != 1) {
                            item.setReadState(1);
                            adapter.setData(position, item);
                            changeReadState = true;
                        }
                        if (RxDataUtils.isNullString(s)) {
                            return;
                        }

                        ReadedBean readedBean = JSON.parseObject(s, ReadedBean.class);
                        if (readedBean.getNotifyOperation() == MyJpushReceiver.TYPE_OPEN_ACTIVITY) {
                            RxBus.getInstance().post(readedBean.getOpenTarget());
                            onBackPressed();
                        } else if (readedBean.getNotifyOperation() == MyJpushReceiver.TYPE_OPEN_URL) {
                            //打开URL
                            WebTitleActivity.startWebActivity(mActivity, readedBean.getAppTitle(), readedBean.getOpenTarget(), true);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Key.BUNDLE_TITLE, readedBean.getAppTitle());
                            bundle.putString(Key.BUNDLE_DATA, readedBean.getAppContent());
                            bundle.putLong(Key.ADD_FOOD_DATE, readedBean.getEditDate());
                            RxActivityUtils.skipActivity(mContext, MessageDetailsFragment.class, bundle);
                        }
                    }

                    @Override
                    public void _onError(String e, int code) {
                        RxToast.normal(e);
                    }
                });
    }

    private void deleteItemById(final int position) {
        String gid = ((MessageBean.ListBean) adapter.getData().get(position)).getGid();
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().removeAppMessage(gid))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        //如果成功，刷新列表，不加载数据
                        adapter.remove(position);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (changeReadState && !hasUnRead()) {
            RxBus.getInstance().post(new MessageChangeBus());
        }
        super.onBackPressed();
    }

}
