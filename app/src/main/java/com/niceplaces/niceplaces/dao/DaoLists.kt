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
import com.niceplaces.niceplaces.models.PlacesList
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class DaoLists(private val mContext: Context) {
    private val mDbMode: String?
    private val isItalian: Boolean
    fun getAll(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/lists"
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonArray = JSONArray(response)
                        val buffer: MutableList<PlacesList> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            var name = jsonObject.getString("name_en")
                            if (name == "" || isItalian) {
                                name = jsonObject.getString("name")
                            }
                            var description = jsonObject.getString("description_en")
                            if (description == "" || isItalian) {
                                description = jsonObject.getString("description")
                            }
                            val list = PlacesList(jsonObject.getString("id"),
                                    name,
                                    description,
                                    jsonObject.getString("count"))
                            buffer.add(list)
                        }
                        successCallback.lists = buffer
                        successCallback.run()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        errorCallback.run()
                    }
                }, Response.ErrorListener {
            errorCallback.run()
            Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
        })
        queue.add(stringRequest)
    }

    fun getPlacesByListId(id: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/lists/" + id
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
                            if (name == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                                name = jsonObject.getString("name")
                            }
                            var area = jsonObject.getString("area_en")
                            if (area == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                                area = jsonObject.getString("area")
                            }
                            var region = jsonObject.getString("region_en")
                            if (region == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                                region = jsonObject.getString("region")
                            }
                            var hasDescription = jsonObject.getBoolean("has_description_en")
                            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
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