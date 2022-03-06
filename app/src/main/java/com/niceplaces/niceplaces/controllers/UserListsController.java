package com.niceplaces.niceplaces.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.models.GeoPoint;
import com.niceplaces.niceplaces.utils.AppUtils;

import java.util.HashSet;
import java.util.Set;

public class UserListsController {

    private SharedPreferences mPref;
    private Context mContext;

    private String FAVOURITES = "favourites";
    private String VISITED = "visited";
    private String WISH_TO_VISIT = "wish_to_visit";

    public UserListsController(Context context){
        mContext = context;
        mPref = context.getSharedPreferences("prefs", 0);
    }

    public Set<String> getFavourite(){
        return mPref.getStringSet(FAVOURITES, new HashSet<String>());
    }

    public boolean isFavourite(String id){
        Set<String> favourites = mPref.getStringSet(FAVOURITES, new HashSet<String>());
        return favourites.contains(id);
    }

    public void addFavourite(String id){
        SharedPreferences.Editor editor = mPref.edit();
        // L'editor controlla il puntatore del set che, se non è diverso, non effettua modifiche7
        // Necessaria la creazione di un nuovo set
        Set<String> oldFavourites = mPref.getStringSet(FAVOURITES, new HashSet<String>());
        Set<String> newFavourites = new HashSet<>(oldFavourites);
        newFavourites.add(id);
        editor.putStringSet(FAVOURITES, newFavourites);
        editor.apply();
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "Aggiunto ai preferiti.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeFavourite(String id){
        SharedPreferences.Editor editor = mPref.edit();
        Set<String> oldFavourites = mPref.getStringSet(FAVOURITES, new HashSet<String>());
        Set<String> newFavourites = new HashSet<>(oldFavourites);
        newFavourites.remove(id);
        editor.putStringSet(FAVOURITES, newFavourites);
        editor.apply();
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "Rimosso dai preferiti.", Toast.LENGTH_SHORT).show();
        }
    }

    public Set<String> getVisited(){
        return mPref.getStringSet(VISITED, new HashSet<String>());
    }

    public boolean isVisited(String id){
        Set<String> visited = mPref.getStringSet(VISITED, new HashSet<String>());
        return visited.contains(id);
    }

    public void addVisited(String id){
        SharedPreferences.Editor editor = mPref.edit();
        Set<String> oldVisited = mPref.getStringSet(VISITED, new HashSet<String>());
        Set<String> newVisited = new HashSet<>(oldVisited);
        newVisited.add(id);
        editor.putStringSet(VISITED, newVisited);
        editor.apply();
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "Aggiunto ai visitati.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeVisited(String id){
        SharedPreferences.Editor editor = mPref.edit();
        Set<String> oldVisited =  mPref.getStringSet(VISITED, new HashSet<String>());
        Set<String> newVisited = new HashSet<>(oldVisited);
        newVisited.remove(id);
        editor.putStringSet(VISITED, newVisited);
        editor.apply();
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "Rimosso dai visitati.", Toast.LENGTH_SHORT).show();
        }
    }

    public Set<String> getWished(){
        return mPref.getStringSet(WISH_TO_VISIT, new HashSet<String>());
    }

    public boolean isWished(String id){
        Set<String> wished = mPref.getStringSet(WISH_TO_VISIT, new HashSet<String>());
        return wished.contains(id);
    }

    public void addWished(String id){
        SharedPreferences.Editor editor = mPref.edit();
        Set<String> oldWished = mPref.getStringSet(WISH_TO_VISIT, new HashSet<String>());
        Set<String> newWished = new HashSet<>(oldWished);
        newWished.add(id);
        editor.putStringSet(WISH_TO_VISIT, newWished);
        editor.apply();
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "Aggiunto ai desiderati.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeWished(String id){
        SharedPreferences.Editor editor = mPref.edit();
        Set<String> oldWished = mPref.getStringSet(WISH_TO_VISIT, new HashSet<String>());
        Set<String> newWished = new HashSet<>(oldWished);
        newWished.remove(id);
        editor.putStringSet(WISH_TO_VISIT, newWished);
        editor.apply();
        if (BuildConfig.DEBUG){
            Toast.makeText(mContext, "Rimosso dai desiderati.", Toast.LENGTH_SHORT).show();
        }
    }


}
