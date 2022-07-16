package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.UserListsController
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.ImageUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONObject

class ExplorePlacesAdapter(private val mContext: Context, resource: Int, objects: List<Place?>?) : ArrayAdapter<Place?>(mContext, resource, objects as MutableList<Place?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val place = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_places_explore, parent, false)
        }
        val textViewName = convertView!!.findViewById<TextView>(R.id.textview_place_name)
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
        Glide.with(this.mContext).clear(imageViewPlaceImage)
        if (place.mImage != "") {
            ImageUtils.setImageViewWithGlide(mContext, place.mImage, imageViewPlaceImage)
        } else {
            if (place.mWikiUrl != "") {
                val pageName = place.mWikiUrl?.let {
                    place.mWikiUrl?.substring(it.lastIndexOf('/')+1)
                }
                if (pageName != null) {
                    DaoPlaces.getWikipediaData(
                        mContext,
                        pageName,
                        object : MyRunnable() {
                            override fun run() {
                                val data = JSONObject(this.wikipediaData)
                                if (place.mImage == "") {
                                    data.getJSONObject("originalimage").getString("source").let {
                                        ImageUtils.setImageViewFromURL(
                                            mContext,
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
        return convertView
    }

}