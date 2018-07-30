package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.vondear.rxtools.utils.RxUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;

/**
 * Created by jk on 2018/7/27.
 */
public class BodyFatFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    Unbinder unbinder;
    BaseQuickAdapter adapter;

    public static QMUIFragment getInstance() {
        return new BodyFatFragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_body_fat, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTopBar();
        initRecyclerView();
        initData();
    }

    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle("体型");
    }

    private void initData() {
        String[] bodys = getResources().getStringArray(R.array.bodyShape);
        int[] bodyImgs = {R.mipmap.man_1_1, R.mipmap.man_1_2, R.mipmap.man_1_3,
                R.mipmap.man_2_1, R.mipmap.man_2_2, R.mipmap.man_2_3,
                R.mipmap.man_3_1, R.mipmap.man_3_2, R.mipmap.man_3_2,};
        for (int i = 0; i < bodys.length; i++) {
            adapter.addData(new BodyFat(bodyImgs[i], bodys[i], i == lastIndex));
        }
    }

    private int lastIndex = 4;

    private void initRecyclerView() {
        mMRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        adapter = new BaseQuickAdapter<BodyFat, BaseViewHolder>(R.layout.item_body_fat) {
            @Override
            protected void convert(BaseViewHolder helper, final BodyFat item) {
                QMUIRoundButton mQMUIRoundButton = helper.getView(R.id.mQMUIRoundButton);
                mQMUIRoundButton.setText(item.text);
                Drawable drawable = getResources().getDrawable(item.img);
                //一定要加这行！！！！！！！！！！！
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mQMUIRoundButton.setCompoundDrawables(null, drawable, null, null);

                QMUIRoundButtonDrawable background = (QMUIRoundButtonDrawable) mQMUIRoundButton.getBackground();
                background.setStrokeData(RxUtils.dp2px(2), ColorStateList.valueOf(getResources().getColor(item.isSelect ? R.color.green_61D97F : R.color.white)));
                helper.addOnClickListener(R.id.mQMUIRoundButton);
            }
        };

        adapter.bindToRecyclerView(mMRecyclerView);
    }


    class BodyFat {
        int img;
        String text;
        boolean isSelect;

        public BodyFat(int img, String text, boolean isSelect) {
            this.img = img;
            this.text = text;
            this.isSelect = isSelect;
        }
    }

}
