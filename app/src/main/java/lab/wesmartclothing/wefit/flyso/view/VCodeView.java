package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/7/3.
 */
public class VCodeView extends RelativeLayout {
    private String vCode;
    private Context context;

    @BindView(R.id.edit_password)
    EditText mEditPassword;
    @BindView(R.id.btn_getVCode)
    QMUIRoundButton mBtnGetVCode;

    public VCodeView(Context context) {
        this(context, null);
    }

    public VCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    private void init(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vcode, this, true);
        ButterKnife.bind(this, view);

        mEditPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    vCode = mEditPassword.getText().toString();
                    if (!RxDataUtils.isNumber(vCode)) {
                        RxToast.warning(context.getString(R.string.VCodeError));
                    }
                }
            }
        });

    }

    public void startCountDown() {
        RxUtils.countDown(mBtnGetVCode, 60, 1, context.getString(R.string.getVCode));


    }
}
