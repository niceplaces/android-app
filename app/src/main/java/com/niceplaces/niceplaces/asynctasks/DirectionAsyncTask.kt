package com.niceplaces.niceplaces.asynctasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.niceplaces.niceplaces.models.Direction

/**
 * Created by Lorenzo on 02/01/2018.
 */
class DirectionAsyncTask(private val mContext: Context, private val mTextView: TextView) : AsyncTask<Direction?, Void?, Int?>() {
    protected override fun doInBackground(vararg directions: Direction?): Int? {
        Log.i("TASK STARTED!", "")
        val startLat = directions[0]?.mStartLatitude.toString()
        val startLon = directions[0]?.mStartLongitude.toString()
        val endLat = directions[0]?.mEndLatitude.toString()
        val endLon = directions[0]?.mEndLongitude.toString()
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + "," + startLon +
                "&destination=" + endLat + "," + endLon
        val jsObjRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response -> mTextView.text = "Response: $response" }, Response.ErrorListener { error -> // TODO Auto-generated method stub
            Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
        })
        return null
    }

}