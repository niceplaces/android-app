package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;

import java.util.Locale;

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
        ImageView IVWeb = findViewById(R.id.imageview_web);
        ImageView IVInstagram = findViewById(R.id.imageview_instagram);
        ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        ImageView IVDebug = findViewById(R.id.imageview_debug);
        TextView textViewPrivacy = findViewById(R.id.textview_privacy_policy);
        IVInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, InfoActivity.class);
                startActivity(intent);
            }
        });
        IVWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://niceplaces.altervista.org/en/";
                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                    url = "http://niceplaces.altervista.org/";
                }
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(i);
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
        virtualTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, ExploreActivity.class);
                startActivity(intent);
            }
        });
        textViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://niceplaces.altervista.org/en/privacy_policy.html";
                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                    url = "http://niceplaces.altervista.org/privacy_policy.html";
                }
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(i);
            }
        });
        if (BuildConfig.DEBUG) {
            IVDebug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(thisActivity, DebugOptionsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            IVDebug.setVisibility(View.GONE);
        }
    }
}
