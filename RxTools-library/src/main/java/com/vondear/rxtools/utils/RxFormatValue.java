package com.vondear.rxtools.utils;

import java.math.BigDecimal;

/**
 * 项目名称：CheckInOut
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/9/28
 */
public final class RxFormatValue {

    public RxFormatValue() {
        throw new RuntimeException("BigDecimalUtil can not init");
    }

    /**
     * 1、ROUND_UP                                        (非零则入)
     * 舍入远离零的舍入模式。
     * 在丢弃非零部分之前始终增加数字(始终对非零舍弃部分前面的数字加1)。
     * 注意，此舍入模式始终不会减少计算值的大小。
     * 2、ROUND_DOWN                                     （直接去掉）
     * 接近零的舍入模式。
     * 在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1，即截短)。
     * 注意，此舍入模式始终不会增加计算值的大小。
     * 3、ROUND_CEILING                                  （正入负舍  正数加一，负数去掉  始终变大）
     * 接近正无穷大的舍入模式。
     * 如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同;
     * 如果为负，则舍入行为与 ROUND_DOWN 相同。
     * 注意，此舍入模式始终不会减少计算值。
     * 4、ROUND_FLOOR                                    （正舍负入  正数去掉，负数加一  始终变小 ）
     * 接近负无穷大的舍入模式。
     * 如果 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同;
     * 如果为负，则舍入行为与 ROUND_UP 相同。
     * 注意，此舍入模式始终不会增加计算值。
     * 5、ROUND_HALF_UP                                  （四舍五入）
     * 向“最接近的”数字舍入，如果与两个相邻数字的距离相等，
     * 则为向上舍入的舍入模式。
     * 如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同;
     * 否则舍入行为与 ROUND_DOWN 相同。
     * 注意，这是我们大多数人在小学时就学过的舍入模式(四舍五入)。
     * 6、ROUND_HALF_DOWN                                （五舍六入）
     * 向“最接近的”数字舍入，如果与两个相邻数字的距离相等，
     * 则为上舍入的舍入模式。
     * 如果舍弃部分 > 0.5，则舍入行为与 ROUND_UP 相同;
     * 否则舍入行为与 ROUND_DOWN 相同(五舍六入)。
     * 7、ROUND_HALF_EVEN                                （别用）
     * 向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
     * 如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同;
     * 如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。
     * 注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
     * 此舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。
     * 如果前一位为奇数，则入位，否则舍去。
     * 以下例子为保留小数点1位，那么这种舍入方式下的结果。
     * 1.15>1.2 1.25>1.2
     * 8、ROUND_UNNECESSARY                              （不舍入）
     * 断言请求的操作具有精确的结果，因此不需要舍入。
     * 如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
     */

    /**
     * 方法描述：doubleToString
     *
     * @param i     值
     * @param scale 保留小数点后几位
     * @param type  保留的模式
     * @return String
     */
    public static String fromatDouble(double i, int scale, int type) {
        BigDecimal bigDecimal = new BigDecimal(i);
        BigDecimal decimal = bigDecimal.setScale(scale, type);//保留小数点后2位，直接去掉值。
        return String.valueOf(decimal);
    }

    public static String fromatFloat(float i, int scale) {
        return fromatDouble(i, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 方法描述：四舍五入格式化
     *
     * @param i     值
     * @param scale 保留小数点后几位
     * @return String
     */
    public static String fromat4S5R(double i, int scale) {
        return fromatDouble(i, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 方法描述：五舍四入格式化
     *
     * @param i     值
     * @param scale 保留小数点后几位
     * @return String
     */
    public static String fromat5S4R(double i, int scale) {
        return fromatDouble(i, scale, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 方法描述：直接舍掉
     *
     * @param i     值
     * @param scale 保留小数点后几位
     * @return String
     */
    public static String fromatDown(double i, int scale) {
        return fromatDouble(i, scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 方法描述：直接入位
     *
     * @param i     值
     * @param scale 保留小数点后几位
     * @return String
     */
    public static String fromatUp(double i, int scale) {
        return fromatDouble(i, scale, BigDecimal.ROUND_UP);
    }
}
