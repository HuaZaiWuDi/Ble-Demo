package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.aboutCarmera.RxCameraUtils;
import com.vondear.rxtools.aboutCarmera.RxImageTools;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

public class PlanWebActivity extends BaseWebActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.prant)
    LinearLayout mPrant;


    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_plan_web;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTopBar();
        initWebView(mPrant);

    }


    @Nullable
    @Override
    protected String getUrl() {
//        return ServiceAPI.SHARE_INFORM_URL;
        return "https://www.apple.com/cn/search/%E8%80%B3%E6%9C%BA?sel=accessories&src=serp" + SPUtils.getString(SPKey.SP_UserId) + "&sign=" + true;
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
                view.loadUrl("javascript:setUserId(\"" + SPUtils.getString(SPKey.SP_UserId) + "\");");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                RxLogUtils.d("页面开始");
            }
        };
    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        mTopBar.setTitle(getString(R.string.appName) + "健康报告");

        mTopBar.addRightImageButton(R.mipmap.icon_save, R.id.btn_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save();
                    }
                });

        mTopBar.addRightImageButton(R.mipmap.icon_share, R.id.img_share)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share();
                    }
                });

    }


    //保存图片
    private void save() {
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                //控件转图片
                WebView webView = mAgentWeb.getWebCreator().getWebView();
                Bitmap bitmap = RxImageTools.WebView2Bitmap(webView);
                //微信分享图片最大尺寸32KB
                File timetofit = RxImageTools.saveBitmap(bitmap, "/Timetofit/", "plan_" + System.currentTimeMillis());
                if (timetofit == null) {
                    emitter.onError(new Throwable(""));
                } else {
                    emitter.onNext(timetofit);
                    emitter.onComplete();
                }
            }
        })
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<File>showDialog(tipDialog))
                .subscribe(new RxSubscriber<File>() {
                    @Override
                    protected void _onNext(File file) {
                        RxToast.normal("保存成功");
                        RxCameraUtils.NotifyAlbum(mContext, file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        RxToast.normal("保存失败");
                    }
                });
    }

    //分享链接
    private void share() {
        tipDialog.show("正在分享...", 3000);
        showSimpleBottomSheetGrid(BitmapFactory.decodeResource(getResources(), R.drawable.img_plan_share),
                "想知道怎样的瘦身方式才是健康的？一键获得健康报告，您想知道的全都有。",
                "营养食谱随心吃，科学训练自由动，不瘦？怎么可能……", ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=" + false);
    }


    private void showSimpleBottomSheetGrid(final Bitmap imgUrl, final String title, final String desc, final String url) {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(mContext);
        builder.addItem(R.mipmap.wechat, "分享到微信", 1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.fr, "分享到朋友圈", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.weib, "分享到微博", 3, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.qq, "分享到QQ", 4, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.zone, "分享到QQ空间", 5, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
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
            RxToast.normal("分享成功");
            tipDialog.dismiss();
        }

        @Override
        public void shareFailure(Exception e) {
            RxLogUtils.d("分享失败");
            RxToast.normal("分享失败");
            tipDialog.dismiss();
        }

        @Override
        public void shareCancel() {
            RxLogUtils.d("分享关闭");
            RxToast.normal("分享关闭");
            tipDialog.dismiss();
        }
    };


}
