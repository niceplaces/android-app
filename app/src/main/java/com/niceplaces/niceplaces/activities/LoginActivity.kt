package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        val thisActivity = this
        FirebaseAnalytics.getInstance(this)
        val ETusername = findViewById<EditText>(R.id.et_username)
        val ETpassword = findViewById<EditText>(R.id.et_password)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val buttonRegister = findViewById<Button>(R.id.button_register)
        buttonRegister.setOnClickListener {
            val intent = Intent(thisActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}