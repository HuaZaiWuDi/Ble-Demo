package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/7/14.
 */
public class HealthLevelView extends RelativeLayout {


    @BindView(R.id.iv_Healthy_level_f)
    ImageView mIvHealthyLevelF;
    @BindView(R.id.iv_Healthy_level_c)
    ImageView mIvHealthyLevelC;
    @BindView(R.id.iv_Healthy_level_b)
    ImageView mIvHealthyLevelB;
    @BindView(R.id.iv_Healthy_level_a)
    ImageView mIvHealthyLevelA;
    @BindView(R.id.iv_Healthy_level_s)
    ImageView mIvHealthyLevelS;
    @BindView(R.id.iv_Healthy_level)
    ImageView mIvHealthyLevel;

    public HealthLevelView(Context context) {
        this(context, null);
    }

    public HealthLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_health_level, this, true);
        ButterKnife.bind(this, view);
    }

    public void switchLevel(String level) {
        setGone();
        switch (level) {
            case "F":
                mIvHealthyLevelF.setVisibility(VISIBLE);
                break;
            case "C":
                mIvHealthyLevelC.setVisibility(VISIBLE);
                break;
            case "B":
                mIvHealthyLevelB.setVisibility(VISIBLE);
                break;
            case "A":
                mIvHealthyLevelA.setVisibility(VISIBLE);
                break;
            case "S":
                mIvHealthyLevelS.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setGone() {
        mIvHealthyLevelF.setVisibility(GONE);
        mIvHealthyLevelC.setVisibility(GONE);
        mIvHealthyLevelB.setVisibility(GONE);
        mIvHealthyLevelA.setVisibility(GONE);
        mIvHealthyLevelS.setVisibility(GONE);
    }
}
