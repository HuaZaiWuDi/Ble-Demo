package com.smartclothing.module_wefit.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.bean.Message;
import com.vondear.rxtools.dateUtils.RxTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ZZP on 2018/5/22.
 */

public class MessageRvAdapter extends BaseQuickAdapter<Message, com.chad.library.adapter.base.BaseViewHolder> {

    private Context mContext;

    public MessageRvAdapter(Context context) {
        super(R.layout.rv_item_message);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        ImageView iv = helper.convertView.findViewById(R.id.iv_rv_item_message);
        TextView title = helper.convertView.findViewById(R.id.tv_message_title);
        TextView text = helper.convertView.findViewById(R.id.tv_message_text);
        TextView time = helper.convertView.findViewById(R.id.tv_message_time);
        View line = helper.convertView.findViewById(R.id.line);
        if(item.getReadState()==0) {
            iv.setVisibility(View.VISIBLE);
        } else{
            iv.setVisibility(View.GONE);
        }
        if(helper.getLayoutPosition() == getData().size()-1) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }
        title.setText(item.getTitle());
        text.setText(item.getContent());
        time.setText(RxTimeUtils.milliseconds2String(item.getPushTime(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
    }
}
