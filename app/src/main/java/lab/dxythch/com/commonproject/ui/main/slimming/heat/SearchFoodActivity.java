package lab.dxythch.com.commonproject.ui.main.slimming.heat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.utils.StatusBarUtils;

public class SearchFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food2);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
    }
}
