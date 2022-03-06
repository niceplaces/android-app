package com.niceplaces.niceplaces.models;

public class PlacesList {

    private String mID;
    private String mName, mCount, mDescription;

    public PlacesList(String id, String name, String description, String count){
        mID = id;
        mName = name;
        mDescription = description;
        mCount = count;
    }

    public String getID(){
        return mID;
    }

    public String getName(){
        return mName;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getCount(){
        return mCount;
    }

}
