package lab.dxythch.com.commonproject.ui.main.slimming.weight;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.dateUtils.RxFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.entity.WeightDataItem;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;


@EActivity(R.layout.activity_weight_data)
public class WeightDataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
    }


    @ViewById
    RecyclerView mRecycler_Receive;
    @ViewById
    RecyclerView mRecycler_Received;

    @Click
    void back() {
        onBackPressed();
    }

    private List<WeightDataItem> listReceives = new ArrayList<>();
    private List<WeightDataItem> listReceiveds = new ArrayList<>();
    private BaseQuickAdapter adapter_Receive, adapter_Received;

    @Override
    @AfterViews
    public void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecycler_Receive.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler_Received.setLayoutManager(new LinearLayoutManager(mContext));

        adapter_Receive = new BaseQuickAdapter<WeightDataItem, BaseViewHolder>(R.layout.item_weight_data) {

            @Override
            protected void convert(BaseViewHolder helper, WeightDataItem item) {
                helper.setImageResource(R.id.img_weight, R.mipmap.scale_icon);
                helper.setBackgroundRes(R.id.tv_receive, R.mipmap.continue_button);
                helper.setText(R.id.tv_weight_data, item.getWeight() + "kg");
                helper.setText(R.id.tv_date, "测量时间:" + item.getTime());
            }
        };
        mRecycler_Receive.setAdapter(adapter_Receive);
        //已领取
        adapter_Received = new BaseQuickAdapter<WeightDataItem, BaseViewHolder>(R.layout.item_weight_data) {
            @Override
            protected void convert(BaseViewHolder helper, WeightDataItem item) {
                helper.setImageResource(R.id.img_weight, R.mipmap.scale_icon);
                helper.setBackgroundRes(R.id.tv_receive, R.mipmap.leave_button);
                helper.setText(R.id.tv_weight_data, item.getWeight() + "kg");
                helper.setText(R.id.tv_date, "测量时间:" + item.getTime());
            }
        };
        mRecycler_Received.setAdapter(adapter_Received);
        initData();
    }

    private void initData() {
        listReceives.clear();
        listReceiveds.clear();

        listReceiveds.add(new WeightDataItem(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd hh:mm:ss"), 99));
        listReceiveds.add(new WeightDataItem(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd hh:mm:ss"), 99));
        listReceives.add(new WeightDataItem(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd hh:mm:ss"), 99));
        listReceives.add(new WeightDataItem(RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd hh:mm:ss"), 99));

        adapter_Receive.setNewData(listReceives);
        adapter_Received.setNewData(listReceiveds);

    }

}
