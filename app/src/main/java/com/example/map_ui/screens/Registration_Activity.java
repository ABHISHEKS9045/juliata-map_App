package com.example.map_ui.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class Registration_Activity extends AppCompatActivity {

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
        ccp = findViewById(R.id.ccp);
findViewById(R.id.buttonR1).setVisibility(View.VISIBLE);
findViewById(R.id.loginWithPass).setVisibility(View.VISIBLE);
findViewById(R.id.regBtn).setVisibility(View.VISIBLE);
findViewById(R.id.buttonR1).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(Registration_Activity.this,LoginActivity.class));
        finish();
    }
});
        back_btnR = findViewById(R.id.back_btnR);
        mobile_no = findViewById(R.id.mobile_no);
        password = findViewById(R.id.password);
        buttonR = findViewById(R.id.buttonR);

        back_btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
     // call registration Api
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ccc   "+ccp.getSelectedCountryCode());
                if (TextUtils.isEmpty(ccp.getSelectedCountryCode().trim())) {
                    Toast.makeText(Registration_Activity.this, "Seleccione el código de país.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(mobile_no.getText().toString().trim())||mobile_no.getText().toString().trim().length()<9) {
                    Toast.makeText(Registration_Activity.this, "Ingrese un número de teléfono móvil válido: ", Toast.LENGTH_SHORT).show();
                }/* else if (TextUtils.isEmpty(password.getText().toString().trim())) {
                    Toast.makeText(Registration_Activity.this, "Please Enter Your Password: ", Toast.LENGTH_SHORT).show();
                }*/ else {

                    callLoginApi(view);    //calling the api function
//
                }
            }
        });
        // response = getIntent().getStringExtra("data");
//        phone_number = getIntent().getStringExtra("0");

    }

    /// Api calling for Login page send Otp--->
    void callLoginApi(View view) {
        if (phone_number == null) {
            Map<String, String> params = new HashMap<>();
            params.put("phone_number","+"+ccp.getSelectedCountryCode()+ mobile_no.getText().toString().trim());
//            params.put("password", password.getText().toString().trim());
            try {
                JSONObject jsonObject = new JSONObject(params);
                new CallApiLoginDriver("http://139.59.21.147:8048/api/drivers/send_otp", jsonObject.toString()).execute();
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(Registration_Activity.this, "Algo salió mal, intenta de nuevo.... ", Toast.LENGTH_LONG).show();
        }
    }
// call driver login details
    private class CallApiLoginDriver extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiLoginDriver(String url, String json) {
            this.url = url;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Registration_Activity.this, "",
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
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(Registration_Activity.this, "Error de inicio de sesion.", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    if (item.getInt("statuscode") == 200) {
                        Intent i = new Intent(Registration_Activity.this, NumberConfirmation_Activity.class);
                        i.putExtra("MO_NO","+"+ccp.getSelectedCountryCode()+ mobile_no.getText().toString().trim());
                        startActivity(i);
//                        JSONObject data = item.getJSONObject("data");
//                        AppPrefrence pref = new AppPrefrence(Registration_Activity.this);
//                        pref.putString("Detail", data.toString());
//                        pref.putBoolean("isLogin", true);
//                        Intent i = new Intent(Registration_Activity.this, MainActivity.class);
//                        startActivity(i);
                        finish();
                    } /*else {
                        if(item.getString("message").equals("phone number not exists!")){
                            Intent i = new Intent(Registration_Activity.this, DriverDetails_Activity.class);
                            i.putExtra("MO_NO",mobile_no.getText().toString().trim());
                            startActivity(i);
                            finish();
                        }*/else {
                        Toast.makeText(Registration_Activity.this, item.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    //startActivity(new Intent(Registration_Activity.this, DriverDetails_Activity.class));
                    Toast.makeText(Registration_Activity.this, "API falló", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}


// registration using parse.class
/*package com.example.map_ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.map_ui.MainActivity;
import com.example.map_ui.R;
//import com.parse.ParseUser;


public class Registration_Activity extends AppCompatActivity {

    ImageView buttonR;
    EditText mobile_no;
    private EditText userNameEdt, passwordEdt;
    private Button loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // initializing our edit text  and buttons.
        mobile_no = findViewById(R.id.mobile_no);
        buttonR = findViewById(R.id.buttonR);
        mobile_no.setText(getIntent().getStringExtra("phone_number"));
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = mobile_no.getText().toString();
                // checking if the entered text is empty or not.
                if (TextUtils.isEmpty(phone_number)) {
                    Toast.makeText(Registration_Activity.this, "Please Enter Your Mobile Number:", Toast.LENGTH_SHORT).show();
                }
                loginUser(phone_number);
            }
        });

    }

    private void loginUser(String phone_number) {
        // calling a method to login a user.
        ParseUser.logInInBackground(phone_number, (parseUser, e) -> {
            // after login checking if the user is null or not.
            if (parseUser != null) {
                Toast.makeText(this, "Login Successful ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Registration_Activity.this, MainActivity.class);
                i.putExtra("phone_number", phone_number);
                startActivity(i);
            } else {
                // display an toast message when user logout of the app.
                ParseUser.logOut();
                Toast.makeText(Registration_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}*/


//registration using backgroundTaskActivity-->
/*
package com.example.map_ui.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.map_ui.R;
import com.example.map_ui.services.BackgroudTask;

public class Registration_Activity extends AppCompatActivity {

    // new code 2
    Button bttnLogin;
    ImageView buttonR;
    EditText mobile_no;
    TextView regLink;
    AlertDialog.Builder alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        mobile_no = (EditText) findViewById(R.id.mobile_no);
       // regLink = (TextView) findViewById(R.id.regLink);
        mobile_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration_Activity.this, DriverDetails_Activity.class));
            }
        });

        buttonR = (ImageView) findViewById(R.id.buttonR);
        buttonR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mobile_no.getText().toString().equals(""))
                {
                    alert = new AlertDialog.Builder(Registration_Activity.this);
                    alert.setTitle("Login Failed");
                    alert.setMessage("Try again");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }

                    });

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else  //if user provides proper data
                {
                    BackgroudTask backgroundTask = new BackgroudTask(Registration_Activity.this);
                    backgroundTask.execute("login", mobile_no.getText().toString());
                }
            }

        });

    }

}*/
