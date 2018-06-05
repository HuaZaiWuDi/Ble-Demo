package lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.dateUtils.RxFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.SportsListBean;

@EActivity(R.layout.activity_sports_details)
public class SportsDetailsActivity extends BaseActivity {


    @ViewById
    TextView tv_heat;
    @ViewById
    TextView tv_curHeart;
    @ViewById
    TextView tv_maxHeart;
    @ViewById
    TextView tv_sportsTime;
    @ViewById
    TextView tv_connectDevice;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_state_title;
    @ViewById
    TextView tv_state;
    @ViewById
    TextView tv_1;
    @ViewById
    TextView tv_2;
    @ViewById
    TextView tv_3;
    @ViewById
    TextView tv_tip;

    @Extra
    SportsListBean.ListBean BUNDLE_SPORTS_INFO;


    @Click
    void back() {
        onBackPressed();
    }

    //是否运动界面
    private boolean isSports = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @AfterViews
    public void initView() {
        isSports = BUNDLE_SPORTS_INFO == null;
        if (isSports) {
            showStartSports();
        } else {
            showSportsInfo();
        }

        initText();
    }

    private void showStartSports() {
        tv_state.setText(R.string.startSlimming);
    }

    private void initText() {
        tv_1.setText(isSports ? R.string.curHeart : R.string.avgHeart);
        tv_2.setText(R.string.maxHeart);
        tv_3.setText(isSports ? R.string.heat : R.string.consumeHeat);
        tv_state_title.setVisibility(isSports ? View.VISIBLE : View.INVISIBLE);
        tv_state.setTextColor(getResources().getColor(isSports ? R.color.colorTheme : R.color.textColor));
        tv_tip.setVisibility(isSports ? View.VISIBLE : View.GONE);
    }


    private void showSportsInfo() {
        tv_state.setText(getString(R.string.sports) + getString(R.string.sportsTime));
        tv_curHeart.setText(BUNDLE_SPORTS_INFO.getAvgHeart() + "bpm");
        tv_maxHeart.setText(BUNDLE_SPORTS_INFO.getMaxHeart() + "bpm");
        tv_heat.setText(BUNDLE_SPORTS_INFO.getCalorie() + getString(R.string.unit_kcal));
        tv_sportsTime.setText(RxFormat.setFormatDateG8(BUNDLE_SPORTS_INFO.getDuration() * 1000, RxFormat.Date_Time));
    }


}
