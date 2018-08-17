package lab.wesmartclothing.wefit.flyso.view.picker;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/8/13.
 */
public class CustomAddressPicker extends AddressPicker {


    public CustomAddressPicker(Activity activity, ArrayList<Province> provinces) {
        super(activity, provinces);
        setTopLineVisible(false);
        setGravity(Gravity.BOTTOM);
        setHeight((int) (getScreenHeightPixels() * 0.4));
        setDividerConfig(null);
        setTextSize(24);
        setLabel("-", "-", "");
        setUseWeight(true);
        setTextColor(activity.getResources().getColor(R.color.Gray));

    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        return null;
    }


    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_picker_footer, null);
        TextView picker_cancel = view.findViewById(R.id.tv_cancel);
        picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        TextView picker_ok = view.findViewById(R.id.tv_ok);
        picker_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        });
        return view;
    }

}
