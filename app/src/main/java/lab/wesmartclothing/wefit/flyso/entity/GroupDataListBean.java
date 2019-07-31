package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName GroupDataListBean
 * @Date 2019/7/1 17:48
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class GroupDataListBean extends GroupRecordBean {

    private List<DataListBean> list;

    public List<DataListBean> getList() {
        return list;
    }

    public void setList(List<DataListBean> list) {
        this.list = list;
    }
}
