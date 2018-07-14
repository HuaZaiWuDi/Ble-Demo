package com.smartclothing.module_wefit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.base.BaseActivity;
import com.smartclothing.module_wefit.widget.MClearEditText;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.view.RxToast;

/*编辑电话*/

public class PhoneEditActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_data_save;
    private MClearEditText et_data_phone;
    private static final int RESULT_CODE_PHONE = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_edit);

        initView();

    }

    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_data_save = findViewById(R.id.tv_data_save);
        et_data_phone = findViewById(R.id.et_data_phone);
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
            String phone = et_data_phone.getText().toString();
            if (phone.length() != 0) {
                if (RxRegUtils.isMobileExact(phone)) {
                    setResult(RESULT_CODE_PHONE, new Intent().putExtra("phone", phone));
                    finish();
                } else {
                    RxToast.showToast("请输入正确的手机号码");
                }
            } else {
                RxToast.showToast("联系电话不能为空");
            }

        }
    }
}
