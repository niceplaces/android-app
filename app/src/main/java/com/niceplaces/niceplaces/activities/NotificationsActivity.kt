package com.niceplaces.niceplaces.activities

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.NotificationsAdapter
import com.niceplaces.niceplaces.localdb.NotificationsDbHelper
import com.niceplaces.niceplaces.utils.AppUtils

class NotificationsActivity : AppCompatActivity() {

    private val mActivity = this
    private lateinit var mListView: ListView
    private lateinit var dbHelper: NotificationsDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        FirebaseAnalytics.getInstance(this)
        supportActionBar!!.hide()
        mListView = findViewById(R.id.listview_notifications)
        dbHelper = NotificationsDbHelper(this)
        val notifications = dbHelper.getAll(dbHelper.readableDatabase)
        mListView.adapter = NotificationsAdapter(this, R.id.listview_notifications, notifications)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}