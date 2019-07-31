package com.wesmarclothing.kotlintools.kotlin.utils

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Description: Fragment相关扩展
 * Create by dance, at 2018/12/5
 */

fun FragmentActivity.replace(layoutId: Int, f: Fragment, bundle: Array<out Pair<String, Any?>>?) {
    if (bundle != null) f.arguments = bundle.toBundle()
    supportFragmentManager.beginTransaction()
        .replace(layoutId, f)
        .commitAllowingStateLoss()
}

fun FragmentActivity.add(layoutId: Int, f: Fragment, bundle: Array<out Pair<String, Any?>>?) {
    if (bundle != null) f.arguments = bundle.toBundle()
    supportFragmentManager.beginTransaction()
        .add(layoutId, f)
        .commitAllowingStateLoss()
}

fun FragmentActivity.hide(f: Fragment) {
    supportFragmentManager.beginTransaction()
        .hide(f)
        .commitAllowingStateLoss()
}

fun FragmentActivity.show(f: Fragment) {
    supportFragmentManager.beginTransaction()
        .show(f)
        .commitAllowingStateLoss()
}

fun FragmentActivity.remove(f: Fragment) {
    supportFragmentManager.beginTransaction()
        .remove(f)
        .commitAllowingStateLoss()
}


fun FragmentActivity.switch(layoutId: Int, fromFragment: Fragment, toFragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    if (!toFragment.isAdded) {    // 先判断是否被add过
        transaction.hide(fromFragment).add(layoutId, toFragment)
            .commitAllowingStateLoss() // 隐藏当前的fragment，add下一个到Activity中
    } else {
        transaction.hide(fromFragment).show(toFragment)
            .commitAllowingStateLoss() // 隐藏当前的fragment，显示下一个
    }
}

fun Fragment.replace(layoutId: Int, f: Fragment, bundle: Array<out Pair<String, Any?>>?) {
    if (bundle != null) f.arguments = bundle.toBundle()
    childFragmentManager.beginTransaction()
        .replace(layoutId, f)
        .commitAllowingStateLoss()
}


fun Fragment.add(layoutId: Int, f: Fragment, bundle: Array<out Pair<String, Any?>>?) {
    if (bundle != null) f.arguments = bundle.toBundle()
    childFragmentManager.beginTransaction()
        .add(layoutId, f)
        .commitAllowingStateLoss()
}


fun Fragment.hide(f: Fragment) {
    childFragmentManager.beginTransaction()
        .hide(f)
        .commitAllowingStateLoss()
}

fun Fragment.show(f: Fragment) {
    childFragmentManager.beginTransaction()
        .show(f)
        .commitAllowingStateLoss()
}

fun Fragment.remove(f: Fragment) {
    childFragmentManager.beginTransaction()
        .remove(f)
        .commitAllowingStateLoss()
}

//post, postDelay
fun Fragment.post(action: () -> Unit) {
    Handler().post { action() }
}

fun Fragment.postDelay(delay: Long = 0, action: () -> Unit) {
    Handler().postDelayed({ action() }, delay)
}