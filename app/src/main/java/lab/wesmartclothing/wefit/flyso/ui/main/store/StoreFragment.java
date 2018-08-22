package lab.wesmartclothing.wefit.flyso.ui.main.store;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxLogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class StoreFragment extends BaseWebFragment {


    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;

    public static StoreFragment getInstance() {
        return new StoreFragment();
    }


    @Override
    public void initData() {

    }

    public void initView() {
        initTopBar();
        initWebView(mParent);
    }


    private void initTopBar() {
        mQMUIAppBarLayout.setTitle("商城");
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
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                RxLogUtils.d("页面结束");
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (url.equals(getUrl())) {
                    mQMUIAppBarLayout.removeAllLeftViews();
                } else {
                    mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mAgentWeb.back()) {
//                                popBackStack();
                            }
                        }
                    });
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                RxLogUtils.d("页面开始");
            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_shore, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


}
