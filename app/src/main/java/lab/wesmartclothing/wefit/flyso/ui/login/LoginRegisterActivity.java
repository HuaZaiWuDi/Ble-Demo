package lab.wesmartclothing.wefit.flyso.ui.login;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBarUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxEncryptUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.UnScrollableViewPager;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.rxbus.PasswordLoginBus;
import lab.wesmartclothing.wefit.flyso.rxbus.VCodeBus;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.LoginSuccessUtils;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import lab.wesmartclothing.wefit.netlib.utils.RxSubscriber;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;

public class LoginRegisterActivity extends BaseActivity {

    @BindView(R.id.mCommonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.mViewPager)
    UnScrollableViewPager mViewPager;
    @BindView(R.id.btn_login)
    QMUIRoundButton btn_login;
    @BindView(R.id.img_wexin)
    ImageView mImgWexin;
    @BindView(R.id.img_qq)
    ImageView mImgQq;
    @BindView(R.id.img_weibo)
    ImageView mImgWeibo;
    @BindView(R.id.v_emptyLayout)
    View mVEmptyLayout;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private int softHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.white))
                .setLightStatusBar(false)
                .process();
        initView();
    }


    @OnClick(R.id.img_wexin)
    void img_wexin() {
        tipDialog.show("正在登陆", 3000);
        LoginUtil.login(mActivity, LoginPlatform.WX, listener, true);
    }

    @OnClick(R.id.img_qq)
    void img_qq() {
        tipDialog.show("正在登陆", 3000);
        LoginUtil.login(mActivity, LoginPlatform.QQ, listener, true);
    }

    @OnClick(R.id.img_weibo)
    void img_weibo() {
        tipDialog.show("正在登陆", 3000);
        LoginUtil.login(mActivity, LoginPlatform.WEIBO, listener, true);
    }


    final LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过
            RxLogUtils.e("登录成功:" + result.toString());
            RxToast.normal("登录成功：" + result.getToken().getOpenid(), 100000);
            loginOther(result);
        }

        @Override
        public void loginFailure(Exception e) {
            RxLogUtils.e("登录失败");
            RxToast.error("登录失败");
        }

        @Override
        public void loginCancel() {
            RxLogUtils.e("登录取消");
            RxToast.normal("登录取消");
        }
    };


    @Override
    protected void onStop() {
        tipDialog.dismiss();
        super.onStop();
    }

    public void initView() {
        initTab();
        initRxBus();
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            //当键盘弹出隐藏的时候会 调用此方法。
            Rect r = new Rect();
            //获取当前界面可视部分
            mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候需要减去导航栏的高度 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;
            RxLogUtils.e("屏幕高度sheightDifference:" + heightDifference);
            if (RxBarUtils.navigationBarExist(mActivity)) {
                heightDifference = heightDifference - RxBarUtils.getDaoHangHeight(mContext);
            }
            if (heightDifference > 0) {
                softHeight = heightDifference;
            }
            if (softHeight * 1f / screenHeight * 1f > 0.40f) {
                mVEmptyLayout.setVisibility(heightDifference == 0 ? View.VISIBLE : View.GONE);
            }
        }
    };


    private void initRxBus() {
        RxBus.getInstance().register2(PasswordLoginBus.class)
                .compose(RxComposeUtils.<PasswordLoginBus>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<PasswordLoginBus>() {
                    @Override
                    protected void _onNext(PasswordLoginBus passwordLoginBus) {
                        checkRegister(passwordLoginBus.phone, passwordLoginBus.password);
                    }
                });
        RxBus.getInstance().register2(VCodeBus.class)
                .compose(RxComposeUtils.<VCodeBus>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<VCodeBus>() {
                    @Override
                    protected void _onNext(VCodeBus vCodeBus) {
                        checkRegister(vCodeBus.phone, vCodeBus.vCode);
                    }
                });
    }


    private void checkRegister(final String phone, final String p2) {
        if (RxRegUtils.isMobileExact(phone) && !RxDataUtils.isNullString(p2)) {
            ((QMUIRoundButtonDrawable) btn_login.getBackground()).setColor(getResources().getColor(R.color.red));
            btn_login.setEnabled(true);
        } else {
            ((QMUIRoundButtonDrawable) btn_login.getBackground()).setColor(getResources().getColor(R.color.BrightGray));
            btn_login.setEnabled(false);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommonTabLayout.getCurrentTab() == 0) {
                    loginPassword(phone, p2);
                } else {
                    loginVCode(phone, p2);
                }
            }
        });
    }

    private void initTab() {
        mFragments.clear();
        mFragments.add(PasswordLoginFragment.getInstance());
        mFragments.add(VCodeLoginFragment.getInstance());

        mViewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        mBottomTabItems.add(new BottomTabItem("密码登录"));
        mBottomTabItems.add(new BottomTabItem("验证码登录"));
        mCommonTabLayout.setTabData(mBottomTabItems);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    private void loginVCode(String phone, String code) {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(code)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.login(phone, code))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.success("登录成功");
                        RxLogUtils.d("结束：" + s);
                        new LoginSuccessUtils(mContext, s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private void loginPassword(String phone, String password) {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isPassword(password)) {
            RxToast.warning(getString(R.string.passwordError));
            return;
        }
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.pwdLogin(phone, RxEncryptUtils.encryptMD5ToString(password)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.success("登录成功");
                        new LoginSuccessUtils(mContext, s);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        if (code == 10021) {//未设置登录密码
                            showDialog2settingPassword();
                        } else {
                            RxToast.error(error);
                        }
                    }
                });
    }

    private void showDialog2Register() {
        RxDialogSure dialog = new RxDialogSure(mActivity)
                .setTitle("手机号未注册")
                .setSure("立即注册")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入设置注册流程，跳转注册界面
                        RxActivityUtils.skipActivity(mActivity, RegisterActivity.class);
                    }
                });
        dialog.show();
    }

    private void showDialog2settingPassword() {
        RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity)
                .setCancel("设置密码")
                .setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入设置密码流程，跳转验证手机号
                        RxActivityUtils.skipActivity(mActivity, VerificationPhoneActivity.class);
                    }
                })
                .setSure("验证码登录")
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommonTabLayout.setCurrentTab(1);
                    }
                })
                .setContent("该手机号还未设置密码");
        dialog.getTvTitle().setVisibility(View.GONE);
        TextView tvCancel = dialog.getTvCancel();
        TextView tvSure = dialog.getTvSure();
        tvCancel.setBackgroundColor(getResources().getColor(R.color.red));
        tvCancel.setTextColor(getResources().getColor(R.color.white));
        tvSure.setBackgroundColor(getResources().getColor(R.color.BrightGray));
        tvSure.setTextColor(getResources().getColor(R.color.white));
        dialog.show();
    }


    private void loginOther(final LoginResult result) {
        String openId = result.getToken().getOpenid();
        String nickname = result.getUserInfo().getNickname();
        String imageUrl = result.getUserInfo().getHeadImageUrl();
        String userType = result.getPlatform() == LoginPlatform.QQ ? Key.LoginType_QQ :
                result.getPlatform() == LoginPlatform.WEIBO ? Key.LoginType_WEIBO : Key.LoginType_WEXIN;
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.outerLogin(openId, nickname, imageUrl, userType))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(s);
                        if (object.has("success")) {
                            boolean success = object.get("success").getAsBoolean();
                            if (!success) {
                                //!success意味着没有绑定手机号码需要跳转到绑定手机号界面
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Key.BUNDLE_OTHER_LOGIN_INFO, result);
                                RxActivityUtils.skipActivity(mContext, BindPhoneActivity.class, bundle);
                            } else
                                new LoginSuccessUtils(mContext, s);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);
                    }
                });
    }


    //不退出app，而是隐藏当前的app
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        super.onDestroy();
    }
}
