package com.niceplaces.niceplaces.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.niceplaces.niceplaces.dao.DaoEvents;
import com.niceplaces.niceplaces.dao.DaoLinks;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.models.Link;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseController extends SQLiteOpenHelper {

    private boolean isCreating = false;
    private SQLiteDatabase currentDB = null;
    private Context mContext;

    public static final String PLACE_IMAGE = "place_image";
    public static final String PLACE_NAME = "place_name";
    public static final String PLACE_URL = "place_url";

    public DatabaseController(Context context){
        super(context, "data.db", null, 1);
        mContext = context;
        currentDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE places (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "description TEXT, " +
                "image TEXT, " +
                "credits TEXT, " +
                "latitude DOUBLE, " +
                "longitude DOUBLE, " +
                "active INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE events (" +
                "id INTEGER PRIMARY KEY, " +
                "place_id INTEGER, " +
                "date TEXT, " +
                "description TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE links (" +
                "id INTEGER PRIMARY KEY, " +
                "place_id INTEGER, " +
                "label TEXT, " +
                "url TEXT)");
        insertData(sqLiteDatabase);
    }

    private void insertData(SQLiteDatabase db){
        DaoPlaces daoPlaces = new DaoPlaces(db, mContext);
        DaoLinks daoLinks = new DaoLinks(db, mContext);
        DaoEvents daoEvents = new DaoEvents(db, mContext);
        int placesIDCount = 1;
        int linksIDCount = 1;
        int eventsIDCount = 1;
        try{
            JSONArray jsonArray = new JSONArray(JSONUtils.loadJSONFromAsset(mContext));
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPlace = jsonArray.getJSONObject(i);
                Log.i("B_ACTIVE", Integer.toString(jsonPlace.getInt("active")));
                if (jsonPlace.getInt("active") == 1){
                    String url = jsonPlace.getString("url");
                    daoPlaces.insert(new Place(String.valueOf(placesIDCount),
                            jsonPlace.getString("nome"),
                            jsonPlace.getString("desc"),
                            jsonPlace.getDouble("lat"),
                            jsonPlace.getDouble("lon"),
                            jsonPlace.getString("img"),
                            jsonPlace.getString("credits")));
                    daoLinks.insert(new Link(String.valueOf(linksIDCount),
                            String.valueOf(placesIDCount),
                            "Wikipedia",
                            url));
                    try {
                        JSONArray events = jsonPlace.getJSONArray("events");
                        for (int j = 0; j < events.length(); j++){
                            JSONObject jsonEvent = events.getJSONObject(j);
                            daoEvents.insert(new Event(String.valueOf(eventsIDCount),
                                    String.valueOf(placesIDCount),
                                    jsonEvent.getString("date"),
                                    jsonEvent.getString("desc")));
                            eventsIDCount++;
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    placesIDCount++;
                    linksIDCount++;
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        // TODO Auto-generated method stub
        if (isCreating && currentDB != null) {
            return currentDB;
        }
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        // TODO Auto-generated method stub
        if (isCreating && currentDB != null) {
            return currentDB;
        }
        return super.getReadableDatabase();
    }

}