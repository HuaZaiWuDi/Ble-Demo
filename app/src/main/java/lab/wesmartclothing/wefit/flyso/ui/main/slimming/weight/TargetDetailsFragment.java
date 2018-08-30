package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

/**
 * Created by jk on 2018/7/27.
 */
public class TargetDetailsFragment extends BaseAcFragment {

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

    public static QMUIFragment getInstance() {
        return new TargetDetailsFragment();
    }

    private Bundle bundle = new Bundle();

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_target_details, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        targetWeight();
        initTopBar();
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
        mTvTargetDays.setTypeface(typeface);
        mTvTargetWeight.setTypeface(typeface);
        mTvDistanceTarget.setTypeface(typeface);

        bundle = getArguments();
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("目标设置");
    }

    @OnClick(R.id.btn_reSet)
    public void onViewClicked() {
        //传递初始体重信息
        QMUIFragment fragment = SettingTargetFragment.getInstance();
        fragment.setArguments(bundle);
        startFragmentAndDestroyCurrent(fragment);
    }

    private void targetWeight() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.fetchTargetWeight())
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
                        mTvDistanceTarget.setText(stillNeed + "");

                        bundle.putInt(Key.BUNDLE_HAS_DAYS, hasDays);
                        bundle.putDouble(Key.BUNDLE_STILL_NEED, stillNeed);
                        bundle.putDouble(Key.BUNDLE_TARGET_WEIGHT, targetWeight);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
