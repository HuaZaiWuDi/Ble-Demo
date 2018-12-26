package lab.wesmartclothing.wefit.flyso.utils;

import android.os.Build;
import android.webkit.WebSettings;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName WebViewSetting
 * @Date 2018/11/27 14:24
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class WebViewSetting extends AbsAgentWebSettings {


    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        WebSettings webSettings = mAgentWeb.getWebCreator().getWebView().getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);


        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(false); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(false); // 缩放至屏幕的大小


        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件


        //设置为http和https混合加载模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }
}
