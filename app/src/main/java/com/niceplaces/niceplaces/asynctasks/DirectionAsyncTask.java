package com.niceplaces.niceplaces.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.niceplaces.niceplaces.models.Direction;

import org.json.JSONObject;

/**
 * Created by Lorenzo on 02/01/2018.
 */

public class DirectionAsyncTask extends AsyncTask<Direction, Void, Integer> {

    private TextView mTextView;
    private Context mContext;

    public DirectionAsyncTask(Context context, TextView textView){
        mContext = context;
        mTextView = textView;
    }

    @Override
    protected Integer doInBackground(Direction... directions) {
        Log.i("TASK STARTED!", "");
        String startLat = String.valueOf(directions[0].mStartLatitude);
        String startLon = String.valueOf(directions[0].mStartLongitude);
        String endLat = String.valueOf(directions[0].mEndLatitude);
        String endLon = String.valueOf(directions[0].mEndLongitude);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + "," + startLon +
                "&destination=" + endLat + "," + endLon;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTextView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        return null;
    }
}
