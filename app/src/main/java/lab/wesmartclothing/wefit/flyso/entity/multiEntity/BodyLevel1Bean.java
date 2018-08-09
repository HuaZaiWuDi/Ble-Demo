package lab.wesmartclothing.wefit.flyso.entity.multiEntity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import lab.wesmartclothing.wefit.flyso.adapter.ExpandableItemAdapter;

/**
 * Created by jk on 2018/7/28.
 */
public class BodyLevel1Bean implements MultiItemEntity {

    private float value;
    private String[] sectionLabels;
    private String[] labels;
    private int[] colors;


    public BodyLevel1Bean(float value) {
        this.value = value;
    }


    public String[] getSectionLabels() {
        return sectionLabels;
    }

    public void setSectionLabels(String[] sectionLabels) {
        this.sectionLabels = sectionLabels;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
    }
}
