package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.models.Notification
import com.niceplaces.niceplaces.utils.ImageUtils

class NotificationsAdapter(private val mContext: Context, resource: Int, objects: List<Notification?>?) :
        ArrayAdapter<Notification?>(mContext, resource, objects as MutableList<Notification?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val notification = getItem(position)
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.listview_notifications, parent, false)
        }
        val imageViewNotifImage = convertView!!.findViewById<ImageView>(R.id.imageview_notif_image)
        val textViewDate = convertView.findViewById<TextView>(R.id.textview_notif_date)
        val textViewTitle = convertView.findViewById<TextView>(R.id.textview_notif_title)
        val textViewMessage = convertView.findViewById<TextView>(R.id.textview_notif_msg)
        if (notification != null ) {
            if (notification.image != null){
                ImageUtils.setImageViewWithGlideFullURL(mContext, notification.image, imageViewNotifImage)
            }
            textViewDate.text = notification.date
            textViewTitle.text = notification.title
            textViewMessage.text = notification.message
            if (notification.link != null) {
                convertView.setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(notification.link))
                    mContext.startActivity(i)
                }
            } else {
                val imageViewLink = convertView.findViewById<ImageView>(R.id.imageview_notif_link)
                imageViewLink.visibility = GONE
            }
        }
        return convertView
    }

}