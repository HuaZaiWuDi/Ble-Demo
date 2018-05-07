package com.vondear.rxtools.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：CommonProject
 * 类描述：
 * 创建人：oden
 * 创建时间：2018/4/17
 */
public class RxFileBySystem {


    private static Map<String, String> FileType;

    static {
        FileType = new HashMap<>();
        FileType.put(".3gp", "video/3gpp");
        FileType.put(".apk", "application/vnd.android.package-archive");
        FileType.put(".asf", "video/x-ms-asf");
        FileType.put(".avi", "video/x-msvideo");
        FileType.put(".bin", "application/octet-stream");
        FileType.put(".bmp", "image/bmp");
        FileType.put(".c", "text/plain");
        FileType.put(".class", "application/octet-stream");
        FileType.put(".conf", "text/plain");
        FileType.put(".cpp", "text/plain");
        FileType.put(".doc", "application/msword");
        FileType.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        FileType.put(".xls", "application/vnd.ms-excel");
        FileType.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        FileType.put(".exe", "application/octet-stream");
        FileType.put(".gif", "image/gif");
        FileType.put(".gtar", "application/x-gtar");
        FileType.put(".gz", "application/x-gzip");
        FileType.put(".h", "text/plain");
        FileType.put(".htm", "text/html");
        FileType.put("", "*/*");
        FileType.put(".zip", "application/x-zip-compressed");
        FileType.put(".z", "application/x-compress");
        FileType.put(".xml", "text/plain");
        FileType.put(".wps", "application/vnd.ms-works");
        FileType.put(".wmv", "audio/x-ms-wmv");
        FileType.put(".wma", "audio/x-ms-wma");
        FileType.put(".wav", "audio/x-wav");
        FileType.put(".txt", "text/plain");
        FileType.put(".tgz", "application/x-compressed");
        FileType.put(".tar", "application/x-tar");
        FileType.put(".sh", "text/plain");
        FileType.put(".rtf", "application/rtf");
        FileType.put(".rmvb", "audio/x-pn-realaudio");
        FileType.put(".rc", "text/plain");
        FileType.put(".prop", "text/plain");
        FileType.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        FileType.put(".ppt", "application/vnd.ms-powerpoint");
        FileType.put(".pps", "application/vnd.ms-powerpoint");
        FileType.put(".png", "image/png");
        FileType.put(".pdf", "application/pdf");
        FileType.put(".ogg", "audio/ogg");
        FileType.put(".msg", "application/vnd.ms-outlook");
        FileType.put(".mpga", "audio/mpeg");
        FileType.put(".mpg4", "video/mp4");
        FileType.put(".mpg", "video/mpeg");
        FileType.put(".mpeg", "video/mpeg");
        FileType.put(".mpe", "video/mpeg");
        FileType.put(".mpc", "application/vnd.mpohun.certificate");
        FileType.put(".mp4", "video/mp4");
        FileType.put(".mp3", "audio/x-mpeg");
        FileType.put(".mp2", "audio/x-mpeg");
        FileType.put(".mov", "video/quicktime");
        FileType.put(".m4v", "video/x-m4v");
        FileType.put(".m4u", "video/vnd.mpegurl");
        FileType.put(".m4p", "audio/mp4a-latm");
        FileType.put(".m4b", "audio/mp4a-latm");
        FileType.put(".m4a", "audio/mp4a-latm");
        FileType.put(".m3u", "audio/x-mpegurl");
        FileType.put(".log", "text/plain");
        FileType.put(".js", "application/x-javascript");
        FileType.put(".jpg", "image/jpeg");
        FileType.put(".jpeg", "image/jpeg");
        FileType.put(".java", "text/plain");
        FileType.put(".jar", "application/java-archive");
        FileType.put(".html", "text/html");
    }


    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(@NonNull Context context, @NonNull File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);

        Uri data = RxShareUtlis.File2UriByN(context, file, intent);

        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/data, type);
        //跳转
        context.startActivity(intent);

    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;

        return FileType.get(end);
    }


}
