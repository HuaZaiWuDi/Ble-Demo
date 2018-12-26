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


    //展示离线数据
    public void offileData(List<Integer> athlRecord, int duration) {
        maxTime = duration;
        for (int i = 0; i < athlRecord.size(); i++) {
            checkHeartRate(athlRecord.get(i));
        }
    }


    public void checkHeartRate(int datum) {
        int type = heartRate(datum);
        float end = 0;

        switch (type) {
            case 1:
                progressPercentage_1 += 2;
                end = progressPercentage_1 / maxTime;
                progress_slimming.setData(end, 1);
                break;
            case 2:
                progressPercentage_2 += 2;
                end = progressPercentage_2 / maxTime;
                progress_grease.setData(end, 2);
                break;
            case 3:
                progressPercentage_3 += 2;
                end = progressPercentage_3 / maxTime;
                progress_aerobic.setData(end, 3);
                break;
            case 4:
                progressPercentage_4 += 2;
                end = progressPercentage_4 / maxTime;
                progress_anaerobic.setData(end, 4);
                break;
            case 5:
                progressPercentage_5 += 2;
                end = progressPercentage_5 / maxTime;
                progress_limit.setData(end, 5);
                break;
        }
        RxLogUtils.d("结束点：" + end);

        if (end >= 1) {
            maxTime = maxTime * 2;

            end = progressPercentage_1 / maxTime;
            progress_slimming.setData(end, 1);

            end = progressPercentage_2 / maxTime;
            progress_grease.setData(end, 2);

            end = progressPercentage_3 / maxTime;
            progress_aerobic.setData(end, 3);

            end = progressPercentage_4 / maxTime;
            progress_anaerobic.setData(end, 4);

            end = progressPercentage_5 / maxTime;
            progress_limit.setData(end, 5);

        }
    }


    private int heartRate(int heart) {
        int type = 1;

        byte[] heartRates = Key.HRART_SECTION;
        int heart_1 = heartRates[1] & 0xff;
        int heart_2 = heartRates[2] & 0xff;
        int heart_3 = heartRates[3] & 0xff;
        int heart_4 = heartRates[4] & 0xff;


        RxLogUtils.d("心率法制：" + Arrays.toString(heartRates));
        if (heart <= heart_1) {
            type = 1;
            RxLogUtils.d("热身");
        } else if (heart > heart_1 && heart <= heart_2) {
            type = 2;
            RxLogUtils.d("燃脂");
        } else if (heart > heart_2 && heart <= heart_3) {
            RxLogUtils.d("耐力");
            type = 3;
        } else if (heart > heart_3 && heart <= heart_4) {
            RxLogUtils.d("无氧");
            type = 4;
        } else if (heart > heart_4) {
            RxLogUtils.d("极限");
            type = 5;
        }
        return type;
    }

}
