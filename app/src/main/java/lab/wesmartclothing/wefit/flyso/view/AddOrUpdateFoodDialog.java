package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.vondear.rxtools.editview.DecimalDigitsInputFilter;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxKeyboardUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.layout.RxEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.subjects.BehaviorSubject;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.base.MyAPP;
import lab.wesmartclothing.wefit.flyso.entity.AddedHeatInfo;
import lab.wesmartclothing.wefit.flyso.entity.FoodListBean;
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.LifeCycleEvent;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxManager;
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber;
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created icon_hide_password jk on 2018/5/14.
 */
public class AddOrUpdateFoodDialog {

    @BindView(R.id.tv_delete)
    ImageView mTvDelete;
    @BindView(R.id.img_food)
    QMUIRadiusImageView mImgFood;
    @BindView(R.id.tv_foodName)
    TextView mTvFoodName;
    @BindView(R.id.tv_heat)
    TextView mTvHeat;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.et_food_g)
    RxEditText mEtFoodG;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.ok)
    TextView mOk;

    private Context mContext;
    private boolean showDelete;//是否显示删除按钮
    private AddOrUpdateFoodListener addOrUpdateFoodListener;
    private DeleteFoodListener mDeleteFoodListener;
    private RxDialog dialog;
    private FoodListBean listBean;
    private int foodType;
    private long currentTime;
    private BehaviorSubject<LifeCycleEvent> lifecycleSubject;//传入生命周期限制

    public void setFoodInfo(Context context, boolean showDelete, int foodType, long currentTime, FoodListBean listBean) {
        mContext = context;
        View view = View.inflate(mContext, R.layout.dialogfragment_add_food, null);
        ButterKnife.bind(this, view);

        dialog = new RxDialog(mContext);
        dialog.setContentView(view);
        dialog.setFullScreenWidth(0.9f);

        this.showDelete = showDelete;
        this.listBean = listBean;
        this.foodType = foodType;
        this.currentTime = currentTime;
        getAddedHeatInfo();//icon_add_white：先请求是否添加过，未添加吧foodId赋值给Gid，添加过直接上传Gid
    }

    public void setLifecycleSubject(BehaviorSubject<LifeCycleEvent> lifecycleSubject) {
        this.lifecycleSubject = lifecycleSubject;
    }

    private void showAddFoodDialog() {
        MyAPP.getImageLoader().displayImage(mContext, listBean.getFoodImg(), mImgFood);
        if (!showDelete)
            mTvDelete.setVisibility(View.INVISIBLE);

        mTvFoodName.setText(listBean.getFoodName());
        mTvUnit.setText(listBean.getUnit());

        if (listBean.getFoodCount() != 0)
            mEtFoodG.setText(listBean.getFoodCount() + "");
//        RxKeyboardUtils.showSoftInput(mEtFoodG);
        RxKeyboardUtils.toggleSoftInput();
        mTvHeat.setText(listBean.getUnitCalorie() + "kcal/" + listBean.getUnitCount() + listBean.getUnit());

        if (dialog.isShowing()) dialog.dismiss();
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RxKeyboardUtils.toggleSoftInput();
            }
        });

        mEtFoodG.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(1)});
        mEtFoodG.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (RxDataUtils.stringToDouble(String.valueOf(s)) > 999.0) {
                    mEtFoodG.setText("999.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void getAddedHeatInfo() {
        AddedHeatInfo heatInfo = new AddedHeatInfo();
        listBean2Added(listBean, heatInfo);

        String s = MyAPP.getGson().toJson(heatInfo, AddedHeatInfo.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().getAddedHeatInfo(body))
                .compose(RxComposeUtils.<String>showDialog(new TipDialog(mContext)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        FoodListBean addedHeatInfo = MyAPP.getGson().fromJson(s, FoodListBean.class);
                        String foodImg = listBean.getFoodImg();
                        listBean = addedHeatInfo;
                        listBean.setFoodImg(foodImg);
                        showAddFoodDialog();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

    private void listBean2Added(FoodListBean item, AddedHeatInfo heatInfo) {
        heatInfo.setCalorie(item.getCalorie());
        heatInfo.setEatType(foodType);
        heatInfo.setFoodCount(item.getFoodCount());
        heatInfo.setFoodName(item.getFoodName());
        heatInfo.setUnit(item.getUnit());
        heatInfo.setHeatDate(currentTime);
        heatInfo.setRemark(item.getRemark());
        heatInfo.setFoodId(RxDataUtils.isNullString(item.getFoodId()) ? item.getGid() : item.getFoodId());
        heatInfo.setGid(RxDataUtils.isNullString(item.getGid()) ? item.getFoodId() : item.getGid());
    }


    @OnClick({R.id.tv_delete, R.id.cancel, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                deleteData(mContext, listBean);
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.ok:
                dialog.dismiss();
                String foodCount = mEtFoodG.getText().toString();
                if (!"0".equals(foodCount) && !RxDataUtils.isNullString(foodCount)) {
                    double count = Double.parseDouble(foodCount);
                    listBean.setCalorie((int) (listBean.getUnitCalorie() * 1f / listBean.getUnitCount() * count));
                    listBean.setFoodCount(count);
                } else {
                    RxToast.warning(mContext.getString(R.string.weightNoZero));
                    return;
                }
                addOrUpdateFoodListener.complete(listBean);
                break;
        }
    }


    public int isExist(List<FoodListBean> addedLists, FoodListBean needFood) {
        for (int i = 0; i < addedLists.size(); i++) {
            RxLogUtils.d("食材：" + addedLists.get(i).toString());
            if (needFood.getGid().equals(addedLists.get(i).getGid())) {
                return i;
            }
        }
        return -1;
    }


    public interface AddOrUpdateFoodListener {
        void complete(FoodListBean listBean);
    }

    public interface DeleteFoodListener {
        void deleteFood(FoodListBean listBean);
    }

    public void setDeleteFoodListener(DeleteFoodListener deleteFoodListener) {
        mDeleteFoodListener = deleteFoodListener;
    }


    public void setAddOrUpdateFoodListener(AddOrUpdateFoodListener addOrUpdateFoodListener) {
        this.addOrUpdateFoodListener = addOrUpdateFoodListener;
    }


    public void deleteData(Context context, final FoodListBean listBean) {
        RxLogUtils.d("删除食材：" + listBean);
        JsonObject object = new JsonObject();
        object.addProperty("gid", listBean.getGid());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().removeHeatInfo(body))
                .compose(RxComposeUtils.<String>showDialog(new TipDialog(context)))
                .compose(RxComposeUtils.<String>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        if (mDeleteFoodListener != null)
                            mDeleteFoodListener.deleteFood(listBean);
                        if (dialog != null)
                            dialog.dismiss();
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }

                });
    }

}
