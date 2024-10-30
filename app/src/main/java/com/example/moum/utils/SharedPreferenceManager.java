package com.example.moum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceManager {

    private Context context;
    private String preference_file_key;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferenceManager(Context context, String preference_file_key){
        this.context = context;
        this.preference_file_key = preference_file_key;
        this.sharedPreferences = context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setCache(String key, String value){

        editor.putString(key, value);
        editor.apply();
        Log.e("SharedPReferenceManager", "setCache: " + sharedPreferences.getString(key, null));
    }

    public String getCache(String key, String defaultValue){

        return sharedPreferences.getString(key, defaultValue);
    }

    public void removeCache(String key){
        editor.remove(key);
        editor.apply();
    }
}

