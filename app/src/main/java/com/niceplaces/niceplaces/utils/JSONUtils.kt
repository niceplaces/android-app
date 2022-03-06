package com.niceplaces.niceplaces.utils

import android.content.Context
import com.niceplaces.niceplaces.models.Event
import com.niceplaces.niceplaces.models.Place
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

object JSONUtils {
    fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        json = try {
            val `is` = context.assets.open("places.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    @Throws(JSONException::class)
    fun placeFromJSON(json: String?, isItalian: Boolean): Place {
        val jsonObject = JSONObject(json)
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
        var description = jsonObject.getString("description_en")
        if (isItalian) {
            description = jsonObject.getString("description")
        }
        var wikiUrl = jsonObject.getString("wiki_url_en")
        if (isItalian) {
            wikiUrl = jsonObject.getString("wiki_url")
        }
        val place = Place(jsonObject.getString("id"),
                name,
                area,
                region,
                description,
                jsonObject.getString("author"),
                jsonObject.getString("desc_sources"),
                jsonObject.getDouble("latitude"),
                jsonObject.getDouble("longitude"),
                jsonObject.getString("image"),
                jsonObject.getString("img_credits"),
                wikiUrl,
                jsonObject.getString("facebook"),
                jsonObject.getString("instagram"))
        val jsonEvents = jsonObject.getJSONArray("events")
        val events: MutableList<Event> = ArrayList()
        for (i in 0 until jsonEvents.length()) {
            val eventObject = jsonEvents.getJSONObject(i)
            val event = Event(eventObject.getString("date"),
                    eventObject.getString("description"))
            events.add(event)
        }
        place.events = events
        return place
    }
}