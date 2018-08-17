package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.dateUtils.RxFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/8/15.
 */
public class MessageDetailsFragment extends BaseAcFragment {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new MessageDetailsFragment();
    }


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_message_details, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTvTitle.setText(bundle.getString(Key.BUNDLE_TITLE));
            mTvDate.setText(RxFormat.setFormatDate(bundle.getLong(Key.ADD_FOOD_DATE), RxFormat.Date));
            mTvContent.setText(bundle.getString(Key.BUNDLE_DATA));
        }
    }

}
