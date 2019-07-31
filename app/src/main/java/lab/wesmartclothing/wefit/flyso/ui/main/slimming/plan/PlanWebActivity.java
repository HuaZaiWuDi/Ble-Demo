package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.fragment.FragmentUtils;
import com.vondear.rxtools.model.antishake.AntiShake;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.bitmap.RxCameraUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseShareActivity;
import lab.wesmartclothing.wefit.flyso.base.BaseWebFragment;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class PlanWebActivity extends BaseShareActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.prant)
    FrameLayout mPrant;

    private static String TEST_URL = "http://39.108.152.50:8088/wisenfit/build/html/healthReport.html?userId=e3e35aaff6b84cc29195f270ab7b95a1&sign=true";


    public static void startActivity(Context mContext, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, url);
        RxActivityUtils.skipActivity(mContext, PlanWebActivity.class, bundle);

    }

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
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        TEST_URL = bundle.getString(Key.BUNDLE_WEB_URL);
        FragmentUtils.replace(getSupportFragmentManager(), BaseWebFragment.getInstance(TEST_URL), R.id.prant);
    }

    private WebView getWebView() {
        WebView mWebView = null;
        try {
            View frameLayout = FragmentUtils.findFragment(getSupportFragmentManager(), BaseWebFragment.class)
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


    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(v -> {
                    WebView webView = getWebView();
                    if (getWebView() != null && webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                });

        mTopBar.setTitle(R.string.healthReport);
        mTopBar.addRightImageButton(R.mipmap.icon_save, R.id.btn_save)
                .setOnClickListener(v -> save());

        mTopBar.addRightImageButton(R.mipmap.icon_share, R.id.img_share)
                .setOnClickListener(v -> {
                    if (AntiShake.getInstance().check(v.getId())) return;
                    share();
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
            File timetofit = RxImageUtils.saveBitmap(bitmap, "/" + Key.COMPANY_KEY + "/", "plan_" + System.currentTimeMillis());
            if (timetofit == null) {
                emitter.onError(new Throwable(""));
            } else {
                emitter.onNext(timetofit);
                emitter.onComplete();
            }
        })
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<File>showDialog(tipDialog))
                .doOnSubscribe(disposable -> new RxPermissions((FragmentActivity) mActivity)
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
                        RxToast.normal(getString(R.string.saveSuccess) + file.getPath(), 2000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        RxToast.normal(getString(R.string.saveFail));
                    }
                });
    }

    private void share() {
        shareURL(
                R.drawable.img_plan_share,
                getString(R.string.healthTitle),
                getString(R.string.healthDesc),
                TEST_URL.replace("sign=true", "sign=false"));
    }

}
