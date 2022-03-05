package com.niceplaces.niceplaces.dao;

import android.content.Context;
import android.util.Log;
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
import com.niceplaces.niceplaces.models.Region;
import com.niceplaces.niceplaces.utils.MyRunnable;
import com.niceplaces.niceplaces.utils.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DaoRegions {

    private Context mContext;
    private String mDbMode;

    public DaoRegions(Context context) {
        mContext = context;
        PrefsController prefs = new PrefsController(context);
        mDbMode = prefs.getDatabaseMode();
    }

    public void getRegions(final MyRunnable successCallback, final Runnable errorCallback) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = Const.DATA_PATH + mDbMode + "/regions";
        Log.i("URL", url);
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "HTTP request " + url, Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("REGIONS", response);
                            JSONArray jsonArray = new JSONArray(response);
                            List<Region> buffer = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name_en");
                                if (name.equals("") || Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    name = jsonObject.getString("name");
                                }
                                Region region = new Region(jsonObject.getString("id"),
                                        name, jsonObject.getString("count"));
                                buffer.add(region);
                            }
                            successCallback.setRegions(buffer);
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

    public void getAreas(String regionID, final MyRunnable successCallback, final Runnable errorCallback) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = Const.DATA_PATH + mDbMode + "/regions/" + regionID;
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "HTTP request " + url, Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<Area> buffer = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name_en");
                                if (name.equals("") || Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())){
                                    name = jsonObject.getString("name");
                                }
                                Area area = new Area(jsonObject.getString("id"),
                                        name, jsonObject.getString("count"));
                                buffer.add(area);
                            }
                            successCallback.setAreas(buffer);
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
