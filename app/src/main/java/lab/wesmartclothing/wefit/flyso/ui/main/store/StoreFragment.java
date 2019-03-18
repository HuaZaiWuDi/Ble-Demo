package lab.wesmartclothing.wefit.flyso.ui.main.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebTFragment;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class StoreFragment extends BaseWebTFragment {


    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;

    public static StoreFragment getInstance() {
        return new StoreFragment();
    }


    @Override
    public void initViews() {
        url = ServiceAPI.Store_Addr;
        super.initViews();
        initTopBar();
    }


    @Override
    public void onVisible() {
        super.onVisible();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initTopBar() {
        mQMUIAppBarLayout.setTitle("商城");
    }


//    @Nullable
//    protected WebViewClient getWebViewClient() {
//        return new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                RxLogUtils.d("页面结束");
//            }
//
//            @Override
//            public void onPageCommitVisible(WebView view, String url) {
//                super.onPageCommitVisible(view, url);
//                if (url.equals(getUrl())) {
//                    mQMUIAppBarLayout.removeAllLeftViews();
//                } else {
//                    mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!mAgentWeb.back()) {
////                                popBackStack();
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                RxLogUtils.d("页面开始");
//            }
//        };
//    }


}
