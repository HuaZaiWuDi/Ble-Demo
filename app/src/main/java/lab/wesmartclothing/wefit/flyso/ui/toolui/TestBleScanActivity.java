package lab.wesmartclothing.wefit.flyso.ui.toolui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtools.model.state.PageLayout;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRandom;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxLinearLayout;
import com.vondear.rxtools.view.roundprogressbar.RxIconRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxTextRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.VerticalProgress;
import com.vondear.rxtools.view.roundprogressbar.common.RxBaseRoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;

public class TestBleScanActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    RelativeLayout mParent;

    private RxTextRoundProgressBar mTextProgress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ble_scan);
        ButterKnife.bind(this);
        RxLogUtils.i("启动时长：无网络请求的界面");

        final RxLinearLayout tv_test = findViewById(R.id.tv_test);


        VerticalProgress mVerticalProgress = findViewById(R.id.mVerticalProgress);
        mVerticalProgress.setProgress(100);

//
//        mParent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RxAnimationUtils.doCircle(mParent, 0, 0);
//            }
//        });


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

        TextView emptyView = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        emptyView.setLayoutParams(params);
        emptyView.setGravity(Gravity.CENTER);

        emptyView.setText("我是空数据");

        TextView errorView = new TextView(this);
        errorView.setLayoutParams(params);
        errorView.setGravity(Gravity.CENTER);
        errorView.setText("我是异常数据");

        TextView LoadingView = new TextView(this);
        LoadingView.setLayoutParams(params);
        LoadingView.setGravity(Gravity.CENTER);
        LoadingView.setText("我是加载数据");


        pageLayout = new PageLayout.Builder(this)
                .initPage(findViewById(R.id.layout_test))
                .setEmpty(emptyView)
                .setError(errorView)
                .setLoading(LoadingView)
                .setOnRetryListener(new PageLayout.OnRetryClickListener() {
                    @Override
                    public void onRetry() {
                        Toast.makeText(TestBleScanActivity.this, "重试", Toast.LENGTH_SHORT).show();
                    }
                }).create();


        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index % 4 == 0) {
                    pageLayout.showLoading();
                } else if (index % 4 == 1) {
                    pageLayout.showEmpty();
                } else if (index % 4 == 2) {
                    pageLayout.showError();
                } else if (index % 4 == 3) {
                    pageLayout.hide();
                }
            }
        });
    }

    int index = 0;
    PageLayout pageLayout;

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
