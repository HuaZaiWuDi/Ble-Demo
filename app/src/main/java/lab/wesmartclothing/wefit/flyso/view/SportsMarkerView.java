package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.vondear.rxtools.utils.RxTextUtils;

import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/5/23.
 */
public class SportsMarkerView extends MarkerView {

    private TextView tv_heat, tv_time;
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
        tv_time = findViewById(R.id.tv_time);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int dataSetIndex = highlight.getDataSetIndex();

        ChartData data = getChartView().getData();

        LineDataSet set1 = (LineDataSet) data.getDataSetByLabel(context.getString(R.string.heat), true);
        LineDataSet set2 = (LineDataSet) data.getDataSetByLabel(context.getString(R.string.sportsTime), true);

        List<Entry> forXValue1 = set1.getEntriesForXValue(e.getX());
        List<Entry> forXValue2 = set2.getEntriesForXValue(e.getX());
        if (forXValue1.get(0) == null) return;
        float y1 = forXValue1.get(0).getY();
        float y2 = forXValue2.get(0).getY();

        RxTextUtils.getBuilder(context.getString(R.string.heat))
                .append("：")
                .setForegroundColor(context.getResources().getColor(R.color.textHeatColor))
                .append((int) y1 + context.getString(R.string.unit_kcal))
                .setForegroundColor(context.getResources().getColor(R.color.colorTheme))
                .into(tv_heat);

        String time = "";
        int ss = (int) y2 / 60;
        if (ss >= 60) {
            time = ss / 60 + "h" + ss % 60 + "min";
        } else
            time = ss % 60 + "min";

        RxTextUtils.getBuilder(context.getString(R.string.sportsTime))
                .append("：")
                .setForegroundColor(context.getResources().getColor(R.color.textHeatColor))
                .append(time)
                .setForegroundColor(context.getResources().getColor(R.color.colorTheme))
                .into(tv_time);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
