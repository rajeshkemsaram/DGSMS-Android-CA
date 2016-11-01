package com.ideabytes.dgsms.ca.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

/**
 * Created by suman on 13/5/16.
 */
public class TextWatcherBillOfLadding implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.e("Suman "," "+s);
    }
}
