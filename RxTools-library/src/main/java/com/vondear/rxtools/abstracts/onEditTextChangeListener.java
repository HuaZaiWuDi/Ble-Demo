package com.vondear.rxtools.abstracts;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @Package com.vondear.rxtools.interfaces
 * @FileName onEditTextChangeListener
 * @Date 2018/9/18 15:46
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit
 */
public abstract class onEditTextChangeListener implements TextWatcher {


    public void beforeTextChanged(CharSequence s, int start,
                                  int count, int after) {
    }

    public void afterTextChanged(Editable s) {

    }

}
