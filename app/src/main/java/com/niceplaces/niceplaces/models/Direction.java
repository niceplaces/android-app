package com.niceplaces.niceplaces.models;

/**
 * Created by Lorenzo on 02/01/2018.
 */

public class Direction {

    public double mStartLatitude;
    public double mStartLongitude;

    public double mEndLatitude;
    public double mEndLongitude;

    public Direction(double startLat, double startLon, double endLat, double endLon){
        mStartLatitude = startLat;
        mStartLongitude = startLon;
        mEndLatitude = endLat;
        mEndLongitude = endLon;
    }

}
