package com.niceplaces.niceplaces.utils;

import android.graphics.Point;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapUtils {

    public static LatLng offset(GoogleMap map, LatLng latLng, int offsetX, int offsetY) {
        Point point = map.getProjection().toScreenLocation(latLng);
        point.x = point.x + offsetX;
        point.y = point.y + offsetY;
        return map.getProjection().fromScreenLocation(point);
    }
}