package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.LatestPlacesAdapter;
import com.niceplaces.niceplaces.controllers.AlertController;
import com.niceplaces.niceplaces.controllers.UserListsController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.MyRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class UserListActivity extends AppCompatActivity {

    public static final String USERLIST = "userlist";
    public static final String VISITED = "visited";
    public static final String WISHED = "wished";
    public static final String FAVOURITE = "favourite";

    private final UserListActivity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        FirebaseAnalytics.getInstance(this);
        getSupportActionBar().hide();
        TextView textViewAreaName = findViewById(R.id.explore_area_name);
        switch (getIntent().getExtras().getString(USERLIST)){
            case VISITED:
                textViewAreaName.setText(getResources().getString(R.string.visited_places).replace("\n", " "));
                break;
            case WISHED:
                textViewAreaName.setText(getResources().getString(R.string.places_to_visit).replace("\n", " "));
                break;
            case FAVOURITE:
                textViewAreaName.setText(getResources().getString(R.string.favorite_places).replace("\n", " "));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserListsController prefs = new UserListsController(this);
        Set<String> placesIDs = null;
        final List<Place> places = new ArrayList<>();
        switch (getIntent().getExtras().getString(USERLIST)){
            case VISITED:
                placesIDs = prefs.getVisited();
                break;
            case WISHED:
                placesIDs = prefs.getWished();
                break;
            case FAVOURITE:
                placesIDs = prefs.getFavourite();
                break;
        }
        final ListView listView = findViewById(R.id.listview_areas);
        final AlertController alertController = new AlertController(this, R.id.layout_loading);
        DaoPlaces daoPlaces = new DaoPlaces(this);
        final int numPlaces = placesIDs.size();
        if (placesIDs.isEmpty()){
            alertController.loadingNoData();
        } else {
            for (String placeID : placesIDs) {
                daoPlaces.getOne(placeID, new MyRunnable() {
                    @Override
                    public void run() {
                        places.add(getPlace());
                        if (places.size() == numPlaces) {
                            Collections.sort(places, new Comparator<Place>() {
                                @Override
                                public int compare(Place o1, Place o2) {
                                    return o1.mName.compareTo(o2.mName);
                                }
                            });
                            LatestPlacesAdapter adapter = new LatestPlacesAdapter(mActivity, R.id.listview_latest_places, places);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(mActivity, PlaceDetailsActivity.class);
                                    intent.putExtra(Const.PLACE_ID, places.get(i).getID());
                                    mActivity.startActivity(intent);
                                }
                            });
                            alertController.loadingSuccess();
                        }
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        alertController.loadingError();
                    }
                });
            }
        }
    }
}
