package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.models.Event

class EventsAdapter(private val mContext: Context, resource: Int, objects: List<Event?>?) : ArrayAdapter<Event?>(mContext, resource, objects as MutableList<Event?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val event = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_events, parent, false)
        }
        val textViewEventDate = convertView!!.findViewById<TextView>(R.id.textview_event_date)
        val textViewEventDesc = convertView.findViewById<TextView>(R.id.textview_event_desc)
        textViewEventDate.text = event!!.date
        textViewEventDesc.text = event.description
        //TextUtils.justify(textViewEventDesc);
        return convertView
    }

}