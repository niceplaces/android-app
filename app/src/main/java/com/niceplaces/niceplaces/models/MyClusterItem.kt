package com.niceplaces.niceplaces.models

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem

class MyClusterItem(place: Place) : ClusterItem {
    private val mPosition: LatLng
    private val mTitle: String
    private val mSnippet: String
    val place: Place
    var marker: Marker? = null
    var cluster: Cluster<MyClusterItem>? = null
    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    val isMarkerClustered: Boolean
        get() = cluster != null

    init {
        mPosition = LatLng(place.mLatitude, place.mLongitude)
        mTitle = place.mName
        mSnippet = place.mName
        this.place = place
    }
}