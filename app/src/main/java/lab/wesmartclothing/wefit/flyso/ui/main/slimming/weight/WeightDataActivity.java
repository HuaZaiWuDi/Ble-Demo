package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.flyso.prefs.Prefs_;
import lab.wesmartclothing.wefit.flyso.rxbus.WeightIsUpdate;
import lab.wesmartclothing.wefit.flyso.utils.StatusBarUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;
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

    @Pref
    Prefs_ mPrefs;

    @Click
    void back() {
        showDialog();
    }

    private List<QNScaleData> listReceiveds = new ArrayList<>();
    private List<QNScaleData> listReceives;
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

        adapter_Receive = new BaseQuickAdapter<QNScaleData, BaseViewHolder>(R.layout.item_weight_data) {
            @Override
            protected void convert(BaseViewHolder helper, QNScaleData item) {
                helper.setImageResource(R.id.img_weight, R.mipmap.scale_icon);
                helper.setBackgroundRes(R.id.tv_receive, R.mipmap.continue_button);
                helper.setText(R.id.tv_weight_data, item.getItemValue(1) + "kg");
                helper.setText(R.id.tv_date, "测量时间:" + RxFormat.setFormatDate(item.getMeasureTime().getTime(), "MM/dd hh:mm:ss"));
                helper.addOnClickListener(R.id.tv_receive);
            }
        };

        adapter_Receive.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_receive) {
                    RxLogUtils.d("点击了领取");
                    showReceiveWeightData(position);
                }
            }
        });

        mRecycler_Receive.setAdapter(adapter_Receive);
        //已领取
        adapter_Received = new BaseQuickAdapter<QNScaleData, BaseViewHolder>(R.layout.item_weight_data) {
            @Override
            protected void convert(BaseViewHolder helper, QNScaleData item) {
                helper.setImageResource(R.id.img_weight, R.mipmap.scale_icon);
                helper.setBackgroundRes(R.id.tv_receive, R.mipmap.leave_button);
                helper.setText(R.id.tv_weight_data, item.getItemValue(1) + "kg");
                helper.setText(R.id.tv_date, "测量时间:" + RxFormat.setFormatDate(item.getMeasureTime().getTime(), "MM/dd hh:mm:ss"));
            }
        };
        mRecycler_Received.setAdapter(adapter_Received);
    }

    private void showReceiveWeightData(final int position) {
        View view = View.inflate(mContext, R.layout.dialog_receive_weight, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view).show();
        view.findViewById(R.id.btn_leave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 接受体重信息
                dialog.dismiss();
                addWeightData(position);
            }
        });
    }

    private void initData() {
        listReceives = new Gson().fromJson(BUNDLE_WEIGHT_HISTORY, new TypeToken<List<QNScaleData>>() {
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
        final QNScaleData qnScaleData = listReceives.get(position);
        String userId = mPrefs.UserId().getOr("testuser");
        RxLogUtils.d("用户ID" + userId);
        WeightAddBean bean = new WeightAddBean();
        bean.setUserId(userId);
        bean.setMeasureTime(System.currentTimeMillis() + "");
        for (QNScaleItemData item : qnScaleData.getAllItem()) {
            WeightTools.ble2Backstage(item, bean);
        }
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
        showDialog();
        return true;
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("退出界面，体重数据将被清除")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        if (listReceives != null)
                            listReceives.clear();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
