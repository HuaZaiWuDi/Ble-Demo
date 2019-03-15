package lab.wesmartclothing.wefit.flyso.utils;

import com.alibaba.fastjson.JSON;
import com.smartclothing.blelibrary.util.ByteUtil;
import com.vondear.rxtools.aboutByte.BitUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * @Package lab.wesmartclothing.wefit.flyso.utils
 * @FileName HeartRateUtil
 * @Date 2018/11/6 16:48
 * @Author JACK
 * @Describe TODO心率保存工具类
 * @Project Android_WeFit_2.0
 */
public class HeartRateUtil {
    private List<Integer> heartRateLists = new ArrayList<>();
    private int maxHeart = 0;//最大心率
    private int minHeart = 200;//最小心率
    private double kcalTotal = 0;//总卡路里
    private int lastHeartRate = 0;//上一次的心率值
    private int realHeartRate = 0;//真实心率
    private static final int heartDeviation = 6;//心率误差值
    //补差值
    private static int heartUpSupplement = (int) (Math.random() * 4 + 2);
    private static int heartDownSupplement = (int) (Math.random() * 4 + 1);
    private long lastTime = 0;//历史记录上一条时间
    private int packageCounts = 0;//历史数据包序号


    public HeartRateUtil() {
    }

    public void addRealTimeData(byte[] bytes) {
        int heartRate = realHeartRate = bytes[8] & 0xff;

//        if (lastHeartRate != 0) {
//            if (lastHeartRate - heartRate > heartDeviation) {
//                heartRate = lastHeartRate - heartUpSupplement;
//            } else if (lastHeartRate - heartRate < -heartDeviation) {
//                heartRate = lastHeartRate + heartDownSupplement;
//            }
//        }

        lastHeartRate = heartRate;

        long time = ByteUtil.bytesToLongD4(bytes, 3) * 1000;
        RxLogUtils.d("硬件时间：" + RxFormat.setFormatDate(time, RxFormat.Date_Date));

        maxHeart = heartRate > maxHeart ? heartRate : maxHeart;
        minHeart = heartRate < minHeart ? heartRate : minHeart;

        SportsDataTab mSportsDataTab = new SportsDataTab();
        mSportsDataTab.setAthlRecord_2(heartRateLists);
        mSportsDataTab.setCurHeart(heartRate);
        mSportsDataTab.setMaxHeart(maxHeart);
        mSportsDataTab.setMinHeart(minHeart);
        mSportsDataTab.setRealHeart(realHeartRate);
        mSportsDataTab.setVoltage(ByteUtil.bytesToIntD2(new byte[]{bytes[15], bytes[16]}));
        mSportsDataTab.setLightColor((BitUtils.setBitValue(bytes[17], 7, (byte) 0) & 0xff));
        mSportsDataTab.setTemp((bytes[10] & 0xff));
        mSportsDataTab.setSteps(ByteUtil.bytesToIntD2(new byte[]{bytes[12], bytes[13]}));
        mSportsDataTab.setData(bytes);
        mSportsDataTab.setDate(System.currentTimeMillis());
        mSportsDataTab.setPower((BitUtils.checkBitValue(bytes[17], 7)));
        //卡路里累加计算
        kcalTotal += HeartRateToKcal.getCalorie(heartRate, 2f / 3600);

        mSportsDataTab.setKcal(RxFormatValue.format4S5R(kcalTotal, 1));//统一使用卡为基本热量单位

        RxLogUtils.d("数据：" + mSportsDataTab.toString());
        RxBus.getInstance().post(mSportsDataTab);

    }


    /**
     * 未使用
     * 瘦身衣历史心率数据
     *
     * @param bytes
     */
    public void addHsitoryData(byte[] bytes) {
        long time = ByteUtil.bytesToLongD4(bytes, 3);

        //根据时间去重
        if (lastTime == time) {
            RxLogUtils.d("表示重复包");
        } else {
            packageCounts++;
            RxLogUtils.d("包序号：" + packageCounts);
            lastTime = time;
            int heartRate = bytes[8] & 0xff;

        }
    }


    public void uploadHeartRate() {
        RxCache.getDefault().<HeartRateBean>load(Key.CACHE_ATHL_RECORD_PLAN, HeartRateBean.class)
                .map(new CacheResult.MapFunc<HeartRateBean>())
                .subscribe(new RxSubscriber<HeartRateBean>() {
                    @Override
                    protected void _onNext(HeartRateBean mHeartRateBean) {
                        saveHeartRate(mHeartRateBean);
                    }
                });

        RxCache.getDefault().<HeartRateBean>load(Key.CACHE_ATHL_RECORD_FREE, HeartRateBean.class)
                .map(new CacheResult.MapFunc<HeartRateBean>())
                .subscribe(new RxSubscriber<HeartRateBean>() {
                    @Override
                    protected void _onNext(HeartRateBean mHeartRateBean) {
                        saveHeartRate(mHeartRateBean);
                    }
                });
    }


    public void saveHeartRate(final HeartRateBean heartRateBean) {
        if (heartRateBean == null) return;
        int sumTime = 0;
        List<HeartRateItemBean> heartList = heartRateBean.getHeartList();
        if (heartList != null) {
            sumTime = heartList.size() * 2;
        }
        if (sumTime < 3 * 60) return;

        String s = JSON.toJSONString(heartRateBean);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addAthleticsInfo(NetManager.fetchRequest(s)))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加心率：保存成功删除本地缓存：");
                        clearData(heartRateBean);
                        //这里因为是后台上传数据，并不是跳转，使用RxBus方式
                        RxBus.getInstance().post(new RefreshSlimming());
                    }

                });
    }


    public void clearData(HeartRateBean heartRateBean) {
        if (heartRateBean != null) {
            try {
                //RxCache 库的一个Bug
                RxCache.getDefault().remove(heartRateBean.getPlanFlag() == 1 ? Key.CACHE_ATHL_RECORD_PLAN :
                        Key.CACHE_ATHL_RECORD_FREE);
            } catch (Exception e) {
                RxLogUtils.e("RxCache LruDiskCache is null");
            }
        }
    }

}
