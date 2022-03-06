package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.LatestPlacesAdapter
import com.niceplaces.niceplaces.controllers.AlertController
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.utils.MyRunnable

class LatestPlacesActivity : AppCompatActivity() {
    private val mActivity = this
    private var mListView: ListView? = null
    private lateinit var mSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_places)
        FirebaseAnalytics.getInstance(this)
        supportActionBar!!.hide()
        mListView = findViewById(R.id.listview_latest_places)
        mSpinner = findViewById(R.id.spinner_latest_places)
        mSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                loadList(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onResume() {
        super.onResume()
        loadList(mSpinner!!.selectedItemPosition)
    }

    private fun loadList(position: Int) {
        val daoPlaces = DaoPlaces(this)
        val alertController = AlertController(mActivity, R.id.layout_loading)
        when (position) {
            0 -> daoPlaces.getLatestInserted(object : MyRunnable() {
                override fun run() {
                    val adapter = LatestPlacesAdapter(mActivity, R.id.listview_latest_places, places)
                    mListView!!.adapter = adapter
                    mListView!!.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                        val intent = Intent(mActivity, PlaceDetailsActivity::class.java)
                        intent.putExtra(Const.PLACE_ID, places?.get(i)?.iD)
                        mActivity.startActivity(intent)
                    }
                    alertController.loadingSuccess()
                }
            }, Runnable { alertController.loadingError() })
            1 -> daoPlaces.getLatestUpdated(object : MyRunnable() {
                override fun run() {
                    val adapter = LatestPlacesAdapter(mActivity, R.id.listview_latest_places, places)
                    mListView!!.adapter = adapter
                    mListView!!.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                        val intent = Intent(mActivity, PlaceDetailsActivity::class.java)
                        intent.putExtra(Const.PLACE_ID, places?.get(i)?.iD)
                        mActivity.startActivity(intent)
                    }
                    alertController.loadingSuccess()
                }
            }, Runnable { alertController.loadingError() })
        }
    }
}