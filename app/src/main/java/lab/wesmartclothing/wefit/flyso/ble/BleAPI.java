package lab.wesmartclothing.wefit.flyso.ble;

import android.util.Log;

import com.clj.fastble.utils.HexUtil;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.listener.BleChartChangeCallBack;
import lab.wesmartclothing.wefit.flyso.ble.util.ByteUtil;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/5/19.
 */
public class BleAPI {

    static final String TGA = "【BleAPI】";


    private static byte[] time2Byte() {
        long time = System.currentTimeMillis() / 1000;
        byte[] bytes = ByteUtil.longToBytesLittle(time);
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
     * <p>
     * 配置设置参数
     */

    /**
     * @param heatState      加热状态
     *                       heartSection   硬件心率区间
     *                       heat           温度
     *                       LED            灯光
     *                       height         用户身高（cm）
     *                       time           采集时长（s）
     * @param bleChartChange 回调
     */
    public static void syncSetting(boolean heatState, BleChartChangeCallBack bleChartChange) {
//        0x40 0x11 0x01 0x01 0x41 0x42 0x43 0x440x45 0x02 0x20 0x03 0x32 0x04 0x01
//        0x02 0x11 0x01
        int heat = 40;
        int LED = 50;
        byte[] heartSection = Key.heartRates;
        int[] stepSpeedSection = Key.HRART_SECTION;
        int height = MyAPP.getgUserInfo().getHeight();
        int time = 10;

        byte[] bytes = new byte[50];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x01;

        //心率阈值
        bytes[3] = 0x01;
        System.arraycopy(heartSection, 0, bytes, 4, heartSection.length);
        int index = 3 + heartSection.length;

        //加热温度
        index++;
        bytes[index] = 0x02;
        index++;
        bytes[index] = (byte) heat;

        //灯光
        index++;
        bytes[index] = 0x03;
        index++;
        bytes[index] = 0x01;

        //加热状态
        index++;
        bytes[index] = 0x04;
        index++;
        bytes[index] = (byte) (heatState ? 0x01 : 0x00);
        index++;

        //身高
        bytes[index] = 0x05;
        index++;
        bytes[index] = (byte) height;

        //采集时长
        //身高
        bytes[index] = 0x06;
        index++;
        bytes[index] = (byte) time;

        //配速区间 去掉区间最小值，发送1-6位置值，小端
        index++;
        bytes[index] = 0x07;

        for (int i = 1; i < stepSpeedSection.length; i++) {
            index++;
            bytes[index] = ByteUtil.intToBytesLittle2(stepSpeedSection[i])[0];
            index++;
            bytes[index] = ByteUtil.intToBytesLittle2(stepSpeedSection[i])[1];
        }

        Log.d("【写配置】", HexUtil.encodeHexStr(bytes));
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

        Log.d("【读配置】", HexUtil.encodeHexStr(bytes));
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

        Log.d("【同步设备时间】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, bleChartChange);
    }


    public static void syncDataCount(BleChartChangeCallBack bleChartChange) {
//        0x40 0x11 0x04
//        0x02 0x11 0x04 0x11 0x22 0x33 0x44

        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x04;

        Log.d("【同步本地数据包数】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, bleChartChange);
    }

    public static void queryData() {
//        0x00 0x11 0x05

        byte[] bytes = new byte[20];
        bytes[0] = 0x00;
        bytes[1] = 0x11;
        bytes[2] = 0x05;

        Log.d("【请求包】", HexUtil.encodeHexStr(bytes));
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

        Log.d("【反馈数据】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().writeNo(bytes);
    }

    //notify
    public static void clothingStop() {
        /**
         * 表示：设备停止活动，心率为0
         * */
//        0x40 0x11 0x060x11 0x22 0x33 0x44 0x01 0x45 0x02 0x20 0x03 0x55 0x66 0x04 0x77 0x88
//        App返回：0x02 0x11 0x06

        byte[] bytes = new byte[20];
        bytes[0] = 0x02;
        bytes[1] = 0x11;
        bytes[2] = 0x08;

        Log.d("【结束活动反馈数据】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().writeNo(bytes);
    }

    //读设备信息
    public static void readDeviceInfo(BleChartChangeCallBack bleChartChange) {
        /**
         * typedef struct
         {
         uint8_t category;
         uint8_t module;
         uint16_t manufacture;
         uint16_t HW_version;
         uint8_t  APP_version[3];
         uint8_t  BOOT_version[3];
         uint8_t  BLE_version;
         uint8_t  SN[4];
         } device_info_t;
         *
         * */

        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x09;

        Log.d("【读设备信息】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, bleChartChange);
    }

    public static void getVoltage(BleChartChangeCallBack bleChartChange) {
        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x0a;

        Log.d("【读设备电压】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, bleChartChange);
    }

    public static void clearStep(BleChartChangeCallBack bleChartChange) {
        byte[] bytes = new byte[20];
        bytes[0] = 0x40;
        bytes[1] = 0x11;
        bytes[2] = 0x0b;

        Log.d("【读设备电压】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, bleChartChange);
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
