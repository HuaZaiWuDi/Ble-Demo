package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.blelibrary.BleKey;
import com.smartclothing.module_wefit.activity.AboutActivity;
import com.smartclothing.module_wefit.activity.CollectActivity;
import com.smartclothing.module_wefit.activity.MessageActivity;
import com.smartclothing.module_wefit.activity.MyDeviceActivity;
import com.smartclothing.module_wefit.activity.PersonalDataActivity;
import com.smartclothing.module_wefit.activity.ProblemSuggestActivity;
import com.smartclothing.module_wefit.activity.SetActivity;
import com.smartclothing.module_wefit.bean.UserCenterBean;
import com.smartclothing.module_wefit.bean.UserInfo;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.BleService;
import lab.wesmartclothing.wefit.flyso.rxbus.SlimmingTab;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.ui.WebTitleActivity;
import lab.wesmartclothing.wefit.flyso.ui.login.AddDeviceActivity_;
import lab.wesmartclothing.wefit.flyso.ui.login.LoginRegisterActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.CollectWebActivity;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.TempActivity_;
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightFragment;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.net.ServiceAPI;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;

/**
 * Created icon_hide_password jk on 2018/5/7.
 */
@EFragment()
public class MineFragment extends BaseAcFragment {

    public static MineFragment getInstance() {
        return new MineFragment_();
    }


    @ViewById
    LinearLayout iv_mine_set;
    @ViewById
    LinearLayout ll_iv_mine_msg;
    @ViewById
    LinearLayout ll_diet_record;
    @ViewById
    LinearLayout ll_sport_record;
    @ViewById
    LinearLayout ll_weight_record;
    @ViewById
    RelativeLayout rl_my_collect;
    @ViewById
    RelativeLayout rl_my_order;
    @ViewById
    RelativeLayout rl_my_shop_car;
    @ViewById
    RelativeLayout rl_my_device;
    @ViewById
    RelativeLayout rl_my_problem_suggest;
    @ViewById
    RelativeLayout rl_my_about;
    @ViewById
    ImageView iv_mine_icon;
    @ViewById
    ImageView iv_mine_msg;
    @ViewById
    TextView user_name;
    @ViewById
    TextView tv_collect_num;
    @ViewById
    TextView tv_device_num;
    @ViewById
    TextView user_slogan;
    @ViewById
    View icon_unread;

//    @Pref
//    Prefs_ mPrefs;


    @Click
    void iv_mine_set() {
        if (!RxUtils.isFastClick(1000))
            RxActivityUtils.skipActivity(mActivity, SetActivity.class);
    }

    @Click
    void ll_iv_mine_msg() {
        if (!RxUtils.isFastClick(1000))
            RxActivityUtils.skipActivity(mActivity, MessageActivity.class);
    }

    @Click
    void ll_diet_record() {
        //饮食记录
        RxBus.getInstance().post(new SlimmingTab(0));
    }

    @Click
    void ll_sport_record() {
        //运动记录
        RxBus.getInstance().post(new SlimmingTab(2));
    }

    @Click
    void ll_weight_record() {
        //体重记录
        RxBus.getInstance().post(new SlimmingTab(1));
    }

    @Click
    void rl_my_collect() {
        //我的收藏
        RxActivityUtils.skipActivity(mActivity, CollectActivity.class);
    }

    @Click
    void rl_my_order() {
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Order_Url);
        bundle.putString(Key.BUNDLE_TITLE, getString(R.string.myOrder));
        RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
    }

    @Click
    void rl_my_shop_car() {
        //我的购物车
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Shopping_Address);
        bundle.putString(Key.BUNDLE_TITLE, getString(R.string.shopping));
        RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
    }

    @Click
    void rl_my_device() {
        //我的设备
        Bundle bundle = new Bundle();
        bundle.putBoolean(Key.ACTION_SCALE_CONNECT, WeightFragment.isConnect);
        RxActivityUtils.skipActivity(mActivity, MyDeviceActivity.class, bundle);

    }

    @Click
    void rl_my_problem_suggest() {
        RxActivityUtils.skipActivity(mActivity, ProblemSuggestActivity.class);
        //问题反馈
    }

    @Click
    void rl_my_about() {
        //我的设备
        Bundle bundle = new Bundle();
        bundle.putString(BleKey.FIRMWARE_VERSION, BleService.firmwareVersion);
        RxActivityUtils.skipActivity(mActivity, AboutActivity.class, bundle);
        //关于我的
    }

    @Click
    void iv_mine_icon() {
        RxActivityUtils.skipActivity(mActivity, PersonalDataActivity.class);
        //我的图标
    }


    @AfterViews
    public void initView() {
        initBus();
    }

    @Override
    public void onStart() {
        super.onStart();
        initMineData();
    }

    private void initBus() {
        Disposable register = RxBus.getInstance().register(UserInfo.class, new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {
                initMineData();
            }
        });
        Disposable web = RxBus.getInstance().register(String.class, new Consumer<String>() {
            @Override
            public void accept(String device) throws Exception {
                if (Key.BUNDLE_WEB_URL.equals(device)) {
                    //服务协议
                    Bundle bundle = new Bundle();
                    bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Term_Service);
                    bundle.putString(Key.BUNDLE_TITLE, getString(R.string.ServiceAgreement));
                    RxActivityUtils.skipActivity(mActivity, WebTitleActivity.class, bundle);
                } else if ("logout".equals(device)) {
//                    mPrefs.clear();
                    SPUtils.clear();
                    MyAPP.getACache().clear();
                    MyAPP.getRxCache().clear2();
                    RxActivityUtils.skipActivityAndFinishAll(mActivity, LoginRegisterActivity.class);
                    RxActivityUtils.finishActivity(SetActivity.class);
                } else if (BleKey.TYPE_SCALE.equals(device) || BleKey.TYPE_CLOTHING.equals(device)) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Key.BUNDLE_FORCE_BIND, true);
                    bundle.putString(Key.BUNDLE_BIND_TYPE, device);
                    RxActivityUtils.skipActivity(mActivity, AddDeviceActivity_.class, bundle);
                } else if ("temp".equals(device)) {
                    RxActivityUtils.skipActivity(mActivity, TempActivity_.class);
                }
            }
        });

        Disposable Collect = RxBus.getInstance().register(com.smartclothing.module_wefit.bean.Collect.class, new Consumer<com.smartclothing.module_wefit.bean.Collect>() {
            @Override
            public void accept(com.smartclothing.module_wefit.bean.Collect device) throws Exception {
                //我的资讯
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_WEB_URL, ServiceAPI.Detail + device.getArticleId() + "&isgo=1");
                RxActivityUtils.skipActivity(mActivity, CollectWebActivity.class, bundle);
            }
        });

        RxBus.getInstance().addSubscription(this, Collect, register, web);
    }


    private void initMineData() {
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.userCenter())
                .compose(MyAPP.getRxCache().<String>transformObservable("userCenter", String.class, CacheStrategy.cacheAndRemote()))
                .map(new CacheResult.MapFunc<String>())
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        Gson gson = new Gson();
                        UserCenterBean user = gson.fromJson(s,
                                new TypeToken<UserCenterBean>() {
                                }.getType());
                        user_name.setText(user.getUserName());
                        tv_collect_num.setText(user.getCollectCount() + "");
                        tv_collect_num.setVisibility(user.getCollectCount() == 0 ? View.GONE : View.VISIBLE);
                        tv_device_num.setText(user.getBindCount() + "");
                        tv_device_num.setVisibility(user.getBindCount() == 0 ? View.GONE : View.VISIBLE);
                        Glide.with(mActivity)
                                .load(user.getImgUrl())
                                .placeholder(R.mipmap.userimg)
                                .bitmapTransform(new CropCircleTransformation(mActivity))//圆角图片
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(iv_mine_icon);//.placeholder(R.mipmap.img_touxiang)
                        //更新消息未读状态
                        if (user.getUnreadCount() == 0) {
                            icon_unread.setVisibility(View.GONE);
                        } else {
                            icon_unread.setVisibility(View.VISIBLE);
                        }
                        user_slogan.setText(user.getSignature());
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
        super.onDestroy();
    }

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_mine, null);
        return rootView;
    }
}
