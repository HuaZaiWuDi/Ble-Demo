package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.UpdateAppBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.UserInfoActivity;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.JPushUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.net.StoreService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;

public class SplashActivity extends BaseActivity {

    private boolean isSaveUserInfo = false;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxLogUtils.i("启动时长：引导页开始");
        setContentView(R.layout.activity_spalsh);
        registerReceiver(APPReplacedReceiver, new IntentFilter(Intent.ACTION_MY_PACKAGE_REPLACED));
        initView();
//        TODO 切换下网络请求框架的设置，现在是手动解析的，之后改为GSON工厂配置，这样能减少因为后台问题导致的崩溃问题
//        RxActivityUtils.skipActivityAndFinish(mContext, WelcomeActivity.class);
        JPushUtils.init(getApplication());
    }

    public void initView() {
        String baseUrl = SPUtils.getString(SPKey.SP_BSER_URL);
        NetManager.getInstance().setUserIdToken(SPUtils.getString(SPKey.SP_UserId), SPUtils.getString(SPKey.SP_token));
        if (!RxDataUtils.isNullString(baseUrl)) {
            ServiceAPI.switchURL(baseUrl);
        }

        initData();
        initUserInfo();
        RxLogUtils.e("用户ID：" + SPUtils.getString(SPKey.SP_UserId));

    }


    private void initUserInfo() {
        RxLogUtils.i("启动时长：获取用户信息");
        if (!SPUtils.getBoolean(SPKey.SP_GUIDE)) {
            SPUtils.put(SPKey.SP_GUIDE, true);
            RxActivityUtils.skipActivityAndFinish(mActivity, GuideActivity.class);
            return;
        }
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.userInfo())
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
                        RxLogUtils.d("获取用户信息：" + s);
                        SPUtils.put(SPKey.SP_UserInfo, s);

                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(s);
                        int sex = object.get("sex").getAsInt();
                        isSaveUserInfo = sex == 0;

                        String clothesMacAddr = object.get("clothesMacAddr").getAsString();
                        String scalesMacAddr = object.get("scalesMacAddr").getAsString();
                        SPUtils.put(SPKey.SP_scaleMAC, scalesMacAddr);
                        SPUtils.put(SPKey.SP_clothingMAC, clothesMacAddr);
                    }
                });
    }


    private void gotoMain() {
        RxLogUtils.d("跳转");
        //通过验证是否保存userId来判断是否登录
        if (RxDataUtils.isNullString(SPUtils.getString(SPKey.SP_UserId))) {
            RxActivityUtils.skipActivityAndFinish(mActivity, LoginRegisterActivity.class);
        } else if (isSaveUserInfo) {
            RxActivityUtils.skipActivityAndFinish(mActivity, UserInfoActivity.class);
        } else {
            RxActivityUtils.skipActivityAndFinish(mActivity, MainActivity.class);
        }
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
        getStoreAddr();
        getOrderUrl();
        getShoppingAddress();
        uploadHistoryData();
    }

    /**
     * 获取商城地址
     */
    private void getStoreAddr() {
        StoreService dxyService = NetManager.getInstance().createString(StoreService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getMallAddress())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        ServiceAPI.Store_Addr = s;
                    }
                });
    }

    /**
     * 获取商城订单地址
     */
    private void getOrderUrl() {
        StoreService dxyService = NetManager.getInstance().createString(StoreService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getOrderUrl())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        ServiceAPI.Order_Url = s;
                    }
                });
    }

    /**
     * 获取商城购物车地址
     */
    private void getShoppingAddress() {
        StoreService dxyService = NetManager.getInstance().createString(StoreService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getShoppingAddress())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        ServiceAPI.Shopping_Address = s;
                    }
                });
    }

    /**
     * 判断本地是否有之前保存的心率数据：有则上传
     */
    public void uploadHistoryData() {
        RxCache.getDefault().<List<HeartRateBean.AthlList>>load(Key.CACHE_ATHL_RECORD, new TypeToken<List<HeartRateBean.AthlList>>() {
        }.getType())
                .map(new CacheResult.MapFunc<List<HeartRateBean.AthlList>>())
                .subscribe(new RxSubscriber<List<HeartRateBean.AthlList>>() {
                    @Override
                    protected void _onNext(List<HeartRateBean.AthlList> athlLists) {
                        if (athlLists != null && !athlLists.isEmpty()) {
                            HeartRateBean mHeartRateBean = new HeartRateBean();
                            mHeartRateBean.setAthlList(athlLists);
                            mHeartRateBean.saveHeartRate(mHeartRateBean);

                        }
                    }
                });
    }
}
