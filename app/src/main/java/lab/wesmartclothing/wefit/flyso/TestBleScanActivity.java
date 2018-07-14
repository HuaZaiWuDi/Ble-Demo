package lab.wesmartclothing.wefit.flyso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIEmptyView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


        mEmptyView = new QMUIEmptyView(this);
        mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mParent.addView(mEmptyView);

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
