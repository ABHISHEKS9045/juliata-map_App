package com.example.map_ui.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DriverDetails_Activity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] vehicleType = {"Tipo de Licencia", "Class A1", "Class A2", "Class A3", "Class A4", "Class A5", "Class B", "Class C", "Class D", "Class F", "Class A1(antigua)", "Class A2(antigua)"};
    //var for driver details -->
    ImageView back_btnDD, ContbuttonDD;
    EditText et_name, et_surname, et_nickname, et_trucktype;
    Spinner et_licensetype;
    String vehTypeStr = "";
    String mobileNo = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mobileNo = getIntent().getStringExtra("MO_NO");
        back_btnDD = findViewById(R.id.back_btnDD);
        et_name = findViewById(R.id.et_name);
        et_surname = findViewById(R.id.et_surname);
        et_nickname = findViewById(R.id.et_nickname);
        et_trucktype = findViewById(R.id.et_trucktype);
        et_licensetype = findViewById(R.id.et_licensetype);
        ContbuttonDD = findViewById(R.id.ContbuttonDD);
        et_licensetype.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, vehicleType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        et_licensetype.setAdapter(aa);
        back_btnDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverDetails_Activity.this, Registration_Activity.class));
            }
        });

        ContbuttonDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    Toast.makeText(DriverDetails_Activity.this, "Por favor, escriba su nombre ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_surname.getText().toString().trim())) {
                    Toast.makeText(DriverDetails_Activity.this, "Por favor ingrese su apellido ", Toast.LENGTH_SHORT).show();

                } else {
                    callRegisterApi(view); //calling the api function
                }

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), vehicleType[position], Toast.LENGTH_LONG).show();
        vehTypeStr = vehicleType[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    //api call for registration the user -->
    void callRegisterApi(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("phone_number", mobileNo);
        params.put("name", et_name.getText().toString());
        params.put("surname", et_surname.getText().toString());
        params.put("nickname", et_nickname.getText().toString());
        params.put("truck_type", et_trucktype.getText().toString());
        if (vehTypeStr.equals("Tipo de Licencia")) {
            params.put("license_type", "");
        } else {
            params.put("license_type", vehTypeStr);
        }
        params.put("profile_pic", "");
        params.put("dob", "");
        params.put("work_type", "");
        params.put("password", "");
        params.put("recovery_email", "");

        try {
            //get the data DriverPicture
            JSONObject jsonObject = new JSONObject(params);
            Intent intent = new Intent(DriverDetails_Activity.this, DriverPicture_Activity.class);
            intent.putExtra("USER", jsonObject.toString());
            intent.putExtra("u_id",getIntent().getStringExtra("u_id"));
            startActivity(intent);

//                new CallApiRegisterDrivers("http://139.59.21.147:8048/api/drivers/", jsonObject.toString()).execute();

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
            dialog = ProgressDialog.show(DriverDetails_Activity.this, "",
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
        //get data Driver response
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(DriverDetails_Activity.this, "Registro con número de móvil falló", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    JSONObject data = item.getJSONObject("data");

                } catch (Exception e) {
                    Toast.makeText(DriverDetails_Activity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
