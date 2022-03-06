package com.niceplaces.niceplaces.utils

import android.content.Context
import android.content.SharedPreferences

// From https://stackoverflow.com/questions/14514579/how-to-implement-rate-it-feature-in-android-app

object AppRater {

    const val APP_PNAME = "com.niceplaces.niceplaces" // Package Name
    private const val DAYS_UNTIL_PROMPT = 7 //Min number of days
    private const val LAUNCHES_UNTIL_PROMPT = 8 //Min number of launches


    fun needToTriggerRateDialog(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences("apprater", 0)
        if (prefs.getBoolean("dontshowagain", false)) {
            return false
        }
        val editor = prefs.edit()

        // Increment launch counter
        val launchCount = prefs.getLong("launch_count", 0) + 1
        editor.putLong("launch_count", launchCount)

        // Get date of first launch
        var dateFirstLaunch = prefs.getLong("date_firstlaunch", 0)
        if (dateFirstLaunch == 0L) {
            dateFirstLaunch = System.currentTimeMillis()
            editor.putLong("date_firstlaunch", dateFirstLaunch)
        }
        editor.apply()

        // Wait at least n days before opening
        return launchCount >= LAUNCHES_UNTIL_PROMPT &&
                System.currentTimeMillis() >= dateFirstLaunch + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000
    }

}