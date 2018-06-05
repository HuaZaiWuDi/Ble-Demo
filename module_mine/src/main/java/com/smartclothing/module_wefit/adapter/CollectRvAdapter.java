package com.smartclothing.module_wefit.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.bean.Collect;

/**
 * Created by ZZP on 2018/5/22.
 */

public class CollectRvAdapter extends BaseQuickAdapter<Collect, BaseViewHolder> {

    private Context mContext;

    public CollectRvAdapter(Context context) {
        super(R.layout.rv_item_collect);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, Collect item) {
        ImageView iv = helper.convertView.findViewById(R.id.iv_rv_item_collect);
        TextView title = helper.convertView.findViewById(R.id.tv_collect_title);
        TextView text = helper.convertView.findViewById(R.id.tv_collect_text);
        Glide.with(mContext).load(item.getCoverPicture()).placeholder(R.mipmap.group13).into(iv);
        title.setText(item.getArticleName());
        text.setText(item.getSummary());

    }

}
