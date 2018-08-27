package lab.wesmartclothing.wefit.flyso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.vondear.rxtools.utils.RxLogUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader;

public class TestBleScanActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    RelativeLayout mParent;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    private QMUIEmptyView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ble_scan);
        ButterKnife.bind(this);


//        mEmptyView = new QMUIEmptyView(this);
//
//
//        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        mParent.addView(mEmptyView);
        initImagePicker();


        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 111);

    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        RxLogUtils.d("requestCode：" + requestCode);
        RxLogUtils.d("resultCode：" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 111) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                RxLogUtils.d("选中的图片：" + images.size());
//                Glide.with(this).load(images.get(0).path)
//                        .asBitmap()
//                        .placeholder(R.mipmap.userimg)
//                        .into(mIvUserImg);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    int count = 0;

    @OnClick(R.id.tv_content)
    public void onViewClicked() {
        count++;
        switch (count % 4) {
            case 0:
                mEmptyView.show("我是title", "我是Content");
                break;
            case 1:
                mEmptyView.show("我是错误的文字", null);
                break;
            case 2:
                mEmptyView.show(true);
                break;
            case 3:
                mEmptyView.show(false, "我是title", null, "我是Content", null);
                break;
            case 4:
                mEmptyView.show(false, "我是title", "我是Content", "点击重试", null);
                break;
            default:
                break;
        }
    }
}
