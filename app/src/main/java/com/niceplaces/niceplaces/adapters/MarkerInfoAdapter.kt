package com.niceplaces.niceplaces.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.ImageUtils

/**
 * Created by Lorenzo on 31/12/2017.
 */
class MarkerInfoAdapter(private val mActivity: Activity, private val mContext: Context) : InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View {
        val place = marker.tag as Place?
        val layoutInflater = LayoutInflater.from(mContext)
        val convertView = layoutInflater.inflate(R.layout.infowindow_places, null)
        val imageViewPlaceStar = convertView.findViewById<ImageView>(R.id.imageview_place_star)
        val imageViewPlaceImage = convertView.findViewById<ImageView>(R.id.infowindow_place_image)
        if (place != null) {
            ImageUtils.setAuthorIcon(place, imageViewPlaceStar)
        }
        val textViewPlaceName = convertView.findViewById<TextView>(R.id.textview_place_name)
        val textViewPlaceDistance = convertView.findViewById<TextView>(R.id.textview_place_distance)
        //Log.i(AppUtils.getTag(), "IMAGE: " + place.mImage);
        ImageUtils.setImageViewWithGlide(mActivity, mContext, marker, place!!.mImage, imageViewPlaceImage)
        textViewPlaceName.text = place.mName
        textViewPlaceDistance.text = Place.formatDistance(place.mDistance)
        return convertView
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

}