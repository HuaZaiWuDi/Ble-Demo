package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmarclothing.mylibrary.net.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.SettingTargetFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightAddFragment;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_Weight)
    TextView mTvWeight;
    @BindView(R.id.tv_goBind)
    RxTextView mTvGoBind;
    @BindView(R.id.tv_WeightInfo)
    TextView mTvWeightInfo;
    @BindView(R.id.tv_tip)
    TextView mTvTip;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();
        initView();
    }


    private void initView() {
        mTvWeight.setTypeface(MyAPP.typeface);
        initTopBar();
        initRxBus();
        initBundle();
        checkState();
    }

    private void initBundle() {
    }


    private void initRxBus() {
        RxBus.getInstance().register2(WeightAddBean.class)
                .compose(RxComposeUtils.<WeightAddBean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<WeightAddBean>() {
                    @Override
                    protected void _onNext(WeightAddBean weightBean) {
                        if (weightBean != null) {
                            mTvWeightInfo.setVisibility(View.VISIBLE);
                            boolean isQualified = weightBean.getBmr() != 0;
                            mTvGoBind.setText(isQualified ? getString(R.string.nextWay) : "重新称重");
                            RxTextUtils.getBuilder(weightBean.getWeight() + "")
                                    .setProportion(3f)
                                    .setBold()
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.green_61D97F))
                                    .append("\t\tkg")
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.green_61D97F))
                                    .into(mTvWeight);

                            RxTextUtils.getBuilder(weightBean.getBodyFat() + " ")
                                    .append("体脂率(%)\t\t\t").setProportion(0.8f)
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.GrayWrite))
                                    .append(weightBean.getBmi() + " ")
                                    .append("BMI\t\t\t").setProportion(0.8f)
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.GrayWrite))
                                    .append(weightBean.getBmr() + " ")
                                    .append("基础代谢(kcal)").setProportion(0.8f)
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.GrayWrite))
                                    .into(mTvWeightInfo);

                            mTvTip.setVisibility(isQualified ? View.GONE : View.VISIBLE);

                            if (isQualified) {

                                RecordInfoActivity.getmSubmitInfoFrom().setWeightInfo(weightBean);
                            }
                        }
                    }
                });

        RxBus.getInstance().register2(RefreshSlimming.class)
                .compose(RxComposeUtils.<RefreshSlimming>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshSlimming>() {
                    @Override
                    protected void _onNext(RefreshSlimming refreshSlimming) {
                        checkState();
                    }
                });
    }

    private void checkState() {
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
            mTvGoBind.setText(R.string.goBind);
            mTvWeight.setText("请绑定您的体脂称");
        } else {
            mTvWeight.setText("请赤脚站在体脂秤上，两脚自然分布两侧确保脚底干净，避免过于潮湿");
            mTvGoBind.setText("去称重");
        }
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    @OnClick(R.id.tv_goBind)
    public void onViewClicked() {
        String text = mTvGoBind.getText().toString();
        if (getString(R.string.goBind).equals(text)) {
            RxActivityUtils.skipActivity(mActivity, AddDeviceActivity.class);
        } else if ("去称重".equals(text) || "重新称重".equals(text)) {
            RxActivityUtils.skipActivity(mActivity, WeightAddFragment.class);
        } else if (getString(R.string.nextWay).equals(text)) {
            RxActivityUtils.skipActivity(mActivity, SettingTargetFragment.class);
        }
    }

}
