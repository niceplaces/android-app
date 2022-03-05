package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.LatestPlacesAdapter;
import com.niceplaces.niceplaces.controllers.AlertController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.utils.MyRunnable;

public class LatestPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_places);
        final LatestPlacesActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        getSupportActionBar().hide();
        final ListView listView = findViewById(R.id.listview_latest_places);
        final DaoPlaces daoPlaces = new DaoPlaces(this);
        Spinner spinner = findViewById(R.id.spinner_latest_places);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final AlertController alertController = new AlertController(thisActivity, R.id.layout_loading);
                if (position == 0){
                    daoPlaces.getLatestInserted(new MyRunnable() {
                        @Override
                        public void run() {
                            LatestPlacesAdapter adapter = new LatestPlacesAdapter(thisActivity, R.id.listview_latest_places, getPlaces());
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
                } else {
                    daoPlaces.getLatestUpdated(new MyRunnable() {
                        @Override
                        public void run() {
                            LatestPlacesAdapter adapter = new LatestPlacesAdapter(thisActivity, R.id.listview_latest_places, getPlaces());
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
