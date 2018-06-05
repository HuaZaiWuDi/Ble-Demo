package com.smartclothing.module_wefit.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.adapter.MessagePagerAdapter;
import com.smartclothing.module_wefit.fragment.MessagePagerItemFragment;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.smartclothing.module_wefit.widget.PagerSlidingTabStrip;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;

/*消息*/

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout iv_back;
    private TextView tv_right_commit;

    PagerSlidingTabStrip slider;
    ViewPager pager;
    private MessagePagerItemFragment fragmentLessen;
    private MessagePagerItemFragment fragmentNews;
    private List<MessagePagerItemFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        initView();

    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_right_commit = findViewById(R.id.tv_right_commit);

        slider = findViewById(R.id.slider_message);
        pager = findViewById(R.id.pager_message);

        fragments = new ArrayList<>();
        fragmentLessen = new MessagePagerItemFragment();
        fragmentLessen.setMsgType(2);
        fragmentLessen.setOnReadStateChangedListener(new MessagePagerItemFragment.readStateChangedListener() {
            @Override
            public void allReadState(boolean isAllRead) {
                if (isAllRead) {
                    tv_right_commit.setTextColor(getResources().getColor(R.color.c));
                    tv_right_commit.setClickable(false);
                } else {
                    tv_right_commit.setTextColor(getResources().getColor(R.color.c));
                    tv_right_commit.setClickable(true);
                }
            }
        });
        fragmentNews = new MessagePagerItemFragment();
        fragmentNews.setMsgType(1);
        fragments.add(fragmentLessen);
        fragments.add(fragmentNews);
        pager.setAdapter(new MessagePagerAdapter(getSupportFragmentManager(), fragments));
        slider.setViewPager(pager);

        listener();
    }

    private void listener() {
        iv_back.setOnClickListener(this);
        tv_right_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();

        } else if (i == R.id.tv_right_commit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("全部标记已读？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            readAllRequest(pager.getCurrentItem() == 0 ? 2 : 1);
                        }
                    }).setNegativeButton(R.string.cancel, null);
            final AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            window.setDimAmount(0.3f);
            window.setWindowAnimations(R.style.dialogWindowAnim);
            dialog.show();

        }
    }

    private void readAllRequest(final int msgType) {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.readedAll(msgType))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        fragments.get(pager.getCurrentItem()).updateRecycler();
                    }

                    @Override
                    public void _onError(String e) {
                        RxToast.showToast(e);
                    }
                });
    }
}
