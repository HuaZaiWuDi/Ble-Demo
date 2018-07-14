package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.rxbus.WeightIsUpdate;
import lab.wesmartclothing.wefit.flyso.tools.SPKey;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;
import lab.wesmartclothing.wefit.netlib.net.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import lab.wesmartclothing.wefit.netlib.utils.RxBus;
import okhttp3.RequestBody;


@EActivity(R.layout.activity_weight_data)
public class WeightDataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.from(this).setTransparentStatusbar(true).process();
    }


    @ViewById
    RecyclerView mRecycler_Receive;
    @ViewById
    RecyclerView mRecycler_Received;
    @ViewById
    TextView tv_receive;
    @ViewById
    TextView tv_received;

    @Extra
    String BUNDLE_WEIGHT_HISTORY;


    @Click
    void back() {
        if (listReceives.size() > 0)
            showDialog();
        else RxActivityUtils.finishActivity();
    }


    private List<QNScaleStoreData> listReceiveds = new ArrayList<>();
    private List<QNScaleStoreData> listReceives;
    private BaseQuickAdapter adapter_Receive, adapter_Received;

    @Override
    @AfterViews
    public void initView() {
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        mRecycler_Receive.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler_Received.setLayoutManager(new LinearLayoutManager(mContext));

        adapter_Receive = new BaseQuickAdapter<QNScaleStoreData, BaseViewHolder>(R.layout.item_weight_data) {
            @Override
            protected void convert(BaseViewHolder helper, QNScaleStoreData item) {
                helper.setImageResource(R.id.img_weight, R.mipmap.scale_icon);
                helper.setBackgroundRes(R.id.tv_receive, R.mipmap.continue_button);
                helper.setText(R.id.tv_weight_data, item.getWeight() + "kg");
                helper.setText(R.id.tv_date, "测量时间:" + RxFormat.setFormatDateG8(item.getMeasureTime(), "MM/dd HH:mm:ss"));
                helper.addOnClickListener(R.id.tv_receive);
            }
        };

        adapter_Receive.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_receive) {
                    RxLogUtils.d("点击了领取");
                    addWeightData(position);
                }
            }
        });

        mRecycler_Receive.setAdapter(adapter_Receive);
        //已领取
        adapter_Received = new BaseQuickAdapter<QNScaleStoreData, BaseViewHolder>(R.layout.item_weight_data) {
            @Override
            protected void convert(BaseViewHolder helper, QNScaleStoreData item) {
                helper.setImageResource(R.id.img_weight, R.mipmap.scale_icon);
                helper.setBackgroundRes(R.id.tv_receive, R.mipmap.leave_button);
                helper.setText(R.id.tv_weight_data, item.getWeight() + "kg");
                helper.setText(R.id.tv_date, "测量时间:" + RxFormat.setFormatDate(item.getMeasureTime().getTime(), "MM/dd hh:mm:ss"));
            }
        };
        mRecycler_Received.setAdapter(adapter_Received);
    }


    private void initData() {
        listReceives = new Gson().fromJson(BUNDLE_WEIGHT_HISTORY, new TypeToken<List<QNScaleStoreData>>() {
        }.getType());
        if (listReceives == null) {
            RxToast.error("获取异常");
            return;
        }
        listReceiveds.clear();
        adapter_Receive.setNewData(listReceives);
        adapter_Received.setNewData(listReceiveds);

        tv_received.setText(getString(R.string.receivedCount, "已", listReceiveds.size()));
        tv_receive.setText(getString(R.string.receivedCount, "待", listReceives.size()));

    }


    private void addWeightData(final int position) {
        final QNScaleStoreData qnScaleData = listReceives.get(position);

        WeightAddBean bean = new WeightAddBean();
        bean.setUserId(SPUtils.getString(SPKey.SP_UserId));
        bean.setMeasureTime(System.currentTimeMillis() + "");
        QNScaleData scaleData = qnScaleData.generateScaleData();
        if (scaleData != null)
            for (QNScaleItemData item : scaleData.getAllItem()) {
                WeightTools.ble2Backstage(item, bean);
            }
        bean.setWeight(qnScaleData.getWeight());
        String s = new Gson().toJson(bean, WeightAddBean.class);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.addWeightInfo(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加体重：" + s);
                        adapter_Received.addData(qnScaleData);
                        adapter_Receive.remove(position);
                        tv_received.setText(getString(R.string.receivedCount, "已", listReceiveds.size()));
                        tv_receive.setText(getString(R.string.receivedCount, "待", listReceives.size()));
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().post(new WeightIsUpdate(true));
        super.onDestroy();
    }

    //监听返回按钮，提示如果返回，体重数据将清除
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (listReceives.size() > 0)
                showDialog();
            else RxActivityUtils.finishActivity();
        return true;
    }


    private void showDialog() {
        final RxDialogSureCancel dialog = new RxDialogSureCancel(mActivity);
        dialog.getTvTitle().setBackgroundResource(R.mipmap.leave_icon);
        dialog.getTvContent().setText("你还有未领取的体重数据，\n离开后将全部被忽略\n？");
        dialog.setCancel(getString(R.string.btn_leave));
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setSure(getString(R.string.btn_continue));
        dialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
