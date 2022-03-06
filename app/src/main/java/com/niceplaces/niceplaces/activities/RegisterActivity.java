package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        final RegisterActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        EditText ETusername = findViewById(R.id.et_username);
        EditText ETpassword = findViewById(R.id.et_password);
        EditText ETname = findViewById(R.id.et_name);
        EditText ETconfirmPassword = findViewById(R.id.et_confirm_password);
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
