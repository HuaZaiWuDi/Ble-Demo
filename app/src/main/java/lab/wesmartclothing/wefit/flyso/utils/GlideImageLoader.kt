package lab.wesmartclothing.wefit.flyso.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.lzy.imagepicker.loader.ImageLoader
import java.io.File

/**
 * @author Jack
 * @date on 2018/10/8
 * @describe 图片加载器
 * @org 智裳科技
 */
object GlideImageLoader : ImageLoader {

    fun displayImage(activity: Context?, path: Any, imageView: ImageView) {
        if (activity != null)
            Glide.with(activity)
                    .asBitmap()
                    .load(path)
                    .into(imageView)
    }

    fun displayImage(activity: Context?, path: Any, @DrawableRes defaultImg: Int, imageView: ImageView) {
        if (activity != null)
            Glide.with(activity)
                    .asBitmap()
                    .load(path)
                    .apply(
                            RequestOptions.placeholderOf(defaultImg)
                    )
                    .apply(
                            RequestOptions.centerCropTransform()
                    )
                    .into(imageView)
    }


    override fun displayImage(activity: Activity?, path: String, imageView: ImageView, width: Int, height: Int) {
        if (activity != null)
            Glide.with(activity)
                    .load(Uri.fromFile(File(path)))
                    .apply(
                            RequestOptions.centerCropTransform()
                    )
                    .transition(withCrossFade(500))
                    .into(imageView)
    }

    override fun displayImagePreview(activity: Activity?, path: String, imageView: ImageView, width: Int, height: Int) {
        if (activity != null)
            Glide.with(activity)
                    .load(Uri.fromFile(File(path)))
                    .apply(
                            RequestOptions.centerCropTransform()
                    )
                    .transition(withCrossFade(500))
                    .into(imageView)
    }

    override fun clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }

}