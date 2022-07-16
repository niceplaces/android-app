package com.niceplaces.niceplaces.utils

import com.niceplaces.niceplaces.models.*

abstract class MyRunnable : Runnable {
    var regions: List<Region>? = null
    private var areas: List<Area>? = null
    var places: List<Place>? = null
    var place: Place? = null
    var lists: List<PlacesList>? = null
    var quiz: MutableList<Quiz>? = ArrayList()
    var wikipediaData: String = ""

    fun setAreas(pAreas: List<Area>) {
        areas = pAreas
    }

    fun getAreas(): List<Area>?{
        return areas
    }
}