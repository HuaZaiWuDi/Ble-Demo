package lab.wesmartclothing.wefit.flyso.ui.guide;

import android.Manifest;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.UpdateAppBean;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity_;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second.BaseHeatActivity;
import lab.wesmartclothing.wefit.flyso.ui.userinfo.UserInfoActivity;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.net.StoreService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

@EActivity(R.layout.activity_spalsh)
public class SpalshActivity extends BaseActivity {


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

        RxActivityUtils.skipActivityAndFinish(this, BaseHeatActivity.class);

//        //测试账号
        NetManager.getInstance().setUserIdToken(SPUtils.getString(SPKey.SP_UserId), SPUtils.getString(SPKey.SP_token));
        RxLogUtils.e("用户ID：" + SPUtils.getString(SPKey.SP_UserId));
//        initUserInfo();
        initData();

//        Bundle bundle = new Bundle();
//        bundle.putString(Key.BUNDLE_FRAGMENT, WeightAddFragment.class.getSimpleName());
//        RxActivityUtils.skipActivity(mActivity, BaseFragmentActivity.class, bundle);

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
        //通过验证是否保存userId来判断是否登录
        if ("".equals(SPUtils.getString(SPKey.SP_UserId))) {
            RxActivityUtils.skipActivityAndFinish(mActivity, LoginRegisterActivity.class);
        } else if (isSaveUserInfo)//
            RxActivityUtils.skipActivityAndFinish(mActivity, UserInfoActivity.class);
        else
            RxActivityUtils.skipActivityAndFinish(mActivity, MainActivity_.class);
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
        super.onDestroy();
    }


    //重传在无网络情况下未上传的数据
    private void initData() {
        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            //没有网络直接返回
            return;
        }
        getStoreAddr();
        getOrderUrl();
        getShoppingAddress();
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


    //不退出app，而是隐藏当前的app
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
        super.onBackPressed();
    }

}
