package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.AreasAdapter
import com.niceplaces.niceplaces.controllers.AlertController
import com.niceplaces.niceplaces.dao.DaoRegions
import com.niceplaces.niceplaces.utils.MyRunnable

class AreasListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)
        val thisActivity = this
        FirebaseAnalytics.getInstance(this)
        val extras = intent.extras
        supportActionBar!!.hide()
        val textViewAreaName = findViewById<TextView>(R.id.explore_area_name)
        if (extras != null){
            textViewAreaName.text = extras.getString("REGION_NAME")
            val listView = findViewById<ListView>(R.id.listview_areas)
            val alertController = AlertController(this, R.id.layout_loading)
            val daoRegions = DaoRegions(this)
            daoRegions.getAreas(extras.getString(Const.REGION_ID), object : MyRunnable() {
                override fun run() {
                    val adapter = AreasAdapter(thisActivity, R.id.listview_areas, getAreas())
                    listView.adapter = adapter
                    listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                        val intent = Intent(thisActivity, PlacesListActivity::class.java)
                        intent.putExtra(Const.AREA_ID, getAreas()?.get(i)?.iD)
                        intent.putExtra("AREA_NAME", getAreas()?.get(i)?.name)
                        thisActivity.startActivity(intent)
                    }
                    alertController.loadingSuccess()
                }
            }, Runnable { alertController.loadingError() })
        }
    }
}