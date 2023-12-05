package com.example.map_ui.screens;

import static com.example.map_ui.R.id.tab1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.adapters.DiscountAdapter;
import com.example.map_ui.data.Discount;
import com.example.map_ui.utility.AppPrefrence;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Discount_Activity extends AppCompatActivity {
    ArrayList<Discount> list = new ArrayList<>();
    RecyclerView discountList;
    ImageView tab1,tab2,tab4,tab5;
    DiscountAdapter discountAdapter;
    String profile_pic;
    String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        discountList = findViewById(R.id.discountList);
        // Ui screen not sleeping mode .
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try{
            AppPrefrence app = new AppPrefrence(Discount_Activity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            name = userObj.getString("name");
            profile_pic = userObj.getString("profile_pic");
            if(!profile_pic.contains("http://")){
                profile_pic = "http://139.59.21.147:8048/"+profile_pic;
                // get profile pic
            }
            ((TextView)findViewById(R.id.name)).setText(name+
                    ", ATENTO a estas promos en la ruta");
            // get username
            ((TextView)findViewById(R.id.tvDistance)).setText(app.getString("Distance"));

        }catch (Exception e){

        }
        Glide.with(this).load(profile_pic).apply(new RequestOptions().circleCrop()).into((ImageView) findViewById(R.id.imageView2));

        tab2=findViewById (R.id.tab2);
        tab4=findViewById (R.id.tab4);
        tab5=findViewById (R.id.tab5);
        tab1=findViewById (R.id.tab1);
        discountList.setLayoutManager(new LinearLayoutManager(this));
        discountAdapter = new DiscountAdapter(list);
        discountList.setAdapter(discountAdapter);
        getPoints();
        // tab1 clicked and go to Map fragment.
        tab1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//
                startActivity(new Intent(Discount_Activity.this, MapFragment.class));
                finish ();

            }
        });
        // tab2 clicked and go to support Activity.
        tab2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//
                startActivity(new Intent(Discount_Activity.this,SupportActivity.class));
                finish ();

            }
        });
        // tab4 clicked and go to point/gift Activity.
        tab4.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//
                startActivity(new Intent(Discount_Activity.this, PointsActivity.class));
                finish ();
            }
        });
        tab5.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//
            }
        });


    }
    //  Points function with Discount Api calling

    void getPoints() {
        try {
            AppPrefrence app = new AppPrefrence(Discount_Activity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            Map<String, String> params = new HashMap<>();
            params.put("driver_id", userObj.getString("id"));
            JSONObject jsonObject = new JSONObject(params);
            // Calling RegisterDriver Api
            new CallApiRegisterDrivers("http://139.59.21.147:8048/api/drivers/discount", jsonObject.toString()).execute();
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
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Discount_Activity.this, "",
                    "Cargando. Espere por favor...", true);
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
        // post the data promision page.
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(Discount_Activity.this, "API falló", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        list.clear();
                        JSONArray data = item.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject itemObj = data.getJSONObject(i);
                            Discount discount = new Discount();
                            discount.setId(itemObj.getInt("id"));
                            discount.setPromotion_title (itemObj.getString ("promotion_title"));
                            discount.setPercent_amount (itemObj.getInt ("percent_amount"));
                            discount.setPromo_code (itemObj.getString ("promo_code"));
                            discount.setQr_code (itemObj.getString ("qr_code"));
                            discount.setAddress (itemObj.getString ("address"));
                            discount.setLongitude (itemObj.getInt ("longitude"));
                            discount.setLatitude (itemObj.getInt ("latitude"));
//                            discount.setResults ((float) itemObj.getInt ("results"));
    

//                            float[] results = new float[1];
//                            Location.distanceBetween(discount.getLatitude (), discount.getLongitude (),0,0
//                                  , results);

//
                            list.add(discount);
                        }
                        discountAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Discount_Activity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Discount_Activity.this, "API fallódd", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
