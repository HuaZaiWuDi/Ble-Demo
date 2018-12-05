package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.SystemConfigBean;
import lab.wesmartclothing.wefit.flyso.entity.UpdateAppBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netutil.net.StoreService;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.service.BleService;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.mine.InvitationCodeActivity;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.UserInfoActivity;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;
import lab.wesmartclothing.wefit.flyso.utils.HeartSectionUtil;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.JPushUtils;

public class SplashActivity extends BaseActivity {

    private boolean isSaveUserInfo = false;
    private boolean hasInviteCode = false;

    BroadcastReceiver APPReplacedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //覆盖升级APP成功
            UpdateAppBean updateAppBean = new UpdateAppBean();
            updateAppBean.setVersionFlag(UpdateAppBean.VERSION_FLAG_APP);
            updateAppBean.setVersion(RxDeviceUtils.getAppVersionName());
            updateAppBean.setSystem("Android");
            updateAppBean.setPhoneType(RxDeviceUtils.getBuildMANUFACTURER());
            //AndroidID
            updateAppBean.setMacAddr(RxDeviceUtils.getAndroidId());
            updateAppBean.addDeviceVersion(updateAppBean);
        }
    };


    @Override
    protected int layoutId() {
        return R.layout.activity_spalsh;
    }


    @Override
    protected void initViews() {
        super.initViews();
        StatusBarUtils.from(this).setHindStatusBar(true).process();

        startService(new Intent(mContext, BleService.class));
//        startService(new Intent(mContext, WebProcessService.class));
        JPushUtils.init(getApplication());
        registerReceiver(APPReplacedReceiver, new IntentFilter(Intent.ACTION_MY_PACKAGE_REPLACED));

        RxLogUtils.e("用户ID：" + SPUtils.getString(SPKey.SP_UserId));

    }

    @Override
    protected void initNetData() {
        super.initNetData();
        initData();
        initUserInfo();

        RxLogUtils.d("APP版本号：" + RxDeviceUtils.getAppVersionNo());

//        RxActivityUtils.skipActivityAndFinish(mContext, InvitationCodeActivity.class);
    }


    private void initUserInfo() {
        RxLogUtils.i("启动时长：获取用户信息");
        if (!SPUtils.getBoolean(SPKey.SP_GUIDE)) {
            SPUtils.put(SPKey.SP_GUIDE, true);
            RxActivityUtils.skipActivityAndFinish(mActivity, GuideActivity.class);
            return;
        }
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().userInfo())
                .timeout(3, TimeUnit.SECONDS)
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        gotoMain();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        SPUtils.put(SPKey.SP_UserInfo, s);

                        UserInfo userInfo = MyAPP.getGson().fromJson(s, UserInfo.class);

                        int sex = userInfo.getSex();
                        SPUtils.put(SPKey.SP_scaleMAC, userInfo.getScalesMacAddr());
                        SPUtils.put(SPKey.SP_clothingMAC, userInfo.getClothesMacAddr());
                        isSaveUserInfo = sex == 0;
                        hasInviteCode = userInfo.isHasInviteCode();
                    }
                });
    }


    private void gotoMain() {
        HeartSectionUtil.initMaxHeart();
        RxLogUtils.d("跳转");
        //通过验证是否保存userId来判断是否登录
        if (RxDataUtils.isNullString(SPUtils.getString(SPKey.SP_UserId))) {
            RxActivityUtils.skipActivity(mActivity, LoginRegisterActivity.class);
        } else if (!hasInviteCode) {
            RxActivityUtils.skipActivity(mActivity, InvitationCodeActivity.class);
        } else if (isSaveUserInfo) {
            RxActivityUtils.skipActivity(mActivity, UserInfoActivity.class);
        } else {
            RxActivityUtils.skipActivity(mActivity, MainActivity.class);
        }
        RxActivityUtils.finishActivity();
        RxLogUtils.i("启动时长：引导页结束");
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(APPReplacedReceiver);
        super.onDestroy();
    }


    /**
     * 重传在无网络情况下未上传的数据
     */
    private void initData() {
        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            //没有网络直接返回
            return;
        }
        getSystemConfig();
        uploadHistoryData();
    }

    /**
     * 获取app配置信息
     */
    private void getSystemConfig() {
        StoreService dxyService = NetManager.getInstance().createString(StoreService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getSystemConfig())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        SystemConfigBean configBean = MyAPP.getGson().fromJson(s, SystemConfigBean.class);
                        ServiceAPI.Order_Url = configBean.getOrderAddress();
                        ServiceAPI.Shopping_Address = configBean.getShppingAddress();
//                        ServiceAPI.Store_Addr = configBean.get
                        ServiceAPI.SHARE_INFORM_URL = configBean.getShareInformUrl();
                        ServiceAPI.APP_DOWN_LOAD_URL = configBean.getAppDownloadUrl();
                    }
                });
    }


    /**
     * 判断本地是否有之前保存的心率数据：有则上传
     */
    public void uploadHistoryData() {
        new HeartRateUtil().uploadHeartRate();
    }
}
