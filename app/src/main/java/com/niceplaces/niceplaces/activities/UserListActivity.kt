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
import com.niceplaces.niceplaces.adapters.LatestPlacesAdapter
import com.niceplaces.niceplaces.controllers.AlertController
import com.niceplaces.niceplaces.controllers.UserListsController
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.MyRunnable
import java.util.*

class UserListActivity : AppCompatActivity() {
    private val mActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)
        FirebaseAnalytics.getInstance(this)
        supportActionBar!!.hide()
        val textViewAreaName = findViewById<TextView>(R.id.explore_area_name)
        when (intent.extras?.getString(USERLIST)) {
            VISITED -> textViewAreaName.text = resources.getString(R.string.visited_places).replace("\n", " ")
            WISHED -> textViewAreaName.text = resources.getString(R.string.places_to_visit).replace("\n", " ")
            FAVOURITE -> textViewAreaName.text = resources.getString(R.string.favorite_places).replace("\n", " ")
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = UserListsController(this)
        var placesIDs: Set<String?>? = null
        val places: MutableList<Place> = ArrayList()
        when (intent.extras?.getString(USERLIST)) {
            VISITED -> placesIDs = prefs.visited
            WISHED -> placesIDs = prefs.wished
            FAVOURITE -> placesIDs = prefs.favourite
        }
        val listView = findViewById<ListView>(R.id.listview_areas)
        val alertController = AlertController(this, R.id.layout_loading)
        val daoPlaces = DaoPlaces(this)
        val numPlaces = placesIDs!!.size
        if (placesIDs.isEmpty()) {
            alertController.loadingNoData()
        } else {
            for (placeID in placesIDs) {
                if (placeID != null) {
                    daoPlaces.getOne(placeID, object : MyRunnable() {
                        override fun run() {
                            place?.let { places.add(it) }
                            if (places.size == numPlaces) {
                                Collections.sort(places) { o1, o2 -> o1.mName.compareTo(o2.mName) }
                                val adapter = LatestPlacesAdapter(mActivity, R.id.listview_latest_places, places)
                                listView.adapter = adapter
                                listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                                    val intent = Intent(mActivity, PlaceDetailsActivity::class.java)
                                    intent.putExtra(Const.PLACE_ID, places[i].iD)
                                    mActivity.startActivity(intent)
                                }
                                alertController.loadingSuccess()
                            }
                        }
                    }, Runnable { alertController.loadingError() })
                }
            }
        }
    }

    companion object {
        const val USERLIST = "userlist"
        const val VISITED = "visited"
        const val WISHED = "wished"
        const val FAVOURITE = "favourite"
    }
}