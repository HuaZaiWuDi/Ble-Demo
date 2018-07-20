package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/7/19.
 */
public class SportsDetailsFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_kcal)
    TextView mTvKcal;
    @BindView(R.id.tv_avHeartRate)
    TextView mTvAvHeartRate;
    @BindView(R.id.tv_maxHeartRate)
    TextView mTvMaxHeartRate;
    @BindView(R.id.tv_sportsTime)
    TextView mTvSportsTime;
    @BindView(R.id.layout_sports)
    LinearLayout mLayoutSports;
    @BindView(R.id.pro_limit)
    RxRoundProgressBar mProLimit;
    @BindView(R.id.tv_limit_time)
    TextView mTvLimitTime;
    @BindView(R.id.pro_anaerobic)
    RxRoundProgressBar mProAnaerobic;
    @BindView(R.id.tv_anaerobic_time)
    TextView mTvAnaerobicTime;
    @BindView(R.id.pro_aerobic)
    RxRoundProgressBar mProAerobic;
    @BindView(R.id.tv_aerobic_time)
    TextView mTvAerobicTime;
    @BindView(R.id.pro_grease)
    RxRoundProgressBar mProGrease;
    @BindView(R.id.tv_grease_time)
    TextView mTvGreaseTime;
    @BindView(R.id.pro_warm)
    RxRoundProgressBar mProWarm;
    @BindView(R.id.tv_warm_time)
    TextView mTvWarmTime;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new SportsDetailsFragment();
    }


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sport_details, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        Bundle args = getArguments();
        mQMUIAppBarLayout.setTitle(RxFormat.setFormatDate(args == null ? System.currentTimeMillis() : args.getLong(Key.BUNDLE_SPORTING_DATE), RxFormat.Date_CH));

    }

//


}
