package lab.wesmartclothing.wefit.flyso.ui.main.find;

import android.graphics.Bitmap;
import android.webkit.ValueCallback;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import lab.wesmartclothing.wefit.flyso.base.BaseWebTFragment;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
public class FindFragment extends BaseWebTFragment {


    public static FindFragment getInstance() {
        return new FindFragment();
    }


    @Override
    public void initViews() {
        url = ServiceAPI.FIND_Addr;
        super.initViews();

        webView.evaluateJavascript("javascript:getUserId(" + SPUtils.getString(SPKey.SP_UserId) + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                RxLogUtils.d("返回的结果：" + s);
            }
        });

        webView.registerHandler("shareEvent", (data, function) -> {
            RxLogUtils.i("传递参数：" + data);

            if (!RxUtils.isFastClick(1000)) {
                try {
                    JSONObject object = new JSONObject(data);
                    String img = object.getString("img");
                    final String title = object.getString("title");
                    final String desc = object.getString("desc");
                    final String url = object.getString("url");

//                        showShareDialog(img, title, desc, url);
                    showSimpleBottomSheetGrid(img, title, desc, url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onVisible() {
        super.onVisible();
    }


    @Override
    public void onStop() {
//        onRelease();
        super.onStop();
    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
//        //只有在显示时才会网络请求
//        RxBus.getInstance().register2(NetWorkType.class)
//                .compose(RxComposeUtils.<NetWorkType>bindLife(lifecycleSubject))
//                .subscribe(new RxSubscriber<NetWorkType>() {
//                    @Override
//                    protected void _onNext(NetWorkType netWorkType) {
//                        RxLogUtils.d("网络状态：" + netWorkType);
//                        if (netWorkType.isBoolean()) {
//                            webView.reload();
//                        }
//                    }
//                });
    }


    private void showSimpleBottomSheetGrid(final String imgUrl, final String title, final String desc, final String url) {

        UMImage image = new UMImage(mContext, imgUrl);//网络图片

//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享压缩格式设置
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
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(mContext).release();
    }

}
