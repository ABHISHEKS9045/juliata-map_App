package com.example.map_ui.utility;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.map_ui.R;


public class GenericKeyEvent implements View.OnKeyListener {
    EditText currentEdt;
    EditText preEdittext;
    public GenericKeyEvent(EditText currentEdt,EditText preEdittext){
        this.currentEdt = currentEdt;
        this.preEdittext = preEdittext;
    }
// gereticKey fuction
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_DEL && currentEdt.getId() != R.id.otpET1 && TextUtils.isEmpty(currentEdt.getText())) {
            //If current is empty then previous EditText's number will also be deleted
            preEdittext.setText(null);
            preEdittext.requestFocus();
            return true;
        }
        return false;
    }
}
