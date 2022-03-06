package com.niceplaces.niceplaces.controllers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.niceplaces.niceplaces.BuildConfig
import com.niceplaces.niceplaces.models.GeoPoint
import com.niceplaces.niceplaces.utils.AppUtils

class PrefsController(private val mContext: Context) {
    private val mPref: SharedPreferences = mContext.getSharedPreferences("prefs", 0)

    var databaseMode: String?
        get() = if (BuildConfig.DEBUG) {
            mPref.getString("database_mode", "debug")
        } else if (BuildConfig.BUILD_TYPE == "demo") {
            "debug"
        } else {
            "release"
        }
        set(mode) {
            val editor = mPref.edit()
            editor.putString("database_mode", mode)
            editor.apply()
        }

    var isPrivacyAccepted: Boolean
        get() = mPref.getBoolean("privacy", false)
        set(accepted) {
            val editor = mPref.edit()
            editor.putBoolean("privacy", accepted)
            editor.apply()
        }

    var storedLocation: GeoPoint
        get() {
            val sienaLat = 43.318498
            val sienaLon = 11.331613
            return GeoPoint(mPref.getFloat("stored_location_lat", sienaLat.toFloat()).toDouble(),
                    mPref.getFloat("stored_location_lon", sienaLon.toFloat()).toDouble())
        }
        set(location) {
            val editor = mPref.edit()
            editor.putFloat("stored_location_lat", location.latitude.toFloat())
            editor.putFloat("stored_location_lon", location.longitude.toFloat())
            editor.apply()
        }

    var zoom: Float
        get() {
            Log.i(AppUtils.tag, "ZOOM: " + mPref.getFloat("map_zoom", 14f))
            return mPref.getFloat("map_zoom", 10f)
        }
        set(zoom) {
            Log.i(AppUtils.tag, "ZOOM: $zoom")
            val editor = mPref.edit()
            editor.putFloat("map_zoom", zoom)
            editor.apply()
        }

    var distanceRadius: Float
        get() = mPref.getFloat("distance_radius", 100f)
        set(radius) {
            val editor = mPref.edit()
            editor.putFloat("distance_radius", radius)
            editor.apply()
        }

    var locationRefreshTime: Int
        get() = mPref.getInt("location_refresh_time", 10)
        set(time) {
            val editor = mPref.edit()
            editor.putInt("location_refresh_time", time)
            editor.apply()
        }

    var locationRefreshDistance: Int
        get() = mPref.getInt("location_refresh_distance", 50)
        set(distance) {
            val editor = mPref.edit()
            editor.putInt("location_refresh_distance", distance)
            editor.apply()
        }

    fun setHidePlacesWithoutDescription(hide: Boolean) {
        val editor = mPref.edit()
        editor.putBoolean("hide_places_without_description", hide)
        editor.apply()
    }

    fun hidePlacesWithoutDescription(): Boolean {
        return mPref.getBoolean("hide_places_without_description", false)
    }

    fun firstOpenDone() {
        val editor = mPref.edit()
        editor.putInt("version_code", AppUtils.getVersionCode(mContext))
        editor.apply()
    }

    // FOR DEBUG
    fun simulateOpenAfterInstall() {
        val editor = mPref.edit()
        editor.putInt("version_code", 0)
        editor.apply()
    }

    fun simulateOpenAfterUpdate() {
        val editor = mPref.edit()
        editor.putInt("version_code", AppUtils.getVersionCode(mContext) - 1)
        editor.apply()
    }

    val isFistOpenAfterInstall: Boolean
        get() {
            Log.i("FIRSTOPEN_AFTER_INSTALL", (mPref.getInt("version_code", 0) == 0).toString())
            Log.i("FIRSTOPEN_VERSION_CODE", mPref.getInt("version_code", 0).toString())
            return mPref.getInt("version_code", 0) == 0
        }

    val isFistOpenAfterUpdate: Boolean
        get() {
            Log.i("FIRSTOPEN_AFTER_UPDATE", (mPref.getInt("version_code", 0) != AppUtils.getVersionCode(mContext)).toString())
            Log.i("FIRSTOPEN_VERSION_CODE", mPref.getInt("version_code", 0).toString())
            return mPref.getInt("version_code", 0) != AppUtils.getVersionCode(mContext)
        }

}