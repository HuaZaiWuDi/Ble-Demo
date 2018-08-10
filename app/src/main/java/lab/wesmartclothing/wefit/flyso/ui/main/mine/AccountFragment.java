package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import me.shaohui.shareutil.LoginUtil;
import me.shaohui.shareutil.login.LoginListener;
import me.shaohui.shareutil.login.LoginPlatform;
import me.shaohui.shareutil.login.LoginResult;

/**
 * Created by jk on 2018/8/9.
 */
public class AccountFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    Unbinder unbinder;

    private QMUICommonListItemView wechatItem, QQItem, weiboItem;
    private RxDialogSureCancel dialog;
    private boolean wechatIsBind, QQIsBind, weiboIsBind;
    private SwitchBindListener switchBindListener;

    public static QMUIFragment getInstance() {
        return new AccountFragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_account, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        groupList();
        initMyDialog();
    }

    private void initMyDialog() {
        dialog = new RxDialogSureCancel(mActivity);
        dialog.getTvContent().setText("解绑后将不能作为登录方式，确定解除微信账号绑定么？");
        dialog.setCancel("解除绑定");
        dialog.setSure("取消");
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void setItemView(QMUICommonListItemView item) {
        item.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        item.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));
    }

    private void groupList() {
        QMUICommonListItemView phoneItem = mGroupListView.createItemView("手机号");
        phoneItem.setImageDrawable(getResources().getDrawable(R.mipmap.icon_phone_number));
        phoneItem.setDetailText("17665248850");
        phoneItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(phoneItem);

        final ImageView view = new ImageView(mContext);


        wechatItem = mGroupListView.createItemView("微信");
        wechatItem.setImageDrawable(getResources().getDrawable(R.mipmap.icon_login_wechat));
        wechatItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        wechatItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        wechatItem.setDetailText("");
        view.setImageResource(wechatIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
        wechatItem.addAccessoryCustomView(view);
        setItemView(wechatItem);

        QQItem = mGroupListView.createItemView("QQ");
        QQItem.setImageDrawable(getResources().getDrawable(R.mipmap.icon_login_qq));
        QQItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        QQItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        QQItem.setDetailText("");
        view.setImageResource(QQIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
        wechatItem.addAccessoryCustomView(view);
        setItemView(QQItem);

        weiboItem = mGroupListView.createItemView("微博");
        weiboItem.setImageDrawable(getResources().getDrawable(R.mipmap.icon_login_weibo));
        weiboItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        weiboItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        weiboItem.setDetailText("");
        view.setImageResource(weiboIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
        wechatItem.addAccessoryCustomView(view);
        setItemView(weiboItem);

        QMUIGroupListView.newSection(mContext)
                .addItemView(phoneItem, null)
                .setUseTitleViewForSectionSpace(false)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(mContext)
                .setTitle("可绑定登录的第三方账号")
                .addItemView(wechatItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchBind(wechatIsBind, Key.LoginType_WEXIN, new SwitchBindListener() {
                            @Override
                            public void complete(String result) {
                                wechatIsBind = !wechatIsBind;
                                view.setImageResource(wechatIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                                wechatItem.addAccessoryCustomView(view);
                            }
                        });
                    }
                })
                .addItemView(QQItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchBind(QQIsBind, Key.LoginType_QQ, new SwitchBindListener() {
                            @Override
                            public void complete(String result) {
                                QQIsBind = !QQIsBind;
                                view.setImageResource(QQIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                                wechatItem.addAccessoryCustomView(view);
                            }
                        });
                    }
                })
                .addItemView(weiboItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchBind(weiboIsBind, Key.LoginType_WEIBO, new SwitchBindListener() {
                            @Override
                            public void complete(String result) {
                                weiboIsBind = !weiboIsBind;
                                view.setImageResource(weiboIsBind ? R.mipmap.icon_bind : R.mipmap.icon_unbind);
                                wechatItem.addAccessoryCustomView(view);
                            }
                        });
                    }
                })
                .addTo(mGroupListView);
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("账号管理");
    }


    private void switchBind(boolean isBind, final String type, SwitchBindListener switchBindListener) {
        this.switchBindListener = switchBindListener;
        if (isBind) {
            //解绑
            dialog.setSureListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    switch (type) {
                        case Key.LoginType_WEXIN:

                            break;
                        case Key.LoginType_QQ:
                            break;
                        case Key.LoginType_WEIBO:
                            break;
                    }
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

    public interface SwitchBindListener {
        void complete(String result);
    }


    final LoginListener listener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过
            RxLogUtils.e("登录成功:" + result.toString());
            if (switchBindListener != null) {
                switchBindListener.complete(result.getUserInfo().getNickname());
            }
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


}
