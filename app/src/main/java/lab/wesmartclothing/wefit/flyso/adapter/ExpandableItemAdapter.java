package lab.wesmartclothing.wefit.flyso.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.entity.Healthy;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel0Bean;
import lab.wesmartclothing.wefit.flyso.entity.multiEntity.BodyLevel1Bean;
import lab.wesmartclothing.wefit.flyso.utils.BodyDataUtil;
import lab.wesmartclothing.wefit.flyso.view.HealthyProgressView;

/**
 * Created by jk on 2018/7/28.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public Map<Integer, Healthy> mHealthyMaps = new HashMap<>();


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

        Healthy healthy = new Healthy();
        healthy.setSections(new double[]{11, 21, 26});
        healthy.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy.setSectionLabels(new String[]{"11.0%", "21.0%", "26.0%"});
        healthy.setLabels(new String[]{"偏低", "标准", "偏高", "严重偏高"});
        mHealthyMaps.put(0, healthy);

        Healthy healthy2 = new Healthy();
        healthy2.setSections(new double[]{18.5, 25});
        healthy2.setSectionLabels(new String[]{"18.5%", "25.0%"});
        healthy2.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00")});
        healthy2.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyMaps.put(1, healthy2);

        Healthy healthy3 = new Healthy();
        healthy3.setSections(new double[]{9, 14});
        healthy3.setSectionLabels(new String[]{"9", "14"});
        healthy3.setColors(new int[]{Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy3.setLabels(new String[]{"标准", "偏高", "严重偏高"});
        mHealthyMaps.put(2, healthy3);

        Healthy healthy4 = new Healthy();
        healthy4.setSections(new double[]{67, 80});
        healthy4.setSectionLabels(new String[]{"67.0%", "80.0%"});
        healthy4.setColors(new int[]{Color.parseColor("#61D97F"),
                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
        healthy4.setLabels(new String[]{"偏低", "标准", "充足"});
        mHealthyMaps.put(3, healthy4);

        Healthy healthy5 = new Healthy();
        healthy5.setSections(new double[]{903});
        healthy5.setSectionLabels(new String[]{"903kcal"});
        healthy5.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F")});
        healthy5.setLabels(new String[]{"未达标", "达标"});
        mHealthyMaps.put(4, healthy5);

        Healthy healthy6 = new Healthy();
        healthy6.setSections(new double[]{55, 65});
        healthy6.setSectionLabels(new String[]{"55.0%,65.0%"});
        healthy6.setColors(new int[]{Color.parseColor("#FF7200"),
                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
        healthy6.setLabels(new String[]{"偏低", "标准", "充足"});
        mHealthyMaps.put(5, healthy6);

        Healthy healthy7 = new Healthy();
        healthy7.setSections(new double[]{3.7, 4.2});
        healthy7.setSectionLabels(new String[]{"3.7%", "4.2%"});
        healthy7.setColors(new int[]{Color.parseColor("#5A7BEE"),
                Color.parseColor("#61D97F"), Color.parseColor("#FF7200")});
        healthy7.setLabels(new String[]{"偏低", "标准", "偏高"});
        mHealthyMaps.put(6, healthy7);

        Healthy healthy8 = new Healthy();
        healthy8.setSections(new double[]{20, 30, 40});
        healthy8.setSectionLabels(new String[]{"20", "30", "40"});
        healthy8.setColors(new int[]{Color.parseColor("#CFED00"), Color.parseColor("#61D97F"),
                Color.parseColor("#17BD4F"), Color.parseColor("#FFBC00")});
        healthy8.setLabels(new String[]{"青少年", "青年", "中年", "中老年"});
        mHealthyMaps.put(7, healthy8);

    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        BodyDataUtil bodyDataUtil = new BodyDataUtil(mHealthyMaps);

        int index = helper.getAdapterPosition();
        int layoutPosition = helper.getLayoutPosition();
        int oldPosition = helper.getOldPosition();
        int position1 = helper.getPosition();

        RxLogUtils.d("当前下标：index:" + index + "---layoutPosition:" + layoutPosition + "---oldPosition:" + oldPosition + "---position1:" + position1);

        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final BodyLevel0Bean level0Bean = (BodyLevel0Bean) item;
                int parentPosition = getParentPosition(level0Bean);
                RxLogUtils.d("父类下标：(第一级)" + parentPosition);
                helper.setImageResource(R.id.iv_switchLayout, level0Bean.isExpanded() ? R.mipmap.icon_up : R.mipmap.icon_down)
                        .setImageResource(R.id.iv_icon, level0Bean.getBodyDataImg())
                        .setText(R.id.tv_title, level0Bean.getBodyData());

                int color = (int) bodyDataUtil.checkStatus(level0Bean.getBodyValue(), parentPosition)[1];
                Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/DIN-Regular.ttf");
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

                btn_status.setText((String) bodyDataUtil.checkStatus(level0Bean.getBodyValue(), parentPosition)[0]);

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
                int position = getParentPosition(level1Bean);
                RxLogUtils.d("父类下标：(第二级)" + position);
                Healthy healthy = mHealthyMaps.get(position % mHealthyMaps.size());
                HealthyProgressView progressView_1 = helper.getView(R.id.mHealthyProgressView);
                progressView_1.setUpDownText(healthy.getSectionLabels(), healthy.getLabels());
                progressView_1.setColors(healthy.getColors());
                progressView_1.setProgress(bodyDataUtil.transformation(position, level1Bean.getValue()));

        }
    }

}
