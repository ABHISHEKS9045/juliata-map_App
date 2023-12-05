package com.example.map_ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.MainActivity;
import com.example.map_ui.R;

public class TermsConditions_Activity extends AppCompatActivity {
    ImageView finish_btnTC, back_btnTC;
    CheckBox androidCheckBoxTC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        back_btnTC=findViewById(R.id.back_btnTC);
        finish_btnTC=findViewById(R.id.finish_btnTC);
        androidCheckBoxTC=findViewById(R.id.androidCheckBoxTC);

        back_btnTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(TermsConditions_Activity.this, PasswordConfiguration_Activity.class);
                startActivity(i);
            }
        });
        // check Term and Conditions data get
        finish_btnTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!androidCheckBoxTC.isChecked()){
                    Toast.makeText(TermsConditions_Activity.this, "Acepte los términos y condiciones.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent finish = new Intent(TermsConditions_Activity.this, MainActivity.class);
                startActivity(finish);
            }
        });

    }
}
