package lab.dxythch.com.commonproject.ui.guide;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.ui.main.MainActivity_;

public class SpalshActivity extends BaseActivity {


    private Handler mHandler = new Handler();
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);


        initPromissions();

    }

    @Override
    public void initView() {

    }

    private void gotoMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RxActivityUtils.skipActivityAndFinish(SpalshActivity.this, MainActivity_.class);
            }
        }, 500);
    }


    private void initPromissions() {
        subscribe = new RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            RxLogUtils.d("没有给定位权限");
                        } else {
                            gotoMain();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.dispose();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }


}
