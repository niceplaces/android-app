package com.niceplaces.niceplaces.utils;

import android.content.Context;

import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.models.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("places.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Place placeFromJSON(String json, boolean isItalian) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.getString("name_en");
        if (name.equals("") || isItalian){
            name = jsonObject.getString("name");
        }
        String area = jsonObject.getString("area_en");
        if (area.equals("") || isItalian){
            area = jsonObject.getString("area");
        }
        String region = jsonObject.getString("region_en");
        if (region.equals("") || isItalian){
            region = jsonObject.getString("region");
        }
        String description = jsonObject.getString("description_en");
        if (isItalian){
            description = jsonObject.getString("description");
        }
        String wikiUrl = jsonObject.getString("wiki_url_en");
        if (isItalian){
            wikiUrl = jsonObject.getString("wiki_url");
        }
        Place place = new Place(jsonObject.getString("id"),
                name,
                area,
                region,
                description,
                jsonObject.getString("author"),
                jsonObject.getString("desc_sources"),
                jsonObject.getDouble("latitude"),
                jsonObject.getDouble("longitude"),
                jsonObject.getString("image"),
                jsonObject.getString("img_credits"),
                wikiUrl,
                jsonObject.getString("facebook"),
                jsonObject.getString("instagram"));
        JSONArray jsonEvents = jsonObject.getJSONArray("events");
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < jsonEvents.length(); i++){
            JSONObject eventObject = jsonEvents.getJSONObject(i);
            Event event = new Event(eventObject.getString("date"),
                    eventObject.getString("description"));
            events.add(event);
        }
        place.setEvents(events);
        return place;
    }


}
