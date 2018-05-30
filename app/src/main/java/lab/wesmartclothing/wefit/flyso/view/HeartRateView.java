package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/5/19.
 */
public class HeartRateView extends LinearLayout {

    private Context mContext;
    private HeartRateProgressView progress_slimming, progress_grease, progress_aerobic, progress_anaerobic, progress_limit;
    private int[] heartRate;
    private boolean isOffile = false;

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

        HeartRate heartRate = new HeartRate();
        heartRate.progressPercentage = new float[][]{new float[]{0f, 0.05f}, new float[]{0.47f, 0.5f}, new float[]{0.7f, 0.75f}};
        heartRate.type = 0;
        progress_slimming.setData(heartRate);

        heartRate.progressPercentage = new float[][]{new float[]{0.05f, 0.15f}, new float[]{0.5f, 0.55f}, new float[]{0.75f, 0.8f}};
        heartRate.type = 1;
        progress_grease.setData(heartRate);

        heartRate.progressPercentage = new float[][]{new float[]{0.15f, 0.3f}, new float[]{0.55f, 0.6f}, new float[]{0.8f, 0.85f}};
        heartRate.type = 2;
        progress_aerobic.setData(heartRate);

        heartRate.progressPercentage = new float[][]{new float[]{0.3f, 0.45f}, new float[]{0.6f, 0.65f}, new float[]{0.85f, 0.9f}};
        heartRate.type = 3;
        progress_anaerobic.setData(heartRate);

        heartRate.progressPercentage = new float[][]{new float[]{0.45f, 0.47f}, new float[]{0.65f, 0.7f}, new float[]{0.9f, 1f}};
        heartRate.type = 4;
        progress_limit.setData(heartRate);

    }


    public void setHeartRate(int[] heartRate) {
        this.heartRate = heartRate;
    }

    class HeartRate {
        float[][] progressPercentage;//宽度百分比
        int type;
    }

}
