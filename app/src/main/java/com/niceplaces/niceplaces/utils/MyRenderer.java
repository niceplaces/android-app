package com.niceplaces.niceplaces.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.UserListsController;
import com.niceplaces.niceplaces.models.MyClusterItem;
import com.niceplaces.niceplaces.models.Place;

import java.util.Iterator;
import java.util.Set;

public class MyRenderer extends DefaultClusterRenderer<MyClusterItem> {

    private Context mContext;

    public MyRenderer(Context context, GoogleMap map, ClusterManager<MyClusterItem> manager){
        super(context, map, manager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyClusterItem item, MarkerOptions markerOptions) {
        UserListsController prefs = new UserListsController(mContext);
        int markerDrawable = R.drawable.marker_places;
        if (prefs.isFavourite(item.getPlace().getID())){
            markerDrawable = R.drawable.marker_fav;
        } else if (prefs.isWished(item.getPlace().getID())){
            markerDrawable = R.drawable.marker_wish;
        } else if (prefs.isVisited(item.getPlace().getID())){
            markerDrawable = R.drawable.marker_visited;
        }
        markerOptions.position(item.getPosition())
                .icon(ImageUtils.bitmapDescriptorFromDrawable(mContext, markerDrawable))
                .title(item.getTitle() + ", " + Place.formatDistance(item.getPlace().mDistance));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MyClusterItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected void onClusterRendered(Cluster<MyClusterItem> cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);
        Iterator<MyClusterItem> iter = cluster.getItems().iterator();
        while (iter.hasNext()){
            MyClusterItem clusterItem = iter.next();
            clusterItem.setMarker(marker);
            clusterItem.setCluster(cluster);
        }
    }

    @Override
    protected void onClusterItemRendered(MyClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        marker.setTag(clusterItem.getPlace());
        clusterItem.setMarker(marker);
        clusterItem.setCluster(null);
    }
}
