package lab.wesmartclothing.wefit.flyso.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.module_wefit.bean.Device;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.ui.login.UserInfoActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.MainActivity_;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

/**
 * Created by jk on 2018/7/5.
 */

public class LoginSuccessUtils {

    private Context mContext;

    public LoginSuccessUtils(Context context, String s) {
        mContext = context;

        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(s);
        String userId = object.get("userId").getAsString();
        String token = object.get("token").getAsString();
        SPUtils.put(SPKey.SP_UserId, userId);
        SPUtils.put(SPKey.SP_token, token);
        NetManager.getInstance().setUserIdToken(userId, token);

        getUserDevice();
        initUserInfo();
    }


    //获取设备列表
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

                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(s);
                        int sex = object.get("sex").getAsInt();
                        int height = object.get("height").getAsInt();
                        int targetWeight = object.get("targetWeight").getAsInt();
                        String birthday;
                        JsonElement jsonElement = object.get("birthday");
                        if (!jsonElement.isJsonNull()) {
                            birthday = jsonElement.getAsString();
                        } else
                            birthday = "631233300000";
                        if (sex == 0) {
                            RxActivityUtils.skipActivityAndFinish(mContext, UserInfoActivity.class);
                        } else {
                            SPUtils.put(SPKey.SP_birthDayMillis, Long.parseLong(birthday));
                            SPUtils.put(SPKey.SP_weight, targetWeight);
                            SPUtils.put(SPKey.SP_height, height);
                            SPUtils.put(SPKey.SP_sex, sex);

                            RxActivityUtils.skipActivityAndFinishAll(mContext, MainActivity_.class);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);
                    }
                });
    }


}
