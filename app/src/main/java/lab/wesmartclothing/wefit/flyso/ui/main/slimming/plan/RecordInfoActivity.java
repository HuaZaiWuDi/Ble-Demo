package lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.layout.RxTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;

public class RecordInfoActivity extends BaseActivity {

    @BindView(R.id.topBar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_infoTitle)
    TextView mTvInfoTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_next)
    RxTextView mTvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);
        ButterKnife.bind(this);
        StatusBarUtils.from(mActivity).setStatusBarColor(ContextCompat.getColor(mContext, R.color.white)).process();
        init();
    }

    private void init() {
        initTopBar();
        initRecyclerView();
    }


    private int lastIndex = 0;

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        BaseQuickAdapter adapter = new BaseQuickAdapter<RecordItem, BaseViewHolder>(R.layout.item_recond) {
            @Override
            protected void convert(BaseViewHolder helper, RecordItem item) {
                TextView tvItem = helper.getView(R.id.tv_item);
                tvItem.setText(item.content);
                Drawable drawable = ContextCompat.getDrawable(mContext, item.isSelect ?
                        R.drawable.shape_green_dot : R.drawable.shape_gray_dot);
                tvItem.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        };
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(
                new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                        if (lastIndex != position) {
//                            RecordItem lastItem = (RecordItem) adapter.getItem(lastIndex);
//                            lastItem.isSelect = !lastItem.isSelect;
//                            RecordItem selectItem = (RecordItem) adapter.getItem(position);
//                            selectItem.isSelect = !selectItem.isSelect;
//                            adapter.setData(lastIndex, lastItem);
//                            adapter.setData(position, selectItem);
//                            lastIndex = position;
//                        }

                        List<RecordItem> data = adapter.getData();
                        for (int i = 0; i < data.size(); i++) {
                            RecordItem item = data.get(i);
                            if (i == position && !item.isSelect) {
                                item.isSelect = true;
                                adapter.setData(i, item);
                            } else if (i != position && item.isSelect) {
                                item.isSelect = false;
                                adapter.setData(i, item);
                            }
                        }
                    }
                }
        );

        RecyclerView gridView = new RecyclerView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        gridView.setLayoutParams(params);

//        adapter.addHeaderView(gridView);
        BaseQuickAdapter gridAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_record_grid) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
//                MyAPP.sImageLoader.displayImage(mActivity, item, R.drawable.ic_default_image,
//                        (ImageView) helper.getView(R.id.img_item));
                helper.setImageResource(R.id.img_item, R.mipmap.icon_clothing_default);
            }
        };
        gridView.setLayoutManager(new GridLayoutManager(mContext, 2));
        gridView.setAdapter(gridAdapter);
        gridView.setNestedScrollingEnabled(false);


        gridAdapter.addData("");
        gridAdapter.addData("");
        gridAdapter.addData("");
        gridAdapter.addData("");


        adapter.addData(new RecordItem("1123", true));
        adapter.addData(new RecordItem("1123", false));
        adapter.addData(new RecordItem("1123", false));
        adapter.addData(new RecordItem("1123", false));


    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        RxActivityUtils.skipActivity(mContext, WelcomeActivity.class);
    }


    class RecordItem {
        String content;
        boolean isSelect;

        public RecordItem(String content, boolean isSelect) {
            this.content = content;
            this.isSelect = isSelect;
        }
    }

}
