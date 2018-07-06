package com.smartclothing.module_wefit.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.bean.AboutDeviceBean;

import static com.vondear.rxtools.utils.RxUtils.getContext;

/**
 * Created by ZZP on 2018/5/22.
 */

public class AboutRvAdapter extends BaseQuickAdapter<AboutDeviceBean, BaseViewHolder> {

    private Context mContext;
    private updateClickListener updateClickListener;

    public AboutRvAdapter(Context context) {
        super(R.layout.rv_item_about);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, AboutDeviceBean item) {
        TextView tv_about_update = helper.convertView.findViewById(R.id.tv_about_update);
        QMUIRelativeLayout layout_item = helper.getView(R.id.layout_item);
        layout_item.setRadiusAndShadow(QMUIDisplayHelper.dp2px(getContext(), 10),
                QMUIDisplayHelper.dp2px(getContext(), 40),
                0.5f);
        tv_about_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateClickListener != null) {
                    updateClickListener.onUpdateClick(helper.getLayoutPosition());
                }
            }
        });
    }

    public interface updateClickListener {
        void onUpdateClick(int position);
    }

    public void setUpdateClickListener(updateClickListener listener) {
        updateClickListener = listener;
    }
}