package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.SaveUserInfo;
import lab.wesmartclothing.wefit.flyso.entity.UpdateAppBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.login.UserInfoActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity_;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.net.StoreService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_spalsh)
public class SpalshActivity extends BaseActivity {


    private Handler mHandler = new Handler();
    private Disposable subscribe;
    private boolean isSaveUserInfo = false;


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
            updateAppBean.setMacAddr(RxDeviceUtils.getAndroidId());//AndroidID);
            updateAppBean.addDeviceVersion(updateAppBean);
        }
    }


    @Override
    @AfterViews
    public void initView() {

//        RxActivityUtils.skipActivityAndFinish(this, UserInfoActivity.class);

//        //测试账号
        NetManager.getInstance().setUserIdToken(SPUtils.getString(SPKey.SP_UserId), SPUtils.getString(SPKey.SP_token));


        initUserInfo();
        initData();

    }

    private void initUserInfo() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.userInfo())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        initPromissions();
                    }
                })
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
                            if (!isSaveUserInfo) {//判断性别是否为0来判断是否录入个人信息

                                SPUtils.put(SPKey.SP_birthDayMillis, Long.parseLong(birthday));
                                SPUtils.put(SPKey.SP_weight, targetWeight);
                                SPUtils.put(SPKey.SP_height, height);
                                SPUtils.put(SPKey.SP_sex, sex);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void gotoMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //通过验证是否保存userId来判断是否登录
                if ("".equals(SPUtils.getString(SPKey.SP_UserId))) {
                    RxActivityUtils.skipActivityAndFinish(mActivity, LoginRegisterActivity.class);
                } else if (isSaveUserInfo)//
                    RxActivityUtils.skipActivityAndFinish(mActivity, UserInfoActivity.class);
                else
                    RxActivityUtils.skipActivityAndFinish(mActivity, MainActivity_.class);
            }
        }, 500);
    }


    private void initPromissions() {
        subscribe = new RxPermissions(this)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (!permission.granted) {
                            RxLogUtils.d("没有给定位权限");
                        }
                        gotoMain();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null)
            subscribe.dispose();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }


    //重传在无网络情况下未上传的数据
    private void initData() {
        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            //没有网络直接返回
            return;
        }
        saveUserInfo();
        getStoreAddr();
        getOrderUrl();
        getShoppingAddress();
    }


    //重传用户信息
    private void saveUserInfo() {
        SaveUserInfo mUserInfo = (SaveUserInfo) MyAPP.getACache().getAsObject(Key.CACHE_USER_INFO);
        if (mUserInfo == null) return;
        else RxLogUtils.e("未上传的用户信息：" + mUserInfo.toString());
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
                });
    }

    //获取商城订单地址
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

    //获取商城购物车地址
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


}
