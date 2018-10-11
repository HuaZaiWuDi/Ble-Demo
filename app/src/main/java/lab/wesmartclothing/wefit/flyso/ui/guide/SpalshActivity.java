package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.content.Intent;
import android.os.Environment;

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
import com.zchu.rxcache.diskconverter.SerializableDiskConverter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.HeartRateBean;
import lab.wesmartclothing.wefit.flyso.entity.UpdateAppBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.UserInfoActivity;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateToKcal;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.jpush.JPushUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.net.StoreService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

@EActivity(R.layout.activity_spalsh)
public class SpalshActivity extends BaseActivity {


    private boolean isSaveUserInfo = false;

    @Bean
    HeartRateBean mHeartRateBean;
    @Bean
    HeartRateToKcal mHeartRateToKcal;

    @Receiver(actions = Intent.ACTION_PACKAGE_REPLACED)
    void replaced(Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        if (this.getPackageName().equals(packageName)) {
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
    }

    @AfterViews
    public void initView() {
        RxLogUtils.i("启动时长：引导页开始");
        JPushUtils.init(getApplication());
        String baseUrl = SPUtils.getString(SPKey.SP_BSER_URL);
        if (!RxDataUtils.isNullString(baseUrl)) {
            ServiceAPI.switchURL(baseUrl);
        }


//        //TODO 切换下网络请求框架的设置，现在是手动解析的，之后改为GSON工厂配置，这样能减少因为后台问题导致的崩溃问题
//        RxActivityUtils.skipActivityAndFinish(mContext, MainActivity.class);

        NetManager.getInstance().setUserIdToken(SPUtils.getString(SPKey.SP_UserId), SPUtils.getString(SPKey.SP_token));
        RxLogUtils.e("用户ID：" + SPUtils.getString(SPKey.SP_UserId));
        initUserInfo();
        initData();

    }

    private void initRxCache() {
        RxLogUtils.e("申请权限");
        RxCache.initializeDefault(new RxCache.Builder()
                .appVersion(2)
                .diskDir(new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "Timetofit-cache"))
                .diskConverter(new SerializableDiskConverter())
                .diskMax((20 * 1024 * 1024))
                .memoryMax((20 * 1024 * 1024))
                .setDebug(true)
                .build());
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
        initRxCache();
        //通过验证是否保存userId来判断是否登录
        if ("".equals(SPUtils.getString(SPKey.SP_UserId))) {
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
    @Background
    public void uploadHistoryData() {
        String value = SPUtils.getString(Key.CACHE_ATHL_RECORD);
        RxLogUtils.i("保存本地的心率数据：" + value);
        if (!RxDataUtils.isNullString(value)) {
            List<HeartRateBean.AthlList> lists = MyAPP.getGson().fromJson(value, new TypeToken<List<HeartRateBean.AthlList>>() {
            }.getType());
            RxLogUtils.i("保存本地的心率数据：" + lists.size());
            if (lists != null && lists.size() > 0) {
                mHeartRateBean.setAthlList(lists);
                mHeartRateBean.saveHeartRate(mHeartRateBean, mHeartRateToKcal);
            }
        }
    }
}
