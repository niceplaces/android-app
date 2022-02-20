package com.niceplaces.niceplaces.models;

/**
 * Created by Lorenzo on 29/12/2017.
 */

public class Link {

    private String mID, mPlaceID, mLabel, mUrl;

    public Link(String id, String placeID, String label, String url){
        mID = id;
        mPlaceID = placeID;
        mLabel = label;
        mUrl = url;
    }

    public String getID(){
        return mID;
    }

    public String getPlaceID(){
        return mPlaceID;
    }

    public String getLabel(){
        return mLabel;
    }

    public String getUrl(){
        return mUrl;
    }

}
