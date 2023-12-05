package com.example.map_ui.screens;

import android.annotation.SuppressLint;
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

public class SupportActivity extends AppCompatActivity {
    ImageView home1;
    String profile_pic;
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        home1 = findViewById(R.id.home1);
        try {
            AppPrefrence app = new AppPrefrence(SupportActivity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            name = userObj.getString("name");
            profile_pic = userObj.getString("profile_pic");
            if (!profile_pic.contains("http://")) {
                profile_pic = "http://139.59.21.147:8048/" + profile_pic;
            }
            // get user names
            ((TextView)findViewById(R.id.name)).setText(name+
                    ", ¿Necesitas ayuda?");
        } catch (Exception e) {

        }
        Glide.with(this).load(profile_pic).apply(new RequestOptions().circleCrop()).into((ImageView) findViewById(R.id.imageView2));
        home1.setOnClickListener(new View.OnClickListener() {
            // home1 clicked and go to Map fragment.
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupportActivity.this, MapFragment.class));
                finish();
            }
        });
        // discount button clicked and go to  Discount_Activity.
        findViewById(R.id.discount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupportActivity.this, Discount_Activity.class));
                finish();
            }
        });
        // support button clicked and go to  SupportActivity.
        findViewById(R.id.support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupportActivity.this, SupportActivity.class));
                finish();
            }
        });
        // puntos button clicked and go to   PointsActivity.
        findViewById(R.id.puntos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupportActivity.this, PointsActivity.class));
                finish();
            }
        });

    }
}
