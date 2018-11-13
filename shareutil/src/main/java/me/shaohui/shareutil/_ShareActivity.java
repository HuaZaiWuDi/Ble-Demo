package me.shaohui.shareutil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/11/19.
 */

public class _ShareActivity extends Activity {

    private int mType;

    private boolean isNew;

    private static final String TYPE = "share_activity_type";

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, _ShareActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareLogger.i(INFO.ACTIVITY_CREATE);
        isNew = true;
        ShareLogger.i("onCreate:");
        // init data
        WbSdk.install(this, new AuthInfo(this, ShareManager.CONFIG.getWeiboId(), ShareManager.CONFIG.getWeiboRedirectUrl(), ShareManager.CONFIG.getWeiboScope()));
        ShareLogger.i("配置信息:" + ShareManager.CONFIG.toString());
        mType = getIntent().getIntExtra(TYPE, 0);
        if (mType == ShareUtil.TYPE) {
            // 分享
            ShareUtil.action(this);
        } else if (mType == LoginUtil.TYPE) {
            // 登录
            LoginUtil.action(this);
        } else {
            // handle 微信回调
            LoginUtil.handleResult(-1, -1, getIntent());
            ShareUtil.handleResult(getIntent());
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShareLogger.i("onStart:");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShareLogger.i("onResume:" + isNew);
        ShareLogger.i(INFO.ACTIVITY_RESUME);
        if (isNew) {
            isNew = false;
        } else
            finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShareLogger.i("onStop:" + isNew);
//        if (isNew) {
//            isNew = false;
//        } else
//            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareLogger.i("onDestroy:" + isNew);
//        ShareUtil.recycle();
//        LoginUtil.recycle();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShareLogger.i(INFO.ACTIVITY_NEW_INTENT);
        // 处理回调
        if (mType == LoginUtil.TYPE) {
            LoginUtil.handleResult(0, 0, intent);
        } else if (mType == ShareUtil.TYPE) {
            ShareUtil.handleResult(intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareLogger.i(INFO.ACTIVITY_RESULT);
        // 处理回调
        if (mType == LoginUtil.TYPE) {
            LoginUtil.handleResult(requestCode, resultCode, data);
        } else if (mType == ShareUtil.TYPE) {
            ShareUtil.handleResult(data);
        }
        finish();
    }


    public void finishAc() {
        finish();
    }

}
