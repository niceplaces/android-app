package com.niceplaces.niceplaces.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.AreasAdapter;
import com.niceplaces.niceplaces.adapters.RegionsAdapter;
import com.niceplaces.niceplaces.controllers.AlertController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.dao.DaoRegions;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.MyRunnable;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_explore);
        getSupportActionBar().hide();
        final ExploreActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        final LinearLayout placeOfDayLayout = findViewById(R.id.layout_place_of_day);
        final ListView listView = findViewById(R.id.listview_areas);
        final AlertController alertPlaceOfDay = new AlertController(this, R.id.layout_loading_place_of_day);
        final AlertController alertRegions = new AlertController(this, R.id.layout_loading_regions);
        DaoPlaces daoPlaces = new DaoPlaces(this);
        daoPlaces.getPlaceOfTheDay(new MyRunnable() {
            @Override
            public void run() {
                ImageView imageView = findViewById(R.id.place_of_day_image);
                TextView textViewName = findViewById(R.id.place_of_day_name);
                TextView textViewArea = findViewById(R.id.place_of_day_area);
                TextView textViewDesc = findViewById(R.id.place_of_day_desc);
                final Place place = getPlace();
                ImageUtils.setImageViewWithGlide(context, place.mImage, imageView);
                textViewName.setText(place.mName);
                textViewArea.setText(place.mArea + ", " + place.mRegion);
                textViewDesc.setText(place.mDescription);
                placeOfDayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(thisActivity, PlaceDetailsActivity.class);
                        intent.putExtra(Const.PLACE_ID, place.getID());
                        thisActivity.startActivity(intent);
                    }
                });
                alertPlaceOfDay.loadingSuccess();
            }
        }, new Runnable() {
            @Override
            public void run() {
                alertPlaceOfDay.loadingError();
            }
        });
        DaoRegions daoRegions = new DaoRegions(this);
        daoRegions.getRegions(new MyRunnable() {
            @Override
            public void run() {
                RegionsAdapter adapter = new RegionsAdapter(thisActivity, R.id.listview_areas, getRegions());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(thisActivity, AreasListActivity.class);
                        intent.putExtra(Const.REGION_ID, getRegions().get(i).getID());
                        intent.putExtra("REGION_NAME", getRegions().get(i).getName());
                        thisActivity.startActivity(intent);
                    }
                });
                alertRegions.loadingSuccess();
            }
        }, new Runnable() {
            @Override
            public void run() {
                alertRegions.loadingError();
            }
        });
    }
}
