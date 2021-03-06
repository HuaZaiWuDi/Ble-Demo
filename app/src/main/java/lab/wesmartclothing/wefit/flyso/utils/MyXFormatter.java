package lab.wesmartclothing.wefit.flyso.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class MyXFormatter implements IAxisValueFormatter {

    private List<String> mValues;

    public MyXFormatter(List<String> values) {
        this.mValues = values;
    }

    private static final String TAG = "MyXFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
//        RxLogUtils.d("X轴：" + value);
//        if (value < 0) return "00/00";
        value = Math.abs(value + 3);
        return mValues.get((int) value % mValues.size());
    }
}