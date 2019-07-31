package com.wesmarclothing.kotlintools.kotlin.utils

import androidx.annotation.IntRange
import java.lang.Double
import java.math.BigDecimal

/**
 * @Package com.wesmarclothing.kotlintools.kotlin
 * @FileName NumberExt
 * @Date 2019/7/26 11:53
 * @Author JACK
 * @Describe TODO
 * @Project WeiMiBra
 */


/**
 *  Float不丢失进度转换成Double
 */
fun Float.tomDouble(): kotlin.Double {
    return Double.parseDouble(this.toString())
}

/**
 *  scale 精度
 */
fun Number.toScale(@IntRange(from = 0) scale: Int): String {
    return String.format("%.${scale}f", Double.parseDouble(this.toString()))
}


/**
 *  scale 精度
 */
fun Number.div(@IntRange(from = 0) scale: Int = 2): kotlin.Double {
    val b1 = BigDecimal(this.toString())
    val b2 = BigDecimal("1")
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
}


/**
 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
 *
 * @param this    被除數
 * @param v2    除數
 * @param scale 表示表示需要精確到小數點以後位数。
 * @return 兩個參數的商
 */
fun Number.div(v2: Number, @IntRange(from = 0) scale: Int = 2): kotlin.Double {
    val b1 = BigDecimal(this.toString())
    val b2 = BigDecimal(v2.toString())
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
}


/**
 * 提供精确的乘法运算
 *
 * @param this 被乘数
 * @param v2 乘数
 * @return 两个参数的积
 */
fun Number.mul(v2: Number): kotlin.Double {
    val b1 = BigDecimal(this.toString())
    val b2 = BigDecimal(v2.toString())
    return b1.multiply(b2).toDouble()
}


/**
 * 提供精确的减法运算
 *
 * @param v1 被減数
 * @param v2 減数
 * @return两个参数的差
 */
fun Number.sub(v2: Number): kotlin.Double {
    val b1 = BigDecimal(this.toString())
    val b2 = BigDecimal(v2.toString())
    return b1.subtract(b2).toDouble()
}

/**
 * 提供精确的加法运算
 *
 * @param v1 被加数
 * @param v2 加数
 * @return 两个参数的和
 */
fun Number.add(v2: Number): kotlin.Double {
    val b1 = BigDecimal(this.toString())
    val b2 = BigDecimal(v2.toString())
    return b1.add(b2).toDouble()
}

