package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.FirebaseMsgHandler;
import com.niceplaces.niceplaces.controllers.PrefsController;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle notification = getIntent().getExtras();
            String link = notification.getString(FirebaseMsgHandler.LINK);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(i);
            finish();
        } catch (Exception e){
            e.printStackTrace();
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
            /*Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    new DatabaseController(thisActivity);
                }
            });
            thread.start();*/
            }
            ImageView splashIcon = findViewById(R.id.splash_icon);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
            splashIcon.startAnimation(fadeInAnimation);
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
                } else {
                    PrefsController prefs = new PrefsController(thisActivity);
                    if (prefs.isPrivacyAccepted()) {
                        Intent i = new Intent(thisActivity, MenuActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(thisActivity, PrivacyActivity.class);
                        startActivity(i);
                    }
                }
                thisActivity.finish();
            }
        }, 2000);
    }

}
