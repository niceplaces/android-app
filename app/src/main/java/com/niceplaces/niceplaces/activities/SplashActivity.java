package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.IntroController;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        final SplashActivity thisActivity = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntroController mIntroController = new IntroController(thisActivity);
                if (mIntroController.check()){
                    if (!BuildConfig.DEBUG) {
                        mIntroController.setFirst(false);
                    }
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
