package com.niceplaces.niceplaces.dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.text.htmlEncode
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.AppUtils
import com.niceplaces.niceplaces.utils.JSONUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DaoPlaces(private val mContext: Context) {

    private val mDbMode: String?
    private val isItalian: Boolean

    fun getOne(id: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/places/" + id
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    successCallback.place = JSONUtils.placeFromJSON(response, isItalian)
                    successCallback.run()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
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
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
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
                        val wikiUrl = jsonObject.getString(if (isItalian) "wiki_url" else "wiki_url_en")
                        val place = Place(jsonObject.getString("id"),
                                name,
                                jsonObject.getDouble("latitude"),
                                jsonObject.getDouble("longitude"),
                                jsonObject.getString("image"),
                                hasDescription,
                                jsonObject.getString("author"),
                                wikiUrl)
                        buffer.add(place)
                    }
                    successCallback.places = buffer
                    successCallback.run()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    errorCallback.run()
                }
            }, {
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
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    successCallback.place = JSONUtils.placeFromJSON(response, isItalian)
                    successCallback.run()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
        errorCallback.run()
        Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
    })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun getLatestInserted(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/latestinserted"
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
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
                        val wikiUrl = jsonObject.getString(if (isItalian) "wiki_url" else "wiki_url_en")
                        val place = Place(jsonObject.getString("id"),
                                name,
                                area,
                                region,
                                jsonObject.getString("image"),
                                hasDescription,
                                jsonObject.getString("author"),
                                wikiUrl)
                        buffer.add(place)
                    }
                    successCallback.places = buffer
                    successCallback.run()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
        errorCallback.run()
        Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
    })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun getLatestUpdated(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/latestupdated"
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
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
                        val wikiUrl = jsonObject.getString(if (isItalian) "wiki_url" else "wiki_url_en")
                        val place = Place(jsonObject.getString("id"),
                                name,
                                area,
                                region,
                                jsonObject.getString("image"),
                                hasDescription,
                                jsonObject.getString("author"),
                                wikiUrl)
                        buffer.add(place)
                    }
                    successCallback.places = buffer
                    successCallback.run()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
        errorCallback.run()
        Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
    })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun getSearchResults(keywords: String, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/search/" + keywords
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    Log.i(AppUtils.tag, "HTTP response array length: " + jsonArray.length())
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
                                jsonObject.getString("author"),
                                jsonObject.getString("wiki_url"))
                        buffer.add(place)
                    }
                    successCallback.places = buffer
                    successCallback.run()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
        errorCallback.run()
        Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show()
    })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    companion object {
        fun getWikipediaData(context: Context, pageName: String, includeImageData: Boolean,
                             successCallback: MyRunnable, errorCallback: Runnable) {
            val queue = Volley.newRequestQueue(context)
            val isItalian = Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage
            val lang = if (isItalian) "it" else "en"
            val url = "https://$lang.wikipedia.org/api/rest_v1/page/summary/$pageName"
            Log.i(AppUtils.tag, "HTTP request $url")
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    try {
                        successCallback.wikipediaData = response
                        if (includeImageData) {
                            var imageName = JSONObject(response).getJSONObject("originalimage")
                                .getString("source")
                            imageName =
                                imageName.substring(imageName.lastIndexOf('/') + 1).htmlEncode()
                            val url2 =
                                "https://$lang.wikipedia.org/w/api.php?action=query&prop=imageinfo&iiprop=extmetadata&titles=File%3a$imageName&format=json"
                            Log.i(AppUtils.tag, "HTTP request $url2")
                            val stringRequest = StringRequest(
                                Request.Method.GET, url2,
                                { response2 ->
                                    try {
                                        successCallback.wikipediaImageData = response2
                                        successCallback.run()
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }, {
                                    errorCallback.run()
                                    Toast.makeText(
                                        context,
                                        R.string.connection_error,
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                            queue.add(stringRequest)
                        } else {
                            successCallback.run()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, {
                    errorCallback.run()
                    Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
                })
            queue.add(stringRequest)
        }
    }

    init {
        val prefs = PrefsController(mContext)
        mDbMode = prefs.databaseMode
        isItalian = Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage
    }
}