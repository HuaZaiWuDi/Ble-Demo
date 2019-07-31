package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.RecordInfoActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;


/**
 * Created by jk on 2018/7/27.
 */
public class TargetDetailsFragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_DistanceTarget)
    TextView mTvDistanceTarget;
    @BindView(R.id.iv_target)
    ImageView mIvTarget;
    @BindView(R.id.tv_targetTitle)
    TextView mTvTargetTitle;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.tv_targetDays)
    TextView mTvTargetDays;
    @BindView(R.id.btn_reSet)
    QMUIRoundButton mBtnReSet;
    Unbinder unbinder;


    RxDialogSureCancel rxDialog;

    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_target_details);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        targetWeight();
        initTopBar();
        Typeface typeface = MyAPP.typeface;
        mTvTargetDays.setTypeface(typeface);
        mTvTargetWeight.setTypeface(typeface);
        mTvDistanceTarget.setTypeface(typeface);

    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle(R.string.setTarget);
    }

    @OnClick(R.id.btn_reSet)
    public void onViewClicked() {
        showChooseDialog();
    }

    private void showChooseDialog() {
        rxDialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent("重置目标会更改已定制的瘦身计划，您确定要重置吗？")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //传递初始体重信息
//                        RxActivityUtils.skipActivityAndFinish(mContext, SettingTargetFragment.class, bundle);
                        //2019-3-26更改，确定修改，重新健康报告流程
                        RxActivityUtils.skipActivity(mActivity, RecordInfoActivity.class);
                    }
                });
        rxDialog.show();
    }

    private void targetWeight() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().fetchTargetWeight())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        JsonObject object = (JsonObject) new JsonParser().parse(s);
                        int hasDays = object.get("hasDays").getAsInt();
                        double targetWeight = object.get("targetWeight").getAsDouble();
                        double stillNeed = object.get("stillNeed").getAsDouble();
                        double initialWeight = object.get("initialWeight").getAsDouble();

                        mTvTargetDays.setText(hasDays + "");
                        mTvTargetWeight.setText(targetWeight + "");
                        mTvDistanceTarget.setText(RxFormatValue.fromat4S5R(stillNeed, 2));

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxDialog != null && rxDialog.isShowing())
            rxDialog.dismiss();
    }
}
