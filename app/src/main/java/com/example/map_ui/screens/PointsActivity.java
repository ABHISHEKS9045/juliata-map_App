package com.example.map_ui.screens;

import static com.example.map_ui.R.id.tab1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.utility.AppPrefrence;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PointsActivity extends AppCompatActivity {
    TextView value_acc, value_points, value_balance;
    ImageView tab1,tab2,tab3,alert;

String profile_pic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        // Ui screen not sleeping mode .
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try{
            AppPrefrence app = new AppPrefrence(PointsActivity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
//            name = userObj.getString("name");
            profile_pic = userObj.getString("profile_pic");
            if(!profile_pic.contains("http://")){
                profile_pic = "http://139.59.21.147:8048/"+profile_pic;
            }
        }catch (Exception e){

        }
        Glide.with(this).load(profile_pic).apply(new RequestOptions().circleCrop()).into((ImageView) findViewById(R.id.imageView2));

        tab1=findViewById (R.id.tab1);
        tab2=findViewById (R.id.tab2);
        tab3=findViewById (R.id.tab3);
        alert=findViewById (R.id.alert);
        value_acc = findViewById(R.id.value_acc);
        value_points = findViewById(R.id.value_points);
        value_balance = findViewById(R.id.value_balance);
        getPoints();
        // tab1 clicked and go to Map fragment.
        tab1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//
                startActivity(new Intent(PointsActivity.this, MapFragment.class));
                finish ();

            }
        });
        // tab2 clicked and go to Support Activity.
        tab2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PointsActivity.this, SupportActivity.class));
                finish ();

            }
        });
        // tab3 clicked and go to Discount/Promositon page.
        tab3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PointsActivity.this, Discount_Activity.class));
                finish ();

            }
        });
        alert.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//                Toast.makeText (getApplicationContext (),"alert no show",Toast.LENGTH_SHORT).show ();

//                startActivity(new Intent(PointsActivity.this, Discount_Activity.class));

            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PointsActivity.this, GiftCardActivity.class));
                finish();
            }
        });
    }
// function called points details Api.
    void getPoints() {
        try {
            AppPrefrence app = new AppPrefrence(PointsActivity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            Map<String, String> params = new HashMap<>();
            params.put("driver_id", userObj.getString("id"));
            JSONObject jsonObject = new JSONObject(params);
            // caling get_points Api .
            new CallApiRegisterDrivers("http://139.59.21.147:8048/api/drivers/get_points", jsonObject.toString()).execute();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class CallApiRegisterDrivers extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiRegisterDrivers(String url, String json) {
            this.url = url;
            this.json = json;
            System.out.println(json);
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(PointsActivity.this, "",
                    "Cargando. Espere por favor....", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody body = RequestBody.create(json, JSON); // new
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        // get data response Api drivre .
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(PointsActivity.this, "API falló", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        // get data Api side available points , redeem point and balance .
                        value_acc.setText(item.getInt("data") + " pts");
                        value_points.setText(item.getInt("redeem_point") + " pts");
                        value_balance.setText(item.getInt("available") + " pts");
                    } else {
                        Toast.makeText(PointsActivity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PointsActivity.this, "API falló", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}