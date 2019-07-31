package lab.wesmartclothing.wefit.flyso.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.tools.Key;

/**
 * Created by jk on 2018/8/10.
 */
public class EditFragment extends BaseActivity {


    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar mQMUIAppBarLayout;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    Unbinder unbinder;


    private Bundle bundle;
    private String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        bundle = getIntent().getExtras();
        title = bundle.getString(Key.BUNDLE_TITLE);
        String data = bundle.getString(Key.BUNDLE_DATA);
        mEditText.setText(data);
        if (getString(R.string.nickname).equals(title)) {
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
                onBackPressed();
            }
        });
        mQMUIAppBarLayout.setTitle(title);
        mQMUIAppBarLayout.addRightTextButton(R.string.save, R.id.btn_save)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save();
                    }
                });
    }

    private void save() {
        String name = mEditText.getText().toString();
        if (getString(R.string.nickname).equals(title)) {
            if (RxDataUtils.isNullString(name.trim())) {
                RxToast.normal(getString(R.string.inputNormalNickname));
                return;
            }

            bundle.putString(Key.BUNDLE_DATA, name);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(UserInfofragment.RESULT_CODE, intent);
            onBackPressed();
        } else {
            if (RxDataUtils.isNullString(name.trim())) {
                RxToast.normal(getString(R.string.inoutNormalSign));
                return;
            }

            bundle.putString(Key.BUNDLE_DATA, name);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(UserInfofragment.RESULT_CODE, intent);
            onBackPressed();
        }


    }
}
