package lab.wesmartclothing.wefit.flyso.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.view.state.PageLayout;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.soinc.SonicJavaScriptInterface;
import lab.wesmartclothing.wefit.flyso.utils.soinc.SonicSessionClientImpl;
import lab.wesmartclothing.wefit.flyso.view.RecyclerViewTouchListener;

/**
 * @Package com.wesmartclothing.tbra.base
 * @FileName BaseWebFragment
 * @Date 2019/1/19 16:24
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BaseWebTFragment extends BaseAcFragment {

    @BindView(R.id.layout_web)
    FrameLayout mLayoutWeb;
    @BindView(R.id.progress_web)
    ProgressBar mProgressWeb;


    private SonicSession sonicSession;
    private SonicSessionClientImpl sonicSessionClient = null;
    public BridgeWebView webView;
    public String url;
    private PageLayout pageLayout;


    public static BaseWebTFragment getInstance(String url) {
        BaseWebTFragment webFragment = new BaseWebTFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, url);
        webFragment.setArguments(bundle);
        return webFragment;
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_web;
    }

    @Override
    public void initBundle(Bundle bundle) {
    }

    private void initWebView(String url) {
        if (url == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                url = bundle.getString(Key.BUNDLE_WEB_URL);
            }
            if (url == null) {
                pageLayout.showError();
                return;
            }
        }

        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        sessionConfigBuilder.setReloadInBadNetwork(true);
//        sessionConfigBuilder.setSupportCacheControl(true);
        RxLogUtils.d("网页地址：" + url);
//        url = "http://mc.vip.qq.com/demo/indexv3";
        // step 2: Create SonicSession
        sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            //相同的Url的Sonic会话只能存在一个，也就是说URL作为一个Key，保证唯一性
            RxLogUtils.e("create session fail!");
        }


        webView = new BridgeWebView(mContext);

        webView.setTag("webView");
        mLayoutWeb.addView(webView);
        webView.setOnTouchListener(new RecyclerViewTouchListener());
        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
                if (!RxNetUtils.isAvailable(MyAPP.sMyAPP)) {
                    pageLayout.showError();
                } else {
                    pageLayout.hide();
                }
                mProgressWeb.setVisibility(View.GONE);
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null && !RxDataUtils.isNullString(url)) {
                    //step 6: Call sessionClient.requestResource when host allow the application
                    // to return the local data .
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressWeb.setVisibility(View.VISIBLE);
                RxLogUtils.d("【webView】:onPageStarted");
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressWeb.setProgress(newProgress);
                if (newProgress >= 90) {
                    mProgressWeb.setVisibility(View.GONE);
                }
                RxLogUtils.d("【webView】:onProgressChanged：" + newProgress);
            }
        });

        WebSettings webSettings = webView.getSettings();

        // step 4: bind javascript
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.setJavaScriptEnabled(true);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");

        Intent intent = mActivity.getIntent();
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        webView.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

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
//        if (sonicSessionClient != null) {
//            sonicSessionClient.bindWebView(webView);
//            sonicSessionClient.clientReady();
//        } else if (!TextUtils.isEmpty(url)) { // default mode
        webView.loadUrl(url);
//        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void onRelease() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            webView.clearSslPreferences();
        }
    }


    @Override
    public void onDestroyView() {
        onRelease();

        mLayoutWeb.removeAllViews();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initViews() {
        pageLayout = new PageLayout.Builder(mContext)
                .initPage(mLayoutWeb)
                .setError(R.layout.layout_web_error, 0)
                .setOnRetryListener(() -> {
                    if (url != null) {
                        webView.loadUrl(url);
                    }
                }).create();

        if (!RxNetUtils.isAvailable(MyAPP.sMyAPP)) {
            pageLayout.showError();
        }
        initWebView(url);
    }

    public BridgeWebView getWebView() {
        return webView;
    }
}
