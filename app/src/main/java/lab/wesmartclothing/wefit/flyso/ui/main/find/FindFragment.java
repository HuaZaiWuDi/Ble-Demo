package lab.wesmartclothing.wefit.flyso.ui.main.find;

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

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.just.agentweb.MiddlewareWebClientBase;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.AndroidInterface;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class FindFragment extends BaseWebFragment {

    @BindView(R.id.parent)
    RelativeLayout mParent;
    Unbinder unbinder;

    public static FindFragment getInstance() {
        return new FindFragment();
    }

    private BridgeWebView mBridgeWebView;

    @Override
    public void initData() {

    }

    public void initView() {
        mBridgeWebView = new BridgeWebView(mActivity);
        initWebView(mParent);
        initWebView();
    }

    private void initWebView() {

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
                RxBus.getInstance().post(url);
//                B.broadUpdate(mActivity, Key.ACTION_SWITCH_BOTTOM_TAB, Key.EXTRA_SWITCH_BOTTOM_TAB, getUrl().equals(url));
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_find, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
