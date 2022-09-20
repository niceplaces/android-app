package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.activities.MapsActivity
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.ImageUtils
import com.niceplaces.niceplaces.utils.MapUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONObject

class PlacesAdapter(val context: Context, private val map: GoogleMap) :
    ListAdapter<Place, PlacesAdapter.PlacesViewHolder>(PlacesDiffCallback) {

    class PlacesViewHolder(itemView: View, val context: Context, val map: GoogleMap) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewPlaceName = itemView.findViewById<TextView>(R.id.textview_place_name)
        private val textViewPlaceDistance =
            itemView.findViewById<TextView>(R.id.textview_place_distance)
        private val imageViewPlaceImage =
            itemView.findViewById<ImageView>(R.id.imageview_place_image)
        private val imageViewPlaceStar = itemView.findViewById<ImageView>(R.id.imageview_place_star)
        private var currentPlace: Place? = null

        fun bind(place: Place) {
            currentPlace = place
            textViewPlaceName.text = place.mName
            textViewPlaceDistance.text = Place.formatDistance(place.mDistance)
            ImageUtils.setAuthorIcon(place, imageViewPlaceStar)
            if (place.mImage != "") {
                ImageUtils.setImageViewWithGlide(context, place.mImage, imageViewPlaceImage)
            } else {
                if (place.mWikiUrl != "") {
                    val pageName = place.mWikiUrl?.let {
                        place.mWikiUrl?.substring(it.lastIndexOf('/') + 1)
                    }
                    if (pageName != null) {
                        DaoPlaces.getWikipediaData(context, pageName, false,
                            object : MyRunnable() {
                                override fun run() {
                                    val data = JSONObject(this.wikipediaData)
                                    if (place.mImage == "") {
                                        data.getJSONObject("thumbnail").getString("source").let {
                                            ImageUtils.setImageViewFromURL(
                                                context,
                                                it,
                                                imageViewPlaceImage
                                            )
                                        }
                                    }
                                }
                            },
                            Runnable() {

                            })
                    }
                }
            }
            itemView.setOnClickListener {
                val center = LatLng(place.mLatitude, place.mLongitude)
                val callback: CancelableCallback = object : CancelableCallback {
                    override fun onFinish() {
                        map.animateCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(MapUtils.offset(map, center, 0, -150))
                                    .tilt(MapsActivity.DEFAULT_TILT.toFloat())
                                    .zoom(map.cameraPosition.zoom).build()
                            )
                        )
                        place.mClusterItem?.marker?.showInfoWindow()
                        map.uiSettings.isMapToolbarEnabled = true
                    }

                    override fun onCancel() {}
                }
                var zoom = map.cameraPosition.zoom
                if (place.mClusterItem?.isMarkerClustered!!) {
                    zoom = 18f
                }
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(center)
                            .tilt(MapsActivity.DEFAULT_TILT.toFloat())
                            .zoom(zoom).build()
                    ), callback
                )
            }
        }
    }

    /* Creates and inflates view and return PlaceViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_places, parent, false)
        return PlacesViewHolder(view, context, map)
    }

    /* Gets current place and uses it to bind view. */
    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

}

object PlacesDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.mName == newItem.mName
    }
}