package com.niceplaces.niceplaces.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;

public class MyClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private Place mPlace;
    private Marker mMarker;
    private Cluster<MyClusterItem> mCluster;

    public MyClusterItem(Place place) {
        mPosition = new LatLng(place.mLatitude, place.mLongitude);
        mTitle = place.mName;
        mSnippet = place.mName;
        mPlace = place;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public Place getPlace() {
        return mPlace;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setCluster(Cluster<MyClusterItem> cluster) {
        mCluster = cluster;
    }

    public Cluster<MyClusterItem> getCluster() {
        return mCluster;
    }

    public boolean isMarkerClustered() {
        return mCluster != null;
    }

}
