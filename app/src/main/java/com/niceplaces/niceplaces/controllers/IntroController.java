package com.niceplaces.niceplaces.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class IntroController {

    private SharedPreferences mPref;
    private Context mContext;

    public IntroController(Context context){
        mContext = context;
        mPref = context.getSharedPreferences("first", 0);
    }

    public void setFirst(boolean isFirst){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean("check", isFirst);
        editor.apply();
    }

    public boolean check(){
        Log.i("IS_FIRST", String.valueOf(mPref.getBoolean("check", true)));
        return mPref.getBoolean("check", true);
    }

}
