package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.vondear.rxtools.utils.RxLogUtils;

import java.util.Arrays;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created icon_hide_password jk on 2018/5/19.
 */
public class HeartRateView extends LinearLayout {

    private Context mContext;
    private HeartRateProgressView progress_slimming, progress_grease, progress_aerobic, progress_anaerobic, progress_limit;

    private float progressPercentage_1;
    private float progressPercentage_2;
    private float progressPercentage_3;
    private float progressPercentage_4;
    private float progressPercentage_5;

    public HeartRateView(Context context) {
        this(context, null);
    }

    public HeartRateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayout();
    }


    private void initLayout() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_heart_rate, this, true);
        progress_slimming = view.findViewById(R.id.progress_slimming);
        progress_grease = view.findViewById(R.id.progress_grease);
        progress_aerobic = view.findViewById(R.id.progress_aerobic);
        progress_anaerobic = view.findViewById(R.id.progress_anaerobic);
        progress_limit = view.findViewById(R.id.progress_limit);

    }

    private float maxTime = 1 * 30;//默认最大的时间范围






}
