package com.smartclothing.module_wefit.tools;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smartclothing.module_wefit.widget.ImageLoaderInterface;

/**
 * Created by haijiang on 2017/5/5.
 */

public class ImageLoaderUtil implements ImageLoaderInterface {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).centerCrop().into(imageView);

    }

    @Override
    public void displayImage(Context context, @DrawableRes Integer resId, ImageView imageView) {
        Glide.with(context).load(resId).centerCrop().into(imageView);
    }
}
