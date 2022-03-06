package com.niceplaces.niceplaces.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.PrefsController;

public class DebugOptionsActivity extends AppCompatActivity {

    private PrefsController mPrefs;
    private RadioButton mRadioDebug, mRadioRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_options);
        final DebugOptionsActivity activity = this;
        final PrefsController prefsController = new PrefsController(this);
        Button simOpenAfterInstall = findViewById(R.id.button_simulate_open_after_install);
        Button simOpenAfterUpdate = findViewById(R.id.button_simulate_open_after_update);
        mRadioDebug = findViewById(R.id.radio_db_debug);
        mRadioRelease = findViewById(R.id.radio_db_release);
        mPrefs = new PrefsController(this);
        simOpenAfterInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefsController.simulateOpenAfterInstall();
                Toast.makeText(activity, "Chiudi e riapri per simulare l'apertura dopo l'installazione.", Toast.LENGTH_LONG).show();
            }
        });
        simOpenAfterUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefsController.simulateOpenAfterUpdate();
                Toast.makeText(activity, "Chiudi e riapri per simulare l'apertura dopo l'aggiornamento.", Toast.LENGTH_LONG).show();
            }
        });
        if (mPrefs.getDatabaseMode().equals("debug")){
            mRadioDebug.setChecked(true);
        } else {
            mRadioRelease.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRadioDebug.isChecked()){
            mPrefs.setDatabaseMode("debug");
        } else {
            mPrefs.setDatabaseMode("release");
        }
    }

}
