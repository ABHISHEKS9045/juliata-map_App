package com.example.map_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.map_ui.data.MapData;
import com.example.map_ui.utility.AppPrefrence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class map_router extends AppCompatActivity implements OnMapReadyCallback {
    ImageView YSbutton;
    String sourceAdd = "";
    String destAdd = "";
    String apiKey = "";
    GoogleMap mapView;
    String response = null;
    String rideId="";
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 234;
    RadioGroup grpRad;
String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_router);
        try{
            AppPrefrence app = new AppPrefrence(map_router.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            id = userObj.getString("id");
        }catch (Exception e){

        }
        apiKey = "AIzaSyBIY6ZYkznWr8DJ-1xF-KYI2Nv8WDgh8RE";// Api key
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        sourceAdd = getIntent().getStringExtra("S_ADD");// direction Start
        destAdd = getIntent().getStringExtra("D_ADD");// direction End.
        YSbutton = findViewById(R.id.YSbutton);
        grpRad = findViewById(R.id.grpRad);
        YSbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationPermission();
            }
        });


    }


// Locations permissions function runtime.
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mapView != null && response != null) {
                //
                if (grpRad.getCheckedRadioButtonId() == -1) {

                } else if (grpRad.getCheckedRadioButtonId() == R.id.radioButton) {
                    Intent intent = new Intent(getApplicationContext(), map_fragment.class).putExtra("data", response).putExtra("rideId",rideId);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mapView != null && response != null) {
                    Intent intent = new Intent(getApplicationContext(), map_fragment.class).putExtra("data", response);
                    startActivity(intent);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
// ready Googal map function .
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapView = googleMap;
        new GetDirection().execute();
    }
// Api directions Drivers
    private class GetDirection extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + sourceAdd + "&destination=" + destAdd + "&sensor=false&mode=driving&key=" + apiKey;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("reees   " + s);

            try {
                MapData respObj = (new Gson()).fromJson(s, MapData.class);
                ArrayList<MapData.Steps> stepsArrayList = respObj.getSteps();
                if (stepsArrayList.size() > 0) {
                    PolylineOptions lineoption = new PolylineOptions();
                    for (int i = 0; i < stepsArrayList.size(); i++) {
                        lineoption.addAll(PolyUtil.decode(stepsArrayList.get(i).getPolyline().getPoints()));
                        lineoption.width(10f);
                        lineoption.color(Color.BLUE);
                        lineoption.geodesic(true);
//                    path.addAll(PolyUtil.decode(stepsArrayList.get(i).getPolyline().getPoints()));
                    }
                    mapView.addPolyline(lineoption);
                    response = s;
                    LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
                    for (LatLng pos : lineoption.getPoints()) {
                        latLngBounds.include(pos);
                    }
                    LatLngBounds latLngBounds1 = latLngBounds.build();
                    mapView.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds1, 10));
                    mapView.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(lineoption.getPoints().get(0)));
                    mapView.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(lineoption.getPoints().get(lineoption.getPoints().size() - 1)));
                    String[] sourceLatlong = sourceAdd.split(",");
                    String[] destLatlong = destAdd.split(",");
                    Map<String, String> params = new HashMap<>();
                    params.put("driver_id", id);
                    params.put("start_lat", "" + sourceLatlong[0]);
                    params.put("start_lng", "" + sourceLatlong[1]);
                    params.put("end_lat", "" + destLatlong[0]);
                    params.put("end_lng", "" + destLatlong[1]);
                    params.put("start_time", new SimpleDateFormat("yyyy-mm-dd HH:MM:SS").format(System.currentTimeMillis()));
                    try {
                        JSONObject jsonObject = new JSONObject(params);
                        // calling ride_create Api
                        new CallApiAlertSave("http://139.59.21.147:8048/api/drivers/ride_create", jsonObject.toString()).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Rute wrong side
                    response = null;
                    Toast.makeText(map_router.this, "Ruta no encontrado:", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class CallApiAlertSave extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiAlertSave(String url, String json) {
            this.url = url;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(map_router.this, "",
                    "Cargando. Espere por favor...", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody body = RequestBody.create(json, JSON); // new
                // RequestBody body = RequestBody.create(JSON, json); // old
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
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
                Toast.makeText(map_router.this, "No se pudo generar ID de viaje", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try{
                JSONObject item = new JSONObject(s);
                JSONObject data = item.getJSONObject("data");
                rideId = data.getString("id");
                }catch (Exception e){
                    Toast.makeText(map_router.this, "No se pudo generar ID de viaje", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
