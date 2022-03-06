package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.models.Area

class AreasAdapter(context: Context?, resource: Int, objects: List<Area?>?) : ArrayAdapter<Area?>(context!!, resource, objects as MutableList<Area?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val area = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_areas, parent, false)
        }
        val textViewAreaName = convertView!!.findViewById<TextView>(R.id.textview_area_name)
        val textViewAreaCount = convertView.findViewById<TextView>(R.id.textview_area_count)
        textViewAreaName.text = area!!.name
        textViewAreaCount.text = "(" + area.count + ")"
        return convertView
    }
}