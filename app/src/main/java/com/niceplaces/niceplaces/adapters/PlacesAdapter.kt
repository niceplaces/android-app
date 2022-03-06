package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.activities.MapsActivity
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.ImageUtils
import com.niceplaces.niceplaces.utils.MapUtils

class PlacesAdapter(private val mContext: Context, resource: Int, objects: List<Place?>?,
                    private val mMap: GoogleMap, position: LatLng?) :
        ArrayAdapter<Place?>(mContext, resource, objects as MutableList<Place?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val place = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_places, parent, false)
        }
        val imageViewPlaceImage = convertView!!.findViewById<ImageView>(R.id.imageview_place_image)
        val imageViewPlaceStar = convertView.findViewById<ImageView>(R.id.imageview_place_star)
        if (place != null) {
            ImageUtils.setAuthorIcon(place, imageViewPlaceStar)
        }
        val textViewPlaceName = convertView.findViewById<TextView>(R.id.textview_place_name)
        val textViewPlaceDistance = convertView.findViewById<TextView>(R.id.textview_place_distance)
        ImageUtils.setImageViewWithGlide(mContext, place!!.mImage, imageViewPlaceImage)
        imageViewPlaceImage.setOnClickListener {
            val center = LatLng(place.mLatitude, place.mLongitude)
            val callback: CancelableCallback = object : CancelableCallback {
                override fun onFinish() {
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                    .target(MapUtils.offset(mMap, center, 0, -150))
                                    .tilt(MapsActivity.DEFAULT_TILT.toFloat())
                                    .zoom(mMap.cameraPosition.zoom).build()))
                    place.mClusterItem?.marker?.showInfoWindow()
                    mMap.uiSettings.isMapToolbarEnabled = true
                }

                override fun onCancel() {}
            }
            var zoom = mMap.cameraPosition.zoom
            if (place.mClusterItem?.isMarkerClustered!!) {
                zoom = 18f
            }
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                            .target(center)
                            .tilt(MapsActivity.DEFAULT_TILT.toFloat())
                            .zoom(zoom).build()), callback)
        }
        textViewPlaceName.text = place.mName
        textViewPlaceDistance.text = Place.formatDistance(place.mDistance)
        /*Direction direction = new Direction(mPosition.latitude, mPosition.longitude, place.mLatitude, place.mLongitude);
        (new DirectionAsyncTask(mContext, textViewPlaceDistance)).execute(direction);*/
        return convertView
    }

}