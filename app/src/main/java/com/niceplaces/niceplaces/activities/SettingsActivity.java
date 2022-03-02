package com.niceplaces.niceplaces.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.PrefsController;

public class SettingsActivity extends AppCompatActivity {

    private EditText mEditTextRadius, mEditTextRefreshTime, mEditTextRefreshDistance;
    private PrefsController mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.settings);
        mEditTextRadius = findViewById(R.id.edittext_distance_radius);
        mEditTextRefreshTime = findViewById(R.id.edittext_position_refresh_time);
        mEditTextRefreshDistance = findViewById(R.id.edittext_position_refresh_distance);
        mPrefs = new PrefsController(this);
        mEditTextRadius.setText(String.valueOf(mPrefs.getDistanceRadius()));
        mEditTextRefreshTime.setText(String.valueOf(mPrefs.getLocationRefreshTime()));
        mEditTextRefreshDistance.setText(String.valueOf(mPrefs.getLocationRefreshDistance()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPrefs.setDistanceRadius(Float.parseFloat(mEditTextRadius.getText().toString()));
        mPrefs.setLocationRefreshTime(Integer.parseInt(mEditTextRefreshTime.getText().toString()));
        mPrefs.setLocationRefreshDistance(Integer.parseInt(mEditTextRefreshDistance.getText().toString()));
    }
}
