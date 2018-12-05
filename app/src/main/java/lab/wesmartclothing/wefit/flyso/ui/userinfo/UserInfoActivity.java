package lab.wesmartclothing.wefit.flyso.ui.userinfo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogGPSCheck;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.picker.CustomDatePicker;
import lab.wesmartclothing.wefit.flyso.view.picker.CustomNumberPicker;

public class UserInfoActivity extends BaseALocationActivity {


    UserInfo mUserInfo;

    @BindView(R.id.tv_titleTop)
    TextView tv_titleTop;
    @BindView(R.id.mCommonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.tv_location)
    QMUIRoundButton tv_location;
    @BindView(R.id.layout_location)
    LinearLayout layout_location;
    @BindView(R.id.tv_top)
    TextView tv_top;
    @BindView(R.id.tv_titleBottom)
    TextView tv_titleBottom;
    @BindView(R.id.tv_bottom)
    TextView tv_bottom;
    @BindView(R.id.btn_nextStep)
    QMUIRoundButton mBtnNextStep;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.mQMUITopBar)
    QMUITopBar mMQMUITopBar;
    @BindView(R.id.layout_bottom)
    LinearLayout mLayoutBottom;
    @BindView(R.id.layout_height)
    LinearLayout mLayoutHeight;


    @OnClick({R.id.tv_location, R.id.tv_top, R.id.tv_bottom, R.id.btn_nextStep})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                if (!RxLocationUtils.isGpsEnabled(this)) {
                    RxDialogGPSCheck rxDialogGPSCheck = new RxDialogGPSCheck(mContext);
                    rxDialogGPSCheck.show();
                    return;
                }
                tipDialog.show();
                startLocation(new MyLocationListener() {
                    @Override
                    public void location(AMapLocation aMapLocation) {
                        tipDialog.dismiss();
                        switchLoactionUI(true);
                        checkNextWay(true);
                        tv_location.setText(aMapLocation.getProvince() + "," + aMapLocation.getCity());
                        mUserInfo.setCountry(aMapLocation.getCountry());
                        mUserInfo.setProvince(aMapLocation.getProvince());
                        mUserInfo.setCity(aMapLocation.getCity());
                    }
                });
                break;
            case R.id.tv_top:
                showHeight();
                break;
            case R.id.tv_bottom:
                showDate();
                break;
            case R.id.btn_nextStep:
                if (viewState < 2) {
                    viewState++;
                    switchView(viewState);
                } else {
                    saveUserInfo(false);
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (viewState > 0) {
                viewState--;
                switchView(viewState);
            } else {
                if (!RxUtils.isFastClick(2000)) {
                    RxToast.normal("再按一次退出");
                } else moveTaskToBack(true);
            }
        return true;
    }

    private int viewState = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        StatusBarUtils.from(this)
                .setStatusBarColor(getResources().getColor(R.color.white))
                .setLightStatusBar(false)
                .process();
        initView();
    }

    public void initView() {
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        mUserInfo = MyAPP.getGson().fromJson(string, UserInfo.class);
        if (mUserInfo.getSex() == 0) mUserInfo.setSex(2);
        RxLogUtils.e("用户信息：" + mUserInfo.toString());
        initTab();
        switchView(viewState);
    }

    private void initTab() {
        ArrayList<CustomTabEntity> sexTabs = new ArrayList<>();
        sexTabs.add(new BottomTabItem(R.mipmap.icon_men_select, R.mipmap.icon_men, ""));
        sexTabs.add(new BottomTabItem(R.mipmap.icon_women_select, R.mipmap.icon_women, ""));
        mCommonTabLayout.setTabData(sexTabs);
    }


    private void switchView(int viewState) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DIN-Regular.ttf");
        tv_top.setTypeface(typeface);
        tv_bottom.setTypeface(typeface);
        mMQMUITopBar.removeAllRightViews();
        mMQMUITopBar.removeAllLeftViews();

        mCommonTabLayout.setVisibility(viewState == 0 ? View.VISIBLE : View.GONE);
        layout_location.setVisibility(viewState == 2 ? View.VISIBLE : View.GONE);
        mLayoutBottom.setVisibility(viewState == 0 ? View.VISIBLE : View.GONE);
        mLayoutHeight.setVisibility(viewState == 1 ? View.VISIBLE : View.GONE);
        checkNextWay(viewState != 2);
        switch (viewState) {
            case 0:
                initSexBirth();
                break;
            case 1:
                initWeightHeight();
                break;
            case 2:
                initLocation();
                break;
        }
    }

    private void checkNextWay(boolean isEnable) {
        ((QMUIRoundButtonDrawable) mBtnNextStep.getBackground())
                .setColor(getResources().getColor(isEnable ? R.color.red : R.color.BrightGray));
        mBtnNextStep.setEnabled(isEnable);
    }

    private void switchLoactionUI(boolean isClick) {
        QMUIRoundButtonDrawable background = (QMUIRoundButtonDrawable) tv_location.getBackground();
        background.setColor(getResources().getColor(isClick ? R.color.Gray : R.color.white));
        background.setStroke(1, getResources().getColor(isClick ? R.color.white : R.color.BrightGray));
        tv_location.setTextColor(getResources().getColor(isClick ? R.color.white : R.color.Gray));
    }


    private void initLocation() {
        checkNextWay(MyAPP.aMapLocation != null);
        Button button = mMQMUITopBar.addRightTextButton(R.string.skip, R.id.tv_skip);
        button.setTextColor(getResources().getColor(R.color.Gray));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo(true);
            }
        });
        mMQMUITopBar.addLeftImageButton(R.mipmap.icon_back, R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewState--;
                switchView(viewState);
            }
        });

        tv_info.setText(R.string.Location_about);
        tv_titleTop.setText(R.string.chooseCity);

    }

    private void initWeightHeight() {
        mMQMUITopBar.addLeftImageButton(R.mipmap.icon_back, R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewState--;
                switchView(viewState);
            }
        });

        tv_info.setText(R.string.weight_about);
        tv_titleTop.setText(R.string.chooseHeight);
        tv_titleBottom.setText(R.string.chooseWeight);
        tv_top.setText(mUserInfo.getHeight() + "cm");
    }

    private void initSexBirth() {
        tv_info.setText(R.string.info_about);
        tv_titleTop.setText(R.string.chooseSex);
        tv_titleBottom.setText(R.string.chooseBirth);
        tv_bottom.setText(RxFormat.setFormatDate(mUserInfo.getBirthday(), RxFormat.Date));
        mCommonTabLayout.setCurrentTab(mUserInfo.getSex() - 1);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //0未设置1男2女
                mUserInfo.setSex(position + 1);//1男2女
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        CustomDatePicker datePicker = new CustomDatePicker(mActivity);
        datePicker.setRangeStart(1940, 01, 01);
        datePicker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTimeInMillis(mUserInfo.getBirthday());
        datePicker.setTextColor(getResources().getColor(R.color.Gray));
        datePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);
                tv_bottom.setText(year + "-" + month + "-" + day);
                Date date = RxFormat.setParseDate(year + "-" + month + "-" + day, RxFormat.Date);
                mUserInfo.setBirthday(date.getTime());
            }
        });
        datePicker.show();
    }

    public void showHeight() {
        CustomNumberPicker picker = new CustomNumberPicker(mActivity);
        picker.setRange(120, 200, 1);//数字范围
        picker.setSelectedItem(mUserInfo.getHeight());
        picker.setLabel("cm");
        picker.setLabelTextColor(getResources().getColor(R.color.Gray));
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                RxLogUtils.d("身高：" + item);
                tv_top.setText(item + "cm");
                mUserInfo.setHeight((int) item);
            }
        });
        picker.show();

    }



    private void saveUserInfo(boolean isSkip) {
        if (isSkip) {
            mUserInfo.setCountry("");
            mUserInfo.setProvince("");
            mUserInfo.setCity("");
        }

        String s = MyAPP.getGson().toJson(mUserInfo, UserInfo.class);
        SPUtils.put(SPKey.SP_UserInfo, s);

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .saveUserInfo(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        //跳转扫描界面
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Key.BUNDLE_FORCE_BIND, false);
                        RxActivityUtils.skipActivity(mContext, AddDeviceActivity.class, bundle);
                    }

                    @Override
                    protected void _onError(String error,int code) {
                        RxToast.error(error,code);
                    }
                });
    }

}
