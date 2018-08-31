package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundRelativeLayout;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static lab.wesmartclothing.wefit.flyso.ui.main.mine.UserInfofragment.IMAGE_PICKER;

/**
 * Created by jk on 2018/8/10.
 */
public class ProblemFragemnt extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.btn_type)
    QMUIRoundButton mBtnType;
    @BindView(R.id.btn_times)
    QMUIRoundButton mBtnTimes;
    @BindView(R.id.edit_proble)
    EditText mEditProble;
    @BindView(R.id.tv_InputCount)
    TextView mTvInputCount;
    @BindView(R.id.layout_problem)
    QMUIRoundRelativeLayout mLayoutProblem;
    @BindView(R.id.edit_phone)
    EditText mEditPhoneEmail;
    @BindView(R.id.layout_phone)
    QMUIRoundRelativeLayout mLayoutPhone;
    @BindView(R.id.tv_imgs_title)
    TextView mTvImgsTitle;
    @BindView(R.id.recycler_imgs)
    RecyclerView mRecyclerImgs;
    @BindView(R.id.btn_submit)
    QMUIRoundButton mBtnSubmit;
    Unbinder unbinder;


    private List<String> problemType, problemTimes;

    private ArrayList<ImageItem> imageLists = new ArrayList<>();
    private QMUIRoundButtonDrawable mBtnTimesDrawable, mBtntypeDrawable, problemDrawable, phoneDrawable;
    private BaseQuickAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_problem);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        initImagePicker();
        initTopBar();
        initRecycler();
        listener();
        problemType = Arrays.asList(getResources().getStringArray(R.array.problemType));
        problemTimes = Arrays.asList(getResources().getStringArray(R.array.problemTimes));

        mBtnTimesDrawable = (QMUIRoundButtonDrawable) mBtnTimes.getBackground();
        mBtntypeDrawable = (QMUIRoundButtonDrawable) mBtnType.getBackground();
        problemDrawable = (QMUIRoundButtonDrawable) mLayoutProblem.getBackground();
        phoneDrawable = (QMUIRoundButtonDrawable) mLayoutPhone.getBackground();

    }

    private void listener() {
        mEditProble.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemDrawable.setStroke(1, getResources().getColor(R.color.BrightGray));
                mTvInputCount.setText((500 - count) + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditPhoneEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneDrawable.setStroke(1, getResources().getColor(R.color.BrightGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);//显示拍照按钮
        imagePicker.setMultiMode(true);
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(4);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void initRecycler() {
        mRecyclerImgs.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_choose_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyAPP.getImageLoader().displayImage(mActivity, item, (QMUIRadiusImageView) helper.getView(R.id.iv_img));
            }
        };
        mRecyclerImgs.setAdapter(adapter);
        adapter.addData(R.mipmap.icon_add_white);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object img = adapter.getData().get(position);
                if (img instanceof ImageItem) {
                    ImageItem imageItem = (ImageItem) img;
//                    RxDialogScaleView scaleView = new RxDialogScaleView(mActivity);
//                    scaleView.setImagePath(imageItem.path);
//                    scaleView.show();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Key.BUNDLE_DATA, imageLists);
                    RxActivityUtils.skipActivity(mActivity, PhotoDetailsFragment.class, bundle);

                } else if (img instanceof Integer) {
                    Intent intent = new Intent(mContext, ImageGridActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageLists);
                    startActivityForResult(intent, IMAGE_PICKER);
                }
            }
        });
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle("问题与建议");
    }

    @OnClick({R.id.btn_type, R.id.btn_times, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_type:
                problemType();
                break;
            case R.id.btn_times:
                problemTimes();
                break;
            case R.id.btn_submit:
                if (checkData()) {
                    if (imageLists.size() > 0) {
                        feedbackImg();
                    } else {
                        commitData("");
                    }
                } else {
                    RxToast.normal("有信息未填写");
                }
                break;
        }
    }

    private boolean checkData() {
        if (problemType.indexOf(mBtnType.getText().toString()) < 0) {
            mBtntypeDrawable.setStroke(1, getResources().getColor(R.color.red));
            return false;
        }
        if (problemTimes.indexOf(mBtnTimes.getText().toString()) < 0) {
            mBtnTimesDrawable.setStroke(1, getResources().getColor(R.color.red));
            return false;
        }
        if (mEditProble.getText().length() <= 0) {
            problemDrawable.setStroke(1, getResources().getColor(R.color.red));
            return false;
        }
        if (!RxRegUtils.isMobileExact(mEditPhoneEmail.getText().toString()) && !RxRegUtils.isEmail(mEditPhoneEmail.getText().toString())) {
            phoneDrawable.setStroke(1, getResources().getColor(R.color.red));
            return false;
        }
        return true;
    }

    public void problemType() {
        new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                .addItem(problemType.get(0))
                .addItem(problemType.get(1))
                .addItem(problemType.get(2))
                .addItem(problemType.get(3))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        mBtnType.setText(tag);
                        mBtnType.setTextColor(getResources().getColor(R.color.GrayWrite));
                        mBtntypeDrawable.setStroke(1, getResources().getColor(R.color.BrightGray));
                    }
                })
                .build()
                .show();
    }

    public void problemTimes() {
        new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                .addItem(problemTimes.get(0))
                .addItem(problemTimes.get(1))
                .addItem(problemTimes.get(2))
                .addItem(problemTimes.get(3))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        mBtnTimes.setText(tag);
                        mBtnTimes.setTextColor(getResources().getColor(R.color.GrayWrite));
                        mBtnTimesDrawable.setStroke(1, getResources().getColor(R.color.BrightGray));
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        RxLogUtils.d("requestCode：" + requestCode);
        RxLogUtils.d("resultCode：" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                imageLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<Object> images = new ArrayList<>();
                images.addAll(0, imageLists);
                images.add(R.mipmap.icon_add_white);
                if (images.size() == 5) {
                    images.remove(4);
                }
                adapter.setNewData(images);
            }
        } else if (requestCode == UserInfofragment.REQUEST_CODE && resultCode == UserInfofragment.RESULT_CODE) {
            if (data != null) {
                imageLists = data.getParcelableArrayListExtra(Key.BUNDLE_DATA);

                ArrayList<Object> images = new ArrayList<>();
                images.addAll(0, imageLists);
                images.add(R.mipmap.icon_add_white);
                if (images.size() == 5) {
                    images.remove(4);
                }
                adapter.setNewData(images);
            }
        }
    }


    /*提交反馈，文字部分*/
    private void commitData(String imgUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("ariseFreq", problemTimes.indexOf(mBtnTimes.getText().toString()));
        object.addProperty("contactInfo", mEditPhoneEmail.getText().toString());
        object.addProperty("dealStatus", problemType.indexOf(mBtnType.getText().toString()));
        object.addProperty("feedbackDesc", mEditProble.getText().toString());
        object.addProperty("feedbackImg", imgUrl);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , object.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.feedback(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        onBackPressed();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    /*上传图片*/
    public void feedbackImg() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < imageLists.size(); i++) {
            File file = new File(imageLists.get(i).path);
            files.add(file);
        }

        List<MultipartBody.Part> body = filesToMultipartBodyParts(files);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.feedbackImg(body))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("上传多张图片成功" + s);
                        commitData(s);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }


}
