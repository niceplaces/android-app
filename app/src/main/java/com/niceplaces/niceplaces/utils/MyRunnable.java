package com.niceplaces.niceplaces.utils;

import com.niceplaces.niceplaces.models.Area;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.models.PlacesList;
import com.niceplaces.niceplaces.models.Region;

import java.util.List;

public abstract class MyRunnable implements Runnable {

    private List<Region> mRegions;
    private List<Area> mAreas;
    private List<Place> mPlaces;
    private Place mPlace;
    private List<PlacesList> mLists;

    public void setRegions(List<Region> regions){
        mRegions = regions;
    }

    public List<Region> getRegions() {
        return mRegions;
    }

    public void setAreas(List<Area> areas){
        mAreas = areas;
    }

    public List<Area> getAreas() {
        return mAreas;
    }

    public void setPlaces(List<Place> places){
        mPlaces = places;
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }

    public void setPlace(Place place){
        mPlace = place;
    }

    public Place getPlace() {
        return mPlace;
    }

    public void setLists(List<PlacesList> lists){
        mLists = lists;
    }

    public List<PlacesList> getLists() {
        return mLists;
    }
}
