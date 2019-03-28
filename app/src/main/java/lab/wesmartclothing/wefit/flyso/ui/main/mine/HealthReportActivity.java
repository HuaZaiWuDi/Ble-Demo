package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.recyclerview.viewpager.OnViewPagerListener;
import com.vondear.rxtools.recyclerview.viewpager.ViewPagerLayoutManager;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HealthReportBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.GoToMainPage;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanWebActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.RecyclerViewTouchListener;

public class HealthReportActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_openReport)
    RxTextView mTvOpenReport;
    @BindView(R.id.layout_notReport)
    LinearLayout mLayoutNotReport;
    @BindView(R.id.recycler_health_report)
    RecyclerView mRecyclerHealthReport;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.tv_left)
    RxTextView mTvLeft;
    @BindView(R.id.tv_center)
    RxTextView mTvCenter;
    @BindView(R.id.tv_right)
    RxTextView mTvRight;
    @BindView(R.id.layout_indicator)
    RxRelativeLayout mLayoutIndicator;

    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_health_report;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initViewPager();

    }

    // TEST_URL = ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=true";
    private void initViewPager() {

        ViewPagerLayoutManager viewPagerLayoutManager = new ViewPagerLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        viewPagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                RxLogUtils.d("onInitComplete：b:");
            }

            @Override
            public void onPageRelease(boolean isStart, int i) {
                RxLogUtils.d("onPageRelease：b:" + isStart + "---i:" + i);
                mTvRight.setVisibility(isStart ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageSelected(int i, boolean isLast) {
                RxLogUtils.d("onPageSelected：b:" + isLast + "---i:" + i);
                mTvLeft.setVisibility(isLast ? View.GONE : View.VISIBLE);

                HealthReportBean item = (HealthReportBean) adapter.getItem(i);
                if (item != null)
                    mTvTip.setText("报告编号：" + item.getUserInform().getInformNo() +
                            "\n创建日期：" + RxFormat.setFormatDate(item.getTargetInfo().getCreateTime(), "yyyy/MM/dd"));
            }
        });
        mRecyclerHealthReport.setLayoutManager(viewPagerLayoutManager);
        adapter = new BaseQuickAdapter<HealthReportBean, BaseViewHolder>(R.layout.item_health_report) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void convert(BaseViewHolder helper, HealthReportBean item) {

                RxTextUtils.getBuilder("起始体重\n")
                        .append(RxFormatValue.fromat4S5R(item.getTargetInfo().getInitialWeight(), 1)).setProportion(1.5f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .into(helper.getView(R.id.tv_initWeight));

                RxTextUtils.getBuilder("目标体重\n")
                        .append(RxFormatValue.fromat4S5R(item.getTargetInfo().getTargetWeight(), 1)).setProportion(1.5f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .into(helper.getView(R.id.tv_targetWeight));

                RxRoundProgressBar mProTarget = helper.getView(R.id.pro_target);

                if (item.getTargetWeight().getComplete() < 0) {
                    mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.red));
                    mProTarget.setProgress(5);
                } else {
                    mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
                    mProTarget.setProgress((float) (item.getTargetWeight().getComplete() * 100));
                }

                int drawableRes = R.mipmap.ic_report_starting;
                switch (item.getTargetInfo().getCompleteStatus()) {
                    case 0:
                        drawableRes = R.mipmap.ic_report_starting;
                        break;
                    case -1:
                        drawableRes = R.mipmap.ic_report_fail;
                        break;
                    case 9:
                        drawableRes = R.mipmap.ic_report_finish;
                        break;
                    case 1:
                        drawableRes = R.mipmap.ic_report_success;
                        break;
                    default:
                }

                helper.setImageResource(R.id.img_reportFlag, drawableRes);

                String reportCycle = RxFormat.setFormatDate(item.getTargetInfo().getCreateTime(), "yyyy/MM/dd") +
                        "~" + RxFormat.setFormatDate(item.getTargetInfo().getTargetDate(), "yyyy/MM/dd");
                helper.setText(R.id.tv_reportCycle, reportCycle);

                FrameLayout frameLayout = helper.getView(R.id.layout_frame);
                String url = ServiceAPI.SHARE_INFORM_URL + item.getUserInform().getGid() + "&sign=true";
                loadUrl(url, frameLayout);
            }
        };
        mRecyclerHealthReport.setAdapter(adapter);

    }


    @SuppressLint("ClickableViewAccessibility")
    private void loadUrl(String url, FrameLayout mLayoutWeb) {
        BridgeWebView webView = new BridgeWebView(mContext);
        webView.setTag("webView");
        webView.setTransitionName("webView");
        mLayoutWeb.addView(webView);
        //WebView 不响应onClick事件，只有onTouch事件
        webView.setOnTouchListener(new RecyclerViewTouchListener(view -> {
            PlanWebActivity.startActivity(mContext, url);
        }));
        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                RxLogUtils.d("【webView】:onPageStarted");
            }
        });

        WebSettings webSettings = webView.getSettings();

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
//
        webView.loadUrl(url);
    }


    private void initTopBar() {
        mTopBar.setTitle("健康报告");
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(view -> onBackPressed());
    }


    @Override
    protected void initNetData() {
        super.initNetData();
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().fetchUserInformList())
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().transformObservable("fetchUserInformList", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        //类型擦除
                        List<HealthReportBean> beans = JSON.parseObject(s, new TypeToken<List<HealthReportBean>>() {
                        }.getType());

                        if (RxDataUtils.isEmpty(beans)) {
                            mLayoutNotReport.setVisibility(View.VISIBLE);
                        } else {
                            mLayoutNotReport.setVisibility(View.GONE);
                            adapter.setNewData(beans);
                            if (beans.size() == 1) {
                                mTvLeft.setVisibility(View.GONE);
                            }
                            HealthReportBean item = (HealthReportBean) adapter.getItem(0);
                            if (item != null)
                                mTvTip.setText("报告编号：" + item.getUserInform().getInformNo() +
                                        "\n创建日期：" + RxFormat.setFormatDate(item.getTargetInfo().getCreateTime(), "yyyy/MM/dd"));
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.normal(error, code);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.tv_openReport, R.id.layout_notReport})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_openReport:
                //退回首页，让用户从首页按钮制定计划
                RxBus.getInstance().post(new GoToMainPage(0));
                onBackPressed();
                break;
            case R.id.layout_notReport:
                break;
            default:
        }


    }
}
