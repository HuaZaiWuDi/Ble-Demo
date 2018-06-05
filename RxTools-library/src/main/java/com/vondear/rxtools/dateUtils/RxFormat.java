package com.vondear.rxtools.dateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 项目名称：TextureViewDome
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/7/18
 */
public class RxFormat {

    public RxFormat() {
        throw new RuntimeException("cannot be instantiated");
    }

    public static final String Date_Date = "yyyy-MM-dd HH:mm:ss";
    public static final String Date_Time = "HH:mm:ss";
    public static final String Date_Date_CH = "yyyy年MM月dd日HH时mm分ss秒";
    public static final String Date_Time_CH = "HH时mm分ss秒";
    public static final String Date = "yyyy-MM-dd";
    public static final String Date_Month_Day = "MM月dd日";


    public static String setFormatDate(long value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(value);
    }

    public static String setFormatDateG8(long value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(value);
    }

    public static String setFormatDateG8(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    public static String setFormatDateG8(Calendar calendar, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    public static String setFormatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    public static String setFormatDate(Calendar calendar, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    public static String setFormatNum(long value, String format) {
        return new DecimalFormat(format).format(value);
    }


    public static long getDateMillis(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }// 毫秒

        return millionSeconds;
    }


    public static Date setParseDate(String value, String format) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Number setParseNumber(String value, String format) {
        try {
            return new DecimalFormat(format).parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
