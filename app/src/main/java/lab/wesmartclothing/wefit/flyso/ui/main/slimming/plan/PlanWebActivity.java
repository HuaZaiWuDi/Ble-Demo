package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtools.model.antishake.AntiShake;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxProcessUtils;
import com.vondear.rxtools.utils.bitmap.RxCameraUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class PlanWebActivity extends BaseWebActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.prant)
    FrameLayout mPrant;

    private static final String TEST_URL = "http://39.108.152.50:8088/wisenfit/build/html/healthReport.html?userId=e3e35aaff6b84cc29195f270ab7b95a1&sign=true";


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

        RxLogUtils.e("当前进程：" + RxProcessUtils.getForegroundProcessName(mContext));
    }

    @Nullable
    @Override
    protected String getUrl() {
        return getIntent().getStringExtra(Key.BUNDLE_WEB_URL);
    }


    @android.support.annotation.Nullable
    @Override
    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                tipDialog.show();
                mAgentWeb.getWebCreator().getWebParentLayout().setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截 url 跳转,在里边添加点击链接跳转或者操作
                //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                RxLogUtils.d("网页地址：" + url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                RxLogUtils.e("网页高度：" + view.getContentHeight());
                RxLogUtils.e("网页高度：" + view.getHeight());
                RxLogUtils.e("网页高度：" + view.getMeasuredHeight());
                if (view.getContentHeight() < 1000 ) {
                    mAgentWeb.getUrlLoader().reload();
                } else {
                    mAgentWeb.getWebCreator().getWebParentLayout().setVisibility(View.VISIBLE);
                    tipDialog.dismiss();
                }

            }

        };
    }

    @Override
    protected void onDestroy() {
//        mAgentWeb.getWebCreator().getWebView().clearHistory();
//        AgentWebConfig.clearDiskCache(mContext);
        super.onDestroy();
        UMShareAPI.get(this).release();

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
                        if (AntiShake.getInstance().check(v.getId())) return;
                        showSimpleBottomSheetGrid(
                                "想知道怎样的瘦身方式才是健康的？一键获得健康报告，您想知道的全都有。",
                                "营养食谱随心吃，科学训练自由动，不瘦？怎么可能……",
                                mAgentWeb.getWebCreator().getWebView().getUrl().replace("sign=true", "sign=false"));
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
                Bitmap bitmap = RxImageUtils.WebView2Bitmap(webView);
                //微信分享图片最大尺寸32KB
                File timetofit = RxImageUtils.saveBitmap(bitmap, "/Timetofit/", "plan_" + System.currentTimeMillis());
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        new RxPermissions(mActivity)
                                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                                .subscribe(new RxSubscriber<Boolean>() {
                                    @Override
                                    protected void _onNext(Boolean aBoolean) {
                                        if (!aBoolean) {
                                            disposable.dispose();
                                        }
                                    }
                                });
                    }
                })
                .subscribe(new RxSubscriber<File>() {
                    @Override
                    protected void _onNext(File file) {
                        RxCameraUtils.NotifyAlbum(mContext, file.getPath());
                        RxToast.normal("保存成功:" + file.getPath(), 2000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        RxToast.normal("保存失败");
                    }
                });
    }


    private void showSimpleBottomSheetGrid(final String title, final String desc, final String url) {
        UMImage image = new UMImage(mContext, R.drawable.img_plan_share);//网络图片

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(desc);//描述
        new ShareAction(mActivity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(mUMShareListener).open();
    }


    UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            RxLogUtils.d("开始分享");
            tipDialog.show("正在分享...", 3000);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            RxLogUtils.d("分享成功");
            RxToast.normal("分享成功");
            tipDialog.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            RxLogUtils.d("分享失败");
            RxToast.normal("分享失败");
            tipDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            RxLogUtils.d("分享关闭");
            RxToast.normal("分享关闭");
            tipDialog.dismiss();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
