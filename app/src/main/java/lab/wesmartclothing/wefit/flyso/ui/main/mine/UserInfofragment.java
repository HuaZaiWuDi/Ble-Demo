package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxPhotoUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.entity.UserInfo;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;

/**
 * Created by jk on 2018/8/9.
 */
public class UserInfofragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.iv_userImg)
    QMUIRadiusImageView mIvUserImg;
    @BindView(R.id.layout_userImg)
    RelativeLayout mLayoutUserImg;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    Unbinder unbinder;

    private QMUICommonListItemView userNameItem, sexItem, birthItem, heightItem, weightItem, cityItem, signItem;
    private UserInfo info;

    public static QMUIFragment getInstance() {
        return new UserInfofragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_user_info, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        groupList();
        String string = SPUtils.getString(SPKey.SP_UserInfo);
        info = new Gson().fromJson(string, UserInfo.class);
        notifyData(info);
    }

    private void notifyData(UserInfo info) {
        Glide.with(mActivity).load(info.getUserImg())
                .asBitmap()
                .placeholder(R.mipmap.userimg)
                .into(mIvUserImg);
        userNameItem.setDetailText(info.getUserName());
        sexItem.setDetailText(info.getSex() == 1 ? "男" : "女");
        birthItem.setDetailText(RxFormat.setFormatDate(info.getBirthday(), RxFormat.Date));
        heightItem.setDetailText(info.getHeight() + "");
        weightItem.setDetailText(info.getTargetWeight() + "");
        cityItem.setDetailText(info.getProvince() + "," + info.getCity());
        signItem.setDetailText(info.getSignature());
    }

    private void groupList() {
        userNameItem = mGroupListView.createItemView("昵称");
        userNameItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(userNameItem);

        sexItem = mGroupListView.createItemView("性别");
        sexItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(sexItem);

        birthItem = mGroupListView.createItemView("生日");
        birthItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(birthItem);

        heightItem = mGroupListView.createItemView("身高");
        heightItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(heightItem);

        weightItem = mGroupListView.createItemView("目标体重");
        weightItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(weightItem);

        cityItem = mGroupListView.createItemView("所在城市");
        cityItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(cityItem);

        signItem = mGroupListView.createItemView("签名");
        signItem.setOrientation(QMUICommonListItemView.HORIZONTAL);
        setItemView(signItem);


        QMUIGroupListView.newSection(mActivity)
                .setUseTitleViewForSectionSpace(false)
                .addItemView(userNameItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(sexItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(mActivity)
                .setUseTitleViewForSectionSpace(false)
                .addItemView(birthItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(heightItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(weightItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(mActivity)
                .setUseTitleViewForSectionSpace(false)
                .addItemView(cityItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(signItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addTo(mGroupListView);


    }

    private void setItemView(QMUICommonListItemView item) {
        item.getTextView().setTextColor(getResources().getColor(R.color.Gray));
        item.getTextView().setTextSize(15);
        item.getTextView().getPaint().setFakeBoldText(true);
        item.getDetailTextView().setTextColor(getResources().getColor(R.color.GrayWrite));
    }


    @OnClick(R.id.layout_userImg)
    public void onViewClicked() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(UserInfofragment.this);
        dialogChooseImage.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoUtils.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    RxPhotoUtils.cropImage(UserInfofragment.this, data.getData());// 裁剪图片
                }
                break;
            case RxPhotoUtils.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    /* data.getExtras().get("data");*/
                    RxPhotoUtils.cropImage(UserInfofragment.this, RxPhotoUtils.imageUriFromCamera);// 裁剪图片
                }
                break;
            case RxPhotoUtils.CROP_IMAGE://普通裁剪后的处理
//                RxPhotoUtils.cropImageUri;
                Glide.with(mActivity).load(RxPhotoUtils.cropImageUri)
                        .asBitmap().placeholder(R.mipmap.userimg).into(mIvUserImg);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
