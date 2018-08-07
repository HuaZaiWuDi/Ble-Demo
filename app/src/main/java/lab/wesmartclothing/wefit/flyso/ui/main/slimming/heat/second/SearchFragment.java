package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.second;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;

/**
 * Created by jk on 2018/8/3.
 */
public class SearchFragment extends BaseAcFragment {
    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.mSearchView)
    SearchView mMSearchView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    @BindView(R.id.recyclerAddFoods)
    RecyclerView mRecyclerAddFoods;
    @BindView(R.id.btn_mark)
    QMUIRoundButton mBtnMark;
    @BindView(R.id.iv_complete)
    ImageView mIvComplete;
    @BindView(R.id.layout_addFoods)
    RelativeLayout mLayoutAddFoods;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new SearchFragment();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_search, null);
        unbinder = ButterKnife.bind(this, view);
        initSearchView();
        return view;
    }

    private void initSearchView() {
        //修改搜索框底部的横线
        mMSearchView.findViewById(R.id.search_plate).setBackgroundColor(getResources().getColor(R.color.GrayWrite));
        //修改输入框的字体颜色的大小
        final EditText editText = mMSearchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        editText.setTextColor(Color.WHITE);
        editText.setHintTextColor(Color.WHITE);

    }

    @OnClick(R.id.iv_complete)
    public void onViewClicked() {
    }
}
