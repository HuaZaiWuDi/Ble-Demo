package lab.wesmartclothing.wefit.flyso.entity.section;

import com.chad.library.adapter.base.entity.SectionEntity;

import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;

/**
 * Created by jk on 2018/5/10.
 */
public class HeatFoodSection extends SectionEntity<FoodListBean> {

    private int intake;
    private int HeadImg;

    public HeatFoodSection(boolean isHeader, String header, int intake, int HeadImg) {
        super(isHeader, header);
        this.intake = intake;
        this.HeadImg = HeadImg;
    }

    public HeatFoodSection(FoodListBean bean, String header) {
        super(bean);
        this.header = header;
    }

    public int getIntake() {
        return intake;
    }

    public void setIntake(int intake) {
        this.intake = intake;
    }

    public int getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(int headImg) {
        HeadImg = headImg;
    }
}
