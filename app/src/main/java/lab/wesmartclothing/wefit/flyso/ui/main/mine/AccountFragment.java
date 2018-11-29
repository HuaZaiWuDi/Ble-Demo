package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.OtherLoginBean;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * Created by jk on 2018/8/9.
 */
public class AccountFragment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.layout_phone)
    LinearLayout mLayoutPhone;
    @BindView(R.id.tv_wechatName)
    TextView mTvWechatName;
    @BindView(R.id.iv_wechat_state)
    ImageView mIvWechatState;
    @BindView(R.id.layout_wechat)
    RelativeLayout mLayoutWechat;
    @BindView(R.id.tv_QQName)
    TextView mTvQQName;
    @BindView(R.id.iv_QQ_state)
    ImageView mIvQQState;
    @BindView(R.id.layout_QQ)
    RelativeLayout mLayoutQQ;
    @BindView(R.id.tv_weiboName)
    TextView mTvWeiboName;
    @BindView(R.id.iv_weibo_state)
    ImageView mIvWeiboState;
    @BindView(R.id.layout_weibo)
    RelativeLayout mLayoutWeibo;

    private RxDialogSureCancel dialog;
    private boolean wechatIsBind, QQIsBind, weiboIsBind;
    private SwitchBindListener switchBindListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initTopBar();
        initMyDialog();
        otherData();
        String phone = MyAPP.getGson().fromJson(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class).getPhone();
        mTvPhone.setText(phone);
    }

    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }

    private void initMyDialog() {
        dialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent("解绑后将不能作为登录方式，确定解除微信账号绑定么？")
                .setSure("解除绑定");

    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("账号管理");
    }

    private void switchBind(boolean isBind, final String type, final SwitchBindListener switchBindListener) {
        this.switchBindListener = switchBindListener;
        if (isBind) {
            //解绑
            dialog.setSureListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unbindOther(type);
                }
            });
            dialog.show();
        } else {
            //绑定
            switch (type) {
                case Key.LoginType_WEXIN:
                    LoginUtil.login(mActivity, LoginPlatform.WX, listener, true);
                    break;
                case Key.LoginType_QQ:
                    LoginUtil.login(mActivity, LoginPlatform.QQ, listener, true);
                    break;
                case Key.LoginType_WEIBO:
                    LoginUtil.login(mActivity, LoginPlatform.WEIBO, listener, true);
                    break;
            }
        }
    }


    @OnClick({R.id.layout_wechat, R.id.layout_QQ, R.id.layout_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_wechat:
                switchBind(wechatIsBind, Key.LoginType_WEXIN, new SwitchBindListener() {
                    @Override
                    public void complete(String result) {
                        wechatIsBind = !wechatIsBind;
                        mIvWechatState.setImageResource(wechatIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                        mTvWechatName.setText(result);
                    }
                });
                break;
            case R.id.layout_QQ:
                switchBind(QQIsBind, Key.LoginType_QQ, new SwitchBindListener() {
                    @Override
                    public void complete(String result) {
                        QQIsBind = !QQIsBind;
                        mIvQQState.setImageResource(QQIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                        mTvQQName.setText(result);
                    }
                });
                break;
            case R.id.layout_weibo:
                switchBind(weiboIsBind, Key.LoginType_WEIBO, new SwitchBindListener() {
                    @Override
                    public void complete(String result) {
                        weiboIsBind = !weiboIsBind;
                        mIvWeiboState.setImageResource(weiboIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                        mTvWeiboName.setText(result);
                    }
                });
                break;
        }
    }

    public interface SwitchBindListener {
        void complete(String result);
    }


    LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过
            RxLogUtils.e("登录成功:" + result.toString());
            bindOther(result);
        }

        @Override
        public void loginFailure(Exception e) {
            RxLogUtils.e("登录失败:" + e.getMessage());
            RxToast.error("登录失败");
        }

        @Override
        public void loginCancel() {
            RxLogUtils.e("登录取消");
            RxToast.normal("登录取消");
        }
    };

    private void bindOther(final LoginResult result) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().bindingOuterInfo(result.getUserInfo().getHeadImageUrl(),
                result.getUserInfo().getNickname(), result.getToken().getOpenid(), typeTransformation(result.getPlatform())))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        if (switchBindListener != null) {
                            switchBindListener.complete(result.getUserInfo().getNickname());
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void unbindOther(final String otherType) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().unbindingOuterInfo(otherType))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        dialog.dismiss();
                        RxToast.normal("解绑成功", 3000);
                        RxLogUtils.d("结束" + s);
                        if (switchBindListener != null) {
                            switchBindListener.complete("");
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    //获取第三方登录信息
    private void otherData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().fetchBindingOuterInfo())
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        /**
                         * {
                         "outerId": "string",
                         "userName": "string",
                         "userType": "string"
                         }
                         * */
                        //类型擦除
                        List<OtherLoginBean> beans = MyAPP.getGson().fromJson(s, new TypeToken<List<OtherLoginBean>>() {
                        }.getType());

                        for (int i = 0; i < beans.size(); i++) {
                            OtherLoginBean loginBean = beans.get(i);
                            switch (loginBean.getUserType()) {
                                case Key.LoginType_WEXIN:
                                    wechatIsBind = true;
                                    mTvWechatName.setText(loginBean.getUserName());
                                    mIvWechatState.setImageResource(R.mipmap.icon_bind);
                                    break;
                                case Key.LoginType_QQ:
                                    QQIsBind = true;
                                    mTvQQName.setText(loginBean.getUserName());
                                    mIvQQState.setImageResource(R.mipmap.icon_bind);
                                    break;
                                case Key.LoginType_WEIBO:
                                    weiboIsBind = true;
                                    mTvWeiboName.setText(loginBean.getUserName());
                                    mIvWeiboState.setImageResource(R.mipmap.icon_bind);
                                    break;
                            }
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private String typeTransformation(int type) {
        String otherType = "";
        switch (type) {
            case SharePlatform.WX:
                otherType = Key.LoginType_WEXIN;
                break;
            case SharePlatform.QQ:
                otherType = Key.LoginType_QQ;
                break;
            case SharePlatform.WEIBO:
                otherType = Key.LoginType_WEIBO;
                break;
        }
        return otherType;
    }

}
