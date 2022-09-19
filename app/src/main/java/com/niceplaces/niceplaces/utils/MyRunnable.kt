package com.niceplaces.niceplaces.utils

import com.niceplaces.niceplaces.models.Area
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.models.PlacesList
import com.niceplaces.niceplaces.models.Region

abstract class MyRunnable : Runnable {
    var regions: List<Region>? = null
    private var areas: List<Area>? = null
    var places: List<Place>? = null
    var place: Place? = null
    var lists: List<PlacesList>? = null
    var wikipediaData: String = ""
    var wikipediaImageData: String = ""

    fun setAreas(pAreas: List<Area>) {
        areas = pAreas
    }

    fun getAreas(): List<Area>?{
        return areas
    }
}