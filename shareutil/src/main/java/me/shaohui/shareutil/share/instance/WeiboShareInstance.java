package me.shaohui.shareutil.share.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Pair;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.shaohui.shareutil.RxThreadUtils;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ImageDecoder;
import me.shaohui.shareutil.share.ShareImageObject;
import me.shaohui.shareutil.share.ShareListener;

/**
 * Created by shaohui on 2016/11/18.
 */

public class WeiboShareInstance implements ShareInstance {
    /**
     * 微博分享限制thumb image必须小于2097152，否则点击分享会没有反应
     */

//    private IWeiboShareAPI mWeiboShareAPI;

    private WbShareHandler shareHandler;
    private static final int TARGET_SIZE = 1024;

    private static final int TARGET_LENGTH = 2097152;

    public WeiboShareInstance(Activity context, String appId) {

        shareHandler = new WbShareHandler(context);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

    }

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = textObject;

        sendRequest(activity, message);
    }

    @Override
    public void shareMedia(int platform, final String title, final String targetUrl, String summary,
                           ShareImageObject shareImageObject, final Activity activity,
                           final ShareListener listener) {
        String content = String.format("%s %s", title, targetUrl);
        shareTextOrImage(shareImageObject, content, activity, listener);
    }

    @Override
    public void shareImage(int platform, ShareImageObject shareImageObject, Activity activity,
                           ShareListener listener) {
        shareTextOrImage(shareImageObject, null, activity, listener);
    }

    @Override
    public void handleResult(Intent intent) {
        shareHandler.doResultIntent(intent, ShareUtil.mShareListener);


//        SendMessageToWeiboResponse baseResponse =
//                new SendMessageToWeiboResponse(intent.getExtras());
//
//        switch (baseResponse.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                ShareUtil.mShareListener.shareSuccess();
//                break;
//            case WBConstants.ErrorCode.ERR_FAIL:
//                ShareUtil.mShareListener.shareFailure(new Exception(baseResponse.errMsg));
//                break;
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                ShareUtil.mShareListener.shareCancel();
//                break;
//            default:
//                ShareUtil.mShareListener.shareFailure(new Exception(baseResponse.errMsg));
//        }
    }

    @Override
    public boolean isInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.sina.weibo")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {
        shareHandler = null;
    }

    private void shareTextOrImage(final ShareImageObject shareImageObject, final String text,
                                  final Activity activity, final ShareListener listener) {

        Observable.create(new ObservableOnSubscribe<Pair<String, byte[]>>() {
            @Override
            public void subscribe(ObservableEmitter<Pair<String, byte[]>> emitter) throws Exception {
                try {
                    String path = ImageDecoder.decode(activity, shareImageObject);
                    emitter.onNext(Pair.create(path,
                            ImageDecoder.compress2Byte(path, TARGET_SIZE, TARGET_LENGTH)));
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).compose(RxThreadUtils.<Pair<String, byte[]>>rxThreadHelper())
                .subscribe(new Observer<Pair<String, byte[]>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.shareRequest();
                    }

                    @Override
                    public void onNext(Pair<String, byte[]> pair) {
                        ImageObject imageObject = new ImageObject();
                        imageObject.imageData = pair.second;
                        imageObject.imagePath = pair.first;

                        WeiboMultiMessage message = new WeiboMultiMessage();
                        message.imageObject = imageObject;
                        if (!TextUtils.isEmpty(text)) {
                            TextObject textObject = new TextObject();
                            textObject.text = text;

                            message.textObject = textObject;
                        }

                        sendRequest(activity, message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.finish();
                        listener.shareFailure(new Exception(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void sendRequest(Activity activity, WeiboMultiMessage message) {

        shareHandler.shareMessage(message, false);

    }
}
