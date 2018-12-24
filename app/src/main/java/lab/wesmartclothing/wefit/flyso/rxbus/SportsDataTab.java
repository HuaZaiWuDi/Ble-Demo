package lab.wesmartclothing.wefit.flyso.rxbus;

import com.vondear.rxtools.utils.dateUtils.RxFormat;

import java.util.List;

/**
 * Created icon_hide_password jk on 2018/6/14.
 */
public class SportsDataTab {

    List<Integer> athlRecord_2;//心率详情数据
    int steps;//步数
    int duration;//耗时
    double kcal;//消耗卡路里（千卡）
    int maxHeart;//最小心率
    int minHeart;//最大心率
    int curHeart;//修改后的心率
    int realHeart;//真实心率
    boolean isPower;//是否充电
    int lightColor;//灯光颜色
    byte[] data;//原始数据
    int temp;//温度
    int voltage;//电压
    long date;//时间
    double score;//分数

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getLightColor() {
        return byte2Color(lightColor);
    }

    public void setLightColor(int lightColor) {
        this.lightColor = lightColor;
    }

    public boolean isPower() {
        return isPower;
    }

    public String getPower() {
        return isPower ? "正在加热" : "停止加热";
    }

    public void setPower(boolean power) {
        isPower = power;
    }

    public int getRealHeart() {
        return realHeart;
    }

    public void setRealHeart(int realHeart) {
        this.realHeart = realHeart;
    }

    public List<Integer> getAthlRecord_2() {
        return athlRecord_2;
    }

    public void setAthlRecord_2(List<Integer> athlRecord_2) {
        this.athlRecord_2 = athlRecord_2;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
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

    public int getCurHeart() {
        return curHeart;
    }

    public void setCurHeart(int curHeart) {
        this.curHeart = curHeart;
    }


    private String byte2Color(int b) {
        String color = "";
        switch (b) {
            case 0:
                color = "白色";
                break;
            case 1:
                color = "红色";
                break;
            case 2:
                color = "蓝色";
                break;
            case 3:
                color = "绿色";
                break;
            case 4:
                color = "紫色";
                break;
            case 5:
                color = "启动白色";
                break;
        }
        return color;
    }


    @Override
    public String toString() {
        return "SportsDataTab{" +
                " steps=" + steps +
                ", kcal=" + kcal +
                ", maxHeart=" + maxHeart +
                ", minHeart=" + minHeart +
                ", curHeart=" + curHeart +
                ", realHeart=" + realHeart +
                ", isPower=" + getPower() +
                ", lightColor=" + byte2Color(lightColor) +
                ", temp=" + temp + "°C" +
                ", voltage=" + voltage +
                ", date='" + RxFormat.setFormatDate(date, RxFormat.Date_Date) + '\'' +
                '}';
    }
}
