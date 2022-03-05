package com.niceplaces.niceplaces.dao;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.PrefsController;
import com.niceplaces.niceplaces.models.Area;
import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.MyRunnable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DaoAreas {

    private Context mContext;
    private String mDbMode;

    public DaoAreas(Context context) {
        mContext = context;
        PrefsController prefs = new PrefsController(context);
        mDbMode = prefs.getDatabaseMode();
    }

    public void getPlaces(String idArea, final MyRunnable successCallback, final Runnable errorCallback) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = Const.DATA_PATH + mDbMode + "/areas/" + idArea;
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "HTTP request " + url, Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<Place> buffer = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name_en");
                                if (name.equals("") || Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    name = jsonObject.getString("name");
                                }
                                Boolean hasDescription = jsonObject.getBoolean("has_description_en");
                                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    hasDescription = jsonObject.getBoolean("has_description");
                                }
                                Place place = new Place(jsonObject.getString("id"),
                                        name,
                                        jsonObject.getString("image"),
                                        hasDescription);
                                buffer.add(place);
                            }
                            successCallback.setPlaces(buffer);
                            successCallback.run();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCallback.run();
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    public void getOne(String id, final MyRunnable successCallback, final Runnable errorCallback) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = Const.DATA_PATH + mDbMode + "/places/" + id;
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "HTTP request " + url, Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String name = jsonObject.getString("name_en");
                            if (name.equals("") || Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                name = jsonObject.getString("name");
                            }
                            String description = jsonObject.getString("description_en");
                            if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                description = jsonObject.getString("description");
                            }
                            String wikiUrl = jsonObject.getString("wiki_url_en");
                            if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                wikiUrl = jsonObject.getString("wiki_url");
                            }
                            Place place = new Place(jsonObject.getString("id"),
                                    name,
                                    description,
                                    jsonObject.getString("desc_sources"),
                                    jsonObject.getDouble("latitude"),
                                    jsonObject.getDouble("longitude"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("img_credits"),
                                    wikiUrl);
                            JSONArray jsonEvents = jsonObject.getJSONArray("events");
                            List<Event> events = new ArrayList<>();
                            for (int i = 0; i < jsonEvents.length(); i++){
                                JSONObject eventObject = jsonEvents.getJSONObject(i);
                                Event event = new Event(eventObject.getString("date"),
                                        eventObject.getString("description"));
                                events.add(event);
                            }
                            place.setEvents(events);
                            successCallback.setPlace(place);
                            successCallback.run();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCallback.run();
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

}
