package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.models.PlacesList

class ListsAdapter(context: Context?, resource: Int, objects: List<PlacesList?>?) : ArrayAdapter<PlacesList?>(context!!, resource, objects as MutableList<PlacesList?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val list = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_areas, parent, false)
        }
        val textViewAreaName = convertView!!.findViewById<TextView>(R.id.textview_area_name)
        val textViewAreaCount = convertView.findViewById<TextView>(R.id.textview_area_count)
        textViewAreaName.text = list!!.name
        textViewAreaCount.text = "(" + list.count + ")"
        return convertView
    }
}