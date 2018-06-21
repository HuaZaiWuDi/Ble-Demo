package com.smartclothing.module_wefit.adapter;

import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.bean.Device;

/**
 * Created by ZZP on 2018/5/22.
 */

public class DeviceRvAdapter extends BaseMultiItemQuickAdapter<Device, BaseViewHolder> {

    private deleteClickListener deleteClickListener;
    private addDeviceClickListener addDeviceClickListener;


    public DeviceRvAdapter() {
        super(null);
        addItemType(0, R.layout.viewstub_device_scale_nodata);
        addItemType(1, R.layout.viewstub_device_scale_hasdata);

    }


    @Override
    protected void convert(final BaseViewHolder helper, Device item) {
        switch (helper.getItemViewType()) {
            case 0:
                LinearLayout device_no_device_add = helper.convertView.findViewById(R.id.device_no_device_add);
                helper.setText(R.id.tv_device, helper.getAdapterPosition() == 0 ? "体脂称" : "瘦身衣");
                device_no_device_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (addDeviceClickListener != null) {
                            addDeviceClickListener.onAddDeviceClick(helper.getAdapterPosition());
                        }
                    }
                });
                break;
            case 1:
                helper.setText(R.id.tv_link_state_scale, item.isConnect() ? "已连接" : "未连接");
                helper.setText(R.id.tv_device, helper.getAdapterPosition() == 0 ? "体脂称" : "瘦身衣");
                helper.setText(R.id.tv_device_id_scale, item.getMacAddr());
                helper.setImageResource(R.id.iv_device_scale, helper.getAdapterPosition() == 0 ? R.mipmap.icon_tizhicheng_big : R.mipmap.icon_ranzhiyi_big);
                if (helper.getAdapterPosition() == 0) {
                    helper.setText(R.id.tv_standby_time_scale0, "使用时长");
                    int i = item.getOnlineDuration() / 60;
                    String time = item.getOnlineDuration() == 0 ? "--" : i / 60 + "h" + i % 60 + "min";
                    helper.setText(R.id.tv_standby_time_scale, time);
                    helper.getView(R.id.tv_residual_electricity_scale).setVisibility(View.GONE);
                    helper.getView(R.id.tv_residual_electricity_scale0).setVisibility(View.GONE);
                } else {

                    helper.setText(R.id.tv_residual_electricity_scale, item.getQuantity() == 0 ? "--" : item.getQuantity() + "%");
                    int i = item.getStandby();
                    String time = item.getStandby() == 0 ? "--" : i + "h";

                    helper.setText(R.id.tv_standby_time_scale, time);
                }

                LinearLayout icon_device_delete = helper.convertView.findViewById(R.id.icon_device_delete);
                icon_device_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (deleteClickListener != null) {
                            deleteClickListener.onUpdateClick(helper.getLayoutPosition());
                        }
                    }
                });

                break;
        }
    }

    public interface deleteClickListener {
        void onUpdateClick(int position);
    }

    public interface addDeviceClickListener {
        void onAddDeviceClick(int position);
    }

    public void setUpdateClickListener(deleteClickListener listener) {
        deleteClickListener = listener;
    }

    public void setAddDeviceClickListener(addDeviceClickListener listener) {
        addDeviceClickListener = listener;
    }


    public void onDestroy() {
        deleteClickListener = null;
        addDeviceClickListener = null;

    }

}
