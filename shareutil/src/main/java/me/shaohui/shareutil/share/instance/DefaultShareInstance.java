package me.shaohui.shareutil.share.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.shaohui.shareutil.R;
import me.shaohui.shareutil.RxThreadUtils;
import me.shaohui.shareutil.share.ImageDecoder;
import me.shaohui.shareutil.share.ShareImageObject;
import me.shaohui.shareutil.share.ShareListener;

/**
 * Created by shaohui on 2016/11/18.
 */

public class DefaultShareInstance implements ShareInstance {

    @Override
    public void shareText(int platform, String text, Activity activity, ShareListener listener) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent,
                activity.getResources().getString(R.string.vista_share_title)));
    }

    @Override
    public void shareMedia(int platform, String title, String targetUrl, String summary,
                           ShareImageObject shareImageObject, Activity activity, ShareListener listener) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s", title, targetUrl));
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent,
                activity.getResources().getString(R.string.vista_share_title)));
    }

    @Override
    public void shareImage(int platform, final ShareImageObject shareImageObject,
                           final Activity activity, final ShareListener listener) {

        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {
                try {
                    Uri uri =
                            Uri.fromFile(new File(ImageDecoder.decode(activity, shareImageObject)));
                    emitter.onNext(uri);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).compose(RxThreadUtils.<Uri>rxThreadHelper())
                .subscribe(new Observer<Uri>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.shareRequest();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("image/jpeg");
                        activity.startActivity(Intent.createChooser(shareIntent,
                                activity.getResources().getText(R.string.vista_share_title)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.shareFailure(new Exception(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void handleResult(Intent data) {
        // Default share, do nothing
    }

    @Override
    public boolean isInstall(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        return context.getPackageManager()
                .resolveActivity(shareIntent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    @Override
    public void recycle() {

    }
}
