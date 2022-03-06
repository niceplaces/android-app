package com.niceplaces.niceplaces.controllers

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.niceplaces.niceplaces.BuildConfig
import java.util.*

class UserListsController(private val mContext: Context) {
    private val mPref: SharedPreferences
    private val FAVOURITES = "favourites"
    private val VISITED = "visited"
    private val WISH_TO_VISIT = "wish_to_visit"
    val favourite: Set<String>
        get() = mPref.getStringSet(FAVOURITES, HashSet()) as Set<String>

    fun isFavourite(id: String): Boolean {
        val favourites = mPref.getStringSet(FAVOURITES, HashSet())
        if (favourites != null) {
            return favourites.contains(id)
        } else {
            return false
        }
    }

    fun addFavourite(id: String) {
        val editor = mPref.edit()
        // L'editor controlla il puntatore del set che, se non è diverso, non effettua modifiche7
        // Necessaria la creazione di un nuovo set
        val oldFavourites = mPref.getStringSet(FAVOURITES, HashSet())
        val newFavourites: MutableSet<String> = HashSet(oldFavourites)
        newFavourites.add(id)
        editor.putStringSet(FAVOURITES, newFavourites)
        editor.apply()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Aggiunto ai preferiti.", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeFavourite(id: String) {
        val editor = mPref.edit()
        val oldFavourites = mPref.getStringSet(FAVOURITES, HashSet())
        val newFavourites: MutableSet<String> = HashSet(oldFavourites)
        newFavourites.remove(id)
        editor.putStringSet(FAVOURITES, newFavourites)
        editor.apply()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Rimosso dai preferiti.", Toast.LENGTH_SHORT).show()
        }
    }

    val visited: Set<String>
        get() = mPref.getStringSet(VISITED, HashSet()) as Set<String>

    fun isVisited(id: String): Boolean {
        val visited = mPref.getStringSet(VISITED, HashSet())
        if (visited != null) {
            return visited.contains(id)
        } else {
            return false
        }
    }

    fun addVisited(id: String) {
        val editor = mPref.edit()
        val oldVisited = mPref.getStringSet(VISITED, HashSet())
        val newVisited: MutableSet<String> = HashSet(oldVisited)
        newVisited.add(id)
        editor.putStringSet(VISITED, newVisited)
        editor.apply()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Aggiunto ai visitati.", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeVisited(id: String) {
        val editor = mPref.edit()
        val oldVisited = mPref.getStringSet(VISITED, HashSet())
        val newVisited: MutableSet<String> = HashSet(oldVisited)
        newVisited.remove(id)
        editor.putStringSet(VISITED, newVisited)
        editor.apply()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Rimosso dai visitati.", Toast.LENGTH_SHORT).show()
        }
    }

    val wished: Set<String>
        get() = mPref.getStringSet(WISH_TO_VISIT, HashSet()) as Set<String>

    fun isWished(id: String): Boolean {
        val wished = mPref.getStringSet(WISH_TO_VISIT, HashSet())
        if (wished != null) {
            return wished.contains(id)
        } else {
            return false
        }
    }

    fun addWished(id: String) {
        val editor = mPref.edit()
        val oldWished = mPref.getStringSet(WISH_TO_VISIT, HashSet())
        val newWished: MutableSet<String> = HashSet(oldWished)
        newWished.add(id)
        editor.putStringSet(WISH_TO_VISIT, newWished)
        editor.apply()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Aggiunto ai desiderati.", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeWished(id: String) {
        val editor = mPref.edit()
        val oldWished = mPref.getStringSet(WISH_TO_VISIT, HashSet())
        val newWished: MutableSet<String> = HashSet(oldWished)
        newWished.remove(id)
        editor.putStringSet(WISH_TO_VISIT, newWished)
        editor.apply()
        if (BuildConfig.DEBUG) {
            Toast.makeText(mContext, "Rimosso dai desiderati.", Toast.LENGTH_SHORT).show()
        }
    }

    init {
        mPref = mContext.getSharedPreferences("prefs", 0)
    }
}