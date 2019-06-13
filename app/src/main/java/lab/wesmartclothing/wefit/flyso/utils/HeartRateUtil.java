package lab.wesmartclothing.wefit.flyso.utils;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxTimeUtils;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.util.ByteUtil;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateItemBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

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
    private int minHeart = 10000;//最小心率
    private int maxPace = Key.HRART_SECTION[6];//最大配速
    private int minPace = 0;//最小配速
    private int avPace = 0;//平均配速
    private double kcalTotal = 0;//总卡路里
    private long lastTime = 0;//历史记录上一条时间
    private int packageCounts = 0;//历史数据包序号
    private List<HeartRateItemBean> heartLists;
    private UserInfo userInfo;
    private int initSteps = -1, lastStep = 0;
    private List<Double> kilometreList;

    public HeartRateUtil() {
        heartLists = new ArrayList<>();
        kilometreList = new ArrayList<>();
        userInfo = MyAPP.getgUserInfo();
    }


    /**
     * 01 心率 1byte  7,8
     * 02 温度 1byte  9,10
     * 03 计步 2byte  11,13
     * 04 电压 2byte  14,16
     * 05 配速 2byte  17,19
     */
    public SportsDataTab addRealTimeData(byte[] bytes) {
        //是否暂停

        int heartRate = bytes[8] & 0xff;

//        //限制心率范围 2019-04-22 去掉最大最小心率范围
//        if (heartRate > (Key.HRART_SECTION[6] & 0xFF)) {
//            heartRate = (Key.HRART_SECTION[6] & 0xFF);
//        } else if (heartRate < (Key.HRART_SECTION[0] & 0xFF)) {
//            heartRate = (Key.HRART_SECTION[0] & 0xFF);
//        }

//        maxHeart = Math.max(heartRate, maxHeart);
//        minHeart = Math.min(heartRate, minHeart);

//        //心率处于静息心率区间时不计算卡路里，
//        if (heartRate >= Key.HRART_SECTION[1]) {
//            kcalTotal += CalorieManager.getCalorie(heartRate, 2f / 3600);
//        } else {
//            //静息卡路里的计算：
//            //静息(Kcal/s)：基础代谢值/(24*60*60)*time*1.2
//            if (userInfo != null) {
//                kcalTotal += userInfo.getBaselHeat() * 2f / (24 * 60 * 60) * 1.2;
//            }
//        }

        int currentSteps = ByteUtil.bytesToIntLittle2(new byte[]{bytes[12], bytes[13]});


        float Weight = SPUtils.getFloat(SPKey.SP_realWeight, 0);
        double kilometre = CalorieManager.getKilometre(userInfo.getHeight(), currentSteps);
        double calorie = CalorieManager.run2Calorie(Weight, kilometre);
//        kilometreList.add(kilometre);

        int currentPace = ByteUtil.bytesToIntLittle2(new byte[]{bytes[18], bytes[19]});

        //步数为0或者数据个数少于10或者步数没变（静止）
        if (currentSteps == 0 || currentPace == 0) {
            return null;
        }

        lastStep = currentSteps;
        //2019-05-30需求修改为配速由APP来实现
        //20秒的单位配速
//        int currentPace = CalorieManager.getStepSpeed(20, kilometre, kilometreList.get(kilometreList.size() - 10));

        int reversePace = reversePace(currentPace);//反转配速，用于显示在图表上

        RxLogUtils.d("当前配速：" + currentPace);
        RxLogUtils.d("反转配速：" + reversePace);
        //最大最小配速(配速值越大，表示越小)
        maxPace = Math.min(maxPace, currentPace);
        minPace = Math.max(minPace, currentPace);

        SportsDataTab mSportsDataTab = new SportsDataTab();
        mSportsDataTab.setCurHeart(heartRate);
        mSportsDataTab.setMaxHeart(maxHeart);
        mSportsDataTab.setMinHeart(minHeart);
        mSportsDataTab.setVoltage(ByteUtil.bytesToIntLittle2(new byte[]{bytes[15], bytes[16]}));
//        mSportsDataTab.setLightColor((BitUtils.setBitValue(bytes[17], 7, (byte) 0) & 0xff));
//        mSportsDataTab.setPower((BitUtils.checkBitValue(bytes[17], 7)));
        mSportsDataTab.setTemp((bytes[10] & 0xff));
        mSportsDataTab.setSteps(Math.abs(currentSteps));
        mSportsDataTab.setData(bytes);
        mSportsDataTab.setDate(System.currentTimeMillis());
        mSportsDataTab.setHeartLists(heartLists);
        mSportsDataTab.setKcal(calorie);//统一使用卡为基本热量单位
        mSportsDataTab.setKilometre(kilometre);
        mSportsDataTab.setStepSpeed(currentPace);
        mSportsDataTab.setReversePace(reversePace);
        mSportsDataTab.setMaxPace(maxPace);
        mSportsDataTab.setMinPace(minPace);

        HeartRateItemBean heartRateTab = new HeartRateItemBean();
        heartRateTab.setHeartRate(heartRate);
        heartRateTab.setHeartTime(mSportsDataTab.getDate());
        heartRateTab.setStepTime(2);
        heartRateTab.step = currentSteps;
        heartRateTab.pace = currentPace;
        heartLists.add(heartRateTab);

        double asDouble = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            asDouble = heartLists.stream().mapToInt(HeartRateItemBean::getPace).average().getAsDouble();
        } else {
            int sum = 0;
            for (HeartRateItemBean bean : heartLists) {
                sum += bean.pace;
            }
            asDouble = sum * 1f / heartLists.size();
        }
        mSportsDataTab.setAvPace(asDouble);

        RxLogUtils.i("瘦身衣心率数据：" + mSportsDataTab.toString());
        return mSportsDataTab;
    }


    public static int reversePace(int currentPace) {
        int reversePace = 0;
        if (currentPace > Key.HRART_SECTION[6]) {
            reversePace = Key.HRART_SECTION[0];
        } else if (currentPace < Key.HRART_SECTION[0]) {
            reversePace = Key.HRART_SECTION[6];
        } else {
            reversePace = (Key.HRART_SECTION[6] + Key.HRART_SECTION[0]) - currentPace;
        }
        return reversePace;
    }


    /**
     * 未使用
     * 瘦身衣历史心率数据
     *
     * @param bytes
     */
    public void addHsitoryData(byte[] bytes) {
        long time = ByteUtil.bytesToLongLittle(bytes, 3);

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


    public void saveSportKey(String sportKey) {
        String keys = SPUtils.getString(Key.CACHE_SPORT_KET, "");
        keys = keys + "," + sportKey;
        SPUtils.put(Key.CACHE_SPORT_KET, keys);
        RxLogUtils.d("保存运动Key：" + sportKey + "---:" + RxTimeUtils.date2String(new Date(RxDataUtils.stringToLong(sportKey))));
        RxLogUtils.d("本地Key列表：" + SPUtils.getString(Key.CACHE_SPORT_KET, ""));
    }

    //10s只上传一次，防止重复上传
    @SuppressLint("CheckResult")
    public synchronized void uploadHeartRate() {
        String keyStr = SPUtils.getString(Key.CACHE_SPORT_KET, "");
        RxLogUtils.d("本地Key列表：" + keyStr);
        String[] keys = keyStr.split(",");

        Observable.fromArray(keys)
                .filter(key -> !RxDataUtils.isNullString(key))
                .flatMap((Function<String, ObservableSource<String>>) key ->
                        RxCache.getDefault().<HeartRateBean>load(key, HeartRateBean.class)
                                .map(new CacheResult.MapFunc<>())
                                .filter(heartRateBean -> {
                                    if (heartRateBean == null) {
                                        RxLogUtils.d("添加心率：heartRateBean == null");
                                    }
                                    return heartRateBean != null;
                                })
                                .filter(heartRateBean -> {
                                    List<HeartRateItemBean> heartList = heartRateBean.getHeartList();
                                    if (heartList != null)
                                        RxLogUtils.d("数据个数：" + heartList.size());
                                    return heartList != null && heartList.size() >= 90;
                                })
                                .flatMap((Function<HeartRateBean, ObservableSource<String>>) heartRateBean ->
                                        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                                                .addRunningData(NetManager.fetchRequest(JSON.toJSONString(heartRateBean)))))
                )
                .lastOrError()
                .subscribe(s -> {
                    SPUtils.put(Key.CACHE_SPORT_KET, "");
                    RxLogUtils.d("添加心率：保存成功删除本地缓存：");
                    //这里因为是后台上传数据，并不是跳转，使用RxBus方式
                    RxBus.getInstance().post(new RefreshSlimming());
                }, e -> RxLogUtils.e("上传出现异常：", e));
    }

    public synchronized void clearData(String sportKey) {
        String keys = SPUtils.getString(Key.CACHE_SPORT_KET, "");
        keys = keys.replace(sportKey, "");
        SPUtils.put(Key.CACHE_SPORT_KET, keys);
        RxLogUtils.d("删除运动Key：" + sportKey + "---:" + RxTimeUtils.date2String(new Date(RxDataUtils.stringToLong(sportKey))));
        RxLogUtils.d("本地Key列表：" + SPUtils.getString(Key.CACHE_SPORT_KET, ""));
    }
}
