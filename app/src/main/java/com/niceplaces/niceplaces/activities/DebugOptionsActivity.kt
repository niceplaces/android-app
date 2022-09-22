package com.niceplaces.niceplaces.activities

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.localdb.NotificationsDbHelper

class DebugOptionsActivity : AppCompatActivity() {

    private var mPrefs: PrefsController? = null
    private var mRadioDebug: RadioButton? = null
    private var mRadioRelease: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_options)
        val activity = this
        val prefsController = PrefsController(this)
        val simRuntimeException = findViewById<Button>(R.id.button_runtime_exception)
        val simOpenAfterInstall = findViewById<Button>(R.id.button_simulate_open_after_install)
        val simOpenAfterUpdate = findViewById<Button>(R.id.button_simulate_open_after_update)
        val deleteDBNotif = findViewById<Button>(R.id.button_delete_database)
        mRadioDebug = findViewById(R.id.radio_db_debug)
        mRadioRelease = findViewById(R.id.radio_db_release)
        mPrefs = PrefsController(this)
        simRuntimeException.setOnClickListener {
            throw RuntimeException("Runtime Exception")
        }
        simOpenAfterInstall.setOnClickListener {
            prefsController.simulateOpenAfterInstall()
            Toast.makeText(activity, "Chiudi e riapri per simulare l'apertura dopo l'installazione.", Toast.LENGTH_LONG).show()
        }
        simOpenAfterUpdate.setOnClickListener {
            prefsController.simulateOpenAfterUpdate()
            Toast.makeText(activity, "Chiudi e riapri per simulare l'apertura dopo l'aggiornamento.", Toast.LENGTH_LONG).show()
        }
        deleteDBNotif.setOnClickListener {
            val db = NotificationsDbHelper(activity)
            db.delete()
            Toast.makeText(activity, "Database eliminato.", Toast.LENGTH_LONG).show()
        }
        if (mPrefs!!.databaseMode == "debug") {
            mRadioDebug?.isChecked = true
        } else {
            mRadioRelease?.isChecked = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mRadioDebug!!.isChecked) {
            mPrefs!!.databaseMode = "debug"
        } else {
            mPrefs!!.databaseMode = "release"
        }
    }
}