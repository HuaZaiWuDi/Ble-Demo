package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.recyclerview.viewpager.ViewPagerLayoutManager;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

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
//
//        mRecyclerView.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                //允许ScrollView截断点击事件，ScrollView可滑动
//                mScrollView.requestDisallowInterceptTouchEvent(false);
//            } else {
//                //不允许ScrollView截断点击事件，点击事件由子View处理
//                mScrollView.requestDisallowInterceptTouchEvent(true);
//            }
//            return false;
//        });

    }

    // TEST_URL = ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=true";
    private void initViewPager() {
        String url = ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=true";


        mRecyclerHealthReport.setLayoutManager(new ViewPagerLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_health_report) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                RxLogUtils.d("加载Fragment");
                FrameLayout frameLayout = helper.getView(R.id.layout_frame);
                WebView webView = new WebView(mContext);
                frameLayout.addView(webView);
                WebSettings webSettings = webView.getSettings();

//                // init webview settings
//                webSettings.setAllowContentAccess(true);
//                webSettings.setDatabaseEnabled(true);
//                webSettings.setDomStorageEnabled(true);
//                webSettings.setAppCacheEnabled(true);
//                webSettings.setSavePassword(false);
//                webSettings.setSaveFormData(false);
//                webSettings.setUseWideViewPort(true);
//                webSettings.setLoadWithOverviewMode(true);
//                webSettings.setDefaultTextEncodingName("utf-8");
//                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

                webView.loadUrl(url);
            }
        };

        mRecyclerHealthReport.setAdapter(adapter);


        adapter.addData("");
        adapter.addData("");
        adapter.addData("");
        adapter.addData("");

    }

    private void initTopBar() {
        mTopBar.setTitle("健康报告");
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(view -> onBackPressed());
    }


    @Override
    protected void initNetData() {
        super.initNetData();

    }
}
