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

    //硬件心率三个档位
    //120-140-200
    byte[] heartRates2 = new byte[]{0x78, (byte) 0x8c, (byte) 0xc8};

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

    /**
     * 心率服务UUID：0000180d-0000-1000-8000-00805f9b34fb
     * 心率特征值UUID：00002a37-0000-1000-8000-00805f9b34fb
     * <p>
     * 电池电量服务UUID：0000180f-0000-1000-8000-00805f9b34fb
     * 电池电量特征值UUID：00002a19-0000-1000-8000-00805f9b34fb
     * <p>
     * 设备交互服务UUID：0000180a-0000-1000-8000-00805f9b34fb
     * D/TAG: 服务type0
     * D/TAG: 特征值UUID：00002a29-0000-1000-8000-00805f9b34fb
     * D/TAG: 特征值Permissions0
     * D/TAG: 特征值Properties2
     * D/TAG: 特征值WriteType2
     * D/TAG: 特征值UUID：00002a24-0000-1000-8000-00805f9b34fb
     * D/TAG: 特征值Permissions0
     * D/TAG: 特征值Properties2
     * D/TAG: 特征值WriteType2
     * D/TAG: 特征值UUID：00002a25-0000-1000-8000-00805f9b34fb
     * D/TAG: 特征值Permissions0
     * D/TAG: 特征值Properties2
     * D/TAG: 特征值WriteType2
     * D/TAG: 特征值UUID：00002a27-0000-1000-8000-00805f9b34fb
     * D/TAG: 特征值Permissions0
     * D/TAG: 特征值Properties2
     * D/TAG: 特征值WriteType2
     * D/TAG: 特征值UUID：00002a26-0000-1000-8000-00805f9b34fb
     * D/TAG: 特征值Permissions0
     * D/TAG: 特征值Properties2
     * D/TAG: 特征值WriteType2
     * D/TAG: 特征值UUID：00002a28-0000-1000-8000-00805f9b34fb
     * D/TAG: 特征值Permissions0
     * D/TAG: 特征值Properties2
     * D/TAG: 特征值WriteType2
     */


    String UUID_Servie = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase();

    String UUID_CHART_WRITE = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase();

    String UUID_CHART_READ_NOTIFY = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E".toLowerCase();


    //心率服务UUID
    String UUID_SERVICE_HEART_RATE = "0000180d-0000-1000-8000-00805f9b34fb".toLowerCase();

    //心率特征值
    String UUID_CHART_HEART_RATE_NOTIFY = "00002a37-0000-1000-8000-00805f9b34fb".toLowerCase();

    //电池服务UUID
    String UUID_SERVICE_BATTERY = "0000180f-0000-1000-8000-00805f9b34fb".toLowerCase();

    //电池特征值
    String UUID_CHART_BATTERY_NOTIFY = "00002a19-0000-1000-8000-00805f9b34fb".toLowerCase();


    //设备信息UUID
    String UUID_SERVICE_DEVICE_INFO = "0000180a-0000-1000-8000-00805f9b34fb".toLowerCase();

    //制造商名字
    String UUID_CHART_manufacturerName_READ = "00002a29-0000-1000-8000-00805f9b34fb".toLowerCase();


    String UUID_CHART_modelNumber_READ = "00002a24-0000-1000-8000-00805f9b34fb".toLowerCase();


    String UUID_CHART_serialNumber_READ = "00002a25-0000-1000-8000-00805f9b34fb".toLowerCase();


    String UUID_CHART_hardwareRevision_READ = "00002a27-0000-1000-8000-00805f9b34fb".toLowerCase();

    //固件版本
    String UUID_CHART_firmwareRevision_READ = "00002a26-0000-1000-8000-00805f9b34fb".toLowerCase();

    //软件版本
    String UUID_CHART_softwareRevision_READ = "00002a28-0000-1000-8000-00805f9b34fb".toLowerCase();


    //体质秤主UUID
    String UUID_QN_SCALE = "0000ffe0-0000-1000-8000-00805f9b34fb".toLowerCase();


    String ACTION_DFU_STARTING = "ACTION_DFU_STARTING";//广播通知开始DFU
    String EXTRA_DFU_STARTING = "EXTRA_DFU_STARTING";//固件升级是否完成

}
