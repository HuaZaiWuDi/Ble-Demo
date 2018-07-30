package lab.wesmartclothing.wefit.flyso.entity.multiEntity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import lab.wesmartclothing.wefit.flyso.adapter.ExpandableItemAdapter;

/**
 * Created by jk on 2018/7/28.
 */
public class BodyLevel1Bean implements MultiItemEntity {

    private float value;



    public BodyLevel1Bean(float value) {
        this.value = value;
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
