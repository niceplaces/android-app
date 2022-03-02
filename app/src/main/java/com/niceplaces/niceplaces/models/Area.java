package com.niceplaces.niceplaces.models;

public class Area {

    private String mID;
    private String mName, mCount;

    public Area(String id, String name){
        mID = id;
        mName = name;
    }

    public Area(String id, String name, String count){
        mID = id;
        mName = name;
        mCount = count;
    }

    public String getID(){
        return mID;
    }

    public String getName(){
        return mName;
    }

    public String getCount(){
        return mCount;
    }

}
