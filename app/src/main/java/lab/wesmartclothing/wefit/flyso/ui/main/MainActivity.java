package lab.wesmartclothing.wefit.flyso.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import lab.wesmartclothing.wefit.flyso.BuildConfig;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.base.FragmentKeyDown;
import lab.wesmartclothing.wefit.flyso.ble.BleService_;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.rxbus.GoToFind;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.WebActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MeFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.MessageFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.Slimming2Fragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver;
import lab.wesmartclothing.wefit.flyso.view.AboutUpdateDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

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
    private Intent bleIntent;

    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();


//    @Receiver(actions = Key.ACTION_SWITCH_BOTTOM_TAB)
//    void switchBottomTab(@Receiver.Extra(Key.EXTRA_SWITCH_BOTTOM_TAB) boolean isVisible) {
//        bottom_tab.setVisibility(isVisible ? View.VISIBLE : View.GONE);
//    }
//


    //检测到运动强制跳转界面

//    //蓝牙秤状态改变(开始测量)
//    @Receiver(actions = Key.ACTION_STATE_START_MEASURE)
//    void scaleStartMeasure() {
//        Bundle bundle = new Bundle();
//        RxLogUtils.d("显示：WeightRecordFragment");
//        QMUIFragment instance = WeightAddFragment.getInstance();
//        instance.setArguments(bundle);
//        startFragment(instance);
//    }
//
//    //心率
//    @Receiver(actions = Key.ACTION_HEART_RATE_CHANGED)
//    void myHeartRate(@Receiver.Extra(Key.EXTRA_HEART_RATE_CHANGED) byte[] heartRate) {
//        startFragment(SportingFragment.getInstance());
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RxLogUtils.e("加载：MainActivity：" + savedInstanceState);

        initView();
        bleIntent = new Intent(mContext, BleService_.class);
        startService(bleIntent);
    }

    public void initView() {
        initReceiverPush();
        initRxBus();
        startLocation(null);
        NetManager.getInstance().setUserIdToken(SPUtils.getString(SPKey.SP_UserId), SPUtils.getString(SPKey.SP_token));

        RxLogUtils.d("手机MAC地址" + RxDeviceUtils.getMacAddress(mContext));
        RxLogUtils.d("手机信息" + RxDeviceUtils.getAndroidId());
        RxLogUtils.d("UserId" + SPUtils.getString(SPKey.SP_UserId));

        initMyViewPager();
        initBottomTab();
        mBottomTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
//                startFragment(MessageFragment.getInstance());
                RxActivityUtils.skipActivity(mActivity, MessageFragment.class);
                break;
        }
    }


    private void initBottomTab() {
        String[] tab_text = getResources().getStringArray(R.array.tab_text);
        int[] imgs_unselect = {R.mipmap.icon_slimming_unselect, R.mipmap.icon_find_unselect,
                R.mipmap.icon_shopping_unselect, R.mipmap.icon_mine_unselect};
        int[] imgs_select = {R.mipmap.icon_slimming_select, R.mipmap.icon_find_select,
                R.mipmap.icon_shopping_select, R.mipmap.icon_mine_select};
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
                //双击或三击我的按钮，出现切换网络界面，同时需要退出重新登录
                if (position == 3 && RxUtils.isFastClick(1000) && BuildConfig.DEBUG) {
                    new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                            .addItem(ServiceAPI.BASE_URL_192)
                            .addItem(ServiceAPI.BASE_URL_208)
                            .addItem(ServiceAPI.BASE_URL_125)
                            .addItem(ServiceAPI.BASE_URL_mix)
                            .addItem(ServiceAPI.BASE_RELEASE)
                            .addItem(ServiceAPI.BASE_DEBUG)
                            .setTitle("修改网络需要重启应用，提示网络错误，需要重新登录")
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    dialog.dismiss();
                                    SPUtils.put(SPKey.SP_BSER_URL, tag);
                                    ServiceAPI.switchURL(tag);
                                }
                            })
                            .build()
                            .show();
//                    showLocalNotify();
                }
            }
        });
    }

    private void showLocalNotify() {
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(1);
        ln.setContent("hhhfff");
        ln.setTitle("lnfff");
        ln.setNotificationId(11111111);
        ln.setBroadcastTime(System.currentTimeMillis() + 1000 * 60 * 10);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "jpush");
        map.put("test", "111");
        JSONObject json = new JSONObject(map);
        ln.setExtras(json.toString());
        JPushInterface.addLocalNotification(mContext.getApplicationContext(), ln);
    }


    private void initMyViewPager() {
        mFragments.clear();
//        mFragments.add(SlimmingFragment.getInstance());
        mFragments.add(Slimming2Fragment.getInstance());
        mFragments.add(FindFragment.getInstance());
        mFragments.add(StoreFragment.getInstance());
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
    private void initReceiverPush() {
        Bundle bundle = getIntent().getExtras();
        RxLogUtils.d("点击通知：" + bundle);
        if (bundle == null) return;

        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(extra);
        RxLogUtils.d("点击通知：" + object.toString());
        String openTarget = object.get("openTarget").getAsString();
        int type = object.get("operation").getAsInt();
        String msgId = object.get("msgId").getAsString();
        switch (type) {
            case TYPE_OPEN_APP:
                break;
            case TYPE_OPEN_ACTIVITY:
                openActivity(openTarget);
                break;
            case TYPE_OPEN_URL:
                //打开URL
                bundle.putString(Key.BUNDLE_WEB_URL, openTarget);
                RxActivityUtils.skipActivity(mActivity, WebActivity.class, bundle);
                break;
        }
        pushMessageReaded(msgId);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initRxBus() {
        RxBus.getInstance().register2(FirmwareVersionUpdate.class)
                .compose(RxComposeUtils.<FirmwareVersionUpdate>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<FirmwareVersionUpdate>() {
                    @Override
                    protected void _onNext(final FirmwareVersionUpdate firmwareVersionUpdate) {
                        final boolean isMust = firmwareVersionUpdate.getMustUpgrade() != 0;
                        //差值大于2kg，体重数据不合理
                        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
                        TextView tvTitle = dialog.getTvTitle();
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("固件升级");
                        dialog.getTvContent().setText("是否升级到最新的版本");
                        dialog.setCancel("升级");
                        dialog.setCancelListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                AboutUpdateDialog updatedialog = new AboutUpdateDialog(mActivity, firmwareVersionUpdate.getFileUrl(), firmwareVersionUpdate.getMustUpgrade() == 0);
                                updatedialog.show();
                            }
                        });
                        dialog.setSure(isMust ? "退出" : "取消");
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (isMust) {
                                    RxActivityUtils.AppExit(mContext);
                                    finish();
                                }
                            }
                        });
                    }
                });

        RxBus.getInstance().register2(GoToFind.class)
                .compose(RxComposeUtils.<GoToFind>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<GoToFind>() {
                    @Override
                    protected void _onNext(GoToFind s) {
                        mViewpager.setCurrentItem(1, true);
                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentKeyDown mStoreFragment = StoreFragment.getInstance();
        FragmentKeyDown mFindFragment = FindFragment.getInstance();

        if (mStoreFragment != null) {
            RxLogUtils.e("mStoreFragment:" + mStoreFragment);
            if (mStoreFragment.onFragmentKeyDown(keyCode, event)) {
                return true;
            }
//            else {
//                if (!RxUtils.isFastClick(2000)) {
//                    RxLogUtils.d("mStoreFragment:再按一次");
//                    RxToast.success("再按一次退出");
//                    return true;
//                }
//            }
        }
        if (mFindFragment != null) {
            RxLogUtils.d("mFindFragment:" + mFindFragment);
            if (mFindFragment.onFragmentKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        stopService(bleIntent);
        super.onDestroy();
    }

    //通知栏推送已读
    private void pushMessageReaded(String msgId) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.pushMessageReaded(msgId))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                    }
                });
    }
}
