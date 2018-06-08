package lab.wesmartclothing.wefit.flyso.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseALocationActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.BottomTabItem;
import lab.wesmartclothing.wefit.flyso.entity.SaveUserInfo;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

@EActivity(R.layout.activity_user_info)
public class UserInfoActivity extends BaseALocationActivity {


    @ViewById
    CommonTabLayout mCommonTabLayout;
    @ViewById
    ImageView img_back;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_info;
    @ViewById
    TextView tv_titleTop;
    @ViewById
    TextView tv_titleBottom;
    @ViewById
    TextView tv_top;
    @ViewById
    TextView tv_bottom;
    @ViewById
    TextView tv_skip;
    @ViewById
    TextView tv_location;
    @ViewById
    LinearLayout layout_location;

    @Pref
    Prefs_ mPrefs;
    @Bean
    SaveUserInfo mUserInfo;

    @Click
    void img_back() {
        if (viewState > 0) {
            viewState--;
            switchView(viewState);
        } else {
            RxActivityUtils.finishActivity(this);
        }
    }

    @Click
    void btn_nextStep() {
        if (viewState < 2) {
            viewState++;
            switchView(viewState);
        } else {
            if (!isLocation) {
                final QMUITipDialog tipDialog = new QMUITipDialog.Builder(mContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord("未获取地理位置")
                        .create();
                tipDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 2000);

                return;
            }
            saveUserInfo(false);
            //跳转扫描界面
            Bundle bundle = new Bundle();
            bundle.putBoolean(Key.BUNDLE_FORCE_BIND, false);
            RxActivityUtils.skipActivity(mContext, AddDeviceActivity_.class, bundle);
        }
    }

    @Click
    void tv_skip() {
        saveUserInfo(true);
        //跳转扫描界面
        Bundle bundle = new Bundle();
        bundle.putBoolean(Key.BUNDLE_FORCE_BIND, false);
        RxActivityUtils.skipActivity(mContext, AddDeviceActivity_.class, bundle);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (viewState > 0) {
                viewState--;
                switchView(viewState);
            } else {
                RxActivityUtils.finishActivity(this);
            }
        return true;
    }

    private int viewState = 0;
    private boolean isLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕沉浸
        StatusBarUtils.from(this).setStatusBarColor(getResources().getColor(R.color.colorTheme)).process();

    }

    @Override
    @AfterViews
    public void initView() {
        initTab();
        switchView(viewState);
    }


    private void initTab() {
        ArrayList<CustomTabEntity> sexTabs = new ArrayList<>();

        sexTabs.add(new BottomTabItem(R.mipmap.icon_girl3x, R.mipmap.icon_girl_no3x, ""));
        sexTabs.add(new BottomTabItem(R.mipmap.icon_boy3x, R.mipmap.icon_boy_no3x, ""));
        mCommonTabLayout.setTabData(sexTabs);
    }


    private void switchView(int viewState) {
        tv_title.setText(viewState != 2 ? R.string.inputUserInfo : R.string.chooseCity2);
        mCommonTabLayout.setVisibility(viewState == 0 ? View.VISIBLE : View.GONE);
        tv_skip.setVisibility(viewState == 2 ? View.VISIBLE : View.GONE);
        tv_bottom.setVisibility(viewState != 2 ? View.VISIBLE : View.GONE);
        tv_titleBottom.setVisibility(viewState != 2 ? View.VISIBLE : View.GONE);
        layout_location.setVisibility(viewState == 2 ? View.VISIBLE : View.GONE);
        tv_top.setVisibility(viewState != 2 ? View.VISIBLE : View.GONE);
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

    private void initLocation() {
        tv_info.setText(R.string.Location_about);
        tv_titleTop.setText(R.string.chooseCity);

        layout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.show();
                startLocation(new MyLocationListener() {
                    @Override
                    public void location(AMapLocation aMapLocation) {
                        isLocation = true;
                        tv_location.setText(aMapLocation.getProvince() + "," + aMapLocation.getCity());
                        mUserInfo.setCountry(aMapLocation.getCountry());
                        mUserInfo.setProvince(aMapLocation.getProvince());
                        mUserInfo.setCity(aMapLocation.getCity());
                    }
                });
            }
        });
    }

    private void initWeightHeight() {
        tv_info.setText(R.string.weight_about);
        tv_titleTop.setText(R.string.chooseHeight);
        tv_titleBottom.setText(R.string.chooseWeight);
        tv_top.setVisibility(View.VISIBLE);
        tv_top.setText(mUserInfo.getHeight() + "cm");
        tv_bottom.setText(mUserInfo.getTargetWeight() + "kg");
        tv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeight();
            }
        });

        tv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeight();
            }
        });
    }

    private void initSexBirth() {
        tv_info.setText(R.string.info_about);
        tv_titleTop.setText(R.string.chooseSex);
        tv_titleBottom.setText(R.string.chooseBirth);
        tv_top.setVisibility(View.GONE);
        tv_bottom.setText(RxFormat.setFormatDate(Long.parseLong(mUserInfo.getBirthday()), RxFormat.Date));
        mCommonTabLayout.setCurrentTab(mUserInfo.getSex() - 1);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //0未设置1男2女
                mPrefs.sex().put(position);
                mUserInfo.setSex(position == 0 ? 2 : 1);//1男2女
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        tv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
    }


    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        DatePicker picker = new DatePicker(this, DateTimePicker.YEAR_MONTH_DAY);
        picker.setGravity(Gravity.BOTTOM);
        picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
        picker.setCycleDisable(false);
        picker.setDividerRatio(0.2f);
        picker.setOffset(2);//偏移量
        picker.setRangeStart(1940, 01, 01);
        picker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTimeInMillis(Long.parseLong(mUserInfo.getBirthday()));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setTextSize(21);
        picker.setLabel("-", "-", "");
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);
                tv_bottom.setText(year + "-" + month + "-" + day);
                Date date = RxFormat.setParseDate(year + "-" + month + "-" + day, RxFormat.Date);
                mPrefs.birthDayMillis().put(date.getTime());
                mUserInfo.setBirthday(date.getTime() + "");
            }
        });
        picker.show();
    }

    public void showHeight() {
        NumberPicker picker = new NumberPicker(this);
        picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
        picker.setCycleDisable(false);
        picker.setDividerRatio(0.2f);
        picker.setOffset(2);//偏移量
        picker.setRange(120, 200, 1);//数字范围
        picker.setSelectedItem(mUserInfo.getHeight());
        picker.setTextSize(21);
        picker.setLabel("cm");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                RxLogUtils.d("身高：" + item);
                tv_top.setText(item + "cm");
                mPrefs.height().put((int) item);
                mUserInfo.setHeight((int) item);
            }
        });
        picker.show();
    }

    public void showWeight() {
        NumberPicker picker = new NumberPicker(this);
        picker.setGravity(Gravity.BOTTOM);
        picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
        picker.setCycleDisable(false);
        picker.setDividerRatio(0.2f);
        picker.setOffset(2);//偏移量
        picker.setRange(35, 90, 1);//数字范围
        picker.setSelectedItem(mUserInfo.getTargetWeight());
        picker.setTextSize(21);
        picker.setLabel("kg");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                RxLogUtils.d("体重：" + item);
                tv_bottom.setText(item + "kg");
                mPrefs.weight().put((int) item);
                mUserInfo.setTargetWeight((int) item);
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
        mUserInfo.setPhone(mPrefs.phone().get());
        mUserInfo.setToken(mPrefs.token().get());
        String s = new Gson().toJson(mUserInfo, SaveUserInfo.class);

        if (!RxNetUtils.isAvailable(mContext.getApplicationContext())) {
            MyAPP.getACache().put(Key.CACHE_USER_INFO, mUserInfo);
            return;
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.saveUserInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
