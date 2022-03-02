package com.niceplaces.niceplaces.models;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo on 29/12/2017.
 */

public class Place {

    private String mID;
    public String mName, mDescription, mImage, mCredits, mWikiUrl, mSources;
    public boolean mHasDescription;
    public double mLatitude, mLongitude;
    public double mDistance;
    public Marker mMarker;
    private List<Event> mEvents;
    private List<Link> mLinks;

    public Place(String id, String name, String image){
        mID = id;
        mName = name;
        mImage = image;
    }

    public Place(String id, String name, String desc, String sources, double latitude, double longitude, String image, String credits, String wikiUrl){
        mID = id;
        mName = name;
        mDescription = desc;
        mSources = sources;
        mLatitude = latitude;
        mLongitude = longitude;
        mImage = image;
        mEvents = new ArrayList<>();
        mCredits = credits;
        mWikiUrl = wikiUrl;
    }

    public Place(String id, String name, double latitude, double longitude, String image, boolean hasDescription){
        mID = id;
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
        mImage = image;
        mEvents = new ArrayList<>();
        mHasDescription = hasDescription;
    }

    public void setEvents(List<Event> events){
        mEvents = events;
    }

    public void setLinks(List<Link> links){
        mLinks = links;
    }

    public String getID(){
        return mID;
    }

    public List<Event> getEvents(){
        return mEvents;
    }

    public List<Link> getLinks(){
        return mLinks;
    }

    public static String formatDistance(double distance){
        String unit = "m";
        if (distance > 1000){
            distance = distance / 1000;
            unit = "km";
            return String.format("%.02f", distance) + " " + unit;
        } else {
            return String.valueOf(Math.round(distance)) + " " + unit;
        }
    }

}
