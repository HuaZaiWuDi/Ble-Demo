package com.smartclothing.blelibrary;

import com.smartclothing.blelibrary.listener.BleChartChangeCallBack;
import com.smartclothing.blelibrary.util.ByteUtil;

/**
 * Created by jk on 2018/5/19.
 */
public class BleAPI {


    private static byte[] time2Byte() {
        long time = System.currentTimeMillis() / 1000;
        byte[] bytes = ByteUtil.longToBytesD4(time);
        return bytes;
    }


    /**
     * 配置信息	写配置	0x01
     * 读配置	0x02
     * 同步信息	同步时间	0x03
     * 同步数据查询	0x04
     * 同步数据请求	0x05
     * 同步数据返回	0x06
     * 数据信息	通知数据	0x07
     * <p>
     * Bit 0：帧类型标志
     * 1：数据帧
     * 0：命令帧
     * <p>
     * b0，b1	描述
     * 10	数据请求
     * 11	数据应答
     * 00	命令请求
     * 01	命令应答
     */

    public static void syncSetting(byte[] heartRates, int heat, int LED, int heatState, BleChartChangeCallBack bleChartChange) {
//        0x40 0x11 0x01 0x01 0x41 0x42 0x43 0x440x45 0x02 0x20 0x03 0x32 0x04 0x01
//        0x02 0x11 0x01
        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x01;

        if (heartRates != null && heartRates.length == 5) {
            bytes[3] = 0x01;
            System.arraycopy(heartRates, 0, bytes, 4, 5);
        }
        if (heat >= 0) {
            bytes[9] = 0x02;
            bytes[10] = (byte) heat;
        }
        if (LED >= 0) {
            bytes[11] = 0x03;
            bytes[12] = (byte) LED;
        }
        if (heatState >= 0) {
            bytes[13] = 0x04;
            bytes[14] = (byte) heatState;
        }

        BleTools.getInstance().write(bytes, bleChartChange);

    }


    public static void readSetting(BleChartChangeCallBack bleChartChange) {
//        0x40 0x11 0x02	0x01 0x02 0x03 0x04
//        0x40 0x11 0x01 0x01 0x41 0x42 0x43 0x440x45 0x02 0x20 0x03 0x32 0x04 0x01

        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x02;
        bytes[3] = time2Byte()[0];
        bytes[4] = time2Byte()[1];
        bytes[5] = time2Byte()[2];
        bytes[6] = time2Byte()[3];

        BleTools.getInstance().write(bytes, bleChartChange);
    }

    public static void syncDeviceTime(BleChartChangeCallBack bleChartChange) {
//        0x40 0x11 0x03 0x11 0x12 0x33 0x44
//        0x02 0x11 0x03
        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x03;
        bytes[3] = time2Byte()[0];
        bytes[4] = time2Byte()[1];
        bytes[5] = time2Byte()[2];
        bytes[6] = time2Byte()[3];
        BleTools.getInstance().write(bytes, bleChartChange);
    }


    public static void syncDataCount(BleChartChangeCallBack bleChartChange) {
//        0x40 0x11 0x04
//        0x02 0x11 0x04 0x11 0x22 0x33 0x44

        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x04;
        BleTools.getInstance().write(bytes, bleChartChange);
    }

    public static void queryData() {
//        0x00 0x11 0x05

        byte[] bytes = new byte[20];
        bytes[0] = 0x00;
        bytes[1] = 0x11;
        bytes[2] = 0x05;
        BleTools.getInstance().writeNo(bytes);
    }


    //notify
    public static void syncData(byte[] time) {
        /**
         * 表示：终端发送同步数据，时间戳为0x44332211 心率为0x45 温度为0x20 计步为0x6655
         * 电池电压为0x8877，整数后两位为小数点之后两位，电压v。
         * */
//        0x40 0x11 0x060x11 0x22 0x33 0x44 0x01 0x45 0x02 0x20 0x03 0x55 0x66 0x04 0x77 0x88
//        App返回：0x02 0x11 0x06

        byte[] bytes = new byte[20];
        bytes[0] = 0x02;
        bytes[1] = 0x11;
        bytes[2] = 0x06;
        bytes[3] = time[0];
        bytes[4] = time[1];
        bytes[5] = time[2];
        bytes[6] = time[3];
        BleTools.getInstance().writeNo(bytes);
    }


    /**
     * 设备Notify反馈数据
     *
     * 终端发送：	0x00 0x11 0x070x11 0x22 0x33 0x44 0x01 0x45 0x02 0x20 0x03 0x55 0x66 0x04 0x77 0x88
     *
     * 表示：终端发送同步数据，时间戳为0x44332211 心率为0x45 温度为0x20 计步为0x6655
     * 电池电压为0x8877，整数后两位为小数点之后两位，电压v。
     * */

    /**
     * 4.	协议交互描述
     1，当上电后，并且心率超过零时，开始采集计步，温度，心率，电池数据，如果蓝牙连接了就以2秒的周期或温度变化1°的条件发送给app。如果蓝牙没有连接，每10秒存一组带时间戳的数据，供后续同步用。

     2，当心率超过零时，并且加热配置位使能时，开始按照设定温度加热，并通过温度传感器实时调节，而当心率跳动过快时考虑关闭加热电路。

     3，一旦蓝牙连接，app就应该下发同步时间命令，读写配置命令根据业务的时间需求来下发。

     4，一旦蓝牙连接，不管心率是多少，只要app发送了同步数据命令，就开始上传存储到flash里的数据。

     5，led呼吸灯的颜色根据配置的心率值分为四档，采用蓝绿黄紫颜色来调节。

     6，电池电量低于15%时亮红色灯。

     7，计步是一个单独的功能，不和设备的任何输出构成关系。

     8，心率服务（HRS），电池服务（BAS），设备信息服务（DIS）采用标准的蓝牙协议。参考https://www.bluetooth.com/zh-cn/specifications/gatt

     9，除了2秒周期的notify上传数据帧，其他命令都采用带ack的确认格式，通过包序列号和蓝牙mac地址，来进行成功或失败的确认机制，和重传机制。

     *
     *
     * */


}
