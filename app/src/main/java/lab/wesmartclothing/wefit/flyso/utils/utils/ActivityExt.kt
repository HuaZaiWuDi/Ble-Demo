package com.wesmarclothing.kotlintools.kotlin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.View

/**
 * Description: Activity相关
 * Create by lxj, at 2018/12/7
 */

inline fun <reified T> Fragment.startAc(
    flag: Int = Intent.FLAG_ACTIVITY_SINGLE_TOP,
    bundle: Array<out Pair<String, Any?>>? = null
) {

    activity?.startAc<T>(flag, bundle)
}

inline fun <reified T> Fragment.startAcForResult(
    flag: Int = Intent.FLAG_ACTIVITY_SINGLE_TOP,
    bundle: Array<out Pair<String, Any?>>? = null,
    requestCode: Int = -1
) {
    activity?.startAcForResult<T>(flag, bundle, requestCode)
}

inline fun <reified T> Context.startAc(
    flag: Int = Intent.FLAG_ACTIVITY_SINGLE_TOP,
    bundle: Array<out Pair<String, Any?>>? = null,
    requestCode: Int = -1
) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        } else if (this !is Activity) {
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (bundle != null) putExtras(bundle.toBundle())
    }
    startActivity(intent)
}

inline fun <reified T> View.startAc(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null) {
    context.startAc<T>(flag, bundle)
}

inline fun <reified T> View.startAcForResult(
    flag: Int = -1,
    bundle: Array<out Pair<String, Any?>>? = null,
    requestCode: Int = -1
) {
    (context as Activity).startAcForResult<T>(flag, bundle, requestCode)
}

inline fun <reified T> Activity.startAcForResult(
    flag: Int = Intent.FLAG_ACTIVITY_SINGLE_TOP,
    bundle: Array<out Pair<String, Any?>>? = null,
    requestCode: Int = -1
) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (bundle != null) putExtras(bundle.toBundle())
    }
    startActivityForResult(intent, requestCode)
}

fun Activity.finishDelay(delay: Long = 1) {
    Handler(Looper.getMainLooper()).postDelayed({ finish() }, delay)
}

//post, postDelay
fun Activity.post(action: () -> Unit) {
    Handler().post { action() }
}

fun Activity.postDelay(delay: Long = 0, action: () -> Unit) {
    Handler().postDelayed({ action() }, delay)
}