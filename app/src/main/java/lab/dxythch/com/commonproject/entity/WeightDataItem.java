package lab.dxythch.com.commonproject.entity;

/**
 * Created by jk on 2018/5/14.
 */
public class WeightDataItem {


    String time;
    int Weight;


    public WeightDataItem(String time, int weight) {
        this.time = time;
        Weight = weight;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }


}
