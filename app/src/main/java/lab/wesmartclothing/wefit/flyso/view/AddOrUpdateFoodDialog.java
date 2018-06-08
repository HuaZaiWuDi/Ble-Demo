package lab.wesmartclothing.wefit.flyso.view;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.wesmartclothing.wefit.flyso.R;
import lab.wesmartclothing.wefit.flyso.entity.AddFoodItem;
import lab.wesmartclothing.wefit.flyso.entity.AddedHeatInfo;
import lab.wesmartclothing.wefit.flyso.entity.ListBean;
import lab.wesmartclothing.wefit.flyso.netserivce.RetrofitService;
import lab.wesmartclothing.wefit.netlib.rx.NetManager;
import lab.wesmartclothing.wefit.netlib.rx.RxManager;
import lab.wesmartclothing.wefit.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/5/14.
 */
public class AddOrUpdateFoodDialog {

    private Context mContext;
    private boolean isAdd;
    private AddOrUpdateFoodListener addOrUpdateFoodListener;

    public AddOrUpdateFoodDialog(Context context, boolean isAdd, ListBean listBean, AddOrUpdateFoodListener addOrUpdateFoodListener) {
        mContext = context;
        this.isAdd = isAdd;
        this.addOrUpdateFoodListener = addOrUpdateFoodListener;
        if (isAdd)
            getAddedHeatInfo(listBean);
        else showAddFoodDialog(listBean);
    }


    private void showAddFoodDialog(final ListBean item) {


        View view = View.inflate(mContext, R.layout.dialogfragment_add_food, null);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view).create();

        if (!dialog.isShowing()) {
            dialog.show();
        }

        ImageView img_food = view.findViewById(R.id.img_food);
        TextView tv_foodName = view.findViewById(R.id.tv_foodName);
        TextView tv_foodInfo = view.findViewById(R.id.tv_info);
        TextView tv_heat = view.findViewById(R.id.tv_heat);
        final EditText et_food_g = view.findViewById(R.id.et_food_g);
        TextView tv_unit = view.findViewById(R.id.tv_unit);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView ok = view.findViewById(R.id.ok);

        loadCricle(item.getFoodImg(), img_food);
        if (!isAdd)
            tv_foodInfo.setVisibility(View.GONE);

        tv_foodName.setText(item.getFoodName());
        tv_foodInfo.setText(item.getRemark());
        tv_unit.setText(item.getFoodUnit());

        et_food_g.setText(item.getFoodCount() + "");


        RxTextUtils.getBuilder("")
                .append(item.getCalorie() + mContext.getString(R.string.unit_kcal)).setForegroundColor(mContext.getResources().getColor(R.color.colorTheme))
                .append(mContext.getString(R.string.unit_heat))
                .into(tv_heat);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodCount = et_food_g.getText().toString();
                if (!"0".equals(foodCount) && !RxDataUtils.isNullString(foodCount)) {
                    item.setFoodCount(Integer.parseInt(foodCount));
                } else {
                    RxToast.warning(mContext.getString(R.string.weightNoZero));
                    return;
                }
                AddFoodItem.intakeList intakeList = new AddFoodItem.intakeList();
                intakeList.setFoodId(isAdd ? item.getGid() : item.getFoodId());
                intakeList.setFoodName(item.getFoodName());
                intakeList.setFoodCount(item.getFoodCount());
                intakeList.setUnit(item.getFoodUnit());

                intakeList.setGid(isAdd ? item.getMGid() : item.getGid());
                //暂时不用传
                intakeList.setWeight("");
                intakeList.setWeightType("");

                addOrUpdateFoodListener.complete(intakeList);
                dialog.dismiss();
            }
        });
    }


    private void getAddedHeatInfo(final ListBean item) {
        RxLogUtils.i("食物信息：" + item.toString());
        AddedHeatInfo heatInfo = new AddedHeatInfo();
        listBean2Added(item, heatInfo);

        String s = new Gson().toJson(heatInfo, AddedHeatInfo.class);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getAddedHeatInfo(body))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
                        AddedHeatInfo addedHeatInfo = new Gson().fromJson(s, AddedHeatInfo.class);
                        if ("".equals(addedHeatInfo.getGid())) {
                            showAddFoodDialog(item);
                        } else {
                            item.setFoodCount(addedHeatInfo.getFoodCount());
                            showAddFoodDialog(item);
                            item.setMGid(addedHeatInfo.getGid());
                        }
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }


    private void listBean2Added(ListBean item, AddedHeatInfo heatInfo) {
        heatInfo.setCalorie(item.getCalorie());
        heatInfo.setEatType(item.getEatType());
        heatInfo.setFoodCount(item.getFoodCount());
        heatInfo.setFoodId(item.getGid());
        heatInfo.setFoodName(item.getFoodName());
        heatInfo.setUnit(item.getFoodUnit());
        heatInfo.setHeatDate(item.getHeatDate());
        heatInfo.setRemark(item.getRemark());
    }


    public interface AddOrUpdateFoodListener {
        void complete(AddFoodItem.intakeList item);
    }


    public void loadCricle(String img_url, @NonNull ImageView img) {
        Glide.with(mContext)
                .load(img_url)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CropCircleTransformation(mContext))//圆角图片
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img);
    }
}
