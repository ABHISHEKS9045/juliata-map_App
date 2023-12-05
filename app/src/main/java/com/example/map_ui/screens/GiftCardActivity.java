package com.example.map_ui.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.adapters.GiftCardAdapter;
import com.example.map_ui.data.GiftCard;
import com.example.map_ui.utility.AppPrefrence;

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

public class GiftCardActivity extends AppCompatActivity {
    ArrayList<GiftCard> giftCardArrayList = new ArrayList<>();
    GiftCardAdapter adapter;
    String profile_pic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        adapter = new GiftCardAdapter(giftCardArrayList);
        try{
            AppPrefrence app = new AppPrefrence(GiftCardActivity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
//            name = userObj.getString("name");
            profile_pic = userObj.getString("profile_pic");
            if(!profile_pic.contains("http://")){
                profile_pic = "http://139.59.21.147:8048/"+profile_pic;
            }
        }catch (Exception e){

        }
        // loaded profile picture
        Glide.with(this).load(profile_pic).apply(new RequestOptions().circleCrop()).into((ImageView) findViewById(R.id.imageView2));

        ((RecyclerView) findViewById(R.id.giftCards)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.giftCards)).setAdapter(adapter);
        getGiftCards();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) findViewById(R.id.androidCheckBoxPC)).isChecked()) {
                    if (adapter.getSelection() < 0) {
                        Toast.makeText(GiftCardActivity.this, "Seleccione la tarjeta de regalo", Toast.LENGTH_LONG).show();
// calling Gift Api
                    } else {
                        try {
                            GiftCard giftCard= giftCardArrayList.get(adapter.getSelection());
                            AppPrefrence app = new AppPrefrence(GiftCardActivity.this);
                            String user = app.getString("Detail");
                            JSONObject userObj = new JSONObject(user);
                            Map<String, String> params = new HashMap<>();
                            params.put("driver_id", userObj.getString("id"));
                            params.put("point_required", ""+giftCard.getPoint_required ());
                            params.put("card_title", giftCard.getCard_title ());
                            JSONObject jsonObject = new JSONObject(params);
                            new CallApiRegisterDrivers2("http://139.59.21.147:8048/api/drivers/redeem_points", jsonObject.toString()).execute();
                        } catch (Exception e) {
                        }
                    }
                } else {
                    Toast.makeText(GiftCardActivity.this, "Por favor marque la casilla de confirmación", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
// Gift Api Calling function.
    void getGiftCards() {
        try {
            new CallApiRegisterDrivers("http://139.59.21.147:8048/api/drivers/gift_card").execute();
        } catch (Exception e) {
        }


    }

    private class CallApiRegisterDrivers extends AsyncTask<Void, Void, String> {
        String url;
        ProgressDialog dialog;

        public CallApiRegisterDrivers(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(GiftCardActivity.this, "",
                    "Cargando. Espere por favor...", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder().url(url).get().build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(GiftCardActivity.this, "API falló", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    // response with statuscode gift Api data.
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        JSONArray jsonArray = item.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GiftCard giftCard = new GiftCard();
                            giftCard.setId(object.getInt("id"));
                            giftCard.setCard_title (object.getString("card_title"));
                            giftCard.setPoint_required (object.getInt("point_required"));
                            giftCardArrayList.add(giftCard);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(GiftCardActivity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // messages Api faild.
                    Toast.makeText(GiftCardActivity.this, "API falló", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private class CallApiRegisterDrivers2 extends AsyncTask<Void, Void, String> {
        String url;
        ProgressDialog dialog;
        String json;
        public CallApiRegisterDrivers2(String url,String json) {
            this.url = url;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(GiftCardActivity.this, "",
                    "Cargando. Espere por favor....", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            // json response created
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
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(GiftCardActivity.this, "API falló", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    //response get Activity with calling Result screen.
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        startActivity(new Intent(GiftCardActivity.this,ResultScreenActivity.class).putExtra("OPEN",1));
                        finish();
                    } else if (item.getInt("statuscode") == 404) {
                        startActivity(new Intent(GiftCardActivity.this,ResultScreenActivity.class).putExtra("OPEN",2));
                        finish();
                    } else {
                        Toast.makeText(GiftCardActivity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GiftCardActivity.this, "API falló", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
