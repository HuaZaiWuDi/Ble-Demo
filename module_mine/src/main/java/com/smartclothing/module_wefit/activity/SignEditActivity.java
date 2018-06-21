package com.smartclothing.module_wefit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.view.RxToast;

/*编辑签名*/

public class SignEditActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_data_save;
    private EditText et_data_sign;
    private static final int RESULT_CODE_SIGN = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_edit);

        initView();

    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_data_save = findViewById(R.id.tv_data_save);
        et_data_sign = findViewById(R.id.et_data_sign);

        String string = getIntent().getExtras().getString(PersonalDataActivity.Dafult_Data);
        if (RxDataUtils.isNullString(string)) {
            et_data_sign.setHint("请输入您的个性签名");
        } else {
            et_data_sign.setText(string);
        }
        listener();
    }

    private void listener() {
        iv_back.setOnClickListener(this);
        tv_data_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();

        } else if (i == R.id.tv_data_save) {
            if (RxDataUtils.isNullString(et_data_sign.getText().toString())) {
                RxToast.showToast("签名不能为空");
                return;
            }
            setResult(RESULT_CODE_SIGN, new Intent().putExtra("sign", "" + et_data_sign.getText().toString().trim()));
            finish();

        }
    }
}
