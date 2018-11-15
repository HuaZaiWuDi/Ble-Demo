package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.smartclothing.blelibrary.BleTools;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
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
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.WelcomeActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;
import lab.wesmartclothing.wefit.flyso.view.RoundDisPlayView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.RequestBody;

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
        Typeface typeface = MyAPP.typeface;
        mTvTargetWeight.setTypeface(typeface);

        lastWeight = SPUtils.getFloat(SPKey.SP_realWeight, (float) lastWeight);
        RxLogUtils.d("上一次体重数据：" + lastWeight);

        mTvTip.setText("请上称...");
        mTvTitle.setText("测量体重");
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        showWeightData(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!BleTools.getBleManager().isBlueEnable())
            BleTools.getBleManager().enableBluetooth();
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
        final QNScaleData qnScaleData = MyAPP.getGson().fromJson(extras.getString(Key.BUNDLE_WEIGHT_QNDATA), QNScaleData.class);
        if (unsteadyWeight > 0) {
            mTvTargetWeight.setText((float) unsteadyWeight + "");
            mTvTip.setText("称重中...");
            mTvTitle.setText("正在测量体重");
            if (mTvTip.getVisibility() == View.INVISIBLE) {
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
            mTvTip.setText("身体成分测量中...");
            mTvTitle.setText("正在测量身体成分");
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
                        mTvTitle.setText("测量身体成分失败");
                    } else if (lastWeight != 0 && (Math.abs(realWeight - lastWeight) > 2)) {
                        //无效
                        mTvTitle.setText("测量数据和之前相差过大");
                    } else if (lastWeight == 0 || realWeight >= 35) {//身体成分成功直接跳转回去
                        mTvTitle.setText("测量身体成分成功");
                        saveWeight();
                    } else {
                        mTvTitle.setText("测量体重失败");
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
            mTvTitle.setText("测量超时，请再试一次");
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
        final WeightAddBean bean = new WeightAddBean();
        if (mQnScaleData == null) {
            RxToast.normal("测量体重失败");
            return;
        }
        bean.setMeasureTime(System.currentTimeMillis() + "");
        for (QNScaleItemData item : mQnScaleData.getAllItem()) {
            WeightTools.ble2Backstage(item, bean);
        }

        String s = MyAPP.getGson().toJson(bean, WeightAddBean.class);
        SPUtils.put(SPKey.SP_realWeight, (float) bean.getWeight());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addWeightInfo(body))
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加体重：");
//                        RxToast.normal("存储体重成功");
//                        onBackPressed();

                        //刷新数据
                        RxBus.getInstance().post(new RefreshSlimming());

                        if (RxActivityUtils.isExistActivity(WelcomeActivity.class)) {
                            //把体重数据传递到欢迎界面
                            RxBus.getInstance().post(bean);
                            onBackPressed();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Key.BUNDLE_DATA_GID, s);
                            RxActivityUtils.skipActivityAndFinish(mContext, BodyDataFragment.class, bundle);
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });

    }
}
