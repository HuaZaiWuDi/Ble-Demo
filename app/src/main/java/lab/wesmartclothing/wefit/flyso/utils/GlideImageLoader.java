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

/**
 * @author Jack
 * @date on 2018/10/8
 * @describe 图片加载器
 * @org 智裳科技
 */
public class GlideImageLoader implements ImageLoader {

    public static final GlideImageLoader getInstance() {
        return new GlideImageLoader();
    }

    public GlideImageLoader() {
    }

    public void displayImage(Context activity, Object path, ImageView imageView) {
        if (activity != null)
            Glide.with(activity)
                    .load(path)
                    .asBitmap()
                    .placeholder(R.mipmap.icon_placeholder)
                    .into(imageView);
    }

    public void displayImage(Context activity, Object path, @DrawableRes int defaultImg, ImageView imageView) {
        if (activity != null)
            Glide.with(activity)
                    .load(path)
                    .asBitmap()
                    .placeholder(defaultImg)
                    .centerCrop()
                    .into(imageView);
    }


    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        if (activity != null)
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
        if (activity != null)
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