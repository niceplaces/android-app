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
import com.niceplaces.niceplaces.models.Event
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DaoAreas(private val mContext: Context) {

    private val mDbMode: String?

    fun getPlaces(idArea: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/areas/" + idArea
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
                            var hasDescription = jsonObject.getBoolean("has_description_en")
                            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                                hasDescription = jsonObject.getBoolean("has_description")
                            }
                            val place = Place(jsonObject.getString("id"),
                                    name,
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
        queue.add(stringRequest)
    }

    fun getOne(id: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/places/" + id
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        var name = jsonObject.getString("name_en")
                        if (name == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                            name = jsonObject.getString("name")
                        }
                        var description = jsonObject.getString("description_en")
                        if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                            description = jsonObject.getString("description")
                        }
                        var wikiUrl = jsonObject.getString("wiki_url_en")
                        if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                            wikiUrl = jsonObject.getString("wiki_url")
                        }
                        val place = Place(jsonObject.getString("id"),
                                name,
                                description,
                                jsonObject.getString("desc_sources"),
                                jsonObject.getDouble("latitude"),
                                jsonObject.getDouble("longitude"),
                                jsonObject.getString("image"),
                                jsonObject.getString("img_credits"),
                                wikiUrl)
                        val jsonEvents = jsonObject.getJSONArray("events")
                        val events: MutableList<Event> = ArrayList()
                        for (i in 0 until jsonEvents.length()) {
                            val eventObject = jsonEvents.getJSONObject(i)
                            val event = Event(eventObject.getString("date"),
                                    eventObject.getString("description"))
                            events.add(event)
                        }
                        place.events = events
                        successCallback.place = place
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

    init {
        val prefs = PrefsController(mContext)
        mDbMode = prefs.databaseMode
    }
}