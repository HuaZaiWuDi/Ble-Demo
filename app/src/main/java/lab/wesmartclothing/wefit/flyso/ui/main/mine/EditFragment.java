package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/8/10.
 */
public class EditFragment extends BaseAcFragment {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    Unbinder unbinder;

    public static QMUIFragment getInstance() {
        return new EditFragment();
    }

    private Bundle bundle;
    private String title;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_edit, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        bundle = getArguments();
        title = bundle.getString(Key.BUNDLE_TITLE);
        String data = bundle.getString(Key.BUNDLE_DATA);
        mEditText.setText(data);
        if ("昵称".equals(title)) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            mTvTip.setText(getString(R.string.must_input, 16));
        } else {
            mTvTip.setText(getString(R.string.must_input, 30));
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        }
        initTopBar();
    }


    private void initTopBar() {
        mQMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mQMUIAppBarLayout.setTitle(title);
        mQMUIAppBarLayout.addRightTextButton("保存", R.id.btn_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save();
                    }
                });
    }

    private void save() {
        String name = mEditText.getText().toString();
        if ("昵称".equals(title)) {
            if (RxDataUtils.isNullString(name.trim())) {
                RxToast.normal("请输入正确的昵称");
                return;
            }

            bundle.putString(Key.BUNDLE_DATA, name);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setFragmentResult(UserInfofragment.RESULT_CODE, intent);
            popBackStack();
        } else {
            if (RxDataUtils.isNullString(name.trim())) {
                RxToast.normal("请输入正确的签名");
                return;
            }

            bundle.putString(Key.BUNDLE_DATA, name);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setFragmentResult(UserInfofragment.RESULT_CODE, intent);
            popBackStack();
        }

    }
}