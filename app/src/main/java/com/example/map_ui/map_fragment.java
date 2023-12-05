package com.example.map_ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.text.HtmlCompat;

import com.example.map_ui.data.MapData;
import com.example.map_ui.screens.Discount_Activity;
import com.example.map_ui.screens.PointsActivity;
import com.example.map_ui.screens.SupportActivity;
import com.example.map_ui.utility.AppPrefrence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class map_fragment extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    GoogleMap mapView;
    String response = null;
    Marker myLocationMarker = null;
    MapData respObj;
    private Context mContext;
    AppCompatTextView instructionTxt;
    // flag for GPS status
    boolean isGPSEnabled = false;
    String rideId = "";
    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    MediaPlayer mp;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    TextView btnCh;
    ImageView alertBtn, opt1, opt2, opt3, opt4;
    LinearLayout linearLayoutVertical;
    HashMap<String, String> times = new HashMap<>();
    long startTime = 0;
    long endTime = 0;
    String name="";
    boolean isBack =false;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            if ((System.currentTimeMillis() - captureTime) > 900000 && captureTime > 0) {
                if (!isDialogShowing && !isReachDest&&!isBack) {
                    isDialogShowing = true;
//                    showDialogCustom();
                }
            }
            recall();// fuction recalled
        }
    };

    @Override
    protected void onPause() {
        isBack = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        captureTime = System.currentTimeMillis();
        isBack = false;
        super.onResume();
    }

    private void recall() {
        handler.postDelayed(run, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(this, R.raw.sound_tone);
        mContext = this;
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map_fragment);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        try{
            AppPrefrence app = new AppPrefrence(map_fragment.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            name = userObj.getString("name");
        }catch (Exception e){

        }
        btnCh = findViewById(R.id.btnCh);
        alertBtn = findViewById(R.id.alertBtn);
        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);
        linearLayoutVertical = findViewById(R.id.linearLayoutVertical);
        captureTime = System.currentTimeMillis();
        handler.postDelayed(run, 1000);
        // changes directions button
        btnCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        // popups Alerts clicked call alert Api.
        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayoutVertical.getVisibility() == View.GONE) {
                    linearLayoutVertical.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutVertical.setVisibility(View.GONE);
                }
            }
        });
        opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutVertical.setVisibility(View.GONE);
                callApi(view);
            }
        });
        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutVertical.setVisibility(View.GONE);
                callApi(view);
            }
        });
        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutVertical.setVisibility(View.GONE);
                callApi(view);
            }
        });
        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutVertical.setVisibility(View.GONE);
                callApi(view);
            }
        });
        // btn2 clicked and go to support Activity.
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(map_fragment.this, SupportActivity.class));
            }
        });
// btn3 clicked and go to Discount/promosion Activity.
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(map_fragment.this, Discount_Activity.class));
            }
        });
        // puntos button clicked and go to Points Activity.
        findViewById(R.id.puntos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(map_fragment.this, PointsActivity.class));
            }
        });
        instructionTxt = findViewById(R.id.instructionTxt);
        response = getIntent().getStringExtra("data");
        rideId = getIntent().getStringExtra("rideId");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
//        showBottomSheetDialogDest();
    }
   // driver location Api Call
    void callApi(View view) {
        if (lastloc != null) {
            try{
            AppPrefrence app = new AppPrefrence(map_fragment.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            Map<String, String> params = new HashMap<>();
            params.put("driver_id", userObj.getString("id"));
            params.put("lat", "" + lastloc.latitude);
            params.put("lng", "" + lastloc.longitude);
            params.put("alert_time", new SimpleDateFormat("yyyy/mm/dd HH:MM:SS").format(System.currentTimeMillis()));
            params.put("msg", view.getTag().toString());
            try {
                JSONObject jsonObject = new JSONObject(params);
                new CallApiAlertSave("http://139.59.21.147:8048/api/alert/", jsonObject.toString()).execute();
            } catch (Exception e) {

            }
        } catch (Exception e) {

            }}else {
            Toast.makeText(map_fragment.this, "Device loation not available restart app", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    /** Called when the map is ready. */
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add some markers to the map, and add a data object to each marker.
        // Add polylines to the map.
        // Polylines are useful to show a route or some other connection between points.
        mapView = googleMap;
        try {
            respObj = (new Gson()).fromJson(response, MapData.class);
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
                mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(lineoption.getPoints().get(0), 18));
                locationDest = lineoption.getPoints().get(lineoption.getPoints().size() - 1);
                getLocation();// calling get fuction Loction
                new CallApiGetAllAlerts("http://139.59.21.147:8048/api/alert/get_alerts", "{\"\":\"\"}").execute();
                // calling get_alerts Api with popup Ui
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
          /*  isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);*/

            if (!isGPSEnabled) {
                showSettingsAlert();
                // no network provider is enabled
                Toast.makeText(this, "Please enable gps or wait for getting location", Toast.LENGTH_LONG).show();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (location != null) {
                                    latitude = location.getLatitude();// location Lat
                                    longitude = location.getLongitude();// location Long.
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(run);
        stopUsingGPS();
        super.onDestroy();
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
// fuction stop GPS
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    LatLng lastloc = null;
    LatLng locationForMovementTracking = null;
    LatLng locationDest = null;
    long captureTime = 0;
    boolean isDialogShowing = false;
    boolean isReachDest = false;
    Handler handler = new Handler();

    @Override
    public void onLocationChanged(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        if (locationForMovementTracking == null) {
            locationForMovementTracking = current;
            captureTime = System.currentTimeMillis();
        }
        if (lastloc == null) {
            lastloc = current;
        }
        // show the currents locations marker icon
        if (myLocationMarker == null) {
            float angle = (float) SphericalUtil.computeHeading(lastloc, current);
            myLocationMarker = mapView.addMarker(new MarkerOptions().position(
                            current)
                    .rotation(angle).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_loc)));

        } else {
            float angle = (float) SphericalUtil.computeHeading(lastloc, current);
            myLocationMarker.setPosition(current);
            myLocationMarker.setRotation(angle);
        }
        lastloc = current;
        mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 16));
        showInstruction(current);
        if (locationDest != null) {
            // Logic to handle location object
            double locdistance = SphericalUtil.computeDistanceBetween(locationDest, current);
            if (!isReachDest && locdistance < 200) {
                isReachDest = true;
                showBottomSheetDialogDest();
            }
        }
        double distance = SphericalUtil.computeDistanceBetween(current, locationForMovementTracking);
        if (distance > 500 && !isReachDest) {
            locationForMovementTracking = current;
            captureTime = System.currentTimeMillis();
        }
    }

    void showInstruction(LatLng current) {
        AppPrefrence appPrefrence = new AppPrefrence(map_fragment.this);
        appPrefrence.putString("Distance",SphericalUtil.computeDistanceBetween(current,locationDest)*(0.001) +"Km");
        String instruction = "";
        for (MapData.Steps step :
                respObj.getSteps()) {
            List<LatLng> all = PolyUtil.decode(step.getPolyline().getPoints());
            if (PolyUtil.isLocationOnPath(current, all, true, 10)) {
                instruction = step.getManeuver();
                break;
            }
        }
        instructionTxt.setText(HtmlCompat.fromHtml(instruction, 0));
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
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
            dialog = ProgressDialog.show(map_fragment.this, "",
                    "Loading. Please wait...", true);
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
                Toast.makeText(map_fragment.this, "Api failed", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    // show responcse Alert ui
                    JSONObject item = new JSONObject(json);
                    IconGenerator iconGenerator = new IconGenerator(map_fragment.this);
                    View v = LayoutInflater.from(map_fragment.this).inflate(R.layout.marker_item, null, false);
                    if (item.getString("msg").equals(opt1.getTag().toString())) {
                        AppCompatImageView iv = v.findViewById(R.id.iconImg);
                        iv.setBackgroundColor(Color.parseColor("#FF0535"));
                        iv.setImageResource(R.drawable.alerta1);
                    } else if (item.getString("msg").equals(opt2.getTag().toString())) {
                        AppCompatImageView iv = v.findViewById(R.id.iconImg);
                        iv.setBackgroundColor(Color.parseColor("#B8D20A"));
                        iv.setImageResource(R.drawable.via2);
                    } else if (item.getString("msg").equals(opt3.getTag().toString())) {
                        AppCompatImageView iv = v.findViewById(R.id.iconImg);
                        iv.setBackgroundColor(Color.parseColor("#F77605"));
                        iv.setImageResource(R.drawable.accidente);
                    } else if (item.getString("msg").equals(opt4.getTag().toString())) {
                        AppCompatImageView iv = v.findViewById(R.id.iconImg);
                        iv.setBackgroundColor(Color.parseColor("#13A8FE"));
                        iv.setImageResource(R.drawable.policia);
                    } else {
                        AppCompatImageView iv = v.findViewById(R.id.iconImg);
                        iv.setBackgroundColor(Color.parseColor("#FF0535"));
                        iv.setImageResource(R.drawable.alerta1);
                    }
                    iconGenerator.setContentView(v);

                    LatLng location = new LatLng(Double.parseDouble(item.getString("lat")), Double.parseDouble(item.getString("lng")));
                    mapView.addMarker(new MarkerOptions().position(
                            // Retrieve the data from the marker.
                            location).title(item.getString("msg")).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class CallApiGetAllAlerts extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiGetAllAlerts(String url, String json) {
            this.url = url;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(map_fragment.this, "",
                    "Loading. Please wait...", true);
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
// post Alert Api
        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(map_fragment.this, "Api failed", Toast.LENGTH_SHORT).show();
            } else {
                IconGenerator iconGenerator = new IconGenerator(map_fragment.this);
                View v = LayoutInflater.from(map_fragment.this).inflate(R.layout.marker_item, null, false);

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        if (item.getString("msg").equals(opt1.getTag().toString())) {
                            AppCompatImageView iv = v.findViewById(R.id.iconImg);
                            iv.setImageResource(R.drawable.map_pin_red);
                        } else if (item.getString("msg").equals(opt2.getTag().toString())) {
                            AppCompatImageView iv = v.findViewById(R.id.iconImg);
                            iv.setImageResource(R.drawable.map_pin_green);
                        } else if (item.getString("msg").equals(opt3.getTag().toString())) {
                            AppCompatImageView iv = v.findViewById(R.id.iconImg);
                            iv.setImageResource(R.drawable.map_pin_orange);
                        } else if (item.getString("msg").equals(opt4.getTag().toString())) {
                            AppCompatImageView iv = v.findViewById(R.id.iconImg);
                            iv.setImageResource(R.drawable.map_pin_blue);
                        } else {
                            AppCompatImageView iv = v.findViewById(R.id.iconImg);
                            iv.setImageResource(R.drawable.map_pin_red);
                        }
                        iconGenerator.setContentView(v);
                        iconGenerator.setBackground(null);
                        LatLng location = new LatLng(Double.parseDouble(item.getString("lat")), Double.parseDouble(item.getString("lng")));
                        mapView.addMarker(new MarkerOptions().position(
                                location).title(item.getString("msg")).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class CallApiSaveRating extends AsyncTask<Void, Void, String> {
        String url, json;
        ProgressDialog dialog;

        public CallApiSaveRating(String url, String json) {
            this.url = url;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(map_fragment.this, "",
                    "Loading. Please wait...", true);
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
                Toast.makeText(map_fragment.this, "Api failed", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(map_fragment.this, MainActivity.class));
                finishAffinity();
            }
        }
    }

    android.app.AlertDialog dialog;

    void showDialogCustom() {

        // Create an alert builder
        android.app.AlertDialog.Builder builder
                = new android.app.AlertDialog.Builder(this);
        builder.setTitle("");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.activity_puntos_art_box,
                        null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        ((TextView)customLayout.findViewById(R.id.textViewName)).setText("¿"+name);
        // custom button
        customLayout.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDialogShowing = false;
                captureTime = System.currentTimeMillis();
                dialog.dismiss();
            }
        });
        dialog
                = builder.create();
        dialog.show();
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.alert_thank_you);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showBottomCargo();
            }
        });
        bottomSheetDialog.show();
    }

    int points = 0;
    boolean yesClicked =false;
    boolean noClicked = false;

    private void showBottomCargo() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_cargo, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ((RadioGroup) v.findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            // radio button selected val 1 to yes and val 0 to No.
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton) {
                    bottomSheetDialog.cancel();
                    showRegistration("1");
                } else if (i == R.id.radioButton2) {
                    bottomSheetDialog.cancel();
                    showRegistration("0");
                }
            }
        });
        bottomSheetDialog.show();
    }

    private void showRegistration(String loadingType) {
        startTime = System.currentTimeMillis();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_registration_center, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ((RadioGroup) v.findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                (v.findViewById(R.id.YSbutton)).setVisibility(View.VISIBLE);
                if (i == R.id.radioButton) {
                    if (!yesClicked){
                        yesClicked = true;
                        points = points + 40;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.white));
                } else if (i == R.id.radioButton2) {
                    if (!noClicked){
                        noClicked = true;
                        points = points + 20;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle_grey);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        (v.findViewById(R.id.YSbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((RadioGroup) v.findViewById(R.id.grpRad)).getCheckedRadioButtonId();
                if (id != -1) {
                    if (id == R.id.radioButton) {
                        endTime = System.currentTimeMillis();
                        times.put("q1", getDuration());
                        resetWithTone();
                        bottomSheetDialog.cancel();
                        showHandover(loadingType);
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }

    String getDuration() {
        if (startTime == 0) {
            return "0";
        }
        return convertTime(endTime - startTime);
    }

    void resetWithTone() {
        yesClicked = false;
        noClicked =false;
        startTime = 0;
        endTime = 0;
        mp.start();
    }
    // caling Dailog handover Layout .

    private void showHandover(String loadingType) {
        startTime = System.currentTimeMillis();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_handover, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ((RadioGroup) v.findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                (v.findViewById(R.id.YSbutton)).setVisibility(View.VISIBLE);
                if (i == R.id.radioButton) {
                    if (!yesClicked){
                        yesClicked =true;
                        points = points + 40;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.white));
                } else if (i == R.id.radioButton2) {
                    if (!noClicked){
                        noClicked =true;
                        points = points + 20;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle_grey);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        (v.findViewById(R.id.YSbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((RadioGroup) v.findViewById(R.id.grpRad)).getCheckedRadioButtonId();
                if (id != -1) {
                    if (id == R.id.radioButton) {
                        endTime = System.currentTimeMillis();
                        times.put("q2", getDuration());
                        resetWithTone();
                        bottomSheetDialog.cancel();
                        showDeck(loadingType);
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }
    // caling dialog_deck layout.
    private void showDeck(String loadingType) {
        startTime = System.currentTimeMillis();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_deck, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ((RadioGroup) v.findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                (v.findViewById(R.id.YSbutton)).setVisibility(View.VISIBLE);
                if (i == R.id.radioButton) {
                    if (!yesClicked){
                        yesClicked = true;
                        points = points + 40;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.white));
                } else if (i == R.id.radioButton2) {
                    if (!noClicked){
                        noClicked = true;
                        points = points + 20;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle_grey);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        (v.findViewById(R.id.YSbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((RadioGroup) v.findViewById(R.id.grpRad)).getCheckedRadioButtonId();
                if (id != -1) {
                    if (id == R.id.radioButton) {
                        endTime = System.currentTimeMillis();
                        times.put("q3", getDuration());
                        resetWithTone();
                        bottomSheetDialog.cancel();
                        showLoadingUnloading(loadingType);
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }
    // caling dialog_load_unload Ui .
    private void showLoadingUnloading(String loadingType) {
        startTime = System.currentTimeMillis();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_load_unload, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ((RadioGroup) v.findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            // radio button selected val 1 to loading and val 0 to Unloading.
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                (v.findViewById(R.id.YSbutton)).setVisibility(View.VISIBLE);
                if (i == R.id.radioButton) {
                    if (!yesClicked){
                        yesClicked = true;
                        points = points + 40;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.white));
                } else if (i == R.id.radioButton2) {
                    if (!noClicked){
                        noClicked = true;
                        points = points + 20;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle_grey);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        (v.findViewById(R.id.YSbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((RadioGroup) v.findViewById(R.id.grpRad)).getCheckedRadioButtonId();
                if (id != -1) {
                    if (id == R.id.radioButton) {
                        endTime = System.currentTimeMillis();
                        times.put("q4", getDuration());
                        resetWithTone();
                        bottomSheetDialog.cancel();
                        showDocument(loadingType);
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }
    // caling dialog_documents Ui .
    private void showDocument(String loadingType) {
        startTime = System.currentTimeMillis();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_documents, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ((RadioGroup) v.findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                (v.findViewById(R.id.YSbutton)).setVisibility(View.VISIBLE);
                if (i == R.id.radioButton) {
                    if (!yesClicked){
                        yesClicked = true;
                        points = points + 40;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.white));
                } else if (i == R.id.radioButton2) {
                    if (!noClicked){
                        noClicked = true;
                        points = points + 20;}

                    (v.findViewById(R.id.YSbutton)).setBackgroundResource(R.drawable.rectangle_grey);
                    ((TextView) v.findViewById(R.id.YSbutton)).setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        (v.findViewById(R.id.YSbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((RadioGroup) v.findViewById(R.id.grpRad)).getCheckedRadioButtonId();
                if (id != -1) {
                    if (id == R.id.radioButton) {
                        endTime = System.currentTimeMillis();
                        times.put("q5", getDuration());
                        resetWithTone();
                        bottomSheetDialog.cancel();
                        showRating(loadingType);
                    }
                }
            }
        });
        bottomSheetDialog.show();
    }
    // caling dialog_rate layout with caling response create Api.
    private void showRating(String loadingType) {
        final Dialog bottomSheetDialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null, false);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        (v.findViewById(R.id.YSbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("load_type", loadingType);
                    map.put("response", ((EditText) v.findViewById(R.id.comment)).getText().toString());
                    map.put("points", ""+points);
                    map.put("rating", ((RatingBar) v.findViewById(R.id.rating)).getRating() + "");
                    map.put("ride_id", rideId);
                    map.put("wait_time", times);
                    new CallApiSaveRating("http://139.59.21.147:8048/api/drivers/response_create", new JSONObject(map).toString()).execute();
                    bottomSheetDialog.show();
                } catch (Exception e) {
                    Toast.makeText(map_fragment.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
        bottomSheetDialog.show();
    }

    String convertTime(long millis) {
        return String.format(Locale.ENGLISH, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
   /*return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));*/
    }
    // caling alert_reach_dest layout.
    private void showBottomSheetDialogDest() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.alert_reach_dest);
        ((TextView)bottomSheetDialog.findViewById(R.id.textView7)).setText(name);
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
//                showBottomSheetDialog();
                showBottomCargo();
            }
        });
        bottomSheetDialog.show();
    }
}