package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.wesmartclothing.wefit.flyso.R;

/**
 * Created by jk on 2018/7/3.
 */
public class PasswordView extends RelativeLayout {
    @BindView(R.id.edit_password)
    EditText mEditPassword;
    @BindView(R.id.img_biyan)
    ImageView mImgBiyan;
    private Context mContext;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_password, this, true);
        ButterKnife.bind(this, view);

        mImgBiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditPassword.setInputType(mEditPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ?
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mImgBiyan.setBackgroundResource(mEditPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ?
                        R.mipmap.icon_dispaly_password : R.mipmap.icon_hide_password);
            }
        });
    }

    public EditText getPassword() {
        return mEditPassword;
    }
}
