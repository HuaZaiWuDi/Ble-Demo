package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.LoginResult;
import lab.wesmartclothing.wefit.flyso.entity.OtherLoginBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;

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
        mTvPhone.setText(MyAPP.getgUserInfo().getPhone());
    }

    private void initMyDialog() {
        dialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent(getString(R.string.unbindOtherAccount))
                .setSure(getString(R.string.unbind));

    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle(R.string.accountManager);
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
            SHARE_MEDIA media = convertType(type);

            UMShareAPI umShareAPI = UMShareAPI.get(this);
            umShareAPI.getPlatformInfo(this, media, mUMAuthListener);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }


    @OnClick({R.id.layout_wechat, R.id.layout_QQ, R.id.layout_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_wechat:
                switchBind(wechatIsBind, Key.LoginType_WEXIN, result -> {
                    wechatIsBind = !wechatIsBind;
                    mIvWechatState.setImageResource(wechatIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                    mTvWechatName.setText(result);
                });
                break;
            case R.id.layout_QQ:
                switchBind(QQIsBind, Key.LoginType_QQ, result -> {
                    QQIsBind = !QQIsBind;
                    mIvQQState.setImageResource(QQIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                    mTvQQName.setText(result);
                });
                break;
            case R.id.layout_weibo:
                switchBind(weiboIsBind, Key.LoginType_WEIBO, result -> {
                    weiboIsBind = !weiboIsBind;
                    mIvWeiboState.setImageResource(weiboIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                    mTvWeiboName.setText(result);
                });
                break;
        }
    }

    public interface SwitchBindListener {
        void complete(String result);
    }

    private UMAuthListener mUMAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            RxLogUtils.d("开始登录");
            tipDialog.show(getString(R.string.logining), 3000);
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            RxLogUtils.d("login:onComplete: ");
            tipDialog.dismiss();
            if (RxDataUtils.isEmpty(map)) return;
            Set<String> strings = map.keySet();
            for (String s : strings) {
                RxLogUtils.d("s: " + s + "--value" + map.get(s));
            }

            bindOther(new LoginResult(map, share_media));
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            RxLogUtils.e("登录失败", throwable);
            RxToast.error(getString(R.string.loginFail));
            tipDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            RxLogUtils.e("登录取消");
            RxToast.error(getString(R.string.loginCancel));
            tipDialog.dismiss();
        }
    };

    private void bindOther(final LoginResult result) {
        if (result == null) return;
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().bindingOuterInfo(result.imageUrl,
                result.nickname, result.openId, result.userType))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        if (switchBindListener != null) {
                            switchBindListener.complete(result.nickname);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.normal(error, code);
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
                        RxToast.normal(getString(R.string.unbindSuccess), 3000);
                        RxLogUtils.d("结束" + s);
                        SHARE_MEDIA media = convertType(otherType);

                        UMShareAPI.get(mContext).deleteOauth(mActivity, media, mUMAuthListener);

                        if (switchBindListener != null) {
                            switchBindListener.complete("");
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
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
                        List<OtherLoginBean> beans = JSON.parseObject(s, new TypeToken<List<OtherLoginBean>>() {
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
                    protected void _onError(String error, int code) {
                        RxToast.normal(error, code);
                    }
                });
    }


    private SHARE_MEDIA convertType(String type) {
        SHARE_MEDIA media = SHARE_MEDIA.QQ;
        switch (type) {
            case Key.LoginType_WEXIN:
                media = SHARE_MEDIA.WEIXIN;
                break;
            case Key.LoginType_QQ:
                media = SHARE_MEDIA.QQ;
                break;
            case Key.LoginType_WEIBO:
                media = SHARE_MEDIA.SINA;
                break;
        }
        return media;
    }


}
