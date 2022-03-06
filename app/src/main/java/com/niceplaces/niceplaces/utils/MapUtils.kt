package com.niceplaces.niceplaces.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

object MapUtils {
    fun offset(map: GoogleMap, latLng: LatLng?, offsetX: Int, offsetY: Int): LatLng {
        val point = map.projection.toScreenLocation(latLng)
        point.x = point.x + offsetX
        point.y = point.y + offsetY
        return map.projection.fromScreenLocation(point)
    }
}