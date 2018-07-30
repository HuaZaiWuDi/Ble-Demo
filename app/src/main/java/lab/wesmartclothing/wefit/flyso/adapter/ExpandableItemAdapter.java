package lab.wesmartclothing.wefit.flyso.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final BodyLevel0Bean level0Bean = (BodyLevel0Bean) item;
                helper.setImageResource(R.id.iv_switchLayout, level0Bean.isExpanded() ? R.mipmap.icon_up : R.mipmap.icon_down)
                        .setImageResource(R.id.iv_icon, level0Bean.getBodyDataImg())
                        .setText(R.id.tv_title, level0Bean.getBodyData());

                int color = mContext.getResources().getColor(level0Bean.isStatus() ? R.color.orange_FF7200 : R.color.green_61D97F);

                TextView bodyValue = helper.getView(R.id.tv_bodyValue);
                RxTextUtils.getBuilder("")
                        .setForegroundColor(color)
                        .append(RxFormatValue.fromat4S5R(level0Bean.getBodyValue(), 1))
                        .setForegroundColor(color)
                        .setProportion(0.6f)
                        .into(bodyValue);

                QMUIRoundButton btn_status = helper.getView(R.id.btn_status);
                QMUIRoundButtonDrawable background = (QMUIRoundButtonDrawable) btn_status.getBackground();
                background.setStrokeData(1, ColorStateList.valueOf(color));
                btn_status.setTextColor(color);

                btn_status.setText(level0Bean.isStatus() ? "偏高" : "偏低");

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
                int value = (int) (Math.random() * 100);
                RxLogUtils.d("条目列表：" + position);
                switch (position) {
                    case 0:
                        //体脂率
                        HealthyProgressView progressView_1 = helper.getView(R.id.mHealthyProgressView);
                        progressView_1.setUpDownText(new String[]{"21.0%", "31.0%", "36.0%"},
                                new String[]{"偏低", "标准", "偏高", "严重偏高"});
                        progressView_1.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
                        progressView_1.setProgress(value);
                        break;
                    case 1:
                        //BMI
                        HealthyProgressView progressView_2 = helper.getView(R.id.mHealthyProgressView);
                        progressView_2.setUpDownText(new String[]{"18.5%", "25.0%"},
                                new String[]{"偏低", "标准", "偏高"});
                        progressView_2.setColors(new int[]{Color.parseColor("#5A7BEE"), Color.parseColor("#61D97F"),
                                Color.parseColor("#FFBC00")});
                        progressView_2.setProgress(value);
                        break;
                    case 2:
                        //内脏脂肪等级
                        HealthyProgressView progressView_3 = helper.getView(R.id.mHealthyProgressView);
                        progressView_3.setUpDownText(new String[]{"9", "11"},
                                new String[]{"标准", "偏高", "严重偏高"});
                        progressView_3.setColors(new int[]{Color.parseColor("#61D97F"),
                                Color.parseColor("#FFBC00"), Color.parseColor("#FF7200")});
                        progressView_3.setProgress(value);
                        break;
                    case 3:
                        //肌肉量
                        HealthyProgressView progressView_4 = helper.getView(R.id.mHealthyProgressView);
                        progressView_4.setUpDownText(new String[]{"36.0kg", "42.5kg"},
                                new String[]{"不足", "偏高", "优秀"});
                        progressView_4.setColors(new int[]{Color.parseColor("#FF7200"),
                                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
                        progressView_4.setProgress(value);
                        break;
                    case 4:
                        //基础代谢率
                        HealthyProgressView progressView_5 = helper.getView(R.id.mHealthyProgressView);
                        progressView_5.setColors(new int[]{Color.parseColor("#FF7200"),
                                Color.parseColor("#61D97F")});
                        progressView_5.setUpDownText(new String[]{"903kcal"},
                                new String[]{"未达标", "达标"});
                        progressView_5.setProgress(value);
                        break;
                    case 5:
                        //水分
                        HealthyProgressView progressView_6 = helper.getView(R.id.mHealthyProgressView);
                        progressView_6.setColors(new int[]{Color.parseColor("#FF7200"),
                                Color.parseColor("#61D97F"), Color.parseColor("#17BD4F")});
                        progressView_6.setUpDownText(new String[]{"45.0%", "60.0%"},
                                new String[]{"不足", "标准", "优秀"});
                        progressView_6.setProgress(value);
                        break;
                    case 6:
                        //骨量
                        HealthyProgressView progressView_7 = helper.getView(R.id.mHealthyProgressView);
                        progressView_7.setColors(new int[]{Color.parseColor("#5A7BEE"),
                                Color.parseColor("#61D97F"), Color.parseColor("#FF7200")});
                        progressView_7.setUpDownText(new String[]{"2.0kg", "2.4kg"},
                                new String[]{"偏低", "标准", "偏高"});
                        progressView_7.setProgress(value);
                        break;
                    case 7:
                        //身体年龄
                        HealthyProgressView progressView_8 = helper.getView(R.id.mHealthyProgressView);
                        progressView_8.setColors(new int[]{Color.parseColor("#CFED00"), Color.parseColor("#61D97F"),
                                Color.parseColor("#17BD4F"), Color.parseColor("#FFBC00")});
                        progressView_8.setUpDownText(new String[]{"20", "20", "40"},
                                new String[]{"青少年", "青年", "中年", "中老年"});
                        progressView_8.setProgress(value);
                        break;
                }
                break;
        }
    }
}
