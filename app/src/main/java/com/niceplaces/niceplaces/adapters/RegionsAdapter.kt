package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.models.Region

class RegionsAdapter(context: Context?, resource: Int, objects: List<Region?>?) : ArrayAdapter<Region?>(context!!, resource, objects as MutableList<Region?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val region = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_areas, parent, false)
        }
        val textViewAreaName = convertView!!.findViewById<TextView>(R.id.textview_area_name)
        val textViewAreaCount = convertView.findViewById<TextView>(R.id.textview_area_count)
        textViewAreaName.text = region!!.name
        textViewAreaCount.text = "(" + region.count + ")"
        return convertView
    }
}