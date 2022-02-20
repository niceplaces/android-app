package com.niceplaces.niceplaces.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.niceplaces.niceplaces.models.Event;

import java.util.ArrayList;
import java.util.List;

public class DaoEvents {

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private String SELECT_ALL = "SELECT id, place_id, date, description FROM events";

    public DaoEvents(SQLiteDatabase db, Context context){
        mDatabase = db;
        mContext = context;
    }

    /*public List<Link> getAll(){
        Cursor res = mDatabase.rawQuery(SELECT_ALL, null);
        List<Link> buffer = new ArrayList<>();
        if (res.getCount() == 0) {
            return null;
        }
        while (res.moveToNext()) {
            Link link = new Link(res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3));
            buffer.add(link);
        }
        return buffer;
    }*/

    public List<Event> getByPlace(String placeID){
        Cursor res = mDatabase.rawQuery(SELECT_ALL + " WHERE place_id = ?", new String[]{placeID});
        List<Event> buffer = new ArrayList<>();
        if (res.getCount() == 0) {
            return null;
        }
        while (res.moveToNext()) {
            Event event = new Event(res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3));
            buffer.add(event);
        }
        return buffer;
    }

    public boolean insert(Event event){
        ContentValues values = new ContentValues();
        values.put("id", event.getID());
        values.put("place_id", event.getPlaceID());
        values.put("date", event.getDate());
        values.put("description", event.getDescription());
        long res = mDatabase.insert("events", null, values);
        return res > -1;
    }

}
