package com.smartclothing.module_wefit.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import com.smartclothing.module_wefit.R;

public class BaseDialog extends Dialog {
	
	@SuppressLint("InlinedApi")
	public BaseDialog(Context context) {
		super(context);
		getContext().setTheme(R.style.CustomDialogStyle);
	}

}