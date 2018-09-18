package lab.wesmartclothing.wefit.flyso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRandom;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.roundprogressbar.RxIconRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.RxTextRoundProgressBar;
import com.vondear.rxtools.view.roundprogressbar.common.RxBaseRoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

public class TestBleScanActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    RelativeLayout mParent;

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


//        initLine();

        final RxRoundProgressBar progressBar = findViewById(R.id.mProgress);
        progressBar.setProgress(50);

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(RxRandom.getRandom(100));
            }
        });
        progressBar.setOnProgressChangedListener(new RxBaseRoundProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                RxLogUtils.e("进度：" + progress);
            }
        });

        final RxTextRoundProgressBar mTextProgress = findViewById(R.id.mTextProgress);
        mTextProgress.setProgress(50);


        mTextProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextProgress.setProgress(RxRandom.getRandom(100));
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

    private void initLine() {
        SuitLines mSuitLines = findViewById(R.id.mSuitLines);
        SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Unit unit_weight = new Unit(RxRandom.getRandom(100), i + "");
            Unit unit_bodyFat = new Unit(RxRandom.getRandom(50), i + "");
            unit_weight.setShowPoint(true);

            unit_bodyFat.setFill(true);
            lines_Heat.add(unit_weight);
            lines_Time.add(unit_bodyFat);
        }
        builder.add(lines_Heat, 0x7fffffff);
        builder.add(lines_Time, 0x7fffffff);

        mSuitLines.setSpaceMaxMin(0.3f, 0f);
        builder.build(mSuitLines, false);
        mSuitLines.setLineChartSelectItemListener(new SuitLines.LineChartSelectItemListener() {
            @Override
            public void selectItem(int valueX) {
                RxToast.normal(valueX + "");
            }
        });
    }
}
