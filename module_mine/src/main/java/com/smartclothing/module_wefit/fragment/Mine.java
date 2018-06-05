package com.smartclothing.module_wefit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartclothing.module_wefit.R;
import com.smartclothing.module_wefit.activity.AboutActivity;
import com.smartclothing.module_wefit.activity.CollectActivity;
import com.smartclothing.module_wefit.activity.MessageActivity;
import com.smartclothing.module_wefit.activity.MyDeviceActivity;
import com.smartclothing.module_wefit.activity.PersonalDataActivity;
import com.smartclothing.module_wefit.activity.ProblemSuggestActivity;
import com.smartclothing.module_wefit.activity.SetActivity;
import com.smartclothing.module_wefit.bean.UserCenterBean;
import com.smartclothing.module_wefit.net.net.RetrofitService;
import com.smartclothing.module_wefit.tools.CircleImageView;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import io.reactivex.functions.Action;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;


/**
 * "我的"
 */
public class Mine extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_UPDATE_USER = 10001;
    private LinearLayout iv_mine_set;
    private LinearLayout ll_iv_mine_msg;
    private LinearLayout ll_diet_record;
    private LinearLayout ll_sport_record;
    private LinearLayout ll_weight_record;
    private RelativeLayout rl_my_collect;
    private RelativeLayout rl_my_order;
    private RelativeLayout rl_my_shop_car;
    private RelativeLayout rl_my_device;
    private RelativeLayout rl_my_problem_suggest;
    private RelativeLayout rl_my_about;
    private CircleImageView iv_mine_icon;
    private ImageView iv_mine_msg;
    private TextView user_name;
    private TextView tv_collect_num;
    private TextView tv_device_num;
    private TextView user_slogan;
    private View icon_unread;


    public static Mine getInstance() {
        return new Mine();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View meLayout = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(meLayout);
        RxUtils.init(getContext());
        initData();
        return meLayout;
    }

    private void initView(View v) {
        iv_mine_set = v.findViewById(R.id.iv_mine_set);
        ll_iv_mine_msg = v.findViewById(R.id.ll_iv_mine_msg);
        iv_mine_msg = v.findViewById(R.id.iv_mine_msg);
        ll_diet_record = v.findViewById(R.id.ll_diet_record);
        ll_sport_record = v.findViewById(R.id.ll_sport_record);
        ll_weight_record = v.findViewById(R.id.ll_weight_record);
        rl_my_collect = v.findViewById(R.id.rl_my_collect);
        rl_my_order = v.findViewById(R.id.rl_my_order);
        rl_my_shop_car = v.findViewById(R.id.rl_my_shop_car);
        rl_my_device = v.findViewById(R.id.rl_my_device);
        rl_my_problem_suggest = v.findViewById(R.id.rl_my_problem_suggest);
        rl_my_about = v.findViewById(R.id.rl_my_about);
        iv_mine_icon = v.findViewById(R.id.iv_mine_icon);
        user_name = v.findViewById(R.id.user_name);
        tv_collect_num = v.findViewById(R.id.tv_collect_num);
        tv_device_num = v.findViewById(R.id.tv_device_num);
        icon_unread = v.findViewById(R.id.icon_unread);
        user_slogan = v.findViewById(R.id.user_slogan);
        setOnClickListener();
    }

    private void setOnClickListener() {
        iv_mine_set.setOnClickListener(this);
        ll_iv_mine_msg.setOnClickListener(this);
        ll_diet_record.setOnClickListener(this);
        ll_sport_record.setOnClickListener(this);
        ll_weight_record.setOnClickListener(this);
        rl_my_collect.setOnClickListener(this);
        rl_my_order.setOnClickListener(this);
        rl_my_shop_car.setOnClickListener(this);
        rl_my_device.setOnClickListener(this);
        rl_my_problem_suggest.setOnClickListener(this);
        rl_my_about.setOnClickListener(this);
        iv_mine_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_mine_set) {
            startActivity(new Intent(getContext(), SetActivity.class));

        } else if (i == R.id.ll_iv_mine_msg) {
            startActivity(new Intent(getContext(), MessageActivity.class));

        } else if (i == R.id.ll_diet_record) {
        } else if (i == R.id.ll_sport_record) {
        } else if (i == R.id.ll_weight_record) {
        } else if (i == R.id.rl_my_collect) {
            startActivity(new Intent(getContext(), CollectActivity.class));

        } else if (i == R.id.rl_my_order) {
        } else if (i == R.id.rl_my_shop_car) {
        } else if (i == R.id.rl_my_device) {
            startActivity(new Intent(getContext(), MyDeviceActivity.class));

        } else if (i == R.id.rl_my_problem_suggest) {
            startActivity(new Intent(getContext(), ProblemSuggestActivity.class));

        } else if (i == R.id.rl_my_about) {
            startActivity(new Intent(getContext(), AboutActivity.class));

        } else if (i == R.id.iv_mine_icon) {
            startActivityForResult(new Intent(getActivity(), PersonalDataActivity.class), REQUEST_CODE_UPDATE_USER);

        }
    }

    private void initData() {
        RetrofitService dxyService = NetManager.getInstance().createString(
                RetrofitService.class
        );
        RxManager.getInstance().doNetSubscribe(dxyService.userCenter())
                .doFinally(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                RxLogUtils.d("结束");
                            }
                        })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        Gson gson = new Gson();
                        UserCenterBean user = gson.fromJson(s,
                                new TypeToken<UserCenterBean>() {
                                }.getType());
                        user_name.setText(user.getUserName());
                        tv_collect_num.setText(user.getCollectCount() + "");
                        tv_device_num.setText(user.getBindCount() + "");
                        String url = user.getImgUrl().replace("\\", "");
                        Glide.with(getActivity()).load(url)
                                .into(iv_mine_icon);//.placeholder(R.mipmap.img_touxiang)
                        //更新消息未读状态
                        if (user.getUnreadCount() == 0) {
                            icon_unread.setVisibility(View.GONE);
                        } else {
                            icon_unread.setVisibility(View.VISIBLE);
                        }
                        user_slogan.setText(user.getSignature());
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //修改个人中心数据成功，返回时加载新数据
        if (requestCode == REQUEST_CODE_UPDATE_USER && resultCode == getActivity().RESULT_OK) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}