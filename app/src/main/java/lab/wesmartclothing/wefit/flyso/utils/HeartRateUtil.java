package lab.wesmartclothing.wefit.flyso.utils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLogUtils;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.util.ByteUtil;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
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
    private List<HeartRateItemBean> heartLists;
    private boolean pause = false;
    private UserInfo userInfo;


    public HeartRateUtil() {
        maxHeart = 0;//最大心率
        minHeart = 200;//最小心率
        kcalTotal = 0;//总卡路里
        heartLists = new ArrayList<>();
        userInfo = MyAPP.gUserInfo;
    }


    public SportsDataTab addRealTimeData(byte[] bytes) {
        //是否暂停


        int heartRate = realHeartRate = bytes[8] & 0xff;


        //降低误差值
//        if (lastHeartRate != 0) {
//            if (lastHeartRate - heartRate > heartDeviation) {
//                heartRate = lastHeartRate - heartUpSupplement;
//            } else if (lastHeartRate - heartRate < -heartDeviation) {
//                heartRate = lastHeartRate + heartDownSupplement;
//            }
//        }

//        //限制心率范围 2019-04-22 去掉最大最小心率范围
//        if (heartRate > (Key.HRART_SECTION[6] & 0xFF)) {
//            heartRate = (Key.HRART_SECTION[6] & 0xFF);
//        } else if (heartRate < (Key.HRART_SECTION[0] & 0xFF)) {
//            heartRate = (Key.HRART_SECTION[0] & 0xFF);
//        }


        lastHeartRate = heartRate;

        maxHeart = Math.max(heartRate, maxHeart);
        minHeart = Math.min(heartRate, minHeart);

        //心率处于静息心率区间时不计算卡路里，
        if (heartRate >= Key.HRART_SECTION[1]) {
            kcalTotal += HeartRateToKcal.getCalorie(heartRate, 2f / 3600);
        } else {
            //静息卡路里的计算：
            //静息(Kcal/s)：基础代谢值/(24*60*60)*time*1.2
            if (userInfo != null) {
                kcalTotal += userInfo.getBaselHeat() * 2f / (24 * 60 * 60) * 1.2;
            }
        }


        SportsDataTab mSportsDataTab = new SportsDataTab();
        mSportsDataTab.setCurHeart(heartRate);
        mSportsDataTab.setMaxHeart(maxHeart);
        mSportsDataTab.setMinHeart(minHeart);
        mSportsDataTab.setRealHeart(realHeartRate);
//        mSportsDataTab.setVoltage(ByteUtil.bytesToIntD2(new byte[]{bytes[15], bytes[16]}));
//        mSportsDataTab.setLightColor((BitUtils.setBitValue(bytes[17], 7, (byte) 0) & 0xff));
//        mSportsDataTab.setTemp((bytes[10] & 0xff));
//        mSportsDataTab.setSteps(ByteUtil.bytesToIntD2(new byte[]{bytes[12], bytes[13]}));
        mSportsDataTab.setData(bytes);
        mSportsDataTab.setDate(System.currentTimeMillis());
//        mSportsDataTab.setPower((BitUtils.checkBitValue(bytes[17], 7)));
        mSportsDataTab.setHeartLists(heartLists);
        mSportsDataTab.setKcal(kcalTotal);//统一使用卡为基本热量单位


        HeartRateItemBean heartRateTab = new HeartRateItemBean();
        heartRateTab.setHeartRate(heartRate);
        heartRateTab.setHeartTime(mSportsDataTab.getDate());
        heartRateTab.setStepTime(2);
        heartLists.add(heartRateTab);

        RxLogUtils.i("瘦身衣心率数据：" + mSportsDataTab.toString());
        return mSportsDataTab;
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


    public boolean pause() {
        return pause;
    }

    public void setPause(boolean play) {
        pause = play;
    }

}
