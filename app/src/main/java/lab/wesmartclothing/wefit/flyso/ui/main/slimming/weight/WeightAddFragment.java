package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.wesmarclothing.mylibrary.net.RxBus;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.QNBleManager;
import lab.wesmartclothing.wefit.flyso.ble.ScannerManager;
import lab.wesmartclothing.wefit.flyso.entity.HealthyInfoBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.WelcomeActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;
import lab.wesmartclothing.wefit.flyso.view.RoundDisPlayView;

/**
 * Created by jk on 2018/7/27.
 */
public class WeightAddFragment extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.mRoundDisPlayView)
    RoundDisPlayView mMRoundDisPlayView;
    @BindView(R.id.tv_targetWeight)
    TextView mTvTargetWeight;
    @BindView(R.id.layout_addWeight)
    RelativeLayout mLayoutAddWeight;
    @BindView(R.id.btn_forget)
    QMUIRoundButton mBtnForget;
    @BindView(R.id.btn_save)
    QMUIRoundButton mBtnSave;
    Unbinder unbinder;


    //最近一次体重
    private double lastWeight;
    private QNScaleData mQnScaleData;


    @Override
    protected int layoutId() {
        return R.layout.fragment_add_weight;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initPermissions();
        Typeface typeface = MyAPP.typeface;
        mTvTargetWeight.setTypeface(typeface);

        lastWeight = SPUtils.getFloat(SPKey.SP_realWeight, (float) lastWeight);
        RxLogUtils.d("上一次体重数据：" + lastWeight);

        mTvTip.setText(R.string.pleaseWeigh);
        mTvTitle.setText(R.string.scaleWeight);


        ScannerManager.INSTANCE.enableBLE();

        if (!QNBleManager.getInstance().isConnect()) {
            ScannerManager.INSTANCE.startScan();
        }
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        showWeightData(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        mMRoundDisPlayView.stopAnimation();
        TimeOutTimer.stopTimer();
        super.onStop();
    }


    private void showWeightData(Bundle extras) {
        if (extras == null) return;
        double unsteadyWeight = extras.getDouble(Key.BUNDLE_WEIGHT_UNSTEADY, 0);
        final QNScaleData qnScaleData = JSON.parseObject(extras.getString(Key.BUNDLE_WEIGHT_QNDATA), QNScaleData.class);
        if (unsteadyWeight > 0) {
            mTvTargetWeight.setText((float) unsteadyWeight + "");
            mTvTip.setText(R.string.weighing);
            if (!mTvTitle.getText().toString().equals(getString(R.string.weightMeasured))) {
                mTvTitle.setText(R.string.weightMeasured);
                mTvTip.setVisibility(View.VISIBLE);
                mBtnForget.setVisibility(View.INVISIBLE);
                mBtnSave.setVisibility(View.INVISIBLE);
                mMRoundDisPlayView.showPoint(false);
                mMRoundDisPlayView.startAnimation();
            }
            TimeOutTimer.startTimer();
        }

        if (qnScaleData != null) {
            RxLogUtils.d("实时的稳定测量数据是否有效：" + Arrays.toString(qnScaleData.getAllItem().toArray()));

            final float realWeight = (float) qnScaleData.getAllItem().get(0).getValue();
            mTvTargetWeight.setText(realWeight + "");
            mTvTip.setText(R.string.bodyCompositionMeasuring);
            mTvTitle.setText(R.string.bodyCompositionMeasure);
            mMRoundDisPlayView.stopAnimation();
            mMRoundDisPlayView.showPoint(true);
            mMRoundDisPlayView.startAnim();
            TimeOutTimer.stopTimer();
            //强行延时三秒用来展示动画效果
            mMRoundDisPlayView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvTip.setVisibility(View.INVISIBLE);
                    mBtnForget.setVisibility(View.VISIBLE);
                    mBtnSave.setVisibility(View.VISIBLE);
                    mMRoundDisPlayView.stopAnimation();
                    //TODO 这里暂时通过返回的数据的个数判断是否有效
                    mQnScaleData = qnScaleData;
                    if (qnScaleData.getItemValue(3) == 0) {
                        //无效
                        mTvTitle.setText(R.string.bodyCompositionMeasureFail);
                    } else if (lastWeight != 0 && (Math.abs(realWeight - lastWeight) > 2)) {
                        //无效
                        mTvTitle.setText(R.string.bodyCompositionless);
                    } else if (realWeight >= 25) {//身体成分成功直接跳转回去
                        mTvTitle.setText(R.string.bodyCompositionMeasureSuccess);
                        saveWeight();
                    } else {
                        mTvTitle.setText(R.string.bodyCompositionMeasureFail);
                    }
                }
            }, 3000);

            for (QNScaleItemData item : qnScaleData.getAllItem()) {
                RxLogUtils.d("---------------------");
                RxLogUtils.d("实时的稳定测量数据：【名字】" + item.getName());
                RxLogUtils.d("实时的稳定测量数据：【类型】" + item.getType());
                RxLogUtils.d("实时的稳定测量数据：【值】" + item.getValue());
            }
        }
    }


    MyTimer TimeOutTimer = new MyTimer(new MyTimerListener() {
        @Override
        public void enterTimer() {
            mTvTip.setVisibility(View.INVISIBLE);
            mBtnForget.setVisibility(View.VISIBLE);
            mBtnSave.setVisibility(View.VISIBLE);
            mMRoundDisPlayView.stopAnimation();
            mTvTitle.setText(R.string.measeureTimeoutAndRetry);
            TimeOutTimer.stopTimer();
        }
    }, 20000);


    @OnClick({R.id.btn_forget, R.id.btn_save, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forget:
                onBackPressed();
                break;
            case R.id.btn_save:
                saveWeight();
                break;
            case R.id.img_close:
                onBackPressed();
                break;
        }
    }

    private void saveWeight() {
        final HealthyInfoBean bean = new HealthyInfoBean();
        if (mQnScaleData == null) {
            RxToast.normal(getString(R.string.bodyCompositionMeasureFail));
            return;
        }

        bean.setMeasureTime(System.currentTimeMillis());
        for (QNScaleItemData item : mQnScaleData.getAllItem()) {
            WeightTools.ble2Backstage(item, bean);
        }


        bean.setHmac(MyAPP.getgUserInfo().getHmac());
        String s = JSON.toJSONString(bean);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addWeightInfo(NetManager.fetchRequest(s)))
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加体重：");
//                        onBackPressed();

                        SPUtils.put(SPKey.SP_realWeight, (float) bean.getWeight());
                        //刷新数据
                        RxBus.getInstance().post(new RefreshSlimming());

                        if (RxActivityUtils.isExistActivity(WelcomeActivity.class)) {
                            //把体重数据传递到欢迎界面
                            RxBus.getInstance().post(bean);
                            onBackPressed();
                        } else {
                            BodyDataFragment.start(mContext, s, bean, false);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    private void initPermissions() {
        new RxPermissions((FragmentActivity) mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxComposeUtils.<Boolean>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                    }
                });
    }
}
