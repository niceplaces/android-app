package com.niceplaces.niceplaces.models;

/**
 * Created by Lorenzo on 05/01/2018.
 */

public class Event {

    private String mID, mPlaceID, mDate, mDescription;

    public Event(String id, String placeID, String date, String description){
        mID = id;
        mPlaceID = placeID;
        mDate = date;
        mDescription = description;
    }

    public String getID(){
        return mID;
    }

    public String getPlaceID(){
        return mPlaceID;
    }

    public String getDate(){
        return mDate;
    }

    public String getDescription(){
        return mDescription;
    }

}
