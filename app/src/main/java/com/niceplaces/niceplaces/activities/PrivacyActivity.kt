package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import java.util.*

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        supportActionBar!!.hide()
        val thisActivity = this
        FirebaseAnalytics.getInstance(this)
        val readButton = findViewById<Button>(R.id.button_read)
        val declineButton = findViewById<Button>(R.id.button_decline)
        val acceptButton = findViewById<Button>(R.id.button_accept)
        val checkBox = findViewById<CheckBox>(R.id.checkbox_privacy)
        readButton.setOnClickListener {
            var url = "http://www.niceplaces.it/en/privacy"
            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                url = "http://www.niceplaces.it/privacy"
            }
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse(url))
            startActivity(i)
        }
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                acceptButton.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                acceptButton.isEnabled = true
            } else {
                acceptButton.setTextColor(resources.getColor(R.color.colorDisabled))
                acceptButton.isEnabled = false
            }
        }
        declineButton.setOnClickListener { thisActivity.finish() }
        acceptButton.setOnClickListener {
            val prefs = PrefsController(thisActivity)
            prefs.isPrivacyAccepted = true
            val i = Intent(thisActivity, MenuActivity::class.java)
            startActivity(i)
            thisActivity.finish()
        }
    }
}