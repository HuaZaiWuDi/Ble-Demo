package lab.wesmartclothing.wefit.flyso.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
        RxLogUtils.d("加载URL：" + url);
        pageLayout = new PageLayout.Builder(mContext)
                .initPage(mLayoutWeb)
                .setError(R.layout.layout_web_error, 0)
                .setOnRetryListener(() -> {
                    RxLogUtils.d("重试");
                    if (webView.canGoBack()) webView.goBack();
                    else
                        webView.reload();
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
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public void onPageFinished(WebView view, String curl) {
                super.onPageFinished(view, url);
                RxLogUtils.d("【webView】:onPageFinished:");
                if (!RxNetUtils.isAvailable(MyAPP.sMyAPP)) {
                    pageLayout.showError();
                } else {
                    pageLayout.hide();
                }
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mProgressWeb != null)
                    mProgressWeb.setVisibility(View.VISIBLE);
                RxLogUtils.d("【webView】:onPageStarted");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //view.clearView()方法已过时，不再使用
                //接收到错误时加载空页面，如果没有这个过程，在显示我们自己的错误页面之前默认错误页面会一闪而过，
                // 可自行实验验证
                pageLayout.showError();
                view.loadUrl("about:blank");
                RxLogUtils.d("【webView】:onReceivedError");
                super.onReceivedError(view, errorCode, description, failingUrl);
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
        webView.loadUrl(url);

    }

    @Override
    public void onPause() {
        webView.onPause();
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
