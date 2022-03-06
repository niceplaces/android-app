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
import com.niceplaces.niceplaces.adapters.ExplorePlacesAdapter
import com.niceplaces.niceplaces.controllers.AlertController
import com.niceplaces.niceplaces.dao.DaoAreas
import com.niceplaces.niceplaces.dao.DaoLists
import com.niceplaces.niceplaces.utils.MyRunnable

class PlacesListActivity : AppCompatActivity() {
    private var mExtras: Bundle? = null
    private val mActivity = this
    private var mMode: Mode? = null

    private enum class Mode {
        AREA, LIST
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)
        FirebaseAnalytics.getInstance(this)
        mExtras = intent.extras
        supportActionBar!!.hide()
        val textViewAreaName = findViewById<TextView>(R.id.explore_area_name)
        if (mExtras?.getString(Const.AREA_ID) != null) {
            mMode = Mode.AREA
            textViewAreaName.text = mExtras!!.getString("AREA_NAME")
        } else {
            mMode = Mode.LIST
            textViewAreaName.text = mExtras!!.getString("LIST_NAME")
        }
    }

    override fun onResume() {
        super.onResume()
        val listView = findViewById<ListView>(R.id.listview_areas)
        val alertController = AlertController(this, R.id.layout_loading)
        val successCallback: MyRunnable = object : MyRunnable() {
            override fun run() {
                val adapter = ExplorePlacesAdapter(mActivity, R.id.listview_areas, places)
                listView.adapter = adapter
                listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                    val intent = Intent(mActivity, PlaceDetailsActivity::class.java)
                    intent.putExtra(Const.PLACE_ID, places?.get(i)?.iD)
                    mActivity.startActivity(intent)
                }
                alertController.loadingSuccess()
            }
        }
        when (mMode) {
            Mode.AREA -> {
                val daoAreas = DaoAreas(this)
                mExtras!!.getString(Const.AREA_ID)?.let { daoAreas.getPlaces(it, successCallback, Runnable { alertController.loadingError() }) }
            }
            Mode.LIST -> {
                val daoLists = DaoLists(this)
                mExtras!!.getString(Const.LIST_ID)?.let { daoLists.getPlacesByListId(it, successCallback, Runnable { alertController.loadingError() }) }
            }
        }
    }
}