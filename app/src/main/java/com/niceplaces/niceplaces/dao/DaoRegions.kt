package com.niceplaces.niceplaces.dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.models.Area
import com.niceplaces.niceplaces.models.Region
import com.niceplaces.niceplaces.utils.AppUtils
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class DaoRegions(private val mContext: Context) {
    private val mDbMode: String?
    fun getRegions(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/regions"
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    Log.i(AppUtils.tag, "HTTP response array length: " + jsonArray.length())
                    val buffer: MutableList<Region> = ArrayList()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        var name = jsonObject.getString("name_en")
                        if (name == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                            name = jsonObject.getString("name")
                        }
                        val region = Region(jsonObject.getString("id"),
                                name, jsonObject.getString("count"))
                        buffer.add(region)
                    }
                    successCallback.regions = buffer
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

    fun getAreas(regionID: String?, successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        val url = Const.DATA_PATH + mDbMode + "/regions/" + regionID
        Log.i(AppUtils.tag, "HTTP request $url")
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val buffer: MutableList<Area> = ArrayList()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        var name = jsonObject.getString("name_en")
                        if (name == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                            name = jsonObject.getString("name")
                        }
                        val area = Area(jsonObject.getString("id"),
                                name, jsonObject.getString("count"))
                        buffer.add(area)
                    }
                    successCallback.setAreas(buffer)
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

    init {
        val prefs = PrefsController(mContext)
        mDbMode = prefs.databaseMode
    }
}