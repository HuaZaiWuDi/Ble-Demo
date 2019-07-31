package lab.wesmartclothing.wefit.flyso.ui.toolui;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxRandom;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.view.NormalLineView;
import lab.wesmartclothing.wefit.flyso.view.line.LineBean;
import lab.wesmartclothing.wefit.flyso.view.line.SuitLines;
import lab.wesmartclothing.wefit.flyso.view.line.Unit;

public class Main2Activity extends BaseActivity {


    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.text)
    RxTextView mText;
    @BindView(R.id.container)
    LinearLayout mContainer;
    @BindView(R.id.lineView)
    NormalLineView mLineView;
    @BindView(R.id.suitLine)
    SuitLines mSuitLine;


    @Override
    protected int statusBarColor() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main2;
    }


    private boolean f;

    @Override
    protected void initViews() {
        super.initViews();
//        RxTextUtils.getBuilder(getString(R.string.plan))
//                .setBackgroundColor(R.drawable.fade_write)
//                .into(mText);
////        mText.setTextColor(R.drawable.fade_write);
        mText.setMovementMethod(ScrollingMovementMethod.getInstance());

        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        mTopBar.setTitle(getString(R.string.appName) + " 健康报告");

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f = !f;
                mText.getHelper().setTextGradientOpen(f);
            }
        });

        mLineView.setBezierLine(true);
        mLineView.setLineWidth(3);
        mLineView.setStepSpace(30);
        mLineView.setDashPath(new DashPathEffect(new float[]{5, 5}, 5));
        mLineView.getLinePaint().setColor(ContextCompat.getColor(mContext, R.color.red));

        mLineView.setMaxMinValue(15, 0);


        List<Unit> lines_bodyFat = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int random = RxRandom.getRandom(0, 100);
            int color = random < 50 ? ContextCompat.getColor(mContext, R.color.GrayWrite) : Color.WHITE;
            Unit unit_bodyFat = new Unit(random, random + "", color);
            lines_bodyFat.add(unit_bodyFat);
        }

        Unit unit_bodyFat1 = new Unit(0, 0 + "");
        Unit unit_bodyFat2 = new Unit(100, 0 + "");
        lines_bodyFat.add(unit_bodyFat1);
        lines_bodyFat.add(unit_bodyFat2);

        LineBean bodyFatLine = new LineBean();
        bodyFatLine.setUnits(lines_bodyFat);
        bodyFatLine.setChartType(SuitLines.ChartType.TYPE_BAR);
        bodyFatLine.setBarWidth(RxUtils.dp2px(10));

        new SuitLines.LineBuilder()
                .add(bodyFatLine)
                .build(mSuitLine);

    }

}
