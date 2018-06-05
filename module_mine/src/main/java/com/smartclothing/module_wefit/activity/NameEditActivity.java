package com.smartclothing.module_wefit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.widget.MClearEditText;
import com.vondear.rxtools.view.RxToast;

/*编辑昵称*/

public class NameEditActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_data_save;
    private MClearEditText et_data_name;
    private static final int RESULT_CODE_NAME = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name_edit);

        initView();

    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_data_save = findViewById(R.id.tv_data_save);
        et_data_name = findViewById(R.id.et_data_name);
        String string = getIntent().getExtras().getString(PersonalDataActivity.Dafult_Data);
        et_data_name.setText(string);
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
            if (et_data_name.getText().length() == 0) {
                RxToast.showToast("用户名不能为空");
                return;
            }
            setResult(RESULT_CODE_NAME, new Intent().putExtra("name", "" + et_data_name.getText().toString()));
            finish();

        }
    }
}
