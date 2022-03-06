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
import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.models.PlacesList;
import com.niceplaces.niceplaces.utils.JSONUtils;
import com.niceplaces.niceplaces.utils.MyRunnable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DaoLists {

    private Context mContext;
    private String mDbMode;
    private boolean isItalian;

    public DaoLists(Context context) {
        mContext = context;
        PrefsController prefs = new PrefsController(context);
        mDbMode = prefs.getDatabaseMode();
        isItalian = Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage());
    }

    public void getAll(final MyRunnable successCallback, final Runnable errorCallback) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = Const.DATA_PATH + mDbMode + "/lists";
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request " + url, Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<PlacesList> buffer = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name_en");
                                if (name.equals("") || isItalian) {
                                    name = jsonObject.getString("name");
                                }
                                String description = jsonObject.getString("description_en");
                                if (description.equals("") || isItalian) {
                                    description = jsonObject.getString("description");
                                }
                                PlacesList list = new PlacesList(jsonObject.getString("id"),
                                        name,
                                        description,
                                        jsonObject.getString("count"));
                                buffer.add(list);
                            }
                            successCallback.setLists(buffer);
                            successCallback.run();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorCallback.run();
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

    public void getPlacesByListId(String id, final MyRunnable successCallback, final Runnable errorCallback) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = Const.DATA_PATH + mDbMode + "/lists/" + id;
        if (BuildConfig.DEBUG) {
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
                                String area = jsonObject.getString("area_en");
                                if (area.equals("") || Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    area = jsonObject.getString("area");
                                }
                                String region = jsonObject.getString("region_en");
                                if (region.equals("") || Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    region = jsonObject.getString("region");
                                }
                                boolean hasDescription = jsonObject.getBoolean("has_description_en");
                                if (Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    hasDescription = jsonObject.getBoolean("has_description");
                                }
                                Place place = new Place(jsonObject.getString("id"),
                                        name,
                                        area,
                                        region,
                                        jsonObject.getString("image"),
                                        hasDescription,
                                        jsonObject.getString("author"));
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
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

}
