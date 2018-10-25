package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.SettingTargetFragment;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_Weight)
    TextView mTvWeight;
    @BindView(R.id.tv_goBind)
    RxTextView mTvGoBind;

    private WeightAddBean weightBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();
        initView();
    }


    private void initView() {
        initTopBar();
        checkState();
        initRxBus();
    }

    private void initRxBus() {
        RxBus.getInstance().register2(WeightAddBean.class)
                .compose(RxComposeUtils.<WeightAddBean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<WeightAddBean>() {
                    @Override
                    protected void _onNext(WeightAddBean weightAddBean) {
                        weightBean = weightAddBean;
                        if (weightBean != null) {
                            mTvGoBind.setText(R.string.nextWay);
                            RxTextUtils.getBuilder(weightBean.getWeight() + "")
                                    .setProportion(3f)
                                    .setBold()
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.green_61D97F))
                                    .append("\t\tkg")
                                    .setForegroundColor(ContextCompat.getColor(mActivity, R.color.green_61D97F))
                                    .into(mTvWeight);
                        }
                    }
                });
    }

    private void checkState() {
        mTvWeight.setTypeface(MyAPP.typeface);
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
            mTvGoBind.setText(R.string.goBind);
            mTvWeight.setText("请绑定您的体脂称");
        } else {
            mTvWeight.setText("请赤脚上称称重");
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
        if (weightBean == null) {
            if (BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC))) {
                RxToast.normal("请赤脚上称称重");
            } else
                RxActivityUtils.skipActivity(mActivity, AddDeviceActivity.class);
        } else {
            RxActivityUtils.skipActivity(mActivity, SettingTargetFragment.class);
        }
    }
}
