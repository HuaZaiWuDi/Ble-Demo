package lab.wesmartclothing.wefit.flyso.ui.main.find;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.MiddlewareWebClientBase;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.boradcast.B;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.AndroidInterface;
import lab.wesmartclothing.wefit.flyso.view.SharePop;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_find)
public class FindFragment extends BaseWebFragment {

    public static FindFragment getInstance() {
        return new FindFragment_();
    }

    @Override
    public void initData() {
        RxLogUtils.d("加载：【FindFragment】");
    }

    @ViewById
    RelativeLayout parent;

    private BridgeWebView mBridgeWebView;

    @Override
    @AfterViews
    public void initView() {
        initWebView();


    }

    private void initWebView() {
        mBridgeWebView = new BridgeWebView(mActivity);

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(parent, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//
                .useDefaultIndicator(-1, 2)//
                .setWebViewClient(new BridgeWebViewClient(mBridgeWebView))
                .setWebView(mBridgeWebView)
                .setMainFrameErrorView(R.layout.layout_web_error, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .useMiddlewareWebClient(getMiddleWareWebClient())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());

        AgentWebConfig.debug();

        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        WebSettings webSettings = mAgentWeb.getWebCreator().getWebView().getSettings();
//        设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
//                自适应屏幕(导致活动页面显示出错了)
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);

        if (mAgentWeb != null)
            mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, mActivity));

        mBridgeWebView.registerHandler("shareEvent", new BridgeHandler() {

            @Override
            public void handler(final String data, CallBackFunction function) {
                RxLogUtils.i("传递参数：" + data);

                if (!RxUtils.isFastClick(1000)) {
                    try {
                        JSONObject object = new JSONObject(data);
                        String img = object.getString("img");
                        final String title = object.getString("title");
                        final String desc = object.getString("desc");
                        final String url = object.getString("url");

                        showSimpleBottomSheetGrid(img, title, desc, url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showSimpleBottomSheetGrid(final String imgUrl, final String title, final String desc, final String url) {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", 1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "分享到微博", 3, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_friend, "分享到QQ", 4, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_friend, "分享到QQ空间", 5, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .addItem(R.mipmap.icon_more_operation_share_friend, "登录到微信", 6, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .addItem(R.mipmap.icon_more_operation_share_friend, "登录到QQ", 7, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .addItem(R.mipmap.icon_more_operation_share_friend, "登录到微博", 8, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case 1:
                                ShareUtil.shareMedia(mActivity, SharePlatform.WX, title, desc, url, imgUrl, shareListener);
                                break;
                            case 2:
                                ShareUtil.shareMedia(mActivity, SharePlatform.WX_TIMELINE, title, desc, url, imgUrl, shareListener);
                                break;
                            case 3:
                                ShareUtil.shareMedia(mActivity, SharePlatform.WEIBO, title, desc, url, imgUrl, shareListener);
                                break;
                            case 4:
                                ShareUtil.shareMedia(mActivity, SharePlatform.QQ, title, desc, url, imgUrl, shareListener);
                                break;
                            case 5:
                                ShareUtil.shareMedia(mActivity, SharePlatform.QZONE, title, desc, url, imgUrl, shareListener);
                                break;

                        }
                    }
                }).build().show();
    }


    public void showShareDialog(final String imgUrl, final String title, final String desc, final String url) {
        SharePop sharePop = new SharePop(mActivity);
        sharePop.initPop();
        sharePop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        sharePop.setShareLinstener(new SharePop.ShareLinstener() {
            @Override
            public void shareType(int type) {
                switch (type) {
                    case SharePop.SHARE_PENGYOUYUAN:

                        ShareUtil.shareMedia(mActivity, SharePlatform.WX_TIMELINE, title, desc, url, imgUrl, shareListener);
                        break;
                    case SharePop.SHARE_WX:
                        ShareUtil.shareMedia(mActivity, SharePlatform.WX, title, desc, url, imgUrl, shareListener);
                        break;
                }
            }
        });
    }

    ShareListener shareListener = new ShareListener() {
        @Override
        public void shareSuccess() {
            RxLogUtils.d("分享成功");
        }

        @Override
        public void shareFailure(Exception e) {
            RxLogUtils.d("分享失败");
        }


        @Override
        public void shareCancel() {
            RxLogUtils.d("分享关闭");
        }

    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    protected WebViewClient getWebViewClient() {
        return new BridgeWebViewClient(mBridgeWebView);
    }

    @NonNull
    @Override
    protected MiddlewareWebClientBase getMiddleWareWebClient() {
        return new MiddlewareWebClientBase() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                RxLogUtils.i("加载网页地址：" + url);
                B.broadUpdate(mActivity, Key.ACTION_SWITCH_BOTTOM_TAB, Key.EXTRA_SWITCH_BOTTOM_TAB, getUrl().equals(url));
                mAgentWeb.getJsAccessEntrace().quickCallJs("getUserId", SPUtils.getString(SPKey.SP_UserId));
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        };
    }


    @Nullable
    @Override
    protected WebView getWebView() {
        return mBridgeWebView;
    }

    @Nullable
    @Override
    protected String getUrl() {
        return ServiceAPI.FIND_Addr;
    }

}
