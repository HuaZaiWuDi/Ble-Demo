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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.SettingTargetFragment;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.AddDeviceActivity;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_Weight)
    TextView mTvWeight;
    @BindView(R.id.tv_goBind)
    RxTextView mTvGoBind;

    private double currentWeight = 0;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentWeight = bundle.getDouble(Key.BUNDLE_LAST_WEIGHT);
        }
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
        initTopBar();
        checkState();
    }

    private void checkState() {
        mTvWeight.setTypeface(MyAPP.typeface);
        if (!BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_scaleMAC)) ||
                !BluetoothAdapter.checkBluetoothAddress(SPUtils.getString(SPKey.SP_clothingMAC))) {
            mTvGoBind.setText(R.string.goBind);
            mTvWeight.setText("请绑定您的体脂称");
        } else {
            if (currentWeight > 0) {
                mTvGoBind.setText(R.string.nextWay);
                RxTextUtils.getBuilder(currentWeight + "")
                        .setProportion(3f)
                        .setBold()
                        .setForegroundColor(ContextCompat.getColor(mActivity, R.color.green_61D97F))
                        .append("\t\tkg")
                        .setForegroundColor(ContextCompat.getColor(mActivity, R.color.green_61D97F))
                        .into(mTvWeight);
            }
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
        if (currentWeight <= 0) {
            RxActivityUtils.skipActivity(mActivity, AddDeviceActivity.class);
        } else {
            RxActivityUtils.skipActivity(mActivity, SettingTargetFragment.class);
        }
    }
}
