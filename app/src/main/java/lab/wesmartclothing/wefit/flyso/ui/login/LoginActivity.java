package lab.wesmartclothing.wefit.flyso.ui.login;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.module_wefit.bean.Device;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity_;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @ViewById
    Button btn_login;
    @ViewById
    EditText edit_phone;
    @ViewById
    EditText edit_VCode;
    @ViewById
    TextView tv_country;
    @ViewById
    TextView tv_about;
    @ViewById
    TextView tv_countDown;


    private String phone;
    private String VCode;


    @Click
    void tv_about() {
        //服务协议
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Term_Service);
        bundle.putString(Key.BUNDLE_TITLE, getString(R.string.ServiceAgreement));
        RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
    }


    @Click
    void btn_login() {
        VCode = edit_VCode.getText().toString();
        phone = edit_phone.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (RxDataUtils.isNullString(VCode) || VCode.length() < 4 || !RxDataUtils.isNumber(VCode)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        login(phone, VCode);
    }

    @Click
    void tv_countDown() {
        phone = edit_phone.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        getVCode(phone);
        RxUtils.countDown(tv_countDown, 60, 1, getString(R.string.getVCode));
    }

    @Override
    @AfterViews
    public void initView() {
        String info_about = getString(R.string.login_clause);
        SpannableStringBuilder builder = RxTextUtils.getBuilder(info_about)
                .setForegroundColor(getResources().getColor(R.color.colorTheme))
                .setUnderline()
                .setLength(7, info_about.length());
        tv_about.setText(builder);
    }

    private void login(String phone, String code) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.login(phone, code))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String userId = jsonObject.getString("userId");
                            String token = jsonObject.getString("token");
                            SPUtils.put(SPKey.SP_UserId, userId);
                            SPUtils.put(SPKey.SP_token, token);

                            NetManager.getInstance().setUserIdToken(userId, token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getUserDevice();
                        initUserInfo();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    //获取用户绑定信息
    private void getUserDevice() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.deviceList())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(s);
                            Type typeList = new TypeToken<List<Device>>() {
                            }.getType();
                            List<Device> beanList = gson.fromJson(jsonObject.getString("list"), typeList);
                            for (int i = 0; i < beanList.size(); i++) {
                                Device device = beanList.get(i);
                                String deviceNo = device.getDeviceNo();
                                if (BleKey.TYPE_SCALE.equals(deviceNo)) {
                                    SPUtils.put(SPKey.SP_scaleMAC, device.getMacAddr());
                                } else if (BleKey.TYPE_CLOTHING.equals(deviceNo)) {
                                    SPUtils.put(SPKey.SP_clothingMAC, device.getMacAddr());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    //获取用户信息
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
                            if (sex == 0) {
                                RxActivityUtils.skipActivity(mContext, UserInfoActivity.class);
                            } else {

                                SPUtils.put(SPKey.SP_birthDayMillis, Long.parseLong(birthday));
                                SPUtils.put(SPKey.SP_weight, targetWeight);
                                SPUtils.put(SPKey.SP_height, height);
                                SPUtils.put(SPKey.SP_sex, sex);

                                RxActivityUtils.skipActivityAndFinishAll(mContext, MainActivity_.class);
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


    private void getVCode(String phone) {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.sendCode2Login(phone))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
//                        edit_VCode.setText(s);
                        RxToast.success(getString(R.string.SMSSended));
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    /**
     * 退出app，而是隐藏当前的app
     * <p>
     * nonRoot=false→ 仅当activity为task根（即首个activity例如启动activity之类的）时才生效
     * <p>
     * nonRoot=true→ 忽略上面的限制
     */

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

}
