package com.example.map_ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.utility.AppPrefrence;

public class Splash_Activity extends AppCompatActivity {

    ImageView buttonS;
    private long pressedTime;
    private final int SPLASH_SCREEN_TIME_OUT = 2000;

    //new code 1 sept
    final String TAG = this.getClass().getName();
    private static int SPLASH_TIME_OUT = 4000;
    private TextView saying;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
//                Intent i = new Intent(Splash_Activity.this, DriverDetails_Activity.class);
//                i.putExtra("MO_NO", "mobileNo");
//                startActivity(i);
//                finish();
                // splase screen go to save data to mainActivity.
                AppPrefrence app = new AppPrefrence(Splash_Activity.this);
                if(app.getBoolean("isLogin")){
                    Intent i = new Intent(Splash_Activity.this, MainActivity.class);
//                     i = new Intent(Splash_Activity.this, DriverDetails_Activity.class);
//                     i = new Intent(Splash_Activity.this, DriverPicture_Activity.class);
//                    i.putExtra("USER","09");
                    startActivity(i);
                    finish();
                }else {
                    // splase screen go to Registration Activity.
                Intent i = new Intent(Splash_Activity.this, Registration_Activity.class);

                startActivity(i);
                finish();}
            }
        }, SPLASH_TIME_OUT);// timing 4000 Secound open Splash Screen.

     /*        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splash_Activity.this, Registration_Activity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);*/

    }

}
