package com.smartclothing.blelibrary;

/**
 * Created by jk on 2018/5/19.
 */
public interface BleKey {

    //扫描名字过滤
    String ScaleName = "QN-Scale";//体脂称
    //    String Smart_Clothing = "W";
//    String Smart_Clothing = "WeSm";
    String Smart_Clothing = "WeSma";
//    String Smart_Clothing = "WeSmartCloth";

    /**
     * 热身 100-120
     * 燃脂 120-140
     * 耐力 140-160
     * 无氧 160-180
     * 极限 180-200
     */
    //心率阈值
    //100-120-140-160-180
    byte[] heartRates = new byte[]{0x64, 0x78, (byte) 0x8c, (byte) 0xa0, (byte) 0xb4};


    //绑定设备类型
    /**
     * ZS-TZC-0001    (体脂称)
     * ZS-SSY-0001    (瘦身衣)
     */
    String TYPE_SCALE = "ZS-TZC-0001";
    String TYPE_CLOTHING = "ZS-SSY-0001";


    ///////////////////////////////////////////////////////////////////////////
    // 固件版本
    ///////////////////////////////////////////////////////////////////////////
    String FIRMWARE_VERSION = "FIRMWARE_VERSION";


    String UUID_Servie = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase();

    String UUID_CHART_WRITE = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase();

    String UUID_CHART_READ_NOTIFY = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase();


    //体质秤主UUID
    String UUID_QN_SCALE = "0000ffe0-0000-1000-8000-00805f9b34fb".toLowerCase();


    String ACTION_DFU_STARTING = "ACTION_DFU_STARTING";//广播通知开始DFU
    String EXTRA_DFU_STARTING = "EXTRA_DFU_STARTING";//固件升级是否完成

}
