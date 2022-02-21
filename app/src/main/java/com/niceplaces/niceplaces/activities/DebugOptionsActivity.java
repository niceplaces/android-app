package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.DatabaseController;
import com.niceplaces.niceplaces.controllers.PrefsController;

import io.fabric.sdk.android.Fabric;

public class DebugOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_options);
        final DebugOptionsActivity activity = this;
        final PrefsController prefsController = new PrefsController(this);
        Button simOpenAfterInstall = findViewById(R.id.button_simulate_open_after_install);
        Button simOpenAfterUpdate = findViewById(R.id.button_simulate_open_after_update);
        final DatabaseController db = new DatabaseController(this);
        simOpenAfterInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefsController.simulateOpenAfterInstall();
                db.deleteDB();
                Toast.makeText(activity, "Chiudi e riapri per simulare l'apertura dopo l'installazione.", Toast.LENGTH_LONG).show();
            }
        });
        simOpenAfterUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefsController.simulateOpenAfterUpdate();
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                sqLiteDatabase.setVersion(sqLiteDatabase.getVersion() - 1);
                Toast.makeText(activity, "Chiudi e riapri per simulare l'apertura dopo l'aggiornamento.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
