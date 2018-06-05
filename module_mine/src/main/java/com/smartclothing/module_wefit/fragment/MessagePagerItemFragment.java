package com.smartclothing.module_wefit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.activity.MessageDetailActivity;
import com.smartclothing.module_wefit.adapter.MessageRvAdapter;
import com.smartclothing.module_wefit.bean.Message;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.vondear.rxtools.fragment.FragmentLazy;
import com.vondear.rxtools.utils.RxLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.footer.ClassicFooter;
import me.dkzwm.widget.srl.extra.header.ClassicHeader;

import static android.app.Activity.RESULT_OK;

/**
 * "消息中viewPager的填充fragment"
 */
public class MessagePagerItemFragment extends FragmentLazy {
    private int msgType = 2;
    private RecyclerView rv;
    private MessageRvAdapter adapter;
    private SmoothRefreshLayout mRefreshLayout;
    private List<Message> firstPageData, nextPageData, totalData;
    private final static int REQEST_CODE_READED = 10001;
    private int selectedPosition = -1;
    private readStateChangedListener readStateChangedListener;
    private int pageNumber = 1;
    private int pageSize = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View meLayout = inflater.inflate(R.layout.fragment_message, container, false);
        initView(meLayout);
//            initData();
        return meLayout;
    }

    private void initView(View v) {
        rv = v.findViewById(R.id.rv_message);
        mRefreshLayout = v.findViewById(R.id.smoothRefreshLayout);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessageRvAdapter(getActivity());

        ClassicHeader classicHeader = new ClassicHeader(getActivity());
        ClassicFooter classicFooter = new ClassicFooter(getActivity());
        classicHeader.setLastUpdateTimeKey("header_last_update_time");
        classicFooter.setLastUpdateTimeKey("footer_last_update_time");
        mRefreshLayout.setHeaderView(classicHeader);
        mRefreshLayout.setFooterView(classicFooter);
        mRefreshLayout.setEnableKeepRefreshView(true);
        mRefreshLayout.setDisableLoadMore(false);
        mRefreshLayout.setEnableNoSpringBackWhenNoMoreData(true);
        mRefreshLayout.setOnRefreshListener(new RefreshingListenerAdapter() {

            @Override
            public void onRefreshBegin(final boolean isRefresh) {
                if (isRefresh) {
                    pageNumber = 1;
                    RxLogUtils.d("刷新");
                    initData();
                } else {
                    RxLogUtils.d("加载更多");
                    pageNumber++;
                    loadNextPageData();
                }
            }
        });

        adapter.bindToRecyclerView(rv);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("messageBean", firstPageData.get(position));
                intent.putExtra("msgType", msgType);
                selectedPosition = position;
                startActivityForResult(intent, REQEST_CODE_READED);
            }
        });

        adapter.setEmptyView(R.layout.layout_not_data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (msgType == 1) {
                    RxBus.getInstance().post(firstPageData.get(position));
                } else {
                    RxBus.getInstance().post(nextPageData.get(position));
                }
            }
        });

    }

    public void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.message(msgType, pageNumber, pageSize))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRefreshLayout.refreshComplete();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        try {
                            JSONObject object = new JSONObject(s);
                            Gson gson = new Gson();
                            Log.e("消息列表---------", object.toString());
                            firstPageData = gson.fromJson(object.getString("list"), new TypeToken<List<Message>>() {
                            }.getType());
                            if (firstPageData != null) {
                                totalData = firstPageData;
                                adapter.setNewData(firstPageData);
                            }
                            //遍历集合，发现都为已读，右上角的标记已读按键，置灰
                            if (isAllReaded()) {
                                if (readStateChangedListener != null) {
                                    readStateChangedListener.allReadState(true);
                                }
                            } else {
                                if (readStateChangedListener != null) {
                                    readStateChangedListener.allReadState(false);
                                }
                            }
                            boolean hasNextPage = object.getBoolean("hasNextPage");
                            mRefreshLayout.setEnableNoMoreData(hasNextPage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(String e) {
                        mRefreshLayout.refreshComplete();
                    }
                });
    }

    private void loadNextPageData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.message(msgType, pageNumber, pageSize))
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        mRefreshLayout.refreshComplete();
                        RxLogUtils.d("结束" + s);
                        try {
                            JSONObject object = new JSONObject(s);
                            Gson gson = new Gson();
                            nextPageData = gson.fromJson(object.getString("list"), new TypeToken<List<Message>>() {
                            }.getType());
                            if (nextPageData != null) {
                                RxLogUtils.d("beanList 's size : " + nextPageData.size());
                                totalData.addAll(nextPageData);
                                adapter.addData(nextPageData);
                            }
                            //遍历集合，发现都为已读，右上角的标记已读按键，置灰
                            if (isAllReaded()) {
                                if (readStateChangedListener != null) {
                                    readStateChangedListener.allReadState(true);
                                }
                            } else {
                                if (readStateChangedListener != null) {
                                    readStateChangedListener.allReadState(false);
                                }
                            }
                            boolean hasNextPage = object.getBoolean("hasNextPage");
                            mRefreshLayout.setEnableNoMoreData(hasNextPage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(String e) {
                        adapter.loadMoreEnd();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEST_CODE_READED && resultCode == RESULT_OK) {
            //更新该选项为已读
            adapter.getData().get(selectedPosition).setReadState(1);
            adapter.notifyItemChanged(selectedPosition);
            //遍历集合，发现都为已读，右上角的标记已读按键，置灰
            if (isAllReaded()) {
                if (readStateChangedListener != null) {
                    readStateChangedListener.allReadState(true);
                }
            }
        }
    }

    //遍历集合，发现都为已读，右上角的标记已读按键，置灰
    private boolean isAllReaded() {
        boolean b = false;
        for (Message message : totalData) {
            if (message.getReadState() == 0) {
                b = false;
            } else {
                b = true;
            }
        }
        return b;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    public void setMsgType(int type) {
        this.msgType = type;
    }

    public void setOnReadStateChangedListener(readStateChangedListener listener) {
        this.readStateChangedListener = listener;
    }

    public interface readStateChangedListener {
        void allReadState(boolean isAllRead);
    }

    public void updateRecycler() {
        mRefreshLayout.autoRefresh();
    }

}