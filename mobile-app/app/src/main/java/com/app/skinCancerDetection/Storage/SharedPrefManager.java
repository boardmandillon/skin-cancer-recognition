package com.app.skinCancerDetection.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static com.app.skinCancerDetection.Storage.SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public static synchronized com.app.skinCancerDetection.Storage.SharedPrefManager getInstance(Context mCtx){
        if(mInstance == null){
            mInstance  = new com.app.skinCancerDetection.Storage.SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveAccessToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("access_token", token);
        editor.apply();
    }

    public void saveRefreshToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("refresh_token", token);
        editor.apply();
    }

    public String getAccessToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("access_token", null);

    }

    public String getRefreshToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("refresh_token", null);

    }

    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

}
