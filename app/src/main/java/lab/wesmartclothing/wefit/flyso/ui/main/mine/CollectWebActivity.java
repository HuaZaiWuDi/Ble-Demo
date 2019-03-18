package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtools.fragment.FragmentUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.BaseWebTFragment;
import lab.wesmartclothing.wefit.flyso.entity.CollectBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;

public class CollectWebActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.parent)
    RelativeLayout mParent;

    private CollectBean.ListBean bean;


    @Override
    protected int layoutId() {
        return R.layout.activity_web_interactive;
    }


    @Override
    protected void initViews() {
        super.initViews();

        FragmentUtils.replace(getSupportFragmentManager(),
                BaseWebTFragment.getInstance(getIntent().getStringExtra(Key.BUNDLE_WEB_URL)), R.id.parent);


        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getWebView() != null && getWebView().canGoBack()) {
                            getWebView().goBack();
                        } else {
                            onBackPressed();
                        }
                    }
                });


        QMUIAlphaImageButton shareImg = mTopBar.addRightImageButton(R.mipmap.icon_share, R.id.img_share);
        shareImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    showSimpleBottomSheetGrid(bean.getCoverPicture(), bean.getArticleName(),
                            bean.getSummary(), getIntent().getStringExtra(Key.BUNDLE_WEB_URL));
                } else {
                    RxToast.normal("分享失败");
                }
            }
        });
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
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        bean = (CollectBean.ListBean) bundle.getSerializable(Key.BUNDLE_DATA);
        if (bean != null)
            mTopBar.setTitle(bean.getArticleName());
    }


    private void showSimpleBottomSheetGrid(final String imgUrl, final String title, final String desc, final String url) {
        UMImage image = new UMImage(mContext, imgUrl);//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享压缩格式设置
        image.compressFormat = Bitmap.CompressFormat.PNG;//图片格式

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(desc);//描述
        new ShareAction(mActivity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(mUMShareListener).open();
    }


    private UMShareListener mUMShareListener = new UMShareListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
