package com.niceplaces.niceplaces.activities

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController

class SettingsActivity : AppCompatActivity() {
    private var mEditTextRadius: EditText? = null
    private var mEditTextRefreshTime: EditText? = null
    private var mEditTextRefreshDistance: EditText? = null
    private var mPrefs: PrefsController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.setTitle(R.string.settings)
        mEditTextRadius = findViewById(R.id.edittext_distance_radius)
        mEditTextRefreshTime = findViewById(R.id.edittext_position_refresh_time)
        mEditTextRefreshDistance = findViewById(R.id.edittext_position_refresh_distance)
        mPrefs = PrefsController(this)
        mEditTextRadius?.setText(mPrefs!!.distanceRadius.toString())
        mEditTextRefreshTime?.setText(mPrefs!!.locationRefreshTime.toString())
        mEditTextRefreshDistance?.setText(mPrefs!!.locationRefreshDistance.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mPrefs!!.distanceRadius = mEditTextRadius!!.text.toString().toFloat()
        mPrefs!!.locationRefreshTime = mEditTextRefreshTime!!.text.toString().toInt()
        mPrefs!!.locationRefreshDistance = mEditTextRefreshDistance!!.text.toString().toInt()
    }
}