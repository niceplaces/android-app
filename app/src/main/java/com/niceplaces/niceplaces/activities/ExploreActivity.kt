package com.niceplaces.niceplaces.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.LatestPlacesAdapter
import com.niceplaces.niceplaces.adapters.ListsAdapter
import com.niceplaces.niceplaces.adapters.RegionsAdapter
import com.niceplaces.niceplaces.controllers.AlertController
import com.niceplaces.niceplaces.dao.DaoLists
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.dao.DaoRegions
import com.niceplaces.niceplaces.utils.ImageUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import com.niceplaces.niceplaces.utils.NonScrollListView
import java.util.*

class ExploreActivity : AppCompatActivity() {

    private lateinit var mListViewSearch: ListView
    private lateinit var mEditTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        setContentView(R.layout.activity_explore)
        supportActionBar!!.hide()
        val thisActivity = this
        FirebaseAnalytics.getInstance(this)
        mEditTextSearch = findViewById(R.id.edittext_search)
        mListViewSearch = findViewById(R.id.listview_search_explore)
        val placeOfDayLayout = findViewById<LinearLayout>(R.id.layout_place_of_day)
        val listViewHighlights = findViewById<NonScrollListView>(R.id.listview_highlights)
        val listViewAreas = findViewById<NonScrollListView>(R.id.listview_areas)
        val alertPlaceOfDay = AlertController(this, R.id.layout_loading_place_of_day)
        val alertLists = AlertController(this, R.id.layout_loading_lists)
        val alertRegions = AlertController(this, R.id.layout_loading_regions)
        val daoPlaces = DaoPlaces(this)
        mEditTextSearch.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b){
                mListViewSearch.visibility = View.VISIBLE
            } else {
                mListViewSearch.visibility = View.GONE
            }
        }
        mEditTextSearch.doAfterTextChanged { text ->
            daoPlaces.getSearchResults(text.toString().toLowerCase(Locale.ROOT).replace(" ", "+"),
                    object: MyRunnable() {
                override fun run() {
                    val adapter = LatestPlacesAdapter(thisActivity, R.id.listview_latest_places, places)
                    mListViewSearch.adapter = adapter
                    mListViewSearch.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                        val intent = Intent(thisActivity, PlaceDetailsActivity::class.java)
                        intent.putExtra(Const.PLACE_ID, places?.get(i)?.iD)
                        thisActivity.startActivity(intent)
                    }
                }
            }, Runnable { alertPlaceOfDay.loadingError() })
        }
        daoPlaces.getPlaceOfTheDay(object : MyRunnable() {
            override fun run() {
                val imageView = findViewById<ImageView>(R.id.place_of_day_image)
                val textViewName = findViewById<TextView>(R.id.place_of_day_name)
                val textViewArea = findViewById<TextView>(R.id.place_of_day_area)
                val textViewDesc = findViewById<TextView>(R.id.place_of_day_desc)
                val place = place
                if (place != null) {
                    ImageUtils.setImageViewWithGlide(context, place.mImage, imageView)
                    textViewName.text = place.mName
                    textViewArea.text = place.mArea + ", " + place.mRegion
                }
                var desc = place?.mDescription
                if (desc != null) {
                    if (desc.length > 150) {
                        desc = desc.substring(0, 150) + "..."
                    }
                }
                textViewDesc.text = desc
                placeOfDayLayout.setOnClickListener {
                    val intent = Intent(thisActivity, PlaceDetailsActivity::class.java)
                    if (place != null) {
                        intent.putExtra(Const.PLACE_ID, place.iD)
                    }
                    thisActivity.startActivity(intent)
                }
                alertPlaceOfDay.loadingSuccess()
            }
        }, Runnable { alertPlaceOfDay.loadingError() })
        val daoLists = DaoLists(this)
        daoLists.getAll(object : MyRunnable() {
            override fun run() {
                val adapter = ListsAdapter(thisActivity, R.id.listview_areas, lists)
                listViewHighlights.adapter = adapter
                listViewHighlights.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                    val intent = Intent(thisActivity, AreaIntroActivity::class.java)
                    intent.putExtra(Const.LIST_ID, lists?.get(i)?.iD)
                    intent.putExtra("LIST_NAME", lists?.get(i)?.name)
                    intent.putExtra("LIST_DESC", lists?.get(i)?.description)
                    thisActivity.startActivity(intent)
                }
                alertLists.loadingSuccess()
            }
        }, Runnable { alertLists.loadingError() })
        val daoRegions = DaoRegions(this)
        daoRegions.getRegions(object : MyRunnable() {
            override fun run() {
                val adapter = RegionsAdapter(thisActivity, R.id.listview_areas, regions)
                listViewAreas.adapter = adapter
                listViewAreas.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
                    val intent = Intent(thisActivity, AreasListActivity::class.java)
                    intent.putExtra(Const.REGION_ID, regions?.get(i)?.iD)
                    intent.putExtra("REGION_NAME", regions?.get(i)?.name)
                    thisActivity.startActivity(intent)
                }
                alertRegions.loadingSuccess()
            }
        }, Runnable { alertRegions.loadingError() })
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val listener: OnGlobalLayoutListener = this
                scrollView.post {
                    scrollView.scrollTo(0, 0)
                    scrollView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        })
    }

    override fun onBackPressed() {
        if (mListViewSearch.isVisible){
            mListViewSearch.visibility = View.GONE
            mEditTextSearch.clearFocus()
        } else {
            super.onBackPressed()
        }
    }
}