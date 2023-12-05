package com.example.map_ui.screens;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
import com.example.map_ui.utility.AppPrefrence;

import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PasswordConfiguration_Activity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    ImageView back_btnPS, finish_btn;
    String[] workType = {"Soy dueño de mi camión", "Trabajo en empresa (0-100 camiones)", "Trabajo en empresa (+100 camiones)"};

    EditText et_birthdate, et_password, et_confirmPass, et_recoveryEmail;
    LinearLayout linearLayout_PC;
    CheckBox androidCheckBoxPC;
    String user = "";
    String img = "";
    Spinner et_companytype;
    String companyType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_configuration);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        user = getIntent().getStringExtra("USER");
        img = getIntent().getStringExtra("img");
        et_birthdate = findViewById(R.id.et_birthdate);
        et_companytype = findViewById(R.id.et_companytype);
        et_password = findViewById(R.id.et_password);
        et_confirmPass = findViewById(R.id.et_confirmPass);
        et_recoveryEmail = findViewById(R.id.et_recoveryEmail);
        back_btnPS = findViewById(R.id.back_btnPS);
        finish_btn = findViewById(R.id.finish_btn);
        androidCheckBoxPC = findViewById(R.id.androidCheckBoxPC);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, workType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        et_companytype.setAdapter(aa);
        et_companytype.setOnItemSelectedListener(this);
        // clicked back button .
        back_btnPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordConfiguration_Activity.this, DriverPicture_Activity.class));
            }
        });
        // clicked button calender popup.
        et_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePopup();
            }
        });
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Por favor, introduzca su contraseña: ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_confirmPass.getText().toString().trim())) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Por favor, confirme su contraseña ", Toast.LENGTH_SHORT).show();

                } else if (!et_password.getText().toString().trim().equals(et_confirmPass.getText().toString().trim())) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Contraseña y Confirmar contraseña no coinciden", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(companyType)) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Seleccione el tipo de trabajo ", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(et_recoveryEmail.getText().toString().trim())) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Ingrese su correo electrónico de recuperación ", Toast.LENGTH_SHORT).show();

                } else if (!androidCheckBoxPC.isChecked()) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Acepte los términos y condiciones.", Toast.LENGTH_SHORT).show();

                } else {
                    // data put Api Drivers infomation .
                    try {
                        JSONObject userData = new JSONObject(user);
                        userData.put("dob", et_birthdate.getText().toString());
                        userData.put("work_type", companyType);
                        userData.put("password", et_password.getText().toString().trim());
                        userData.put("recovery_email", et_recoveryEmail.getText().toString());
                        userData.put("profile_pic", img);
                        new CallApiRegisterDrivers("http://139.59.21.147:8048/api/drivers/", userData.toString()).execute();
                    } catch (Exception e) {

                    }


                }
                // startActivity(new Intent(PasswordConfiguration_Activity.this, TermsConditions_Activity.class));
            }
        });
    }
    //calling datepopup function
private void datePopup(){
    final Calendar c = Calendar.getInstance();
   int  mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

// calling Datepicker library
    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    et_birthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                }
            }, mYear, mMonth, mDay);
    datePickerDialog.show();
}
    @Override
    // selected item position.
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), workType[position], Toast.LENGTH_LONG).show();
        companyType = workType[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
            dialog = ProgressDialog.show(PasswordConfiguration_Activity.this, "",
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
        // response login data
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(PasswordConfiguration_Activity.this, "Registro con número de móvil falló", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        JSONObject data = item.getJSONArray("data").getJSONObject(0);
                        AppPrefrence pref = new AppPrefrence(PasswordConfiguration_Activity.this);
                        pref.putString("Detail", data.toString());
                        pref.putBoolean("isLogin", true);
                        Intent i = new Intent(PasswordConfiguration_Activity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finishAffinity();
                    } else {
                        Toast.makeText(PasswordConfiguration_Activity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PasswordConfiguration_Activity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
