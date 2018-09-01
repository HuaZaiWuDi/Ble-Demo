package lab.wesmartclothing.wefit.flyso.rxbus;

import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import java.util.List;

/**
 * Created by jk on 2018/9/1.
 */
public class ScaleHistoryData {

    private List<QNScaleStoreData> mList;

    public ScaleHistoryData(List<QNScaleStoreData> list) {
        mList = list;
    }

    public List<QNScaleStoreData> getList() {
        return mList;
    }

    public void setList(List<QNScaleStoreData> list) {
        mList = list;
    }
}
