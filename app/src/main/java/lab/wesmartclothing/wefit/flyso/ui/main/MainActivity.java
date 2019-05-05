package lab.wesmartclothing.wefit.flyso.ui.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.entity.NotifyDataBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.GoToMainPage;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.ui.guide.SplashActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MeFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.record.SlimmingFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.record.SlimmingRecordFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver;

import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.TYPE_OPEN_ACTIVITY;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.TYPE_OPEN_APP;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.TYPE_OPEN_URL;


public class MainActivity extends BaseALocationActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.mCommonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.bottom_tab)
    LinearLayout mBottomTab;
    @BindView(R.id.parent)
    RelativeLayout mParent;

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();


    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        RxLogUtils.e("加载：MainActivity：" + savedInstanceState);
        super.onCreate(savedInstanceState);
        //防止应用处于后台，被杀死，再次唤醒时，重走启动流程
        if (savedInstanceState != null && mContext != null) {
            RxActivityUtils.skipActivityAndFinish(mContext, SplashActivity.class);
            return;
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
    }

    @Override
    protected void initNetData() {
        super.initNetData();
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        RxLogUtils.d("bundle:" + bundle.toString());
        initReceiverPush(bundle);
    }

    @Override
    protected void initRxBus2() {
        super.initRxBus2();
        RxBus.getInstance().register2(GoToMainPage.class)
                .compose(RxComposeUtils.<GoToMainPage>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<GoToMainPage>() {
                    @Override
                    protected void _onNext(GoToMainPage s) {
                        mViewpager.setCurrentItem(s.page, true);
                    }
                });


    }

    public void initView() {
        startLocation(null);
        initSystemConfig();
        RxLogUtils.d("手机MAC地址:" + RxDeviceUtils.getMacAddress());
        RxLogUtils.d("androidID:" + RxDeviceUtils.getAndroidId());
        RxLogUtils.d("UserId:" + SPUtils.getString(SPKey.SP_UserId));

        initMyViewPager();
        initBottomTab();
        mBottomTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        startService(new Intent(mContext, BleService.class));

    }

    private void initSystemConfig() {
        //判断是否有权限
        new RxPermissions((FragmentActivity) mActivity)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxComposeUtils.<Permission>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Permission>() {
                    @Override
                    protected void _onNext(Permission aBoolean) {
                        RxLogUtils.e("是否开启了权限：" + aBoolean);
                    }
                });

        //判断是否关闭了通知栏权限
        RxLogUtils.e("通知栏权限：" + NotificationManagerCompat.from(mContext).areNotificationsEnabled());
        if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
            new RxDialogSureCancel(mContext)
                    .setTitle("提示")
                    .setContent("您的通知权限未开启，可能影响APP的正常使用")
                    .setSure("现在去开启")
                    .setSureListener(view -> {
                        /**
                         * 跳到通知栏设置界面
                         * @param context
                         */
                        Intent localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        localIntent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        mContext.startActivity(localIntent);
                    }).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxLogUtils.d("启动时长" + "主页可交互");
    }


    private void initBottomTab() {
        String[] tab_text = getResources().getStringArray(R.array.tab_text);
        int[] imgs_unselect = {R.mipmap.icon_slimming_unselect, R.mipmap.icon_record,
                R.mipmap.icon_find_unselect, R.mipmap.icon_mine_unselect};
        int[] imgs_select = {R.mipmap.icon_slimming_select, R.mipmap.icon_record_unselect,
                R.mipmap.icon_find_select, R.mipmap.icon_mine_select};
        mBottomTabItems.clear();
        for (int i = 0; i < tab_text.length; i++) {
            mBottomTabItems.add(new BottomTabItem(imgs_select[i], imgs_unselect[i], tab_text[i]));
        }

        mCommonTabLayout.setTextSelectColor(getResources().getColor(R.color.Gray));
        mCommonTabLayout.setTextUnselectColor(getResources().getColor(R.color.GrayWrite));
        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewpager.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {
                //双击我的按钮，出现切换网络界面，同时需要退出重新登录
                if (position == mFragments.size() - 1 && RxUtils.isFastClick(1000) && BuildConfig.DEBUG) {
                    new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                            .addItem(ServiceAPI.BASE_URL_192)
                            .addItem(ServiceAPI.BASE_URL_208)
                            .addItem(ServiceAPI.BASE_URL_125)
                            .addItem(ServiceAPI.BASE_URL_mix)
                            .addItem(ServiceAPI.BASE_RELEASE)
                            .addItem(ServiceAPI.BASE_DEBUG)
                            .setTitle("修改网络需要重启应用，提示网络错误，需要重新登录")
                            .setOnSheetItemClickListener((dialog, itemView, position1, tag) -> {
                                dialog.dismiss();
                                SPUtils.put(SPKey.SP_BSER_URL, tag);
                                ServiceAPI.switchURL(tag);
                            })
                            .build()
                            .show();
                }
            }
        });
    }


    private void initMyViewPager() {
        mFragments.clear();
        mFragments.add(SlimmingFragment.newInstance());
//        mFragments.add(Slimming2Fragment.getInstance());
        mFragments.add(SlimmingRecordFragment.newInstance());
        mFragments.add(FindFragment.getInstance());
//        mFragments.add(StoreFragment.getInstance());
        mFragments.add(MeFragment.getInstance());

        mViewpager.setOffscreenPageLimit(4);
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCommonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * {
     * "operation":1,      // 通知操作。1-打开应用，2-打开应用内指定页面，3-APP内打开url
     * "openTarget":""      //operation：2（ slim-瘦身首页，find-发现首页，shop-商城首页，user-我的首页，message-站内信,url）
     * }
     */
    private void initReceiverPush(Bundle bundle) {
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        RxLogUtils.d("点击通知：" + extra);
        NotifyDataBean notifyDataBean = null;
        try {
            notifyDataBean = JSON.parseObject(extra, NotifyDataBean.class);
        } catch (Exception e) {
            notifyDataBean = null;
        }
        if (notifyDataBean == null) return;
        String openTarget = notifyDataBean.getOpenTarget();
        int type = notifyDataBean.getOperation();
        String msgId = notifyDataBean.getMsgId();
        switch (type) {
            case TYPE_OPEN_APP:
                break;
            case TYPE_OPEN_ACTIVITY:
                openActivity(openTarget);
                break;
            case TYPE_OPEN_URL:
                //打开URL
                WebTitleActivity.startWebActivity(mActivity, getString(R.string.appName), openTarget);
                break;
        }
        pushMessageReaded(msgId);
    }


    private void openActivity(String openTarget) {
        switch (openTarget) {
            case MyJpushReceiver.ACTIVITY_SLIM:
                mViewpager.setCurrentItem(0, true);
                break;
            case MyJpushReceiver.ACTIVITY_FIND:
                mViewpager.setCurrentItem(1, true);
                break;
            case MyJpushReceiver.ACTIVITY_SHOP:
                mViewpager.setCurrentItem(2, true);
                break;
            case MyJpushReceiver.ACTIVITY_USER:
                mViewpager.setCurrentItem(3, true);
                break;
            case MyJpushReceiver.ACTIVITY_MESSAGE:
                //跳转消息通知
                RxActivityUtils.skipActivity(mActivity, MessageFragment.class);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //通知栏推送已读
    private void pushMessageReaded(String msgId) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().pushMessageReaded(msgId))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
