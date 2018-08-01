package lab.wesmartclothing.wefit.flyso.utils;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;

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

        initUserInfo();
    }


    //获取用户信息
    private void initUserInfo() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.userInfo())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("获取用户信息：" + s);
                        SPUtils.put(SPKey.SP_UserInfo, s);

                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(s);
                        int sex = object.get("sex").getAsInt();
//                        int height = object.get("height").getAsInt();
//                        int targetWeight = object.get("targetWeight").getAsInt();
//                        String birthday;
//                        JsonElement jsonElement = object.get("birthday");
//                        if (!jsonElement.isJsonNull()) {
//                            birthday = jsonElement.getAsString();
//                        } else
//                            birthday = "631233300000";

                        String clothesMacAddr = object.get("clothesMacAddr").getAsString();
                        String scalesMacAddr = object.get("scalesMacAddr").getAsString();
                        SPUtils.put(SPKey.SP_scaleMAC, scalesMacAddr);
                        SPUtils.put(SPKey.SP_clothingMAC, clothesMacAddr);

                        if (sex == 0) {
                            RxActivityUtils.skipActivityAndFinishAll(mContext, UserInfoActivity.class);
                        } else {
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
