package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtools.fragment.FragmentUtils;
import com.vondear.rxtools.model.antishake.AntiShake;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.bitmap.RxCameraUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.BaseWebTFragment;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class PlanWebActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.prant)
    FrameLayout mPrant;

    private static String TEST_URL = "http://39.108.152.50:8088/wisenfit/build/html/healthReport.html?userId=e3e35aaff6b84cc29195f270ab7b95a1&sign=true";


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

        TEST_URL = ServiceAPI.SHARE_INFORM_URL + SPUtils.getString(SPKey.SP_UserId) + "&sign=true";
        FragmentUtils.replace(getSupportFragmentManager(), BaseWebTFragment.getInstance(TEST_URL), R.id.prant);

    }


    private WebView getWebView() {
        WebView mWebView = null;
        try {
            View frameLayout = FragmentUtils.findFragment(getSupportFragmentManager(), BaseWebTFragment.class)
                    .getView().findViewWithTag("webView");

            if (frameLayout instanceof WebView) {
                mWebView = (WebView) frameLayout;
            }
            return mWebView;
        } catch (Exception e) {
            RxLogUtils.e(e);
        }
        return mWebView;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebView webView = getWebView();
                        if (getWebView() != null && webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            onBackPressed();
                        }
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
                        share();
                    }
                });
    }


    //保存图片
    private void save() {
        Observable.create((ObservableOnSubscribe<File>) emitter -> {
            WebView webView = getWebView();
            if (webView == null) {
                emitter.onError(new Throwable(""));
                return;
            }
            //控件转图片
            Bitmap bitmap = RxImageUtils.WebView2Bitmap(webView);
            //微信分享图片最大尺寸32KB
            File timetofit = RxImageUtils.saveBitmap(bitmap, "/Timetofit/", "plan_" + System.currentTimeMillis());
            if (timetofit == null) {
                emitter.onError(new Throwable(""));
            } else {
                emitter.onNext(timetofit);
                emitter.onComplete();
            }
        })
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<File>showDialog(tipDialog))
                .doOnSubscribe(disposable -> new RxPermissions(mActivity)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                        .subscribe(new RxSubscriber<Boolean>() {
                            @Override
                            protected void _onNext(Boolean aBoolean) {
                                if (!aBoolean) {
                                    disposable.dispose();
                                }
                            }
                        }))
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


    private void share() {
        showSimpleBottomSheetGrid(
                "想知道怎样的瘦身方式才是健康的？一键获得健康报告，您想知道的全都有。",
                "营养食谱随心吃，科学训练自由动，不瘦？怎么可能……",
                TEST_URL.replace("sign=true", "sign=false"));
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
