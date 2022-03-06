package com.niceplaces.niceplaces.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.niceplaces.niceplaces.R

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        supportActionBar!!.hide()
        val thisActivity = this
    }
}