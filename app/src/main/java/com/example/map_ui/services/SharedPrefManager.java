package com.example.map_ui.services;
/*
package com.example.map_ui.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.map_ui.models.RegisterDrivers;
import com.example.map_ui.screens.NumberConfirmation_Activity;


import java.text.SimpleDateFormat;

public class SharedPrefManager {

    //the constants
    private static final String PHONE_NUMBER = "0";
    private static final String ID = "10";
    private static final String DRIVER_ID = "3";
    private static final String LAT = "23.38501";
    private static final String LNG = "76.588339";
    private static final String ALERT_TIME = "2022-07-29 12:19:24";
    private static final String MESSAGE = "Road Collapse Happens";
    private static final String createdAt = "2022-06-14T08:21:07.000Z";
    private static final String updatedAt = "2022-06-14T08:21:07.000Z";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void registerDrivers(RegisterDrivers registerDrivers) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PHONE_NUMBER, registerDrivers.getDriverID());
        editor.putInt(DRIVER_ID, registerDrivers.getDriverID());
        editor.putString(LAT, registerDrivers.getLat());
        editor.putString(LNG, registerDrivers.getLng());
        editor.putString(ALERT_TIME,new SimpleDateFormat("yyyy/mm/dd HH:MM:SS").format(System.currentTimeMillis()));
        editor.putString(MESSAGE, registerDrivers.getMsg());
        editor.apply();

    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DRIVER_ID, null) != null;
    }

    //this method will give the logged in user
    public RegisterDrivers getRegisterDrivers() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(ID, Context.MODE_PRIVATE);
        return new RegisterDrivers(
                sharedPreferences.getInt(PHONE_NUMBER,'0'),
                sharedPreferences.getInt(ID, 10)
//                sharedPreferences.getString(DRIVER_ID, null),
//                sharedPreferences.getString(LAT, null),
//                sharedPreferences.getString(LNG, null),
//                sharedPreferences.getString(MESSAGE, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, NumberConfirmation_Activity.class));
    }
}
*/
