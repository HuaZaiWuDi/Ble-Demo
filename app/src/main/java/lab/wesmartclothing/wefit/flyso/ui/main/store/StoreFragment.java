package lab.wesmartclothing.wefit.flyso.ui.main.store;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.flyso.netserivce.ServiceAPI;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_shore)
public class StoreFragment extends BaseWebFragment {

    public static StoreFragment getInstance() {
        return new StoreFragment_();
    }


    @ViewById
    RelativeLayout parent;

    @Override
    public void initData() {

    }


    @AfterViews
    public void initView() {
        initWebView(parent);
    }


    @Nullable
    @Override
    protected String getUrl() {
        return ServiceAPI.Store_Addr;
    }


    @Nullable
    @Override
    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                tipDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                tipDialog.dismiss();
            }
        };
    }
}
