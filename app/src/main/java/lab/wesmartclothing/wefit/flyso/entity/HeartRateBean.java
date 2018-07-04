package lab.wesmartclothing.wefit.flyso.entity;

import com.google.gson.Gson;
import com.vondear.rxtools.utils.RxLogUtils;

import org.androidannotations.annotations.EBean;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created icon_hide_password jk on 2018/6/11.
 */
@EBean
public class HeartRateBean {


    /**
     * athlDate : 2018-06-13T06:22:20.436Z
     * athlDesc : string
     * athlList : [0]
     * avgHeart : 0
     * calorie : 0
     * duration : 0
     * gid : string
     * kilometers : 0
     * maxHeart : 0
     * minHeart : 0
     * stepNumber : 0
     * userId : string
     */

    private String athlDate;
    private String athlDesc;
    private int avgHeart;
    private int calorie;
    private int duration;
    private String gid;
    private int kilometers;
    private int maxHeart;
    private int minHeart;
    private int stepNumber;
    private String userId;
    private List<Integer> athlList;


    public void saveHeartRate(HeartRateBean heartRateBean, HeartRateToKcal mHeartRateToKcal) {
        List<Integer> athlList = heartRateBean.getAthlList();
        int duration = heartRateBean.getDuration();

        int sum = 0;
        for (int heart : athlList) {
            sum += heart;
        }
        int avgHeart = sum / athlList.size();
        RxLogUtils.d("平均心率：" + avgHeart);

        double hour = duration / 3600f;
        RxLogUtils.d("持续时间(小时)：" + hour);

        double calorie = mHeartRateToKcal.getCalorie(avgHeart, hour);
        RxLogUtils.d("卡路里（千卡）：" + calorie);

        heartRateBean.setCalorie((int) (calorie * 1000));
        heartRateBean.setAthlDate(System.currentTimeMillis() + "");

        String s = new Gson().toJson(heartRateBean, HeartRateBean.class);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addAthleticsInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加心率：" + s);
                    }

                    @Override
                    protected void _onError(String error) {
                    }
                });
    }

    public String getAthlDate() {
        return athlDate;
    }

    public void setAthlDate(String athlDate) {
        this.athlDate = athlDate;
    }

    public String getAthlDesc() {
        return athlDesc;
    }

    public void setAthlDesc(String athlDesc) {
        this.athlDesc = athlDesc;
    }

    public int getAvgHeart() {
        return avgHeart;
    }

    public void setAvgHeart(int avgHeart) {
        this.avgHeart = avgHeart;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public int getMaxHeart() {
        return maxHeart;
    }

    public void setMaxHeart(int maxHeart) {
        this.maxHeart = maxHeart;
    }

    public int getMinHeart() {
        return minHeart;
    }

    public void setMinHeart(int minHeart) {
        this.minHeart = minHeart;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getAthlList() {
        return athlList;
    }

    public void setAthlList(List<Integer> athlList) {
        this.athlList = athlList;
    }
}
