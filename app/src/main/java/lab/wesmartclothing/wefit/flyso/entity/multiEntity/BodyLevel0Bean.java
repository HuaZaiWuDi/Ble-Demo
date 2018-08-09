package lab.wesmartclothing.wefit.flyso.entity.multiEntity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import lab.wesmartclothing.wefit.flyso.adapter.ExpandableItemAdapter;

/**
 * Created by jk on 2018/7/28.
 */
public class BodyLevel0Bean extends AbstractExpandableItem<BodyLevel1Bean> implements MultiItemEntity {

    private int bodyDataImg;
    private String bodyData;
    private String unit;
    private double bodyValue;
    private String status;
    private int statusColor;

    public double getBodyValue() {
        return bodyValue;
    }

//    public BodyLevel0Bean(int bodyDataImg, String bodyData, String unit, double bodyValue) {
//        this.bodyDataImg = bodyDataImg;
//        this.bodyData = bodyData;
//        this.unit = unit;
//        this.bodyValue = bodyValue;
//    }

    public void setBodyValue(double bodyValue) {
        this.bodyValue = bodyValue;
    }

    public int getBodyDataImg() {
        return bodyDataImg;
    }

    public void setBodyDataImg(int bodyDataImg) {
        this.bodyDataImg = bodyDataImg;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }
}
