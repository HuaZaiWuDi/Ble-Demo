package lab.wesmartclothing.wefit.flyso;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRandom;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.vondear.rxtools.view.roundprogressbar.RxIconRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxTextRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.common.RxBaseRoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestBleScanActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    RelativeLayout mParent;

    private QMUIEmptyView mEmptyView;
    private RxTextRoundProgressBar mTextProgress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ble_scan);
        ButterKnife.bind(this);
        RxLogUtils.i("启动时长：无网络请求的界面");

        final RxLinearLayout tv_test = findViewById(R.id.tv_test);


//        mEmptyView = new QMUIEmptyView(this);
//
//
//        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        mParent.addView(mEmptyView);


//        initLine();

        final RxRoundProgressBar progressBar = findViewById(R.id.mProgress);
        progressBar.setProgress(50);

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(RxRandom.getRandom(100));
                tv_test.setElevation(RxUtils.dp2px(progressBar.getProgress()));
            }
        });
        progressBar.setOnProgressChangedListener(new RxBaseRoundProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                RxLogUtils.e("进度：" + progress);
            }
        });

        mTextProgress = findViewById(R.id.mTextProgress);
        mTextProgress.setProgress(50);


        mTextProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTextProgress.setProgress(RxRandom.getRandom(100));
//                tv_test.setTranslationZ(RxUtils.dp2px(mTextProgress.getProgress()));
                mTimer.startTimer();
            }
        });
        final RxIconRoundProgressBar mIconProgress = findViewById(R.id.mIconProgress);
        mIconProgress.setProgress(50);

        mIconProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconProgress.setProgress(RxRandom.getRandom(100));
            }
        });

    }


    int progress = 0;
    MyTimer mTimer = new MyTimer(1000, 100, new MyTimerListener() {
        @Override
        public void enterTimer() {
            progress++;
            if (progress == 100) progress = 0;
            mTextProgress.setProgress(progress, false);
        }
    });


    public void onClick(View view) {

    }
}
