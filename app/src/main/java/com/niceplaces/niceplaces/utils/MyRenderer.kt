package com.niceplaces.niceplaces.utils

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.UserListsController
import com.niceplaces.niceplaces.models.MyClusterItem
import com.niceplaces.niceplaces.models.Place.Companion.formatDistance

class MyRenderer(private val mContext: Context, map: GoogleMap?, manager: ClusterManager<MyClusterItem>?) : DefaultClusterRenderer<MyClusterItem>(mContext, map, manager) {
    override fun onBeforeClusterItemRendered(item: MyClusterItem, markerOptions: MarkerOptions) {
        val prefs = UserListsController(mContext)
        var markerDrawable = R.drawable.marker_places
        if (prefs.isFavourite(item.place.iD)) {
            markerDrawable = R.drawable.marker_fav
        } else if (prefs.isWished(item.place.iD)) {
            markerDrawable = R.drawable.marker_wish
        } else if (prefs.isVisited(item.place.iD)) {
            markerDrawable = R.drawable.marker_visited
        }
        markerOptions.position(item.position)
                .icon(ImageUtils.bitmapDescriptorFromDrawable(mContext, markerDrawable))
                .title(item.title + ", " + formatDistance(item.place.mDistance))
    }

    override fun onBeforeClusterRendered(cluster: Cluster<MyClusterItem>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
    }

    override fun onClusterRendered(cluster: Cluster<MyClusterItem>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        val iter: Iterator<MyClusterItem> = cluster.items.iterator()
        while (iter.hasNext()) {
            val clusterItem = iter.next()
            clusterItem.marker = marker
            clusterItem.cluster = cluster
        }
    }

    override fun onClusterItemRendered(clusterItem: MyClusterItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        marker.tag = clusterItem.place
        clusterItem.marker = marker
        clusterItem.cluster = null
    }

}