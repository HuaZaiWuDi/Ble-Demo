package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxWebViewTool;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HealthReportBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
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
    @BindView(R.id.viewPager_health_report)
    ViewPager mViewPagerHealthReport;


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
    }


    // TEST_URL = ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=true";
    private void initViewPager(List<HealthReportBean> beans) {
        mViewPagerHealthReport.setOffscreenPageLimit(4);
        mViewPagerHealthReport.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTvLeft.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
                mTvRight.setVisibility(i == beans.size() - 1 ? View.GONE : View.VISIBLE);

                HealthReportBean item = beans.get(i);
                if (item != null)
                    mTvTip.setText("报告编号：" + item.getUserInform().getInformNo() +
                            "\n创建日期：" + RxFormat.setFormatDate(item.getTargetInfo().getCreateTime(), "yyyy/MM/dd"));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPagerHealthReport.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return beans.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                WebView webView = view.findViewWithTag("webView");
                if (webView != null) {
                    webView.clearCache(true);
                    webView.clearHistory();
                    webView.clearFormData();
                    webView.clearSslPreferences();
                }
                container.removeView(view);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(mContext, R.layout.item_health_report, null);

                HealthReportBean bean = beans.get(position);

                FrameLayout frameLayout = view.findViewById(R.id.layout_frame);
                String url = ServiceAPI.SHARE_INFORM_URL + bean.getUserInform().getGid() + "&sign=true";
                loadUrl(url, frameLayout);

                RxTextUtils.getBuilder("起始体重\n")
                        .append(RxFormatValue.fromat4S5R(bean.getTargetInfo().getInitialWeight(), 1)).setProportion(1.5f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .into(view.findViewById(R.id.tv_initWeight));

                RxTextUtils.getBuilder("目标体重\n")
                        .append(RxFormatValue.fromat4S5R(bean.getTargetInfo().getTargetWeight(), 1)).setProportion(1.5f)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .append("kg").setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                        .into(view.findViewById(R.id.tv_targetWeight));


                RxRoundProgressBar mProTarget = view.findViewById(R.id.pro_target);

                if (bean.getTargetWeight().getComplete() < 0) {
                    mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.red));
                    mProTarget.setProgress(5);
                } else {
                    mProTarget.setProgressColor(ContextCompat.getColor(mContext, R.color.green_61D97F));
                    mProTarget.setProgress((float) (bean.getTargetWeight().getComplete() * 100));
                }

                int drawableRes = R.mipmap.ic_report_starting;
                switch (bean.getTargetInfo().getCompleteStatus()) {
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

                ((ImageView) view.findViewById(R.id.img_reportFlag)).setImageResource(drawableRes);

                String reportCycle = RxFormat.setFormatDate(bean.getTargetInfo().getCreateTime(), "yyyy/MM/dd") +
                        "~" + RxFormat.setFormatDate(bean.getTargetInfo().getTargetDate(), "yyyy/MM/dd");
                ((TextView) view.findViewById(R.id.tv_reportCycle)).setText(reportCycle);

                container.addView(view);
                return view;
            }
        });
        mViewPagerHealthReport.setCurrentItem(beans.size() - 1);

    }


    @SuppressLint("ClickableViewAccessibility")
    private void loadUrl(String url, FrameLayout mLayoutWeb) {
        RxLogUtils.d("加载URL：" + url);
//        mLayoutWeb.addView(webView);
        //WebView 不响应onClick事件，只有onTouch事件

        WebView mWebView = new WebView(mContext);
        mWebView.setTag("webView");
        mWebView.setTransitionName("webView");

        RxWebViewTool.initWebView(mContext, mWebView);

        mLayoutWeb.addView(mWebView);
        //WebView 不响应onClick事件，只有onTouch事件
        mWebView.setOnTouchListener(new RecyclerViewTouchListener(view -> {
            PlanWebActivity.startActivity(mContext, url);
        }));
        mWebView.loadUrl(url);

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
                            Collections.reverse(beans);
                            mLayoutNotReport.setVisibility(View.GONE);
                            initViewPager(beans);

                            if (beans.size() == 1) {
                                mTvLeft.setVisibility(View.GONE);
                            }
                            HealthReportBean item = beans.get(0);
                            if (item != null) {
                                mTvTip.setText("报告编号：" + item.getUserInform().getInformNo() +
                                        "\n创建日期：" + RxFormat.setFormatDate(item.getTargetInfo().getCreateTime(), "yyyy/MM/dd"));
                            }
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
