package com.niceplaces.niceplaces.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.utils.AppUtils;

public class PrefsController {

    private SharedPreferences mPref;
    private Context mContext;

    public PrefsController(Context context){
        mContext = context;
        mPref = context.getSharedPreferences("prefs", 0);
    }

    public void setDatabaseMode(String mode){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("database_mode", mode);
        editor.apply();
    }

    public String getDatabaseMode(){
        if (BuildConfig.DEBUG){
            return mPref.getString("database_mode", "debug");
        } else {
            return "release";
        }
    }

    public void setDistanceRadius(float radius){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putFloat("distance_radius", radius);
        editor.apply();
    }

    public float getDistanceRadius(){
        return mPref.getFloat("distance_radius", 10);
    }

    public int getLocationRefreshTime(){
        return mPref.getInt("location_refresh_time", 10);
    }

    public void setLocationRefreshTime(int time){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("location_refresh_time", time);
        editor.apply();
    }

    public int getLocationRefreshDistance(){
        return mPref.getInt("location_refresh_distance", 50);
    }

    public void setLocationRefreshDistance(int distance){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("location_refresh_distance", distance);
        editor.apply();
    }

    public void setHidePlacesWithoutDescription(boolean hide){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean("hide_places_without_description", hide);
        editor.apply();
    }

    public boolean hidePlacesWithoutDescription(){
        return mPref.getBoolean("hide_places_without_description", false);
    }

    public void firstOpenDone(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("version_code", AppUtils.getVersionCode(mContext));
        editor.apply();
    }

    // FOR DEBUG
    public void simulateOpenAfterInstall(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("version_code", 0);
        editor.apply();
    }

    public void simulateOpenAfterUpdate(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("version_code", AppUtils.getVersionCode(mContext) - 1);
        editor.apply();
    }

    public boolean isFistOpenAfterInstall(){
        Log.i("FIRSTOPEN_AFTER_INSTALL", String.valueOf(mPref.getInt("version_code", 0) == 0));
        Log.i("FIRSTOPEN_VERSION_CODE", String.valueOf(mPref.getInt("version_code", 0)));
        return mPref.getInt("version_code", 0) == 0;
    }

    public boolean isFistOpenAfterUpdate(){
        Log.i("FIRSTOPEN_AFTER_UPDATE", String.valueOf(mPref.getInt("version_code", 0) != AppUtils.getVersionCode(mContext)));
        Log.i("FIRSTOPEN_VERSION_CODE", String.valueOf(mPref.getInt("version_code", 0)));
        return mPref.getInt("version_code", 0) != AppUtils.getVersionCode(mContext);
    }

}
