package lab.dxythch.com.commonproject.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import lab.dxythch.com.commonproject.R;

/**
 * Created by jk on 2018/5/23.
 */
public class SportsMarkerView extends MarkerView {

    private TextView tv_heat, tv_value;
    private Context context;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public SportsMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        this.context = context;
        tv_heat = findViewById(R.id.tv_heat);
        tv_value = findViewById(R.id.tv_value);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int dataSetIndex = highlight.getDataSetIndex();

        if (dataSetIndex == 0) {
            tv_heat.setText(R.string.heat);
            tv_heat.append(":");
            tv_value.setText((int) e.getY() + context.getString(R.string.unit_kcal));
        } else {
            tv_heat.setText(R.string.sportsTime);
            tv_heat.append(":");
            int ss = (int) e.getY() / 60;
            tv_value.setText(ss / 60 + "h" + ss % 60 + "m");
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
