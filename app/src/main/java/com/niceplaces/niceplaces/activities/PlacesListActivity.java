package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.ExplorePlacesAdapter;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.utils.MyRunnable;

import static com.niceplaces.niceplaces.activities.MapsActivity.PLACE_ID;

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
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        DaoPlaces daoPlaces = new DaoPlaces(this);
        daoPlaces.getByArea(extras.getString("AREA_ID"), new MyRunnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                ExplorePlacesAdapter adapter = new ExplorePlacesAdapter(thisActivity, R.id.listview_areas, getPlaces());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(thisActivity, PlaceDetailsActivity.class);
                        intent.putExtra(PLACE_ID, getPlaces().get(i).getID());
                        thisActivity.startActivity(intent);
                    }
                });
            }
        });
    }
}
