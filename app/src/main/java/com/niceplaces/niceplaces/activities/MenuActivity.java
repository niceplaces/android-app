package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        final MenuActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        Button placesNearYou = findViewById(R.id.btn_places_near_you);
        Button virtualTour = findViewById(R.id.btn_virtual_tour);
        ImageView IVInfo = findViewById(R.id.imageview_info);
        ImageView IVInstagram = findViewById(R.id.imageview_instagram);
        ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        IVInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, InfoActivity.class);
                startActivity(intent);
            }
        });
        IVInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/niceplacesapp/"));
                startActivity(i);
            }
        });
        IVFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/niceplacesapp/"));
                startActivity(i);
            }
        });
        placesNearYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, MapsActivity.class);
                startActivity(intent);
            }
        });
        if (BuildConfig.DEBUG) {
            virtualTour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(thisActivity, VirtualTourActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

}
