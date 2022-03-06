package com.niceplaces.niceplaces.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo on 29/12/2017.
 */

public class Place {

    private String mID;
    public String mName, mArea, mRegion, mDescription, mAuthor, mImage, mCredits, mWikiUrl, mSources, mFacebook, mInstagram;
    public boolean mHasDescription;
    public double mLatitude, mLongitude;
    public double mDistance;
    public MyClusterItem mClusterItem;
    private List<Event> mEvents;
    private List<Link> mLinks;
    public boolean isInfoWindowShown;

    public Place(String id, String name, String image){
        mID = id;
        mName = name;
        mImage = image;
    }

    public Place(String id, String name, String image, boolean hasDesc, String author){
        mID = id;
        mName = name;
        mImage = image;
        mHasDescription = hasDesc;
        mAuthor = author;
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
        isInfoWindowShown = false;
    }

    public Place(String id, String name, String area, String region, String desc, String author, String sources, double latitude, double longitude,
                 String image, String credits, String wikiUrl, String facebook, String instagram){
        mID = id;
        mName = name;
        mArea = area;
        mRegion = region;
        mDescription = desc;
        mAuthor = author;
        mSources = sources;
        mLatitude = latitude;
        mLongitude = longitude;
        mImage = image;
        mEvents = new ArrayList<>();
        mCredits = credits;
        mWikiUrl = wikiUrl;
        mFacebook = facebook;
        mInstagram = instagram;
    }

    public Place(String id, String name, String area, String desc, String sources, double latitude, double longitude, String image, String credits, String wikiUrl){
        mID = id;
        mName = name;
        mArea = area;
        mDescription = desc;
        mSources = sources;
        mLatitude = latitude;
        mLongitude = longitude;
        mImage = image;
        mEvents = new ArrayList<>();
        mCredits = credits;
        mWikiUrl = wikiUrl;
        isInfoWindowShown = false;
    }

    public Place(String id, String name, double latitude, double longitude, String image, boolean hasDescription, String author){
        mID = id;
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
        mImage = image;
        mEvents = new ArrayList<>();
        mHasDescription = hasDescription;
        mAuthor = author;
    }

    public Place(String id, String name, String area, String region, String image, boolean hasDescription, String author){
        mID = id;
        mName = name;
        mArea = area;
        mRegion = region;
        mImage = image;
        mEvents = new ArrayList<>();
        mHasDescription = hasDescription;
        mAuthor = author;
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
