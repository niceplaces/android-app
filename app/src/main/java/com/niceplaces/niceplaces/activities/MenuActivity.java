package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.Const;
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
        Button explore = findViewById(R.id.btn_virtual_tour);
        Button newPlaces = findViewById(R.id.btn_new_places);
        //ImageView IVlogin = findViewById(R.id.imageview_login);
        ImageView IVInfo = findViewById(R.id.imageview_info);
        ImageView IVWeb = findViewById(R.id.imageview_web);
        ImageView IVInstagram = findViewById(R.id.imageview_instagram);
        ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        ImageView IVDebug = findViewById(R.id.imageview_debug);
        Button buttonVisited = findViewById(R.id.button_visited);
        Button buttonWished = findViewById(R.id.button_wished);
        Button buttonFav = findViewById(R.id.button_fav);
        TextView textViewPrivacy = findViewById(R.id.textview_privacy_policy);
        /*IVlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, LoginActivity.class);
                startActivity(intent);
            }
        });*/
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
                String url = Const.WEBSITE_EN_URL;
                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                    url = Const.WEBSITE_URL;
                }
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
        });
        IVInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.INSTAGRAM));
                startActivity(i);
            }
        });
        IVFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.FACEBOOK));
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
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, ExploreActivity.class);
                startActivity(intent);
            }
        });
        newPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, LatestPlacesActivity.class);
                startActivity(intent);
            }
        });
        buttonVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, UserListActivity.class);
                intent.putExtra(UserListActivity.USERLIST, UserListActivity.VISITED);
                startActivity(intent);
            }
        });
        buttonWished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, UserListActivity.class);
                intent.putExtra(UserListActivity.USERLIST, UserListActivity.WISHED);
                startActivity(intent);
            }
        });
        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, UserListActivity.class);
                intent.putExtra(UserListActivity.USERLIST, UserListActivity.FAVOURITE);
                startActivity(intent);
            }
        });
        textViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.niceplaces.it/en/privacy";
                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                    url = "http://www.niceplaces.it/privacy";
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
