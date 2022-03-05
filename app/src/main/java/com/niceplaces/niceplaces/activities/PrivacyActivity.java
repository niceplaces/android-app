package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.PrefsController;

import java.util.Locale;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        getSupportActionBar().hide();
        final PrivacyActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        Button readButton = findViewById(R.id.button_read);
        Button declineButton = findViewById(R.id.button_decline);
        final Button acceptButton = findViewById(R.id.button_accept);
        CheckBox checkBox = findViewById(R.id.checkbox_privacy);
        readButton.setOnClickListener(new View.OnClickListener() {
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
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    acceptButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    acceptButton.setEnabled(true);
                } else {
                    acceptButton.setTextColor(getResources().getColor(R.color.colorDisabled));
                    acceptButton.setEnabled(false);
                }
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsController prefs = new PrefsController(thisActivity);
                prefs.setPrivacyAccepted(true);
                Intent i = new Intent(thisActivity, MenuActivity.class);
                startActivity(i);
                thisActivity.finish();
            }
        });
    }
}
