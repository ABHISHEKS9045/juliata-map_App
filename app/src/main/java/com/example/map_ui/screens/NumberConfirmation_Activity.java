package com.example.map_ui.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.utility.AppPrefrence;
import com.example.map_ui.utility.GenericKeyEvent;
import com.example.map_ui.utility.GenericTextWatcher;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NumberConfirmation_Activity extends AppCompatActivity {

    ImageView ContbuttonNC,back_btnNC;
    EditText otpET1,otpET2,otpET3,otpET4,otpET5,otpET6;
    EditText mobile_no;
    String mobileNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_confirmation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ContbuttonNC = findViewById(R.id.ContbuttonNC);
        back_btnNC = findViewById(R.id.back_btnNC);
        mobileNo = getIntent().getStringExtra("MO_NO");
        /// new code for otp verify
        otpET1 = findViewById(R.id.otpET1);
        otpET2 = findViewById(R.id.otpET2);
        otpET3 = findViewById(R.id.otpET3);
        otpET4 = findViewById(R.id.otpET4);
        otpET5 = findViewById(R.id.otpET5);
        otpET6 = findViewById(R.id.otpET6);
        ContbuttonNC = findViewById(R.id.ContbuttonNC);
        otpET1.addTextChangedListener(new GenericTextWatcher(otpET1, otpET2));
        otpET2.addTextChangedListener(new GenericTextWatcher(otpET2, otpET3));
        otpET3.addTextChangedListener(new GenericTextWatcher(otpET3, otpET4));
        otpET4.addTextChangedListener(new GenericTextWatcher(otpET4, otpET5));
        otpET5.addTextChangedListener(new GenericTextWatcher(otpET5, otpET6));
        otpET6.addTextChangedListener(new GenericTextWatcher(otpET6, null));

//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        otpET1.setOnKeyListener(new GenericKeyEvent(otpET1, null));
        otpET2.setOnKeyListener(new GenericKeyEvent(otpET2, otpET1));
        otpET3.setOnKeyListener(new GenericKeyEvent(otpET3, otpET2));
        otpET4.setOnKeyListener(new GenericKeyEvent(otpET4,otpET3));
        otpET5.setOnKeyListener(new GenericKeyEvent(otpET5,otpET4));
        otpET6.setOnKeyListener(new GenericKeyEvent(otpET6,otpET5));
        // OTp Successfully button
         ContbuttonNC.setOnClickListener(new View.OnClickListener() {
              @Override
              // Caling Api otp Veryfy
              public void onClick(View v) {
                  try {
                      if(otpET1.getText().toString().isEmpty()||otpET2.getText().toString().isEmpty()||otpET3.getText().toString().isEmpty()||otpET4.getText().toString().isEmpty()||otpET5.getText().toString().isEmpty()||otpET6.getText().toString().isEmpty()){
                          Toast.makeText(NumberConfirmation_Activity.this, "Por favor ingrese OTP", Toast.LENGTH_SHORT).show();
                      }else {
                          Map<String, String> params = new HashMap<>();
                          params.put("phone_number", mobileNo);
                          params.put("otp", otpET1.getText().toString()+otpET2.getText().toString()+otpET3.getText().toString()+otpET4.getText().toString()+otpET5.getText().toString()+otpET6.getText().toString());
                          JSONObject j = new JSONObject(params);
                        new   CallApiLoginDriver("http://139.59.21.147:8048/api/drivers/is_verify",j.toString()).execute();
//                      Intent number = new Intent(NumberConfirmation_Activity.this, DriverDetails_Activity.class)
                      };
//                      startActivity(number);
//                     Toast.makeText(NumberConfirmation_Activity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
                  } catch (Exception e) {}
              }
          });

// back button clicked the priveous page.
        back_btnNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NumberConfirmation_Activity.this, Registration_Activity.class);
//                i.putExtra("MO_NO",mobile_no.getText().toString().trim());
                startActivity(i);
                finish();
              }
        });

      /*  ContbuttonNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent number = new Intent(NumberConfirmation_Activity.this, DriverDetails_Activity.class);
                startActivity(number);
            }
        });*/

    }
    // fuction called Api Login
    private class CallApiLoginDriver extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiLoginDriver(String url, String json) {
            this.url = url;
            this.json = json;
            System.out.println(json);
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(NumberConfirmation_Activity.this, "",
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
        // response the register data
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(NumberConfirmation_Activity.this, "API falló.", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        int isRegistered = item.getInt("is_register");
                        if(isRegistered==0){
                            JSONObject data = item.getJSONArray("data").getJSONObject(0);
                            Intent i = new Intent(NumberConfirmation_Activity.this, DriverDetails_Activity.class);
                            i.putExtra("MO_NO", mobileNo);
                            i.putExtra("u_id", data.getString("id"));
                            startActivity(i);
                            finish();
                        }else {
                            // Details of register than login.
                        JSONObject data = item.getJSONArray("data").getJSONObject(0);
                        AppPrefrence pref = new AppPrefrence(NumberConfirmation_Activity.this);
                        pref.putString("Detail", data.toString());
                        pref.putBoolean("isLogin", true);
                        Intent i = new Intent(NumberConfirmation_Activity.this, MainActivity.class);
                        startActivity(i);
                        finish();}
                    } else {
                        Toast.makeText(NumberConfirmation_Activity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // register Api faild
                    //startActivity(new Intent(Registration_Activity.this, DriverDetails_Activity.class));
                    Toast.makeText(NumberConfirmation_Activity.this, "API falló", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


