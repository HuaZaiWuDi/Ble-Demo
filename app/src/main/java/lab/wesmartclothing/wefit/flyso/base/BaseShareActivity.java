package lab.wesmartclothing.wefit.flyso.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * @Package lab.wesmartclothing.wefit.flyso.base
 * @FileName BaseShareActivity
 * @Date 2019/6/27 11:47
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public abstract class BaseShareActivity extends BaseActivity {


    public void shareBitmap(Bitmap bitmap) {
        UMImage image = new UMImage(mContext, bitmap);//网络图片

//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享压缩格式设置
        image.compressFormat = Bitmap.CompressFormat.PNG;//图片格式

        UMImage thumb = new UMImage(mContext, bitmap);
        image.setThumb(thumb);
        new ShareAction(mActivity)
                .withMedia(image)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(mUMShareListener).open();
    }

    public void shareURL(final String imgUrl, final String title, final String desc, final String url) {
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


    public void shareURL(@DrawableRes int imgUrl, final String title, final String desc, final String url) {
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


    UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            tipDialog.show(getString(R.string.sharing), 3000);
            RxLogUtils.d("开始分享");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            RxLogUtils.d("分享成功");
            RxToast.normal(getString(R.string.shareSuccess));
            tipDialog.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            RxLogUtils.d("分享失败");
            RxToast.normal(getString(R.string.shareFail));
            tipDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            RxLogUtils.d("分享关闭");
            RxToast.normal(getString(R.string.shareCancel));
            tipDialog.dismiss();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        UMShareAPI.get(this).release();
        super.onDestroy();
    }


}
