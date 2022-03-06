package com.niceplaces.niceplaces.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object StringUtils {
    fun listToString(list: List<Int?>): String {
        var string = ""
        for (i in list.indices) {
            string = string + Integer.toString(list[i]!!)
        }
        return string
    }

    fun printList(list: List<String>) {
        for (i in list.indices) {
            Log.i("STRING_ARRAY", "[" + i + "] " + list[i])
        }
    }

    fun timestampToDate(l: Long?): String? {
        return if (l != null){
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
                val netDate = Date(l)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        } else {
            l.toString()
        }
    }
}