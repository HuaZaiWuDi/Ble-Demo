package lab.dxythch.com.commonproject.ui.main.slimming.heat;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vondear.rxtools.fragment.FragmentLazy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import lab.dxythch.com.commonproject.R;
import me.dkzwm.smoothrefreshlayout.SmoothRefreshLayout;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_heat)
public class HeatFragment extends FragmentLazy {

    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    SmoothRefreshLayout refresh;

    public boolean isComplete = false;


    public static HeatFragment getInstance() {
        return new HeatFragment_();
    }


    @Override
    @AfterViews
    public void initData() {
        initRevycler();
    }

    private void initRevycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
//        BaseQuickAdapter adapter = new BaseQuickAdapter<BaseViewHolder>(R.layout.heat_item_title) {
//        }

    }
}
