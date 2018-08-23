package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.UserCenterBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

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
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new MeFragment();
    }

    private QMUICommonListItemView deivceItem, collectionItem;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_me, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        initMineData();
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        RxLogUtils.d("显示：MeFragment");
        initMineData();
    }


    private void initView() {
        groupList();
        initTypeface();

        RxTextUtils.getBuilder("--")
                .append("\t小时\t").setProportion(0.6f).setForegroundColor(getResources().getColor(R.color.GrayWrite))
                .append("--")
                .append("\t分").setProportion(0.6f).setForegroundColor(getResources().getColor(R.color.GrayWrite))
                .into(mTvSportingTime);
    }

    private void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DIN-Regular.ttf");
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

    private void groupList() {
        //设置
        deivceItem = mGroupListView.createItemView(
                getResources().getDrawable(R.mipmap.icon_device), "我的设备", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        setItemView(deivceItem);
        collectionItem = mGroupListView.createItemView(
                getResources().getDrawable(R.mipmap.icon_collection), "我的收藏", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        setItemView(collectionItem);
        final QMUICommonListItemView orderItem = mGroupListView.createItemView(
                getResources().getDrawable(R.mipmap.icon_order), "我的订单", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        setItemView(orderItem);
        final QMUICommonListItemView shoppingItem = mGroupListView.createItemView(
                getResources().getDrawable(R.mipmap.icon_shopping), "我的购物车", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        setItemView(shoppingItem);

        //关于我的
        QMUICommonListItemView problemItem = mGroupListView.createItemView(
                getResources().getDrawable(R.mipmap.icon_problem), "问题与建议", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        setItemView(problemItem);
        QMUICommonListItemView aboutUsItem = mGroupListView.createItemView(
                getResources().getDrawable(R.mipmap.icon_about_us), "关于我们", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        setItemView(aboutUsItem);

        QMUIGroupListView.newSection(getContext())
                .addItemView(deivceItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startFragment(DeviceFragment.getInstance());
                    }
                })
                .addItemView(collectionItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startFragment(CollectFragment.getInstance());
                    }
                })
                .addItemView(orderItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //服务协议
                        Bundle bundle = new Bundle();
                        bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Order_Url);
                        bundle.putString(Key.BUNDLE_TITLE, orderItem.getText().toString());
                        RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
                    }
                })
                .addItemView(shoppingItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //服务协议
                        Bundle bundle = new Bundle();
                        bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Shopping_Address);
                        bundle.putString(Key.BUNDLE_TITLE, shoppingItem.getText().toString());
                        RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
                    }
                })
                .setUseTitleViewForSectionSpace(false)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .addItemView(problemItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startFragment(ProblemFragemnt.getInstance());
                    }
                })
                .addItemView(aboutUsItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startFragment(AboutFragment.getInstance());
                    }
                })
                .addTo(mGroupListView);

    }

    private void setItemView(QMUICommonListItemView item) {
        item.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        item.getTextView().setTextSize(15);
        item.getTextView().getPaint().setFakeBoldText(true);
        item.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));
    }

    @OnClick({R.id.iv_userImg, R.id.iv_setting, R.id.iv_notify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_userImg:
                startFragment(UserInfofragment.getInstance());
                break;
            case R.id.iv_setting:
                startFragment(Settingfragment.getInstance());
                break;
            case R.id.iv_notify:
                startFragment(MessageFragment.getInstance());
                break;
        }
    }

    private void initMineData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.userCenter())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(MyAPP.getRxCache().<String>transformObservable("userCenter", String.class, CacheStrategy.cacheAndRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        Gson gson = MyAPP.getGson();
                        UserCenterBean user = gson.fromJson(s, UserCenterBean.class);

                        Glide.with(mContext)
                                .load(user.getImgUrl())
                                .asBitmap()
                                .placeholder(R.mipmap.userimg)
                                .into(mIvUserImg);//

                        mTvUserName.setText(user.getUserName());
                        deivceItem.setDetailText(user.getBindCount() == 0 ? "" : user.getBindCount() + "");
                        collectionItem.setDetailText(user.getCollectCount() == 0 ? "" : user.getCollectCount() + "");

                        //更新消息未读状态
                        mIvNotify.setImageResource(user.getUnreadCount() == 0 ? R.mipmap.icon_email_white : R.mipmap.icon_email_white_mark);
                        mTvSign.setText(RxDataUtils.isNullString(user.getSignature()) ? "他什么也没留下" : user.getSignature());
                        SportingTime(user.getDuration());
                        mTvTotalHeat.setText(user.getCalorie() + "");
                        mTvTotalDays.setText(user.getAthlDays() + "");
                        mTvMaxHeartRate.setText(user.getMaxHeart() + "");
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


}
