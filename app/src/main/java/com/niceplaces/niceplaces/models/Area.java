package com.niceplaces.niceplaces.models;

public class Area {

    private String mID;
    private String mName;

    public Area(String id, String name){
        mID = id;
        mName = name;
    }

    public String getID(){
        return mID;
    }

    public String getName(){
        return mName;
    }

}
