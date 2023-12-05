package com.example.map_ui.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.utility.AppPrefrence;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    ImageView back_btnR, buttonR;
    EditText mobile_no;
    EditText password;
    int openFor = 0;
    CountryCodePicker ccp;
    String response = null;
    String rideId = "";
    String phone_number = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViewById(R.id.loginWithPass).setVisibility(View.GONE);
        findViewById(R.id.buttonR1).setVisibility(View.GONE);
        findViewById(R.id.regBtn).setVisibility(View.GONE);
        ccp = findViewById(R.id.ccp);
        back_btnR = findViewById(R.id.back_btnR);
        mobile_no = findViewById(R.id.mobile_no);
        password = findViewById(R.id.password);
        buttonR = findViewById(R.id.buttonR);
        findViewById(R.id.et_pass).setVisibility(View.VISIBLE);
        // clicked back button and go to preveous page.
        back_btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Registration_Activity.class));
                finish();
            }
        });

        // click button login

        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ccp.getSelectedCountryCode().trim())) {
                    Toast.makeText(LoginActivity.this, "Seleccione el código de país.", Toast.LENGTH_SHORT).show();
                }else   if (TextUtils.isEmpty(mobile_no.getText().toString().trim()) || mobile_no.getText().toString().trim().length() < 9) {
                    Toast.makeText(LoginActivity.this, "Ingrese un número de teléfono móvil válido.: ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, "Por favor, introduzca su contraseña: ", Toast.LENGTH_SHORT).show();
                } else {

                    callLoginApi(view);    //calling the api function
//
                }
            }
        });
        // response = getIntent().getStringExtra("data");
//        phone_number = getIntent().getStringExtra("0");

    }

    /// Api calling for Login page--->
    void callLoginApi(View view) {
        if (phone_number == null) {
            Map<String, String> params = new HashMap<>();
            params.put("phone_number", "+"+ccp.getSelectedCountryCode()+ mobile_no.getText().toString().trim());
            params.put("password", password.getText().toString().trim());
            try {
                JSONObject jsonObject = new JSONObject(params);
                new CallApiLoginDriver("http://139.59.21.147:8048/api/drivers/login", jsonObject.toString()).execute();
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(LoginActivity.this, "Algo salió mal, intenta de nuevo.... ", Toast.LENGTH_LONG).show();
        }
    }

    private class CallApiLoginDriver extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiLoginDriver(String url, String json) {
            this.url = url;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "",
                    "Cargando. Espere por favor...", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            // response login page
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
        // get the data Api response Login Details.
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(LoginActivity.this, "Error de inicio de sesion.", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                       /* Intent i = new Intent(Registration_Activity.this, NumberConfirmation_Activity.class);
                        i.putExtra("MO_NO", mobile_no.getText().toString().trim());
                        startActivity(i);*/
                        JSONObject data = item.getJSONArray("data").getJSONObject(0);
                        AppPrefrence pref = new AppPrefrence(LoginActivity.this);
                        pref.putString("Detail", data.toString());
                        pref.putBoolean("isLogin", true);

                        // login Successful and go to mainActivity page.
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } /*else {
                        if(item.getString("message").equals("phone number not exists!")){
                            Intent i = new Intent(Registration_Activity.this, DriverDetails_Activity.class);
                            i.putExtra("MO_NO",mobile_no.getText().toString().trim());
                            startActivity(i);
                            finish();
                        }*/ else {
                        Toast.makeText(LoginActivity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    // Login Api Faild
                    //startActivity(new Intent(Registration_Activity.this, DriverDetails_Activity.class));
                    Toast.makeText(LoginActivity.this, "API falló", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}


