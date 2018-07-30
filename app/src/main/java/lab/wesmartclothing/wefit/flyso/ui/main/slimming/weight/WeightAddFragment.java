package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.utils.RxLogUtils;
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
import lab.wesmartclothing.wefit.flyso.view.RoundDisPlayView;

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

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_add_weight, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        MyAPP.QNapi.setDataListener(null);
        super.onDestroy();
    }

    private void initView() {
        initBleCallBack();
    }


    //体脂称提取数据回调
    private void initBleCallBack() {
        MyAPP.QNapi.setDataListener(new QNDataListener() {
            @Override
            public void onGetUnsteadyWeight(QNBleDevice qnBleDevice, double v) {
                RxLogUtils.d("体重秤实时重量：" + v);
                mTvTargetWeight.setText((float) v + "");
                mTvTip.setText("称重中...");
                mMRoundDisPlayView.startAnimation();
            }

            @Override
            public void onGetScaleData(QNBleDevice qnBleDevice, final QNScaleData qnScaleData) {
                RxLogUtils.d("实时的稳定测量数据是否有效：");
                mTvTargetWeight.setText((float) qnScaleData.getAllItem().get(1).getValue() + "");
                mTvTip.setText("身体成分测量中...");
                mMRoundDisPlayView.startAnimation();
                mMRoundDisPlayView.showPoint(true);
                mMRoundDisPlayView.startAnim();


                for (QNScaleItemData item : qnScaleData.getAllItem()) {
                    RxLogUtils.d("---------------------");
                    RxLogUtils.d("实时的稳定测量数据：" + item.getName());
                    RxLogUtils.d("实时的稳定测量数据：" + item.getType());
                    RxLogUtils.d("实时的稳定测量数据：" + item.getValue());
                }
            }

            @Override
            public void onGetStoredScale(QNBleDevice qnBleDevice, List<QNScaleStoreData> list) {
                RxLogUtils.d("历史数据：" + list.size());
            }
        });
    }


    @OnClick({R.id.btn_forget, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forget:
                mMRoundDisPlayView.stopAnimation();
                mMRoundDisPlayView.showPoint(true);
                mMRoundDisPlayView.startAnim();
                break;
            case R.id.btn_save:
                mMRoundDisPlayView.stopAnimation();
                mMRoundDisPlayView.showPoint(false);
                mMRoundDisPlayView.stopAnimation();
                break;
        }
    }
}
