package lab.wesmartclothing.wefit.flyso.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import cn.jpush.android.api.JPushInterface;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.base.FragmentKeyDown;
import lab.wesmartclothing.wefit.flyso.ble.BleService_;
import lab.wesmartclothing.wefit.flyso.entity.FirmwareVersionUpdate;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.WebActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.find.FindFragment;
import lab.wesmartclothing.wefit.flyso.ui.main.store.StoreFragment;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.AboutUpdateDialog;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.TYPE_OPEN_ACTIVITY;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.TYPE_OPEN_APP;
import static lab.wesmartclothing.wefit.flyso.utils.jpush.MyJpushReceiver.TYPE_OPEN_URL;

public class MainActivity extends BaseALocationActivity {

    private Intent bleIntent;

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
//
//        startFragment(SportingFragment.getInstance());
//    }


    @Override
    protected int getContextViewId() {
        return R.id.main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxLogUtils.e("加载：MainActivity：" + savedInstanceState);
//        startFragment(MainFragment.getInstance());

        initView();
        if (savedInstanceState == null) {
            startFragment(MainFragment.getInstance());
        } else {
            RxLogUtils.e("savedInstanceState:" + savedInstanceState);
            //防止后台杀掉APP出现白屏问题
            startFragmentAndDestroyCurrent(MainFragment.getInstance(), false);
        }

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
//        TextSpeakUtils.speakFlush("主人！我想你了");
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
                RxBus.getInstance().post(openTarget);
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
                        if (isMust)
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
        if (RxUtils.isFastClick(500)) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            moveTaskToBack(true);
        } else {
            QMUIFragment fragment = getCurrentFragment();
            if (fragment != null) {
                popBackStack();
            }
        }
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
