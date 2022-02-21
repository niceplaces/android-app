package com.niceplaces.niceplaces.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.niceplaces.niceplaces.utils.AppUtils;

public class PrefsController {

    private SharedPreferences mPref;
    private Context mContext;

    public PrefsController(Context context){
        mContext = context;
        mPref = context.getSharedPreferences("prefs", 0);
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
