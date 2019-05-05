package lab.wesmartclothing.wefit.flyso.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;
import com.vondear.rxtools.utils.RxLogUtils;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.soinc.OfflinePkgSessionConnection;
import lab.wesmartclothing.wefit.flyso.utils.soinc.SonicJavaScriptInterface;
import lab.wesmartclothing.wefit.flyso.utils.soinc.SonicRuntimeImpl;
import lab.wesmartclothing.wefit.flyso.utils.soinc.SonicSessionClientImpl;

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


    private SonicSession sonicSession;
    private String url;
    private SonicSessionClientImpl sonicSessionClient = null;


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
        url = "http://mc.vip.qq.com/demo/indexv3";
        initWebView();

    }

    private void initWebView() {
        // step 1: 必要时初始化sonic引擎，或者在创建应用程序时进行初始化
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(mContext.getApplicationContext()), new SonicConfig.Builder().build());
        }
        //如果是脱机pkg模式，我们需要拦截会话连接
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
//        sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
//            @Override
//            public String getCacheData(SonicSession session) {
//                return null; // offline pkg does not need cache
//            }
//        });

        sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
            @Override
            public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                return new OfflinePkgSessionConnection(mContext, session, intent);
            }
        });

        // step 2: Create SonicSession
        sonicSession = SonicEngine.getInstance().createSession(url, new SonicSessionConfig.Builder().build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            // this only happen when a same sonic session is already running,
            // u can comment following codes to feedback as a default mode.
//            throw new UnknownError("create session fail!");
            RxLogUtils.e("create session fail!");
        }
        // step 3: BindWebView for sessionClient and bindClient for SonicSession
        // in the real world, the init flow may cost a long time as startup
        // runtime、init configs....

        WebView webView = new WebView(mContext);
        mLayoutWeb.addView(webView);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
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
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    //step 6: Call sessionClient.requestResource when host allow the application
                    // to return the local data .
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mProgressWeb != null)
                    mProgressWeb.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressWeb != null)
                    mProgressWeb.setProgress(newProgress);
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

        // step 5: webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(webView);
            sonicSessionClient.clientReady();
        } else { // default mode
            webView.loadUrl(url);
        }
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
        if (sonicSessionClient != null) {
            sonicSessionClient.destroy();
            sonicSessionClient = null;
        }
        mLayoutWeb.removeAllViews();
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
