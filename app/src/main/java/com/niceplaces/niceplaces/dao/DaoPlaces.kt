package com.niceplaces.niceplaces.dao

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.niceplaces.niceplaces.BuildConfig
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.JSONUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class DaoPlaces(private val mContext: Context) {

    private val mDbMode: String?
    private val isItalian: Boolean

    fun getOne(id: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/places/" + id
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        successCallback.place = JSONUtils.placeFromJSON(response, isItalian)
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
            errorCallback.run()
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
        })
        queue.add(stringRequest)
    }

    fun getNearest(latitude: Double, longitude: Double, successCallback: MyRunnable,
                   errorCallback: MyRunnable) {
        val prefs = PrefsController(mContext)
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.BASE_URL + "data/query.php?version=" + Const.DB_VERSION + "&mode=" + mDbMode +
                "&p1=getnearestplaces&p2=" + latitude.toString() +
                "&p3=" + longitude.toString() + "&p4=" + prefs.distanceRadius.toString()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonArray = JSONArray(response)
                        val buffer: MutableList<Place> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            var name = jsonObject.getString("name_en")
                            if (name == "" || isItalian) {
                                name = jsonObject.getString("name")
                            }
                            var hasDescription = jsonObject.getBoolean("has_description_en")
                            if (isItalian) {
                                hasDescription = jsonObject.getBoolean("has_description")
                            }
                            val place = Place(jsonObject.getString("id"),
                                    name,
                                    jsonObject.getDouble("latitude"),
                                    jsonObject.getDouble("longitude"),
                                    jsonObject.getString("image"),
                                    hasDescription,
                                    jsonObject.getString("author"))
                            buffer.add(place)
                        }
                        successCallback.places = buffer
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        errorCallback.run()
                    }
                }, Response.ErrorListener {
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
            errorCallback.run()
        })
        queue.add(stringRequest)
    }

    fun getPlaceOfTheDay(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        var url = Const.DATA_PATH + mDbMode + "/placeoftheday"
        if (Locale.getDefault().displayLanguage != Locale.ITALIAN.displayLanguage) {
            url = "$url-en"
        }
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        successCallback.place = JSONUtils.placeFromJSON(response, isItalian)
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
            errorCallback.run()
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
        })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun getLatestInserted(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/latestinserted"
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonArray = JSONArray(response)
                        val buffer: MutableList<Place> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            var name = jsonObject.getString("name_en")
                            if (name == "" || isItalian) {
                                name = jsonObject.getString("name")
                            }
                            var area = jsonObject.getString("area_en")
                            if (area == "" || isItalian) {
                                area = jsonObject.getString("area")
                            }
                            var region = jsonObject.getString("region_en")
                            if (region == "" || isItalian) {
                                region = jsonObject.getString("region")
                            }
                            var hasDescription = jsonObject.getBoolean("has_description_en")
                            if (isItalian) {
                                hasDescription = jsonObject.getBoolean("has_description")
                            }
                            val place = Place(jsonObject.getString("id"),
                                    name,
                                    area,
                                    region,
                                    jsonObject.getString("image"),
                                    hasDescription,
                                    jsonObject.getString("author"))
                            buffer.add(place)
                        }
                        successCallback.places = buffer
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
            errorCallback.run()
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
        })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun getLatestUpdated(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/latestupdated"
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonArray = JSONArray(response)
                        val buffer: MutableList<Place> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            var name = jsonObject.getString("name_en")
                            if (name == "" || isItalian) {
                                name = jsonObject.getString("name")
                            }
                            var area = jsonObject.getString("area_en")
                            if (area == "" || isItalian) {
                                area = jsonObject.getString("area")
                            }
                            var region = jsonObject.getString("region_en")
                            if (region == "" || isItalian) {
                                region = jsonObject.getString("region")
                            }
                            var hasDescription = jsonObject.getBoolean("has_description_en")
                            if (isItalian) {
                                hasDescription = jsonObject.getBoolean("has_description")
                            }
                            val place = Place(jsonObject.getString("id"),
                                    name,
                                    area,
                                    region,
                                    jsonObject.getString("image"),
                                    hasDescription,
                                    jsonObject.getString("author"))
                            buffer.add(place)
                        }
                        successCallback.places = buffer
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
            errorCallback.run()
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
        })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun getSearchResults(keywords: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/search/" + keywords
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonArray = JSONArray(response)
                        val buffer: MutableList<Place> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            var name = jsonObject.getString("name_en")
                            if (name == "" || isItalian) {
                                name = jsonObject.getString("name")
                            }
                            var area = jsonObject.getString("area_en")
                            if (area == "" || isItalian) {
                                area = jsonObject.getString("area")
                            }
                            var region = jsonObject.getString("region_en")
                            if (region == "" || isItalian) {
                                region = jsonObject.getString("region")
                            }
                            var hasDescription = jsonObject.getBoolean("has_description_en")
                            if (isItalian) {
                                hasDescription = jsonObject.getBoolean("has_description")
                            }
                            val place = Place(jsonObject.getString("id"),
                                    name,
                                    area,
                                    region,
                                    jsonObject.getString("image"),
                                    hasDescription,
                                    jsonObject.getString("author"))
                            buffer.add(place)
                        }
                        successCallback.places = buffer
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
            errorCallback.run()
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
        })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    init {
        val prefs = PrefsController(mContext)
        mDbMode = prefs.databaseMode
        isItalian = Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage
    }
}