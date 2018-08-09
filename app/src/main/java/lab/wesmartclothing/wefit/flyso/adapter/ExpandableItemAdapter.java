package lab.wesmartclothing.wefit.flyso.adapter;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel0Bean;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel1Bean;
import lab.wesmartclothing.wefit.flyso.view.HealthyProgressView;

/**
 * Created by jk on 2018/7/28.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;


    private Typeface typeface;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_body_data);
        addItemType(TYPE_LEVEL_1, R.layout.item_body_data_level2);

    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        if (typeface != null)
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/DIN-Regular.ttf");

        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final BodyLevel0Bean level0Bean = (BodyLevel0Bean) item;
                helper.setImageResource(R.id.iv_switchLayout, level0Bean.isExpanded() ? R.mipmap.icon_up : R.mipmap.icon_down)
                        .setImageResource(R.id.iv_icon, level0Bean.getBodyDataImg())
                        .setText(R.id.tv_title, level0Bean.getBodyData());

                int color = level0Bean.getStatusColor();
                TextView bodyValue = helper.getView(R.id.tv_bodyValue);
                bodyValue.setTypeface(typeface);
                RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(level0Bean.getBodyValue(), 1))
                        .setForegroundColor(color)
                        .append(" " + level0Bean.getUnit())
                        .setForegroundColor(color)
                        .setProportion(0.6f)
                        .into(bodyValue);

                QMUIRoundButton btn_status = helper.getView(R.id.btn_status);
                QMUIRoundButtonDrawable background = (QMUIRoundButtonDrawable) btn_status.getBackground();
                background.setStrokeData(1, ColorStateList.valueOf(color));
                btn_status.setTextColor(color);

                btn_status.setText(level0Bean.getStatus());

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = helper.getAdapterPosition();
                        if (level0Bean.isExpanded()) {
                            collapse(position);
                        } else {
                            expand(position);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final BodyLevel1Bean level1Bean = (BodyLevel1Bean) item;
                HealthyProgressView progressView_1 = helper.getView(R.id.mHealthyProgressView);
                progressView_1.setUpDownText(level1Bean.getSectionLabels(), level1Bean.getLabels());
                progressView_1.setColors(level1Bean.getColors());
                progressView_1.setProgress(level1Bean.getValue());

        }
    }

}
