package com.niceplaces.niceplaces.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.niceplaces.niceplaces.models.Place;

import java.util.ArrayList;
import java.util.List;

public class DaoPlaces {

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private String SELECT_ALL = "SELECT id, name, description, image, credits, latitude, longitude FROM places";

    public DaoPlaces(SQLiteDatabase db, Context context){
        mDatabase = db;
        mContext = context;
    }

    public List<Place> getAll(){
        Cursor res = mDatabase.rawQuery(SELECT_ALL, null);
        List<Place> buffer = new ArrayList<>();
        if (res.getCount() == 0) {
            return null;
        }
        DaoLinks daoLinks = new DaoLinks(mDatabase, mContext);
        while (res.moveToNext()) {
            Place place = new Place(res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getDouble(5),
                    res.getDouble(6),
                    res.getString(3),
                    res.getString(4));
            place.setLinks(daoLinks.getByPlace(res.getString(0)));
            buffer.add(place);
        }
        return buffer;
    }

    public Place getOne(String id){
        Cursor res = mDatabase.rawQuery(SELECT_ALL + " WHERE id = ?", new String[]{id});
        Place place = null;
        if (res.getCount() == 0) {
            return null;
        }
        DaoLinks daoLinks = new DaoLinks(mDatabase, mContext);
        DaoEvents daoEvents = new DaoEvents(mDatabase, mContext);
        if (res.moveToNext()) {
            place = new Place(res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getDouble(5),
                    res.getDouble(6),
                    res.getString(3),
                    res.getString(4));
            place.setEvents(daoEvents.getByPlace(res.getString(0)));
            place.setLinks(daoLinks.getByPlace(res.getString(0)));
        }
        return place;
    }

    public boolean insert(Place place){
        ContentValues placeValues = new ContentValues();
        placeValues.put("id", place.getID());
        placeValues.put("name", place.mName);
        placeValues.put("description", place.mDescription);
        placeValues.put("image", place.mImage);
        placeValues.put("credits", place.mCredits);
        placeValues.put("latitude", place.mLatitude);
        placeValues.put("longitude", place.mLongitude);
        long res = mDatabase.insert("places", null, placeValues);
        return res > -1;
    }

}
