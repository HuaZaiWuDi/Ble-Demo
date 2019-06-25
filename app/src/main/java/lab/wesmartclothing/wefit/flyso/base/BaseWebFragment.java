package lab.wesmartclothing.wefit.flyso.base;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.utils.RxWebViewTool;
import com.vondear.rxtools.view.state.PageLayout;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * @Package com.wesmartclothing.tbra.base
 * @FileName BaseWebFragment
 * @Date 2019/1/19 16:24
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BaseWebFragment extends BaseAcFragment {

    @BindView(R.id.layout_web)
    FrameLayout mLayoutWeb;
    @BindView(R.id.progress_web)
    ProgressBar mProgressWeb;


    private BridgeWebView webView;
    private String url;
    private PageLayout pageLayout;

    public static BaseWebFragment getInstance(String url) {
        BaseWebFragment webFragment = new BaseWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, url);
        webFragment.setArguments(bundle);
        return webFragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_web;
    }

    @Override
    public void initBundle(Bundle bundle) {
        url = bundle.getString(Key.BUNDLE_WEB_URL);

        pageLayout = new PageLayout.Builder(mContext)
                .initPage(mLayoutWeb)
                .setError(R.layout.layout_web_error, 0)
                .setOnRetryListener(() -> {
                    if (url != null) {
                        webView.loadUrl(url);
                        RxLogUtils.d("重试");
                    }
                }).create();

        if (!RxNetUtils.isAvailable(MyAPP.sMyAPP) || url == null) {
            pageLayout.showError();
        }
        initWebView();

    }

    private void initWebView() {
        webView = new BridgeWebView(mContext);

        webView.setTag("webView");
        mLayoutWeb.addView(webView);

        RxWebViewTool.initWebView(mContext, webView);
        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!RxNetUtils.isAvailable(MyAPP.sMyAPP)) {
                    pageLayout.showError();
                } else {
                    pageLayout.hide();
                }
                if (mProgressWeb != null)
                    mProgressWeb.setVisibility(View.GONE);
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mProgressWeb != null)
                    mProgressWeb.setVisibility(View.VISIBLE);
                RxLogUtils.d("【webView】:onPageStarted");
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressWeb != null) {
                    mProgressWeb.setProgress(newProgress);
                    if (newProgress >= 90) {
                        mProgressWeb.setVisibility(View.GONE);
                    }
                }
                RxLogUtils.d("【webView】:onProgressChanged：" + newProgress);
            }
        });


    }

    @Override
    public void onPause() {

        super.onPause();
    }

    public void onRelease() {

        mLayoutWeb.removeAllViews();
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            webView.clearSslPreferences();
            webView.destroy();
            webView = null;
        }
    }


    @Override
    public void onDestroyView() {
        onRelease();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initNetData() {

    }


}
