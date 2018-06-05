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
                            addDeviceClickListener.onAddDeviceClick();
                        }
                    }
                });
                break;
            case 1:
                helper.setText(R.id.tv_device, helper.getAdapterPosition() == 0 ? "体脂称" : "瘦身衣");

                helper.setText(R.id.tv_device_id_scale, item.getProductId());
                helper.setText(R.id.tv_residual_electricity_scale, item.getQuantity() + "%");

                helper.setText(R.id.tv_link_state_scale, item.isConnect() ? "已连接" : "未连接");
                helper.setText(R.id.tv_standby_time_scale, "11h11min");

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
        void onAddDeviceClick();
    }

    public void setUpdateClickListener(deleteClickListener listener) {
        deleteClickListener = listener;
    }

    public void setAddDeviceClickListener(addDeviceClickListener listener) {
        addDeviceClickListener = listener;
    }

}
