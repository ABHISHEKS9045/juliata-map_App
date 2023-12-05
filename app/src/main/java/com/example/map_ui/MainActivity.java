package com.example.map_ui;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.map_ui.screens.Discount_Activity;
import com.example.map_ui.screens.PointsActivity;
import com.example.map_ui.screens.SupportActivity;
import com.example.map_ui.utility.AppPrefrence;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    int AUTOCOMPLETE_REQUEST_CODE = 193;
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 293;
    ImageView alert, puntos, button;
    int openfor = 0;
    EditText editText, address;
    String name = "";
    String profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Drivers details Username and profile.
        try {
            AppPrefrence app = new AppPrefrence(MainActivity.this);
            String user = app.getString("Detail");
            JSONObject userObj = new JSONObject(user);
            name = userObj.getString("name");
            profile_pic = userObj.getString("profile_pic");
            if (!profile_pic.contains("http://")) {
                profile_pic = "http://139.59.21.147:8048/" + profile_pic;
            }
        } catch (Exception e) {

        }
        Glide.with(this).load(profile_pic).apply(new RequestOptions().circleCrop()).into((ImageView) findViewById(R.id.imageView));
//        alert = findViewById(R.id.alert);
        puntos = findViewById(R.id.puntos);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        address = findViewById(R.id.address);
        ((TextView) findViewById(R.id.textView)).setText("¿HOLA " + name +
                " DÓNDE VAMOS HOY?");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfor = 0;
                getAddress(); // called fuction
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfor = 1;
                getAddress();// called fuction
            }
        });
        (findViewById(R.id.curLoc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLocation();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty() && !address.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "clicked button", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), map_router.class).putExtra("S_ADD", editText.getTag().toString()).putExtra("D_ADD", address.getTag().toString());
                    startActivity(in);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor ingrese la dirección", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // discount button clicked and go to  Discount_Activity.
        findViewById(R.id.discount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Discount_Activity.class));
            }
        });
        // support button clicked and go to  SupportActivity.
        findViewById(R.id.support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SupportActivity.class));
            }
        });
        // puntos button clicked and go to  PointsActivity.
        findViewById(R.id.puntos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PointsActivity.class));
            }
        });
//     alert.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View view) {
//             Intent intent =new Intent(getApplicationContext(),Googal_map.class);
//             startActivity(intent);
//
//         }
//     });
//        alert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "click alrt", Toast.LENGTH_SHORT).show();
//                PopupMenu popup = new PopupMenu(MainActivity.this, alert);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popup_arlt, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//
//                popup.show();//showing popup menu
//            }
//        });//closing the setOnClickListener method
    }

    public void showAlertDialogButtonClicked(View view) {
        Toast.makeText(MainActivity.this, "diologe box", Toast.LENGTH_SHORT).show();

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.activity_puntos_art_box,
                        null);
        builder.setView(customLayout);


        AlertDialog dialog
                = builder.create();
        dialog.show();
    }
// function current Location
    private void currentLocation() {
        if (!Places.isInitialized()) {
            // Googal Api key- "AIzaSyBIY6ZYkznWr8DJ-1xF-KYI2Nv8WDgh8RE"
            Places.initialize(getApplicationContext(), "AIzaSyBIY6ZYkznWr8DJ-1xF-KYI2Nv8WDgh8RE");
        }
       // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

       // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

           // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = Places.createClient(this)
                    .findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    if (response.getPlaceLikelihoods().size() > 0) {
                        Place place = response.getPlaceLikelihoods().get(0).getPlace();
                        editText.setText(place.getAddress());
                        editText.setTag(place.getLatLng().latitude + "," + place.getLatLng().longitude);
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission();// called fuction
        }


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                // You can use the API that requires the permission.
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void sendDialogDataToActivity(String data) {
        Toast.makeText(this,
                        data,
                        Toast.LENGTH_SHORT)
                .show();
    }

    public void showAlertDialogButtonClicked1(View view) {
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setView(R.layout.activity_alret_popup);
        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.activity_alret_popup,
                        null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();
        dialog.show();
        Intent in = new Intent(getApplicationContext(), Googal_map.class);
        startActivity(in);
    }


    //    private void sendDialogDataToActivity1(String data) {
//        Toast.makeText(this,
//                        data,
//                        Toast.LENGTH_SHORT)
//                .show();
//    }
    void getAddress() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBIY6ZYkznWr8DJ-1xF-KYI2Nv8WDgh8RE");
        }

        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                if (openfor == 0) {
                    editText.setText(place.getAddress());
                    editText.setTag("" + place.getLatLng().latitude + "," + place.getLatLng().longitude);
                } else {
                    address.setText(place.getAddress());
                    address.setTag("" + place.getLatLng().latitude + "," + place.getLatLng().longitude);
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }
    }
}






