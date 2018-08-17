package lab.wesmartclothing.wefit.flyso.ui.main.store;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.just.agentweb.MiddlewareWebClientBase;
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


    @NonNull
    @Override
    protected MiddlewareWebClientBase getMiddleWareWebClient() {
        return new MiddlewareWebClientBase() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                RxLogUtils.d("页面结束");
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                RxLogUtils.d("页面显示");
                tipDialog.dismiss();
                if (url.equals(getUrl())) {
                    mQMUIAppBarLayout.removeAllLeftViews();
                } else {
                    mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mAgentWeb.back()) {
                                popBackStack();
                            }
                        }
                    });
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                RxLogUtils.d("页面开始");
                if (url.equals(getUrl())) {
                    tipDialog.show();
                }
            }
        };
    }


    @Override
    protected View onCreateView() {
        View view = View.inflate(mContext, R.layout.fragment_shore, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

}
