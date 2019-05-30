package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.QuestionListBean;
import lab.wesmartclothing.wefit.flyso.entity.SubmitInfoFrom;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class RecordInfoActivity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_last)
    TextView mTvLast;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    @BindView(R.id.tv_infoTitle)
    TextView mTvInfoTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_nextWay)
    RxTextView mTvNextWay;


    private RecyclerView gridView;
    private BaseQuickAdapter adapter, gridAdapter;
    private List<QuestionListBean> listBean;
    private int currentIndex = 1;
    private int seeIndex = 0;//已经查看的页面下标
    //通过静态变量传递数据到设置目标界面提交信息
    public static SubmitInfoFrom mSubmitInfoFrom;
    private boolean isOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();
        init();
    }

    private void init() {
        mSubmitInfoFrom = new SubmitInfoFrom();
        initTopBar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().questionList())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        listBean = JSON.parseObject(s, new TypeToken<List<QuestionListBean>>() {
                        }.getType());
                        switchQuestion();

                    }

                    @Override
                    protected void _onError(String error,int code) {
                        RxToast.error(error,code);
                    }
                });
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<QuestionListBean.OptionListBean, BaseViewHolder>(R.layout.item_recond) {
            @Override
            protected void convert(BaseViewHolder helper, QuestionListBean.OptionListBean item) {
                TextView tvItem = helper.getView(R.id.tv_item);
                tvItem.setText(item.getOptionDesc());
                Drawable drawable = ContextCompat.getDrawable(mContext, item.isSelect() ?
                        R.drawable.shape_green_dot : R.drawable.shape_gray_dot);
                tvItem.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        };
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(
                new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        List<QuestionListBean.OptionListBean> data = adapter.getData();
                        for (int i = 0; i < data.size(); i++) {
                            QuestionListBean.OptionListBean item = data.get(i);
                            if (i == position && !item.isSelect()) {
                                item.setSelect(true);
                                adapter.setData(i, item);
                            } else if (i != position && item.isSelect()) {
                                item.setSelect(false);
                                adapter.setData(i, item);
                            }
                        }

                        if (currentIndex == listBean.size()) {
                            isOver = true;
                            mTvNextWay.setVisibility(View.VISIBLE);
                        } else {
                            mTvNextWay.setVisibility(View.GONE);
                            currentIndex++;
                            seeIndex = Math.max(seeIndex, currentIndex);
                            switchQuestion();
                        }
                    }
                }
        );

        gridView = new RecyclerView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        gridView.setLayoutParams(params);

        gridAdapter = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_record_grid) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyAPP.getImageLoader().displayImage(mActivity, item, R.drawable.ic_default_image,
                        (ImageView) helper.getView(R.id.img_item));
            }
        };
        gridView.setLayoutManager(new GridLayoutManager(mContext, 2));
        gridView.setAdapter(gridAdapter);
        gridView.setNestedScrollingEnabled(false);


        gridAdapter.addData(R.mipmap.slimming_1);
        gridAdapter.addData(R.mipmap.slimming_2);
        gridAdapter.addData(R.mipmap.slimming_3);
        gridAdapter.addData(R.mipmap.slimming_4);

    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    private void switchQuestion() {
        if (currentIndex <= 1) {
            this.currentIndex = 1;
            mTvLast.setAlpha(0.2f);
            mTvLast.setEnabled(false);
        } else {
            mTvLast.setAlpha(1f);
            mTvLast.setEnabled(true);
        }

        RxLogUtils.d("页面：" + seeIndex);

        if (currentIndex < seeIndex) {
            mTvNext.setAlpha(1f);
            mTvNext.setEnabled(true);
        } else {
            mTvNext.setAlpha(0.2f);
            mTvNext.setEnabled(false);
        }

        mTvCount.setText(currentIndex + "/" + listBean.size());


        if (currentIndex < listBean.size()) {
            mTvNextWay.setVisibility(View.GONE);
        } else if (currentIndex == listBean.size() && isOver) {
            mTvNextWay.setVisibility(View.VISIBLE);
        }


        QuestionListBean question = this.listBean.get((currentIndex - 1) % listBean.size());
        mTvInfoTitle.setText(question.getQuestion());
        adapter.setNewData(question.getOptionList());


        if (currentIndex == 2) {
            adapter.addHeaderView(gridView);
        } else {
            adapter.removeHeaderView(gridView);
        }
//        if (!RxDataUtils.isNullString(question.getImgUrl())) {
//            String[] imgUrls = question.getImgUrl().split(",");
//            for (String url : imgUrls) {
//                gridAdapter.addData(url);
//            }
//
//        } else {
//
//        }
    }


    @OnClick({R.id.tv_last, R.id.tv_next, R.id.tv_nextWay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_last:
                currentIndex--;
                switchQuestion();
                break;
            case R.id.tv_next:
                currentIndex++;
                if (currentIndex >= listBean.size()) {
                    currentIndex = listBean.size();
                }
                switchQuestion();
                break;
            case R.id.tv_nextWay:
                mSubmitInfoFrom.setAnswer(submitQuestion());
                RxActivityUtils.skipActivity(mContext, WelcomeActivity.class);
                break;
        }
    }

    private String submitQuestion() {
        StringBuffer stringBuffer = new StringBuffer();
        for (QuestionListBean bean : listBean) {
            for (QuestionListBean.OptionListBean option : bean.getOptionList()) {
                if (option.isSelect()) {
                    stringBuffer.append(option.getOptionNo());
                }
            }
        }
        return stringBuffer.toString();
    }


}
