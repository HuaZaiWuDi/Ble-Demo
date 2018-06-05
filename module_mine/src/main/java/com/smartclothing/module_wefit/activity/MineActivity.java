package com.smartclothing.module_wefit.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.fragment.Mine;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mine);

        Mine mine = new Mine();

        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, mine);
        transaction.commit();
    }
}
