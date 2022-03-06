package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        final LoginActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        EditText ETusername = findViewById(R.id.et_username);
        EditText ETpassword = findViewById(R.id.et_password);
        Button buttonLogin = findViewById(R.id.button_login);
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
