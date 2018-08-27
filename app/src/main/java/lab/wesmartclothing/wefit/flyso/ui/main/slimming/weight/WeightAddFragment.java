package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.listen.QNDataListener;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;
import lab.wesmartclothing.wefit.flyso.view.RoundDisPlayView;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/7/27.
 */
public class WeightAddFragment extends BaseAcFragment {

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

    public static QMUIFragment getInstance() {
        return new WeightAddFragment();
    }

    //最近一次体重
    private double lastWeight;
    private QNScaleData mQnScaleData;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_add_weight, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onStart() {
        initBleCallBack();
        mMRoundDisPlayView.postDelayed(timeOut, 8000);
        super.onStart();
    }

    @Override
    public void onStop() {
        mMRoundDisPlayView.stopAnimation();
        MyAPP.QNapi.setDataListener(null);
        super.onStop();
    }


    private void initView() {
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvTargetWeight.setTypeface(typeface);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lastWeight = bundle.getDouble(Key.BUNDLE_LAST_WEIGHT);
        }
        RxLogUtils.d("上一次体重数据：" + lastWeight);
    }


    Runnable timeOut = new Runnable() {
        @Override
        public void run() {
            mTvTip.setVisibility(View.INVISIBLE);
            mBtnForget.setVisibility(View.VISIBLE);
            mBtnSave.setVisibility(View.VISIBLE);
            mMRoundDisPlayView.stopAnimation();
            mTvTitle.setText("测量超时，请再试一次");
        }
    };

    //体脂称提取数据回调
    private void initBleCallBack() {
        MyAPP.QNapi.setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
                RxLogUtils.d("体重秤实时重量：" + v);
                mTvTargetWeight.setText((float) v + "");
                mTvTip.setText("称重中...");
                mTvTitle.setText("正在测量体重");
                if (mTvTip.getVisibility() == View.INVISIBLE) {
                    mTvTip.setVisibility(View.VISIBLE);
                    mBtnForget.setVisibility(View.INVISIBLE);
                    mBtnSave.setVisibility(View.INVISIBLE);
                    mMRoundDisPlayView.showPoint(false);
                    mMRoundDisPlayView.startAnimation();
                    mMRoundDisPlayView.postDelayed(timeOut, 10000);
                }
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
                RxLogUtils.d("实时的稳定测量数据是否有效：");
                final float realWeight = (float) qnScaleData.getAllItem().get(0).getValue();
                mTvTargetWeight.setText(realWeight + "");
                mTvTip.setText("身体成分测量中...");
                mTvTitle.setText("正在测量身体成分");
                mMRoundDisPlayView.stopAnimation();
                mMRoundDisPlayView.showPoint(true);
                mMRoundDisPlayView.startAnim();
                mMRoundDisPlayView.removeCallbacks(timeOut);
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

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, List<QNScaleStoreData> list) {
            }
        });
    }


    @OnClick({R.id.btn_forget, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forget:
                popBackStack();
                break;
            case R.id.btn_save:
                saveWeight();
                break;
        }
    }

    private void saveWeight() {
        WeightAddBean bean = new WeightAddBean();
        if (mQnScaleData == null) {
            RxToast.normal("体重测量失败");
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
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加体重：");
                        RxToast.normal("存储体重成功");
                        popBackStack();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });

    }
}
