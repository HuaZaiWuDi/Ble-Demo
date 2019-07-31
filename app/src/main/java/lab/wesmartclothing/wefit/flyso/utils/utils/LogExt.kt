package com.wesmarclothing.kotlintools.kotlin.utils

import android.util.Log

/**
 * Description: log相关，日志的开关和默认tag通过AndroidKtxConfig来配置
 * Create by lxj, at 2018/12/5
 */

private enum class LogLevel {
    Verbose, Debug, Info, Warn, Error
}


fun Any.v(tag: String = DEFAULT_LOG_TAG) {
    intervalLog(LogLevel.Verbose, tag, this.toString())
}

fun Any.d(tag: String = DEFAULT_LOG_TAG) {
    intervalLog(LogLevel.Debug, tag, this.toString())
}

fun Any.i(tag: String = DEFAULT_LOG_TAG) {
    intervalLog(LogLevel.Info, tag, this.toString())
}

fun Any.w(tag: String = DEFAULT_LOG_TAG) {
    intervalLog(LogLevel.Warn, tag, this.toString())
}

fun Any.e(tag: String = DEFAULT_LOG_TAG) {
    intervalLog(LogLevel.Error, tag, this.toString())
}


private fun intervalLog(level: LogLevel, tag: String, msg: String) {
    if (isDebug) {
        when (level) {
            LogLevel.Verbose -> Log.v(tag, msg)
            LogLevel.Debug -> Log.d(tag, msg)
            LogLevel.Info -> Log.i(tag, msg)
            LogLevel.Warn -> Log.w(tag, msg)
            LogLevel.Error -> Log.e(tag, msg)
        }
    }
}
