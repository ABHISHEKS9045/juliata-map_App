package com.example.map_ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.utility.AppPrefrence;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONObject;

public class ResultScreenActivity extends AppCompatActivity {
    int open;
    TextView txt1, txt2, txt01, txt02;
    ImageView tab1,tab2,tab3,tab4;
    String profile_pic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Ui screen not sleeping mode .
        try{
            AppPrefrence app = new AppPrefrence(ResultScreenActivity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
//            name = userObj.getString("name");
            profile_pic = userObj.getString("profile_pic");
            if(!profile_pic.contains("http://")){
                profile_pic = "http://139.59.21.147:8048/"+profile_pic;
            }
        }catch (Exception e){

        }
        // get profile pic
        Glide.with(this).load(profile_pic).apply(new RequestOptions().circleCrop()).into((ImageView) findViewById(R.id.imageView2));
        tab1=findViewById (R.id.tab1);
        tab2=findViewById (R.id.tab2);
        tab3=findViewById (R.id.tab3);
//       tab4=findViewById (R.id.tab4);
        // tab1 clicked and go to Map fragment.
        tab1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//
                startActivity(new Intent(ResultScreenActivity.this, MapFragment.class));
                finish ();

            }
        });
        // tab2 clicked and go to support Activity.
        tab2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultScreenActivity.this, SupportActivity.class));
                finish ();

            }
        });
        // tab2 clicked and go to Discount/Promosion Activity.
        tab3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultScreenActivity.this, Discount_Activity.class));
                finish ();

            }
        });


        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt01 = findViewById(R.id.txt01);
        txt02 = findViewById(R.id.txt02);
        open = getIntent().getIntExtra("OPEN", 0);
        // Api calling resultScreen Pyment mode available balance this open Ui
        if (open == 1) {
            txt1.setText("Aplana Calls\n\nTU CANJE SE HA REALIZADO CON ÉXITO");
            txt2.setText("Las instrucciones serán \nenviadas a tu mail y celular");
            txt01.setText("");
            txt02.setText("GRACIAS POR TU APORTE A Julieta");
        } else {
// Api calling resultScreen Pyment mode  not available balance this open Ui
            txt1.setText("Aplana Calls\n\nTU SALDO ES INSUFICIENTE PARA ESE CANJE");
            txt2.setText("ATRÁS");
            txt01.setText("");
            txt02.setText("GRACIAS POR TU APORTE A Julieta");
            findViewById(R.id.card_view2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ResultScreenActivity.this, PointsActivity.class));
                    finish();
                }
            });
        }
    }
}
