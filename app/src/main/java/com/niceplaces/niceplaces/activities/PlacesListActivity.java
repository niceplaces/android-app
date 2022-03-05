package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.ExplorePlacesAdapter;
import com.niceplaces.niceplaces.controllers.AlertController;
import com.niceplaces.niceplaces.dao.DaoAreas;
import com.niceplaces.niceplaces.utils.MyRunnable;

public class PlacesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        final PlacesListActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().hide();
        TextView textViewAreaName = findViewById(R.id.explore_area_name);
        textViewAreaName.setText(extras.getString("AREA_NAME"));
        final ListView listView = findViewById(R.id.listview_areas);
        final AlertController alertController = new AlertController(this, R.id.layout_loading);
        DaoAreas daoAreas = new DaoAreas(this);
        daoAreas.getPlaces(extras.getString(Const.AREA_ID), new MyRunnable() {
            @Override
            public void run() {
                ExplorePlacesAdapter adapter = new ExplorePlacesAdapter(thisActivity, R.id.listview_areas, getPlaces());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(thisActivity, PlaceDetailsActivity.class);
                        intent.putExtra(Const.PLACE_ID, getPlaces().get(i).getID());
                        thisActivity.startActivity(intent);
                    }
                });
                alertController.loadingSuccess();
            }
        }, new Runnable() {
            @Override
            public void run() {
                alertController.loadingError();
            }
        });
    }
}
