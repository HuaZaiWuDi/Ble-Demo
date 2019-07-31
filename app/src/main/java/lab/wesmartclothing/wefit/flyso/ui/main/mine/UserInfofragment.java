package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.dateUtils.RxTimeUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.wesmarclothing.mylibrary.net.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.ble.MyBleManager;
import lab.wesmartclothing.wefit.flyso.ble.QNBleManager;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshMe;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.view.picker.AddressPickTask;
import lab.wesmartclothing.wefit.flyso.view.picker.CustomDatePicker;
import lab.wesmartclothing.wefit.flyso.view.picker.CustomNumberPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/8/9.
 */
public class UserInfofragment extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.iv_userImg)
    QMUIRadiusImageView mIvUserImg;
    Unbinder unbinder;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.layout_userName)
    RxRelativeLayout mLayoutUserName;
    @BindView(R.id.tv_userSex)
    TextView mTvUserSex;
    @BindView(R.id.layout_userSex)
    RxRelativeLayout mLayoutUserSex;
    @BindView(R.id.tv_Birth)
    TextView mTvBirth;
    @BindView(R.id.layout_userBirth)
    RxRelativeLayout mLayoutUserBirth;
    @BindView(R.id.tv_userHeight)
    TextView mTvUserHeight;
    @BindView(R.id.layout_userHeight)
    RxRelativeLayout mLayoutUserHeight;
    @BindView(R.id.tv_userCity)
    TextView mTvUserCity;
    @BindView(R.id.layout_userCity)
    RxRelativeLayout mLayoutUserCity;
    @BindView(R.id.tv_userSign)
    TextView mTvUserSign;
    @BindView(R.id.layout_userSign)
    RxRelativeLayout mLayoutUserSign;

    private UserInfo info;
    public static final int RESULT_CODE = 500;
    public static final int REQUEST_CODE = 501;
    public static final int IMAGE_PICKER = 503;
    private boolean userImgIsChange = false;//是否需要上传头像

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_info);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initTopBar();
        initImagePicker();
        info = JSON.parseObject(SPUtils.getString(SPKey.SP_UserInfo), UserInfo.class);

        info.setChange(false);
        notifyData(info);
    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(GlideImageLoader.INSTANCE);   //设置图片加载器
        imagePicker.setShowCamera(true);//显示拍照按钮
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle(R.string.personalInfo);
        mQMUIAppBarLayout.addRightTextButton(R.string.save, R.id.btn_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (info.isChange()) {
                            if (userImgIsChange) {
                                uploadImage();
                            } else {
                                requestSaveUserInfo();
                            }
                        }
                    }
                });
    }

    private void notifyData(UserInfo info) {

        MyAPP.getImageLoader().displayImage(mActivity, info.getImgUrl(), R.mipmap.userimg, mIvUserImg);

        mTvUserName.setText(info.getUserName());
        mTvUserSex.setText(info.getSex() == 1 ? getString(R.string.man) : getString(R.string.woman));
        mTvBirth.setText(RxFormat.setFormatDate(info.getBirthday(), RxFormat.Date));
        mTvUserHeight.setText(info.getHeight() + "\tcm");
        if (!RxDataUtils.isNullString(info.getProvince()) && !RxDataUtils.isNullString(info.getCity()))
            mTvUserCity.setText(info.getProvince() + "," + info.getCity());
        if (!RxDataUtils.isNullString(info.getSignature()))
            mTvUserSign.setText(info.getSignature());
    }


    @OnClick(R.id.layout_userImg)
    public void onViewClicked() {
        Intent intent = new Intent(mContext, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                MyAPP.getImageLoader().displayImage(mActivity, images.get(0).path, R.mipmap.userimg, mIvUserImg);

                //成功后将本地图片设置到imageView中，并在退回到个人中心时，刷新贴图url
                info.setImgUrl(images.get(0).path);
                userImgIsChange = true;
            }
        } else if (resultCode == UserInfofragment.RESULT_CODE && requestCode == UserInfofragment.REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            String title = bundle.getString(Key.BUNDLE_TITLE);
            if (getString(R.string.nickname).equals(title)) {
                mTvUserName.setText(bundle.getString(Key.BUNDLE_DATA));
                info.setUserName(bundle.getString(Key.BUNDLE_DATA));
            } else {
                mTvUserSign.setText(bundle.getString(Key.BUNDLE_DATA));
                info.setSignature(bundle.getString(Key.BUNDLE_DATA));
            }
        }
    }


    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        CustomDatePicker datePicker = new CustomDatePicker(mActivity);
        datePicker.setRangeStart(1940, 01, 01);
        datePicker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTimeInMillis(info.getBirthday());
        datePicker.setTextColor(getResources().getColor(R.color.Gray));
        datePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);
                mTvBirth.setText(year + "-" + month + "-" + day);
                Date date = RxTimeUtils.string2Date(RxFormat.Date, year + "-" + month + "-" + day);
                info.setBirthday(date.getTime());
            }
        });
        datePicker.show();

    }

    private void showAddress() {
        AddressPickTask task = new AddressPickTask(mActivity);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                RxLogUtils.e(getString(R.string.initFail));
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                mTvUserCity.setText(province.getName() + "," + city.getName());
                info.setProvince(province.getName());
                info.setCity(city.getName());
            }
        });
        task.execute(RxDataUtils.isNullString(info.getProvince()) ? "" : info.getProvince(),
                RxDataUtils.isNullString(info.getCity()) ? "" : info.getCity());
    }

    public void showHeight() {
        CustomNumberPicker picker = new CustomNumberPicker(mActivity);
        picker.setTextColor(getResources().getColor(R.color.Gray));
        picker.setRange(120, 200, 1);//数字范围
        picker.setSelectedItem(info.getHeight());
        picker.setLabel("cm");
        picker.setLabelTextColor(getResources().getColor(R.color.Gray));
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                RxLogUtils.d("身高：" + item);
                mTvUserHeight.setText(item + "cm");
                info.setHeight((int) item);
            }
        });
        picker.show();
    }


    private void showSex() {
        new QMUIBottomSheet.BottomListSheetBuilder(mActivity)
                .addItem(getString(R.string.man))
                .addItem(getString(R.string.woman))
                .setCheckedIndex(info.getSex() - 1)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    mTvUserSex.setText(tag);
                    info.setSex(position + 1);
                })
                .build()
                .show();
    }

    private void uploadImage() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(RxImageUtils.compressImage(info.getImgUrl())));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", info.getImgUrl(), requestFile);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().uploadUserImg(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        requestSaveUserInfo();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    /*保存按钮，提交用户数据*/
    private void requestSaveUserInfo() {
        final String gson = JSON.toJSONString(info);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().saveUserInfo(NetManager.fetchRequest(gson)))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        RxToast.success(getString(R.string.saveSuccess), 2000);
                        SPUtils.put(SPKey.SP_UserInfo, gson);
                        MyAPP.gUserInfo = info;
                        RxActivityUtils.finishActivity();
                        QNBleManager.getInstance().disConnectDevice();
                        MyBleManager.Companion.getInstance().disConnect();
                        //刷新数据
                        RxBus.getInstance().post(new RefreshMe());
                        RxBus.getInstance().post(new RefreshSlimming());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        RxLogUtils.d("用户数据：" + info.toString());
        if (info.isChange()) {
            RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                    .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                    .setContent(getString(R.string.changedInfoAndSave))
                    .setSure(getString(R.string.save))
                    .setSureListener(v -> {
                        if (userImgIsChange) {
                            uploadImage();
                        } else {
                            requestSaveUserInfo();
                        }
                    })
                    .setCancel(getString(R.string.signOut))
                    .setCancelListener(v ->
                            RxActivityUtils.finishActivity());
            rxDialog.show();
        } else
            super.onBackPressed();
    }


    @OnClick({
            R.id.layout_userName,
            R.id.layout_userSex,
            R.id.layout_userBirth,
            R.id.layout_userHeight,
            R.id.layout_userCity,
            R.id.layout_userSign})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.layout_userName:
                bundle.putString(Key.BUNDLE_TITLE, getString(R.string.nickname));
                bundle.putString(Key.BUNDLE_DATA, info.getUserName());
                RxActivityUtils.skipActivityForResult(mActivity, EditFragment.class, bundle, UserInfofragment.REQUEST_CODE);
                break;
            case R.id.layout_userSex:
                showSex();
                break;
            case R.id.layout_userBirth:
                showDate();
                break;
            case R.id.layout_userHeight:
                showHeight();
                break;

            case R.id.layout_userCity:
                showAddress();
                break;
            case R.id.layout_userSign:
                bundle.putString(Key.BUNDLE_TITLE, getString(R.string.sign));
                bundle.putString(Key.BUNDLE_DATA, info.getSignature());
                RxActivityUtils.skipActivityForResult(mActivity, EditFragment.class, bundle, UserInfofragment.REQUEST_CODE);
                break;
        }
    }
}
