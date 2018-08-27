package lab.wesmartclothing.wefit.flyso.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

import lab.wesmartclothing.wefit.flyso.R;

public class GlideImageLoader implements ImageLoader {


    public void displayImage(Context activity, Object path, ImageView imageView) {
        Glide.with(activity)
                .load(path)
                .asBitmap()
                .placeholder(R.mipmap.icon_placeholder)
                .into(imageView);
    }

    public void displayImage(Context activity, Object path, @DrawableRes int defaultImg, ImageView imageView) {
        Glide.with(activity)
                .load(path instanceof String ? Uri.fromFile(new File((String) path)) : path)
                .placeholder(defaultImg)
                .centerCrop()
                .crossFade(500)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }


    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .placeholder(R.mipmap.icon_placeholder)
                .centerCrop()
                .crossFade(500)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .placeholder(R.mipmap.icon_placeholder)
                .centerCrop()
                .crossFade(500)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}