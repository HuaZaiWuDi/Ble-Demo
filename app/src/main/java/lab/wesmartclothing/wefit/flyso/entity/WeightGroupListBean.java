package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName WeightGroupListBean
 * @Date 2019/7/1 16:28
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class WeightGroupListBean extends GroupRecordBean {

    private List<HealthyInfoBean> list;

    public List<HealthyInfoBean> getList() {
        return list;
    }

    public void setList(List<HealthyInfoBean> list) {
        this.list = list;
    }
}
