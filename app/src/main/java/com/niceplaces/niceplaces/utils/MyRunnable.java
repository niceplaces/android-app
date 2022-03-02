package com.niceplaces.niceplaces.utils;

import com.niceplaces.niceplaces.models.Area;
import com.niceplaces.niceplaces.models.Place;

import java.util.List;

public abstract class MyRunnable implements Runnable {

    private List<Area> mAreas;
    private List<Place> mPlaces;
    private Place mPlace;

    public void setAreas(List<Area> areas){
        mAreas = areas;
    }

    public void setPlaces(List<Place> places){
        mPlaces = places;
    }

    public void setPlace(Place place){
        mPlace = place;
    }

    public List<Area> getAreas() {
        return mAreas;
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }

    public Place getPlace() {
        return mPlace;
    }
}
