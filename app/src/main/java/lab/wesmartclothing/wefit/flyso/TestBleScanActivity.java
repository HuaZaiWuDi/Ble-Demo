package lab.wesmartclothing.wefit.flyso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.vondear.rxtools.utils.RandomUtils;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

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


        initLine();

    }

    private void initLine() {
        SuitLines mSuitLines = findViewById(R.id.mSuitLines);
        SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
        List<Unit> lines_Heat = new ArrayList<>();
        List<Unit> lines_Time = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Unit unit_weight = new Unit(RandomUtils.getRandom(100), i + "");
            Unit unit_bodyFat = new Unit(RandomUtils.getRandom(50), i + "");
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
