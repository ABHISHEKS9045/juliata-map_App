package com.example.map_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class puntos_ArtBox extends AppCompatActivity {
RadioButton radioButton3,radioButton4,radioButton5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_art_box);


        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"radiobutton",Toast.LENGTH_SHORT).show();
            }
        });
    }



}