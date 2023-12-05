package com.example.map_ui.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.map_ui.R;
// Otp text watcher laibrary.
public class GenericTextWatcher implements TextWatcher {
    EditText curEdt;
    EditText nextEdt;
    public GenericTextWatcher(EditText curEdt,EditText nextEdt){
        this.curEdt = curEdt;
        this.nextEdt = nextEdt;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (curEdt.getId()) {
            case R.id.otpET1: if (text.length() == 1) nextEdt.requestFocus();
            case R.id.otpET2: if (text.length() == 1) nextEdt.requestFocus();
            case R.id.otpET3: if (text.length() == 1) nextEdt.requestFocus();
            case R.id.otpET4: if (text.length() == 1) nextEdt.requestFocus();
            case R.id.otpET5: if (text.length() == 1) nextEdt.requestFocus();
            //You can use EditText4 same as above to hide the keyboard
        }
    }
}
