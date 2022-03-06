package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.UserListsController
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.ImageUtils

class LatestPlacesAdapter(private val mContext: Context, resource: Int, objects: List<Place?>?) : ArrayAdapter<Place?>(mContext, resource, objects as MutableList<Place?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val place = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_new_places, parent, false)
        }
        val textViewName = convertView!!.findViewById<TextView>(R.id.textview_place_name)
        val textViewArea = convertView.findViewById<TextView>(R.id.textview_place_area)
        val imageViewPlaceImage = convertView.findViewById<ImageView>(R.id.imageview_place_image)
        val IVStar = convertView.findViewById<ImageView>(R.id.imageview_place_star)
        val IVVisited = convertView.findViewById<ImageView>(R.id.imageview_visited)
        val IVWished = convertView.findViewById<ImageView>(R.id.imageview_wished)
        val IVFavourite = convertView.findViewById<ImageView>(R.id.imageview_fav)
        val prefs = UserListsController(mContext)
        if (prefs.isVisited(place!!.iD)) {
            IVVisited.visibility = View.VISIBLE
        } else {
            IVVisited.visibility = View.GONE
        }
        if (prefs.isWished(place.iD)) {
            IVWished.visibility = View.VISIBLE
        } else {
            IVWished.visibility = View.GONE
        }
        if (prefs.isFavourite(place.iD)) {
            IVFavourite.visibility = View.VISIBLE
        } else {
            IVFavourite.visibility = View.GONE
        }
        ImageUtils.setAuthorIcon(place, IVStar)
        textViewName.text = place.mName
        textViewArea.text = place.mArea + ", " + place.mRegion
        ImageUtils.setImageViewWithGlide(mContext, place.mImage, imageViewPlaceImage)
        return convertView
    }

}