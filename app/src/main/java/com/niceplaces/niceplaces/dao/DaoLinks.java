package com.niceplaces.niceplaces.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.niceplaces.niceplaces.models.Link;

import java.util.ArrayList;
import java.util.List;

public class DaoLinks {

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private String SELECT_ALL = "SELECT id, place_id, label, url FROM links";

    public DaoLinks(SQLiteDatabase db, Context context){
        mDatabase = db;
        mContext = context;
    }

    public List<Link> getAll(){
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
    }

    public List<Link> getByPlace(String placeID){
        Cursor res = mDatabase.rawQuery(SELECT_ALL + " WHERE place_id = ?", new String[]{placeID});
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
    }

    public boolean insert(Link link){
        ContentValues values = new ContentValues();
        values.put("id", link.getID());
        values.put("place_id", link.getPlaceID());
        values.put("label", link.getLabel());
        values.put("url", link.getUrl());
        long res = mDatabase.insert("links", null, values);
        return res > -1;
    }

}
