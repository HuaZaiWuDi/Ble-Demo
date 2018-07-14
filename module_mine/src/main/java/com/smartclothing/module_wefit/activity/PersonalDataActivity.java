package com.smartclothing.module_wefit.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.smartclothing.module_wefit.bean.Sex;
import com.smartclothing.module_wefit.bean.UserInfo;
import com.smartclothing.module_wefit.tools.ClipImageActivity;
import com.smartclothing.module_wefit.widget.dialog.DepositDialog;
import com.smartclothing.module_wefit.widget.wheelpicker.picker.DatePicker;
import com.smartclothing.module_wefit.widget.wheelpicker.picker.DateTimePicker;
import com.smartclothing.module_wefit.widget.wheelpicker.picker.NumberPicker;
import com.smartclothing.module_wefit.widget.wheelpicker.picker.SinglePicker;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/*个人资料*/

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_data_save;
    private RelativeLayout rl_data_icon;
    private RelativeLayout rl_data_name;
    private RelativeLayout rl_data_sex;
    private RelativeLayout rl_data_birth;
    private RelativeLayout rl_data_height;
    private RelativeLayout rl_data_weight;
    private RelativeLayout rl_data_phone;
    private RelativeLayout rl_data_sign;
    private View parentView;
    private ImageView iv_data_icon;
    private TextView tv_data_name;
    private TextView tv_data_sex;
    private TextView tv_data_birth;
    private TextView tv_data_height;
    private TextView tv_data_weight;
    private TextView tv_data_phone;
    private TextView tv_data_sign;


    public static final String Dafult_Data = "Dafult_Data";
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //请求摄像头
    private static final int CAMERA_REQUEST_CODE = 105;
    private static final int REQUEST_CODE_NAME = 106;
    private static final int RESULT_CODE_NAME = 107;
    private static final int REQUEST_CODE_PHONE = 108;
    private static final int RESULT_CODE_PHONE = 109;
    private static final int REQUEST_CODE_SING = 110;
    private static final int RESULT_CODE_SIGN = 111;
    //调用照相机返回图片临时文件
    private File tempFile;
    private String cropImagePath;
    private UserInfo user;
    private String name;//用于保存修改的用户名
    private DepositDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentView = LayoutInflater.from(this).inflate(R.layout.activity_personal_data, null);

        setContentView(parentView);

        initView();

        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);

        initData();
    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_data_save = findViewById(R.id.tv_data_save);
        rl_data_icon = findViewById(R.id.rl_data_icon);
        rl_data_name = findViewById(R.id.rl_data_name);
        rl_data_sex = findViewById(R.id.rl_data_sex);
        rl_data_birth = findViewById(R.id.rl_data_birth);
        rl_data_height = findViewById(R.id.rl_data_height);
        rl_data_weight = findViewById(R.id.rl_data_weight);
        rl_data_phone = findViewById(R.id.rl_data_phone);
        rl_data_sign = findViewById(R.id.rl_data_sign);
        iv_data_icon = findViewById(R.id.iv_data_icon);
        tv_data_name = findViewById(R.id.tv_data_name);
        tv_data_sex = findViewById(R.id.tv_data_sex);
        tv_data_birth = findViewById(R.id.tv_data_birth);
        tv_data_height = findViewById(R.id.tv_data_height);
        tv_data_weight = findViewById(R.id.tv_data_weight);
        tv_data_phone = findViewById(R.id.tv_data_phone);
        tv_data_sign = findViewById(R.id.tv_data_sign);
        listener();
    }

    /**
     * 获取用户资料
     */
    private void initData() {
        final QMUITipDialog dialog = new QMUITipDialog.Builder(PersonalDataActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.userInfo())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        RxLogUtils.d("doOnSubscribe");
                        dialog.show();
                    }
                })
                .doFinally(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                dialog.dismiss();
                                RxLogUtils.d("结束");
                            }
                        })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        Gson gson = new Gson();
                        user = gson.fromJson(s, UserInfo.class);
                        if (user != null) {
                            tv_data_sex.setText(user.getSex() == 1 ? "男" : "女");
                            tv_data_name.setText(user.getUserName());
                            tv_data_birth.setText(RxFormat.setFormatDate(Long.parseLong(user.getBirthday()), RxFormat.Date));
                            tv_data_height.setText(user.getHeight() + "");
                            tv_data_weight.setText(user.getTargetWeight() + "");
                            tv_data_sign.setText(RxDataUtils.isNullString(user.getSignature()) ? "请输入您的个性签名" : user.getSignature());
                            tv_data_phone.setText(user.getPhone());

                            if (user.getUserImg() != null) {
                                Glide.with(PersonalDataActivity.this)
                                        .load(user.getUserImg())
                                        .placeholder(R.mipmap.userimg)
                                        .bitmapTransform(new CropCircleTransformation(PersonalDataActivity.this))//圆角图片
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(iv_data_icon);//.placeholder(R.mipmap.img_touxiang)
                            }
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void listener() {
        iv_back.setOnClickListener(this);
        tv_data_save.setOnClickListener(this);
        rl_data_icon.setOnClickListener(this);
        rl_data_name.setOnClickListener(this);
        rl_data_sex.setOnClickListener(this);
        rl_data_birth.setOnClickListener(this);
        rl_data_height.setOnClickListener(this);
        rl_data_weight.setOnClickListener(this);
        rl_data_phone.setOnClickListener(this);
        rl_data_sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.iv_back) {
            if (user != null && user.isChange) {
                final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
                dialog.getTvTitle().setBackgroundResource(R.mipmap.slice);
                dialog.getTvContent().setText("信息已更改，是否保存？");
                dialog.setCancel("保存");
                dialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestSaveUserInfo();
                    }
                });
                dialog.setSure("取消");
                dialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        RxActivityUtils.finishActivity();
                    }
                });
                dialog.show();
            } else RxActivityUtils.finishActivity();

        } else if (i1 == R.id.tv_data_save) {
            requestSaveUserInfo();

        } else if (i1 == R.id.rl_data_icon) {
            selectImage();

        } else if (i1 == R.id.rl_data_name) {
            Bundle bundle = new Bundle();
            bundle.putString(Dafult_Data, user.getUserName());
            RxActivityUtils.skipActivityForResult(PersonalDataActivity.this, NameEditActivity.class, bundle, REQUEST_CODE_NAME);
        } else if (i1 == R.id.rl_data_sex) {
            List<Sex> data = new ArrayList<>();
            data.add(new Sex(1, "男"));
            data.add(new Sex(2, "女"));
            SinglePicker<Sex> picker = new SinglePicker<>(this, data);
            picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
            picker.setTextColor(getResources().getColor(R.color.picker_text_color));
            picker.setLineColor(getResources().getColor(R.color.picker_line_color));
            picker.setTopBackgroundColor(getResources().getColor(R.color.white));
            picker.setTopLineColor(getResources().getColor(R.color.white));
            picker.setTitleTextColor(getResources().getColor(R.color.picker_title_text_color));
            picker.setCancelTextColor(getResources().getColor(R.color.picker_title_text_color));
            picker.setSubmitTextColor(getResources().getColor(R.color.picker_title_text_color));
            picker.setCanceledOnTouchOutside(true);
            picker.setSelectedIndex(user.getSex() - 1);
            picker.setCycleDisable(true);
            picker.setDividerRatio(0.2f);
            picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<Sex>() {
                @Override
                public void onItemPicked(int index, Sex item) {
                    user.setSex(item.getId());
//                        RxToast.showToast(item.getId() + "");
                    tv_data_sex.setText(item.getName());
                }
            });
            picker.show();

        } else if (i1 == R.id.rl_data_birth) {

            Calendar calendar = Calendar.getInstance();
            DatePicker picker = new DatePicker(this, DateTimePicker.YEAR_MONTH_DAY);
            picker.setGravity(Gravity.BOTTOM);
            picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
            picker.setCycleDisable(false);
            picker.setDividerRatio(0.2f);
            picker.setOffset(2);//偏移量

            picker.setRangeStart(1940, 01, 01);
            picker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            calendar.setTimeInMillis(Long.parseLong(user.getBirthday()));
            picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            picker.setTextSize(21);
            picker.setLabel("-", "-", "");
            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);
                    tv_data_birth.setText(year + "-" + month + "-" + day);
                    Date date = RxFormat.setParseDate(year + "-" + month + "-" + day, RxFormat.Date);
                    user.setBirthday(date.getTime() + "");
                }
            });
            picker.show();
        } else if (i1 == R.id.rl_data_height) {

            NumberPicker picker = new NumberPicker(this);
            picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
            picker.setCycleDisable(false);
            picker.setDividerRatio(0.2f);
            picker.setOffset(2);//偏移量
            picker.setRange(120, 200, 1);//数字范围
            picker.setSelectedItem(user.getHeight());
            picker.setTextSize(21);
            picker.setLabel("cm");
            picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                @Override
                public void onNumberPicked(int index, Number item) {
                    RxLogUtils.d("身高：" + item);
                    tv_data_height.setText(item + "");
                    user.setHeight((int) item);
                }
            });
            picker.show();
        } else if (i1 == R.id.rl_data_weight) {

            NumberPicker picker = new NumberPicker(this);
            picker.setGravity(Gravity.BOTTOM);
            picker.setHeight((int) (picker.getScreenHeightPixels() * 0.4));
            picker.setCycleDisable(false);
            picker.setDividerRatio(0.2f);
            picker.setOffset(2);//偏移量
            picker.setRange(35, 90, 1);//数字范围
            picker.setSelectedItem(user.getTargetWeight());
            picker.setTextSize(21);
            picker.setLabel("kg");
            picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                @Override
                public void onNumberPicked(int index, Number item) {
                    RxLogUtils.d("体重：" + item);
                    tv_data_weight.setText(item + "");
                    user.setTargetWeight((int) item);
                }
            });
            picker.show();

        } else if (i1 == R.id.rl_data_phone) {//                startActivityForResult(new Intent(PersonalDataActivity.this, PhoneEditActivity.class), REQUEST_CODE_PHONE);

        } else if (i1 == R.id.rl_data_sign) {
            Bundle bundle = new Bundle();
            bundle.putString(Dafult_Data, user.getSignature());
            RxActivityUtils.skipActivityForResult(PersonalDataActivity.this, SignEditActivity.class, bundle, REQUEST_CODE_SING);
        }
    }

    /*保存按钮，提交用户数据*/
    private void requestSaveUserInfo() {
        if (user == null) RxToast.showToast("用户数据不能为空");

        String gson = new Gson().toJson(user, UserInfo.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , gson);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.saveUserInfo(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        tipDialog.show();
                        RxLogUtils.d("doOnSubscribe");
                    }
                })
                .doFinally(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                tipDialog.dismiss();
                            }
                        })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        RxToast.success("保存成功", 2000);
                        RxBus.getInstance().post(user);
                        onBackPressed();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    /*弹出选择图片的方式popupWindow*/
    private void selectImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCamera = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(tv_data_sign, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /*跳到相机*/
    private void gotoCamera() {
        //权限判断
        if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(
                    this,
                    "lab.wesmartclothing.wefit.flyso" + ".provider",
                    tempFile);// getPackageName()
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_CAPTURE);
        }
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQUEST_CODE);
                }
            } else {

            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoPhoto();
            } else {

            }
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoCamera();
            } else {
                RxToast.showToast("请允许请求摄像头权限，否则无法使用该功能");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK)
                    gotoClipActivity(Uri.fromFile(tempFile));
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK)
                    gotoClipActivity(intent.getData());
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) return;
                    //得到的剪切后的图片uri
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    uploadImage(cropImagePath);
                }
                break;
            case REQUEST_CODE_NAME://保存用户名=
                if (intent == null) {
                    return;
                }
                user.setUserName(intent.getStringExtra("name"));
                tv_data_name.setText(intent.getStringExtra("name"));
                break;
            case REQUEST_CODE_PHONE://保存联系电话
                if (intent == null) {
                    return;
                }
                user.setPhone(intent.getStringExtra("phone"));
                tv_data_phone.setText(intent.getStringExtra("phone"));
                break;
            case REQUEST_CODE_SING://保存签名
                if (intent == null) {
                    return;
                }
                user.setSignature(intent.getStringExtra("sign"));
                tv_data_sign.setText(intent.getStringExtra("sign"));

                break;
        }
    }

    private void uploadImage(String cropImagePath) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(cropImagePath));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", cropImagePath, requestFile);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.uploadUserImg(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        RxToast.success("保存成功", 2000);
                        //成功后将本地图片设置到imageView中，并在退回到个人中心时，刷新贴图url
                        user.setUserImg(s);
                        //此处后面可以将bitMap转为二进制上传后台网络
                        Glide.with(PersonalDataActivity.this)
                                .load(s)
                                .placeholder(R.mipmap.userimg)
                                .bitmapTransform(new CropCircleTransformation(PersonalDataActivity.this))//圆角图片
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(iv_data_icon);//.placeholder(R.mipmap.img_touxiang)

                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", 1);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (hasUpdateUserData)
//            setResult(RESULT_OK);
//    }
}
