package lab.dxythch.com.commonproject.ui.main.slimming.heat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseActivity;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;

public class AddFoodActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
        initView();
    }

    @Override
    public void initView() {
        initRecycler();
    }

    private void initRecycler() {
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mRecyclerView.setAdapter();
    }

}
