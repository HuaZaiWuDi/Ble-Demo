package lab.dxythch.com.commonproject.view;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.view.RxToast;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.entity.AddFoodItem;
import lab.dxythch.com.commonproject.entity.ListBean;

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
        showAddFoodDialog(listBean);
    }


    private void showAddFoodDialog(final ListBean item) {
        View view = View.inflate(mContext, R.layout.dialogfragment_add_food, null);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .show();

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
        tv_unit.setText(item.getUnit());

        et_food_g.setText(item.getUnitCount() + "");


        RxTextUtils.getBuilder("")
                .append(item.getHeat() + mContext.getString(R.string.unit_kcal)).setForegroundColor(mContext.getResources().getColor(R.color.colorTheme))
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
                    item.setUnitCount(Integer.parseInt(foodCount));
                } else {
                    RxToast.warning(mContext.getString(R.string.weightNoZero));
                    return;
                }
                AddFoodItem.intakeList intakeList = new AddFoodItem.intakeList();
                intakeList.setFoodId(!isAdd ? item.getFoodId() : item.getGid());
                intakeList.setFoodName(item.getFoodName());
                intakeList.setFoodCount(item.getUnitCount());
                intakeList.setFoodUnit(item.getUnit());

                intakeList.setGid(!isAdd ? item.getGid() : "");
                //暂时不用传
                intakeList.setWeight("");
                intakeList.setWeightType("");

                addOrUpdateFoodListener.complete(intakeList);
                dialog.dismiss();
            }
        });
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
