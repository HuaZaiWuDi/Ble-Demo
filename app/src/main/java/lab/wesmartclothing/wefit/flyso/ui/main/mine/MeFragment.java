package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserCenterBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.MessageChangeBus;
import lab.wesmartclothing.wefit.flyso.rxbus.NetWorkType;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

/**
 * Created by jk on 2018/8/9.
 */
public class MeFragment extends BaseAcFragment {

    @BindView(R.id.iv_userImg)
    QMUIRadiusImageView mIvUserImg;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.iv_notify)
    ImageView mIvNotify;
    @BindView(R.id.tv_sportingTime)
    TextView mTvSportingTime;
    @BindView(R.id.tv_totalHeat)
    TextView mTvTotalHeat;
    @BindView(R.id.tv_totalDays)
    TextView mTvTotalDays;
    @BindView(R.id.tv_MaxHeartRate)
    TextView mTvMaxHeartRate;
    Unbinder unbinder;
    @BindView(R.id.iv_myDevice)
    ImageView mIvMyDevice;
    @BindView(R.id.tv_deviceCount)
    TextView mTvDeviceCount;
    @BindView(R.id.layout_myDevice)
    RxRelativeLayout mLayoutMyDevice;
    @BindView(R.id.iv_Collect)
    ImageView mIvCollect;
    @BindView(R.id.tv_collectCount)
    TextView mTvCollectCount;
    @BindView(R.id.layout_myCollect)
    RxRelativeLayout mLayoutMyCollect;
    @BindView(R.id.iv_Order)
    ImageView mIvOrder;
    @BindView(R.id.layout_myOrder)
    RxRelativeLayout mLayoutMyOrder;
    @BindView(R.id.iv_shoppingAddress)
    ImageView mIvShoppingAddress;
    @BindView(R.id.layout_myShoppingAddress)
    RxRelativeLayout mLayoutMyShoppingAddress;
    @BindView(R.id.iv_problem)
    ImageView mIvProblem;
    @BindView(R.id.layout_problem)
    RxRelativeLayout mLayoutProblem;
    @BindView(R.id.iv_aboutUs)
    ImageView mIvAboutUs;
    @BindView(R.id.layout_aboutUs)
    RxRelativeLayout mLayoutAboutUs;
    @BindView(R.id.tv_invitation)
    TextView mTvInvitation;

    public static Fragment getInstance() {
        return new MeFragment();
    }


    @Override
    protected int layoutId() {
        return R.layout.fragment_me;
    }


    @Override
    protected void initViews() {
        super.initViews();
        initTypeface();
        RxTextUtils.getBuilder("--")
                .append("\t小时\t").setProportion(0.6f).setForegroundColor(getResources().getColor(R.color.GrayWrite))
                .append("--")
                .append("\t分").setProportion(0.6f).setForegroundColor(getResources().getColor(R.color.GrayWrite))
                .into(mTvSportingTime);

        UserInfo userInfo = JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);
        if (userInfo != null) {
            mTvInvitation.setText(userInfo.getInvitationCode());
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        initMineData();
    }


    @Override
    protected void initRxBus() {
        //后台上传心率数据成功，刷新界面
        RxBus.getInstance().register2(RefreshMe.class)
                .compose(RxComposeUtils.<RefreshMe>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<RefreshMe>() {
                    @Override
                    protected void _onNext(RefreshMe hearRateUpload) {
                        initMineData();
                    }
                });

        //只有在显示时才会网络请求
        RxBus.getInstance().register2(NetWorkType.class)
                .compose(RxComposeUtils.<NetWorkType>bindLifeResume(lifecycleSubject))
                .subscribe(new RxSubscriber<NetWorkType>() {
                    @Override
                    protected void _onNext(NetWorkType netWorkType) {
                        if (netWorkType.isBoolean())
                            initMineData();
                    }
                });

        //消息通知
        RxBus.getInstance().register2(MessageChangeBus.class)
                .compose(RxComposeUtils.<MessageChangeBus>bindLifeResume(lifecycleSubject))
                .subscribe(new RxSubscriber<MessageChangeBus>() {
                    @Override
                    protected void _onNext(MessageChangeBus messageChangeBus) {
                        mIvNotify.setBackgroundResource(R.mipmap.icon_email_white);
                    }
                });
    }

    private void initTypeface() {
        Typeface typeface = MyAPP.typeface;
        mTvSportingTime.setTypeface(typeface);
        mTvTotalHeat.setTypeface(typeface);
        mTvTotalDays.setTypeface(typeface);
        mTvMaxHeartRate.setTypeface(typeface);
    }

    private void SportingTime(long time) {
        int totalMin = (int) (time / 60);
        int hour = (totalMin / 60);
        int min = totalMin % 60;
        RxTextUtils.getBuilder(hour + "")
                .append("\t小时\t").setProportion(0.6f).setForegroundColor(getResources().getColor(R.color.GrayWrite))
                .append(min + "")
                .append("\t分").setProportion(0.6f).setForegroundColor(getResources().getColor(R.color.GrayWrite))
                .into(mTvSportingTime);
    }


    private void initMineData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().userCenter())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("userCenter", String.class, CacheStrategy.firstRemote()))
                .map(new CacheResult.MapFunc<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        UserCenterBean user = JSON.parseObject(s, UserCenterBean.class);

                        MyAPP.getImageLoader().displayImage(mActivity, user.getImgUrl(), R.mipmap.userimg, mIvUserImg);

                        mTvUserName.setText(user.getUserName());
                        mTvDeviceCount.setText(user.getBindCount() == 0 ? "" : user.getBindCount() + "");
                        mTvCollectCount.setText(user.getCollectCount() == 0 ? "" : user.getCollectCount() + "");

                        //更新消息未读状态
                        mIvNotify.setImageResource(user.getUnreadCount() == 0 ? R.mipmap.icon_email_white : R.mipmap.icon_email_white_mark);
                        mTvSign.setText(RxDataUtils.isNullString(user.getSignature()) ? "他什么也没留下" : user.getSignature());
                        SportingTime(user.getDuration());
                        mTvTotalHeat.setText(user.getCalorie() + "");
                        mTvTotalDays.setText(user.getAthlDays() + "");
                        mTvMaxHeartRate.setText(user.getMaxHeart() + "");
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    @OnClick({
            R.id.layout_myDevice,
            R.id.layout_myCollect,
            R.id.layout_myOrder,
            R.id.layout_myShoppingAddress,
            R.id.layout_problem,
            R.id.layout_aboutUs,
            R.id.layout_HealthReport,
            R.id.iv_userImg,
            R.id.iv_setting,
            R.id.iv_notify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_myDevice:
                RxActivityUtils.skipActivity(mContext, DeviceFragment.class);
                break;
            case R.id.layout_myCollect:
                RxActivityUtils.skipActivity(mContext, CollectFragment.class);
                break;
            case R.id.layout_myOrder:
                WebTitleActivity.startWebActivity(mActivity, "我的订单", ServiceAPI.Order_Url, true);
                break;
            case R.id.layout_myShoppingAddress:
                WebTitleActivity.startWebActivity(mActivity, "我的购物车", ServiceAPI.Shopping_Address, true);
                break;
            case R.id.layout_problem:
                RxActivityUtils.skipActivity(mContext, ProblemFragemnt.class);
                break;
            case R.id.layout_aboutUs:
                RxActivityUtils.skipActivity(mContext, AboutFragment.class);
                break;
            case R.id.iv_userImg:
                RxActivityUtils.skipActivity(mContext, UserInfofragment.class);
                break;
            case R.id.iv_setting:
                RxActivityUtils.skipActivity(mContext, Settingfragment.class);
                break;
            case R.id.iv_notify:
                RxActivityUtils.skipActivity(mContext, MessageFragment.class);
                break;
            case R.id.layout_HealthReport:
                RxActivityUtils.skipActivity(mContext, HealthReportActivity.class);
                break;
            default:

        }
    }

}
