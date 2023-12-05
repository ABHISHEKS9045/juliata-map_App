package com.example.map_ui.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrence {
    SharedPreferences sharedPreferences;
    public AppPrefrence(Context ctx){
         sharedPreferences =  ctx.getSharedPreferences("MySharedPref",ctx.MODE_PRIVATE);
    }
    // get String value save data .
    public  void putString(String key,String value){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key, value);
        myEdit.apply();
    }

    public void putBoolean(String key,boolean value){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean(key, value);
        myEdit.apply();
    }
    // genrated Key
    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }
    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

}
