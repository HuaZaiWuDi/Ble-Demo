package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.DeviceLink;
import lab.wesmartclothing.wefit.flyso.entity.SaveUserInfo;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.netserivce.ServiceAPI;
import lab.wesmartclothing.wefit.flyso.netserivce.StoreService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginActivity_;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity_;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_spalsh)
public class SpalshActivity extends BaseActivity {


    private Handler mHandler = new Handler();
    private Disposable subscribe;
    private boolean isSaveUserInfo = false;

    @Pref
    Prefs_ mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_spalsh);

    }

    @Override
    @AfterViews
    public void initView() {
        initPromissions();


        //测试账号
        mPrefs.UserId().put("testuser");
        mPrefs.clothingBind().put(true);
        mPrefs.scaleIsBind().put(true);
        NetManager.getInstance().setUserIdToken(mPrefs.UserId().get(), mPrefs.token().get());
        initDevice();

        initUserInfo();
        initData();
    }

    private void initUserInfo() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.userInfo())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取用户信息：" + s);
                        try {
                            JSONObject object = new JSONObject(s);
                            int sex = object.getInt("sex");
                            int height = object.getInt("height");
                            int targetWeight = object.getInt("targetWeight");
                            String birthday = object.getString("birthday");
                            isSaveUserInfo = sex == 0;
                            if (!isSaveUserInfo) {
                                mPrefs.birthDayMillis().put(Long.parseLong(birthday));
                                mPrefs.weight().put(targetWeight);
                                mPrefs.height().put(height);
                                mPrefs.sex().put(sex);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void _onError(String error) {
//                        RxToast.error(error);
                    }
                });
    }

    //数据统计接口
    private void initDevice() {
        DeviceLink deviceLink = new DeviceLink();
        deviceLink.setMacAddr(RxDeviceUtils.getAndroidId(this.getApplicationContext()));//AndroidID
//        deviceLink.setDeviceName(RxDeviceUtils.getBuildMANUFACTURER());//设备厂商名字，如：小米
        deviceLink.setDeviceName(getString(R.string.scale));//测试数据
        deviceLink.setLinkStatus("".equals(mPrefs.UserId().get()) ? 0 : 1);
        deviceLink.deviceLink(deviceLink);
    }

    private void gotoMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //通过验证是否保存userId来判断是否登录
                if ("".equals(mPrefs.UserId().get())) {
                    RxActivityUtils.skipActivityAndFinish(SpalshActivity.this, LoginActivity_.class);
                } else if (isSaveUserInfo)
                    RxActivityUtils.skipActivityAndFinish(SpalshActivity.this, AddDeviceActivity_.class);
                else
                    RxActivityUtils.skipActivityAndFinish(SpalshActivity.this, MainActivity_.class);
            }
        }, 500);
    }


    private void initPromissions() {
        subscribe = new RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            RxLogUtils.d("没有给定位权限");
                        }
                        gotoMain();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.dispose();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }


    //重传在无网络情况下未上传的数据
    private void initData() {
        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            //没有网络直接返回
            return;
        }
        saveUserInfo();
        getStoreAddr();
    }


    //重传用户信息
    private void saveUserInfo() {
        SaveUserInfo mUserInfo = (SaveUserInfo) MyAPP.getACache().getAsObject(Key.CACHE_USER_INFO);
        if (mUserInfo == null) return;
        String s = new Gson().toJson(mUserInfo, SaveUserInfo.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.saveUserInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        MyAPP.getACache().remove(Key.CACHE_USER_INFO);
                    }

                    @Override
                    protected void _onError(String error) {
//                        RxToast.error(error);
                    }
                });
    }


    //获取商城地址
    private void getStoreAddr() {
        StoreService dxyService = NetManager.getInstance().createString(StoreService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getMallAddress())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        ServiceAPI.Store_Addr = s;
                    }

                    @Override
                    protected void _onError(String error) {
                        //这里不做处理
                    }
                });
    }


}
