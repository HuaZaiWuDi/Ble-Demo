package lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.dateUtils.RxFormat;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.BaseActivity;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.WeightAddBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxBus;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming;
import lab.wesmartclothing.wefit.flyso.tools.Key;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import lab.wesmartclothing.wefit.flyso.utils.WeightTools;


public class WeightDataActivity extends BaseActivity {

    @BindView(R.id.QMUIAppBarLayout)
    QMUITopBar QMUIAppBarLayout;
    @BindView(R.id.tv_receive)
    TextView tv_receive;
    @BindView(R.id.mRecycler_Receive)
    RecyclerView mRecycler_Receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_data);
        ButterKnife.bind(this);
        initView();
        weightHistoryData = getIntent().getExtras().getString(Key.BUNDLE_WEIGHT_HISTORY);
    }


    String weightHistoryData;


    private BaseQuickAdapter adapter_Receive;

    public void initView() {
        initTopBar();
        initRecyclerView();
        initData();
    }

    private void initTopBar() {
        QMUIAppBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter_Receive.getItemCount() > 0)
                    showDialog();
                else RxActivityUtils.finishActivity();
            }
        });
        QMUIAppBarLayout.setTitle("体重数据");
    }


    private void initRecyclerView() {
        final Typeface typeface = MyAPP.typeface;
        mRecycler_Receive.setLayoutManager(new LinearLayoutManager(mContext));

        adapter_Receive = new BaseQuickAdapter<QNScaleStoreData, BaseViewHolder>(R.layout.item_weight_data) {
            @Override
            protected void convert(BaseViewHolder helper, QNScaleStoreData item) {

                helper.setText(R.id.tv_weight, RxFormatValue.fromat4S5R(item.getWeight(), 2))
                        .setTypeface(R.id.tv_weight, typeface);
                helper.setText(R.id.tv_date, "测量时间:" + RxFormat.setFormatDateG8(item.getMeasureTime(), "yyyy年MM月dd日 HH:mm"));
                helper.setVisible(R.id.btn_new, helper.getAdapterPosition() == 0);
                helper.addOnClickListener(R.id.btn_receive);
            }
        };

        adapter_Receive.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RxLogUtils.d("点击了领取");
                addWeightData(position);
            }
        });

        mRecycler_Receive.setAdapter(adapter_Receive);
    }


    private void initData() {
        List<QNScaleStoreData> listReceives = MyAPP.getGson().fromJson(weightHistoryData, new TypeToken<List<QNScaleStoreData>>() {
        }.getType());

        adapter_Receive.setNewData(listReceives);

        tv_receive.setText(getString(R.string.receivedCount, "待", listReceives.size()));
    }


    private void addWeightData(final int position) {
        final QNScaleStoreData qnScaleData = (QNScaleStoreData) adapter_Receive.getItem(position);
        WeightAddBean bean = new WeightAddBean();

        bean.setMeasureTime(qnScaleData.getMeasureTime().getTime() + "");
        QNScaleData scaleData = qnScaleData.generateScaleData();
        if (scaleData != null)
            for (QNScaleItemData item : scaleData.getAllItem()) {
                WeightTools.ble2Backstage(item, bean);
            }
        bean.setWeight(qnScaleData.getWeight());
        String s = MyAPP.getGson().toJson(bean, WeightAddBean.class);

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .addWeightInfo(NetManager.fetchRequest(s)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .compose(RxComposeUtils.<String>showDialog(tipDialog))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("添加体重：" + s);
                        adapter_Receive.remove(position);
                        tv_receive.setText(getString(R.string.receivedCount, "待", adapter_Receive.getItemCount()));

                        //刷新数据
                        RxBus.getInstance().post(new RefreshSlimming());

                    }

                    @Override
                    protected void _onError(String error,int code) {
                        RxToast.error(error,code);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //监听返回按钮，提示如果返回，体重数据将清除
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (adapter_Receive.getItemCount() > 0)
                showDialog();
            else RxActivityUtils.finishActivity();
        return true;
    }


    private void showDialog() {
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setCancelBgColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .setSureBgColor(ContextCompat.getColor(mContext, R.color.green_61D97F))
                .setContent("你还有未领取的体重数据，离开后将全部被忽略？")
                .setSure(getString(R.string.btn_continue))
                .setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxActivityUtils.finishActivity();
                    }
                })
                .setCancel(getString(R.string.btn_continue));
        rxDialog.show();

    }

}
