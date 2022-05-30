package com.niceplaces.niceplaces.dao

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.niceplaces.niceplaces.BuildConfig
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.models.Quiz
import com.niceplaces.niceplaces.utils.MyRunnable
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class DaoQuiz(private val mContext: Context) {

    private val mDbMode: String?

    init {
        val prefs = PrefsController(mContext)
        mDbMode = prefs.databaseMode
    }

    fun getAll(successCallback: MyRunnable, errorCallback: Runnable) {
        val queue = Volley.newRequestQueue(mContext)
        // TODO restore previous definition of url
        //val url = Const.DATA_PATH + mDbMode + "/quiz/"
        val url = Const.BASE_URL + "data/v4/" + mDbMode + "/quiz/"
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "HTTP request $url", Toast.LENGTH_SHORT).show()
        }
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val buffer: MutableList<Quiz> = ArrayList()
                    for (i in 0 until 10 /*jsonArray.length()*/) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        var name = jsonObject.getString("name_en")
                        if (name == "" || Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                            name = jsonObject.getString("name")
                        }
                        val quiz = Quiz(name,
                            jsonObject.getString("area"),
                            jsonObject.getString("question"),
                            jsonObject.getString("correct_answer"),
                            jsonObject.getString("wrong_answer_1"),
                            jsonObject.getString("wrong_answer_2"))
                        buffer.add(quiz)
                    }
                    successCallback.quiz = buffer
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
}