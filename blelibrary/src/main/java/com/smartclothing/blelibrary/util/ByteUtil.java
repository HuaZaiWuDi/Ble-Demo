package com.smartclothing.blelibrary.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * 项目名称：DXYBle_GM
 * 类描述： 进制转换
 * 创建人：Jack
 * 创建时间：2017/6/23
 */
public class ByteUtil {


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用 小端模式
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytesD4(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) (value & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[3] = (byte) ((value >> 24) & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用 小端模式
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] longToBytesD4(long value) {
        byte[] src = new byte[4];
        src[0] = (byte) (value & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[3] = (byte) ((value >> 24) & 0xFF);
        return src;
    }


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用 大端模式
     */
    public static byte[] intToBytesG4(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToIntD4(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static long bytesToLongD4(byte[] src, int offset) {
        if (src.length < 4) return 0;
        long value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }


    /**
     * byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToIntG4(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }


    /**
     * int到byte[]
     * <p>
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，bytes2ToInt（）配套使用
     *
     * @param i
     * @return
     */
    public static byte[] intToBytesD2(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) (i & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        return result;
    }


    /**
     * bytes到int
     * <p>
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，bytes2ToInt（）配套使用
     *
     * @return
     */
    public static int bytesToIntD2(byte[] bytes) {
        int value = 0;
        value = (int) ((bytes[0] & 0xFF)
                | ((bytes[1] & 0xFF) << 8));
        return value;
    }

    /**
     * int到byte[]
     * <p>
     * byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序，bytes2ToInt（）配套使用
     *
     * @param i
     * @return
     */
    public static byte[] intToBytesG2(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }


    /**
     * bytes到int
     * <p>
     * byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序，bytes2ToInt（）配套使用
     *
     * @return
     */
    public static int bytesToIntG2(byte[] bytes) {
        int value = 0;
        value = (int) ((bytes[1] & 0xFF)
                | ((bytes[0] & 0xFF) << 8));
        return value;
    }


    /**
     * bytes到16进制String
     * <p>
     *
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * bytes到ascii对照表String
     * <p>
     *
     * @return
     */
    public static String byteToString(byte[] data) throws UnsupportedEncodingException {
        byte[] src = new byte[data[1] - 1];
        System.arraycopy(data, 3, src, 0, src.length);
        Log.d("ByteUtil", "byte[]src:" + src);

        Log.d("ByteUtil", new String(src, "ascii"));
        return new String(src, "ascii");

    }


    /**
     * 数字字符串转ASCII码字符串
     *
     * @param
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }

    /**
     * ASCII码字符串转数字字符串
     *
     * @param
     * @return 字符串
     */
    public static String AsciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }

    /**
     * 十六进制字符串装十进制
     *
     * @param hex 十六进制字符串
     * @return 十进制数值
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }


    /**
     * 十六进制字符串到16进制字节
     *
     * @return hexString 十六进制数值
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 把byte转化成2进制字符串
     *
     * @param b
     * @return
     */
    public static String getBinaryStrFromByte2(byte b) {
        String result = "";
        byte a = b;
        for (int i = 0; i < 8; i++) {
            result = (a % 2) + result;
            a = (byte) (a >> 1);
        }
        return result;
    }

}
