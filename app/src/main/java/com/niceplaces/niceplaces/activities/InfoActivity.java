package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.niceplaces.niceplaces.R;

import java.util.Locale;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().hide();
        final InfoActivity thisActivity = this;
        TextView textViewVersion = findViewById(R.id.textview_version_number);
        try {
            textViewVersion.setText("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        ImageView IVInstagram = findViewById(R.id.imageview_instagram);
        ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        ImageView IVTwitter = findViewById(R.id.imageview_twitter);
        TextView textViewWebsite = findViewById(R.id.textview_website);
        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.niceplaces.it/en/";
                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                    url = "http://www.niceplaces.it/";
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
        IVTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/niceplacesapp"));
                startActivity(i);
            }
        });
        Button buttonTutorial = findViewById(R.id.button_tutorial);
        buttonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, IntroActivity.class);
                startActivity(intent);
            }
        });
        Button buttonEmail = findViewById(R.id.button_email);
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:niceplacesit@gmail.com"));
                startActivity(i);
            }
        });
    }

}
