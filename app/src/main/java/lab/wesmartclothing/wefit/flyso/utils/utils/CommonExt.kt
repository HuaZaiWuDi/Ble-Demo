package com.wesmarclothing.kotlintools.kotlin.utils


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * Description:  通用扩展
 * Create by dance, at 2018/12/5
 */

/** dp和px转换 **/
fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.dp2px(dpValue: Int): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Int): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}

fun Context.px2dp(pxValue: Float): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}

fun Fragment.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun Fragment.dp2px(dpValue: Int): Int {
    return context!!.dp2px(dpValue)
}

fun Fragment.px2dp(pxValue: Int): Float {
    return context!!.px2dp(pxValue)
}

fun View.px2dp(pxValue: Float): Float {
    return context!!.px2dp(pxValue)
}

fun View.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun View.dp2px(dpValue: Int): Int {
    return context!!.dp2px(dpValue)
}

fun View.px2dp(pxValue: Int): Float {
    return context!!.px2dp(pxValue)
}

fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Float {
    return itemView.px2dp(pxValue)
}

fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int {
    return itemView.dp2px(dpValue)
}

fun RecyclerView.ViewHolder.dp2px(dpValue: Int): Int {
    return itemView.dp2px(dpValue)
}

fun RecyclerView.ViewHolder.px2dp(pxValue: Int): Float {
    return itemView.px2dp(pxValue)
}

/**sp转换成px**/

fun Context.sp2px(spValue: Int): Int {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(), this.resources.displayMetrics).toInt()
}

fun Fragment.sp2px(spValue: Int): Int {
    return context!!.sp2px(spValue)
}

fun View.sp2px(spValue: Int): Int {
    return context!!.sp2px(spValue)
}

fun RecyclerView.ViewHolder.sp2px(spValue: Int): Int {
    return itemView.sp2px(spValue)
}


var toast: Toast? = null
/** toast相关 **/
fun Context.toast(msg: CharSequence) {
    if (toast == null) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast?.setGravity(Gravity.CENTER, 0, 0)
    } else {
        toast?.setText(msg)
    }
    toast?.show()
}

fun Context.longToast(msg: CharSequence) {
    if (toast == null) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        toast?.setGravity(Gravity.CENTER, 0, 0)
    } else {
        toast?.setText(msg)
    }
    toast?.show()
}

fun Fragment.toast(msg: CharSequence) {
    context?.toast(msg)
}

fun Fragment.longToast(msg: CharSequence) {
    context?.longToast(msg)
}

fun View.toast(msg: CharSequence) {
    context?.toast(msg)
}

fun View.longToast(msg: CharSequence) {
    context?.longToast(msg)
}


//单例
val mGson: Gson by lazy {
    Gson()
}

/** json相关 **/
fun Any.toJson(): String {
    return mGson.toJson(this).toString()
}

inline fun <reified T> String.toBean() = mGson.fromJson<T>(this, object : TypeToken<T>() {}.type)


/** Window相关 **/
fun Context.windowWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.width
}

fun Context.windowHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.height
}

fun Fragment.windowWidth(): Int {
    return context!!.windowWidth()
}

fun Fragment.windowHeight(): Int {
    return context!!.windowHeight()
}

fun View.windowWidth(): Int {
    return context!!.windowWidth()
}

fun View.windowHeight(): Int {
    return context!!.windowHeight()
}

fun RecyclerView.ViewHolder.windowWidth(): Int {
    return itemView.windowWidth()
}

fun RecyclerView.ViewHolder.windowHeight(): Int {
    return itemView.windowHeight()
}


private var lastClickTime: Long = 0
/**
 * 是否是快速点击
 *
 * retrun true 快速点击
 * */
fun isFastClick(millisecond: Int = 800): Boolean {
    val curClickTime = System.currentTimeMillis()
    val interval = curClickTime - lastClickTime

    if (interval in 1..(millisecond - 1)) {
        // 超过点击间隔后再将lastClickTime重置为当前点击时间
        return true
    }
    lastClickTime = curClickTime
    return false
}



/**
 * 数组转bundle
 */
fun Array<out Pair<String, Any?>>.toBundle(): Bundle? {
    return Bundle().apply {
        forEach {
            val value = it.second
            when (value) {
                null -> putSerializable(it.first, null as Serializable?)
                is Int -> putInt(it.first, value)
                is Long -> putLong(it.first, value)
                is CharSequence -> putCharSequence(it.first, value)
                is String -> putString(it.first, value)
                is Float -> putFloat(it.first, value)
                is Double -> putDouble(it.first, value)
                is Char -> putChar(it.first, value)
                is Short -> putShort(it.first, value)
                is Boolean -> putBoolean(it.first, value)
                is Serializable -> putSerializable(it.first, value)
                is Parcelable -> putParcelable(it.first, value)

                is IntArray -> putIntArray(it.first, value)
                is LongArray -> putLongArray(it.first, value)
                is FloatArray -> putFloatArray(it.first, value)
                is DoubleArray -> putDoubleArray(it.first, value)
                is CharArray -> putCharArray(it.first, value)
                is ShortArray -> putShortArray(it.first, value)
                is BooleanArray -> putBooleanArray(it.first, value)

                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putCharSequenceArray(it.first, value as Array<CharSequence>)
                    value.isArrayOf<String>() -> putStringArray(it.first, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putParcelableArray(it.first, value as Array<Parcelable>)
                }
            }
        }
    }

}