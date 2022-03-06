package com.niceplaces.niceplaces.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.EventsAdapter
import com.niceplaces.niceplaces.controllers.AlertController
import com.niceplaces.niceplaces.controllers.UserListsController
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.utils.ImageUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import com.niceplaces.niceplaces.utils.NonScrollListView
import java.util.*

class PlaceDetailsActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    private var visitedChecked = false
    private var wishChecked = false
    private var favChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        mContext = this
        mActivity = this
        supportActionBar!!.hide()
        val layoutTitleBar = findViewById<RelativeLayout>(R.id.layout_titlebar)
        layoutTitleBar.visibility = View.GONE
        val extras = intent.extras
        val placeID = extras?.getString(Const.PLACE_ID)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        /*final LinearLayout layoutPlaceShare = findViewById(R.id.layout_place_share);
        final ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        final ImageView IVInstagram = findViewById(R.id.imageview_instagram);*/
        val IVVisited = findViewById<ImageView>(R.id.imageview_visited)
        val IVWish = findViewById<ImageView>(R.id.imageview_wish)
        val IVFav = findViewById<ImageView>(R.id.imageview_fav)
        val prefs = UserListsController(this)
        visitedChecked = placeID?.let { prefs.isVisited(it) }!!
        wishChecked = prefs.isWished(placeID)
        favChecked = prefs.isFavourite(placeID)
        var TVCaption = findViewById<TextView>(R.id.textview_visited)
        if (visitedChecked) {
            IVVisited.setImageResource(R.drawable.check)
            TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorVisited))
        } else {
            IVVisited.setImageResource(R.drawable.check_outline)
            TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled))
        }
        IVVisited.setOnClickListener { v ->
            val TVCaption = (mActivity as PlaceDetailsActivity).findViewById<TextView>(R.id.textview_visited)
            if (!visitedChecked) {
                (v as ImageView).setImageResource(R.drawable.check)
                TVCaption.setTextColor((mActivity as PlaceDetailsActivity).getResources().getColor(R.color.colorVisited))
                prefs.addVisited(placeID)
            } else {
                (v as ImageView).setImageResource(R.drawable.check_outline)
                TVCaption.setTextColor((mActivity as PlaceDetailsActivity).getResources().getColor(R.color.colorDisabled))
                prefs.removeVisited(placeID)
            }
            visitedChecked = !visitedChecked
        }
        TVCaption = findViewById(R.id.textview_wish)
        if (wishChecked) {
            IVWish.setImageResource(R.drawable.wish)
            TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorWish))
        } else {
            IVWish.setImageResource(R.drawable.wish_outline)
            TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled))
        }
        IVWish.setOnClickListener { v ->
            val TVCaption = (mActivity as PlaceDetailsActivity).findViewById<TextView>(R.id.textview_wish)
            if (!wishChecked) {
                (v as ImageView).setImageResource(R.drawable.wish)
                TVCaption.setTextColor((mActivity as PlaceDetailsActivity).getResources().getColor(R.color.colorWish))
                prefs.addWished(placeID)
            } else {
                (v as ImageView).setImageResource(R.drawable.wish_outline)
                TVCaption.setTextColor((mActivity as PlaceDetailsActivity).getResources().getColor(R.color.colorDisabled))
                prefs.removeWished(placeID)
            }
            wishChecked = !wishChecked
        }
        TVCaption = findViewById(R.id.textview_fav)
        if (favChecked) {
            IVFav.setImageResource(R.drawable.heart)
            TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorFavourite))
        } else {
            IVFav.setImageResource(R.drawable.heart_outline)
            TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled))
        }
        IVFav.setOnClickListener { v ->
            val TVCaption = (mActivity as PlaceDetailsActivity).findViewById<TextView>(R.id.textview_fav)
            if (!favChecked) {
                (v as ImageView).setImageResource(R.drawable.heart)
                TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorFavourite))
                prefs.addFavourite(placeID)
            } else {
                (v as ImageView).setImageResource(R.drawable.heart_outline)
                TVCaption.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled))
                prefs.removeFavourite(placeID)
            }
            favChecked = !favChecked
        }
        val alertController = AlertController(this, R.id.layout_loading)
        val daoPlaces = DaoPlaces(this)
        daoPlaces.getOne(placeID, object : MyRunnable() {
            override fun run() {
                val textViewPlaceName = findViewById<TextView>(R.id.textview_place_name)
                val textViewPlaceNameActionBar = findViewById<TextView>(R.id.textview_place_name_titlebar)
                val textViewPlaceDesc = findViewById<TextView>(R.id.textview_place_desc)
                val layoutNavigation = findViewById<LinearLayout>(R.id.layout_navigation)
                layoutNavigation.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + (place?.mLatitude) + "," + (place?.mLongitude))))
                }
                val imageViewPlace = findViewById<ImageView>(R.id.imageview_place_image)
                val imageViewPlaceActionBar = findViewById<ImageView>(R.id.imageview_place_image_titlebar)
                val placeArea = findViewById<TextView>(R.id.place_area)
                val imageViewWikipedia = findViewById<ImageView>(R.id.imageview_wikipedia)
                val listViewEvents = findViewById<NonScrollListView>(R.id.listview_place_events)
                textViewPlaceName.text = place?.mName
                textViewPlaceNameActionBar.text = place?.mName
                textViewPlaceDesc.text = place?.mDescription
                placeArea.text = place?.mArea + ", " + place?.mRegion
                //TextUtils.justify(textViewPlaceDesc);
                place?.mImage?.let { ImageUtils.setImageViewWithGlide(mContext, it, imageViewPlace) }
                place?.mImage?.let { ImageUtils.setImageViewWithGlide(mContext, it, imageViewPlaceActionBar) }
                if (place?.mDescription?.compareTo("") == 0) {
                    val textViewComingSoon = findViewById<TextView>(R.id.textview_coming_soon)
                    textViewComingSoon.visibility = View.VISIBLE
                    val layoutPlaceInfo = findViewById<LinearLayout>(R.id.layout_place_info)
                    layoutPlaceInfo.visibility = View.GONE
                } else {
                    val badgeProLoco = findViewById<LinearLayout>(R.id.badge_proloco)
                    val badgeViaSacra = findViewById<LinearLayout>(R.id.badge_viasacra)
                    when (place?.mAuthor) {
                        "1" -> {
                            badgeProLoco.visibility = View.GONE
                            badgeViaSacra.visibility = View.GONE
                            try {
                                val wikipediaUrl = place!!.mWikiUrl
                                if (wikipediaUrl != "") {
                                    imageViewWikipedia.setOnClickListener {
                                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
                                        startActivity(i)
                                    }
                                } else {
                                    imageViewWikipedia.alpha = 0.5f
                                }
                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                                imageViewWikipedia.alpha = 0.5f
                            }
                            imageViewWikipedia.visibility = View.VISIBLE
                            findViewById<View>(R.id.imageview_proloco).visibility = View.GONE
                            findViewById<View>(R.id.imageview_viasacra).visibility = View.GONE
                        }
                        "2" -> {
                            val listener = View.OnClickListener {
                                val i = Intent(Intent.ACTION_VIEW,
                                        Uri.parse(Const.PROLOCO_URL))
                                startActivity(i)
                            }
                            badgeProLoco.setOnClickListener(listener)
                            badgeProLoco.visibility = View.VISIBLE
                            badgeViaSacra.visibility = View.GONE
                            imageViewWikipedia.visibility = View.GONE
                            findViewById<View>(R.id.imageview_proloco).visibility = View.VISIBLE
                            findViewById<View>(R.id.imageview_proloco).setOnClickListener(listener)
                            findViewById<View>(R.id.imageview_viasacra).visibility = View.GONE
                        }
                        "3" -> {
                            val listener1 = View.OnClickListener {
                                val i = Intent(Intent.ACTION_VIEW,
                                        Uri.parse(Const.VIASACRA_URL))
                                startActivity(i)
                            }
                            badgeViaSacra.setOnClickListener(listener1)
                            badgeViaSacra.visibility = View.VISIBLE
                            badgeProLoco.visibility = View.GONE
                            imageViewWikipedia.visibility = View.GONE
                            findViewById<View>(R.id.imageview_proloco).visibility = View.GONE
                            findViewById<View>(R.id.imageview_viasacra).visibility = View.VISIBLE
                            findViewById<View>(R.id.imageview_viasacra).setOnClickListener(listener1)
                        }
                    }
                }
                val events = place?.events
                if (events != null) {
                    if (!events.isEmpty() && Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage && place?.mAuthor == "1") {
                        val adapter = EventsAdapter(mContext as PlaceDetailsActivity, R.layout.listview_events, events)
                        listViewEvents.adapter = adapter
                        listViewEvents.isEnabled = false
                    } else {
                        val layoutHistory = findViewById<LinearLayout>(R.id.layout_history)
                        layoutHistory.visibility = View.GONE
                    }
                }
                textViewPlaceName.viewTreeObserver.addOnScrollChangedListener {
                    val location = IntArray(2)
                    textViewPlaceName.getLocationOnScreen(location)
                    val pos = location[1]
                    textViewPlaceNameActionBar.getLocationOnScreen(location)
                    val posTitleBar = location[1]
                    if (pos > posTitleBar) {
                        layoutTitleBar.visibility = View.GONE
                    } else {
                        layoutTitleBar.visibility = View.VISIBLE
                    }
                }
                val textViewDescSources = findViewById<TextView>(R.id.textview_desc_sources)
                val textViewImageCredits = findViewById<TextView>(R.id.textview_img_credits)
                if (place?.mSources != "") {
                    textViewDescSources.text = getString(R.string.sources, place?.mSources)
                } else {
                    textViewDescSources.text = ""
                }
                if (place?.mCredits != "") {
                    textViewImageCredits.text = getString(R.string.photo, place?.mCredits)
                } else {
                    textViewImageCredits.text = ""
                }
                /*if (getPlace().mFacebook.equals("") && getPlace().mInstagram.equals("")) {
                    layoutPlaceShare.setVisibility(View.GONE);
                } else {
                    IVFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getPlace().mFacebook));
                            startActivity(i);
                        }
                    });
                    IVInstagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getPlace().mInstagram));
                            startActivity(i);
                        }
                    });
                }*/scrollView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val listener: OnGlobalLayoutListener = this
                        scrollView.post {
                            scrollView.scrollTo(0, 0)
                            scrollView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                        }
                    }
                })
                alertController.loadingSuccess()
            }
        }, Runnable { alertController.loadingError() })
    }
}