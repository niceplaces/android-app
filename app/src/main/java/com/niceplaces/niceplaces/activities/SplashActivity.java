package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.DatabaseController;
import com.niceplaces.niceplaces.controllers.PrefsController;
import com.niceplaces.niceplaces.utils.AppUtils;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG){
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);
        } else {
            Fabric.with(this, new Crashlytics());
        }
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        PrefsController prefsController = new PrefsController(this);
        if (prefsController.isFistOpenAfterInstall() || prefsController.isFistOpenAfterUpdate()){
            Log.i("FIRSTOPEN_", "DB!");
            new DatabaseController(this);
        } else {
            Log.i("FIRSTOPEN_", "CLOSE!");
            close();
        }
    }

    public void close(){
        final SplashActivity thisActivity = this;
        final PrefsController prefsController = new PrefsController(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefsController.isFistOpenAfterInstall()){
                    prefsController.firstOpenDone();
                    Intent intent = new Intent(thisActivity, IntroActivity.class);
                    startActivity(intent);
                    thisActivity.finish();
                } else {
                    Intent i = new Intent(thisActivity, MenuActivity.class);
                    startActivity(i);
                    thisActivity.finish();
                }
            }
        }, 2000);
    }

}
