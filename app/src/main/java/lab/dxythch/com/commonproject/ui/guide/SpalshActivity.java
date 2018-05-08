package lab.dxythch.com.commonproject.ui.guide;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.vondear.rxtools.activity.RxActivityUtils;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.ui.main.MainActivity_;

public class SpalshActivity extends AppCompatActivity {


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RxActivityUtils.skipActivityAndFinish(SpalshActivity.this, MainActivity_.class);
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }


}
