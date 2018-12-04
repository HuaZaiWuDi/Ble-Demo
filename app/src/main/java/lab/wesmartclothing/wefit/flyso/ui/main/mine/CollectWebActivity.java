package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxProcessUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseWebActivity;
import lab.wesmartclothing.wefit.flyso.entity.CollectBean;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

public class CollectWebActivity extends BaseWebActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.parent)
    RelativeLayout mParent;

    private CollectBean.ListBean bean;


    @Override
    protected int layoutId() {
        return R.layout.activity_web_interactive;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initWebView(mParent);
        RxLogUtils.e("当前进程：" + RxProcessUtils.getForegroundProcessName(mContext));
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });


        QMUIAlphaImageButton shareImg = mTopBar.addRightImageButton(R.mipmap.icon_share, R.id.img_share);
        shareImg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    showSimpleBottomSheetGrid(bean.getCoverPicture(), bean.getArticleName(),
                            bean.getSummary(), getUrl());
                } else {
                    RxToast.normal("分享失败");
                }
            }
        });
    }


    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        bean = (CollectBean.ListBean) bundle.getSerializable(Key.BUNDLE_DATA);
        if (bean != null)
            mTopBar.setTitle(bean.getArticleName());
    }


    private void showSimpleBottomSheetGrid(final String imgUrl, final String title, final String desc, final String url) {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(mContext);
        builder.addItem(R.mipmap.wechat, "分享到微信", 1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.fr, "分享到朋友圈", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.weib, "分享到微博", 3, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.qq, "分享到QQ", 4, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.zone, "分享到QQ空间", 5, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case 1:
                                ShareUtil.shareMedia(mActivity, SharePlatform.WX, title, desc, url, imgUrl, shareListener);
                                break;
                            case 2:
                                ShareUtil.shareMedia(mActivity, SharePlatform.WX_TIMELINE, title, desc, url, imgUrl, shareListener);
                                break;
                            case 3:
                                ShareUtil.shareMedia(mActivity, SharePlatform.WEIBO, title, desc, url, imgUrl, shareListener);
                                break;
                            case 4:
                                ShareUtil.shareMedia(mActivity, SharePlatform.QQ, title, desc, url, imgUrl, shareListener);
                                break;
                            case 5:
                                ShareUtil.shareMedia(mActivity, SharePlatform.QZONE, title, desc, url, imgUrl, shareListener);
                                break;

                        }
                    }
                }).build().show();
    }


    ShareListener shareListener = new ShareListener() {
        @Override
        public void shareSuccess() {
            RxLogUtils.d("分享成功");
        }

        @Override
        public void shareFailure(Exception e) {
            RxLogUtils.d("分享失败");
        }


        @Override
        public void shareCancel() {
            RxLogUtils.d("分享关闭");
        }

    };


    @Nullable
    @Override
    protected String getUrl() {
        return getIntent().getStringExtra(Key.BUNDLE_WEB_URL);
    }

}
