package lab.wesmartclothing.wefit.flyso.view.picker;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.qqtheme.framework.picker.DatePicker;
import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/8/13.
 */
public class CustomDatePicker extends DatePicker {
    public CustomDatePicker(Activity activity) {
        super(activity);
        setTopLineVisible(false);
        setGravity(Gravity.BOTTOM);
        setHeight((int) (getScreenHeightPixels() * 0.4));
        setTopLineVisible(false);
        setCycleDisable(false);
        setDividerConfig(null);
        setOffset(2);//偏移量
        setTextSize(24);
        setLabel("-", "-", "");
        setUseWeight(true);
        setTextColor(activity.getResources().getColor(R.color.Gray));
    }

    public CustomDatePicker(Activity activity, int mode) {
        super(activity, mode);
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
