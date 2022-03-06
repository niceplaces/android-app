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
import com.niceplaces.niceplaces.dao.DaoLists;
import com.niceplaces.niceplaces.utils.MyRunnable;

public class PlacesListActivity extends AppCompatActivity {

    private Bundle mExtras;
    private final PlacesListActivity mActivity = this;
    private Mode mMode;

    private enum Mode {
        AREA, LIST
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        FirebaseAnalytics.getInstance(this);
        mExtras = getIntent().getExtras();
        getSupportActionBar().hide();
        TextView textViewAreaName = findViewById(R.id.explore_area_name);
        if (mExtras.getString(Const.AREA_ID) != null){
            mMode = Mode.AREA;
            textViewAreaName.setText(mExtras.getString("AREA_NAME"));
        } else {
            mMode = Mode.LIST;
            textViewAreaName.setText(mExtras.getString("LIST_NAME"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ListView listView = findViewById(R.id.listview_areas);
        final AlertController alertController = new AlertController(this, R.id.layout_loading);
        final MyRunnable successCallback = new MyRunnable() {
            @Override
            public void run() {
                ExplorePlacesAdapter adapter = new ExplorePlacesAdapter(mActivity, R.id.listview_areas, getPlaces());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(mActivity, PlaceDetailsActivity.class);
                        intent.putExtra(Const.PLACE_ID, getPlaces().get(i).getID());
                        mActivity.startActivity(intent);
                    }
                });
                alertController.loadingSuccess();
            }
        };
        switch (mMode){
            case AREA:
                DaoAreas daoAreas = new DaoAreas(this);
                daoAreas.getPlaces(mExtras.getString(Const.AREA_ID), successCallback, new Runnable() {
                    @Override
                    public void run() {
                        alertController.loadingError();
                    }
                });
                break;
            case LIST:
                DaoLists daoLists = new DaoLists(this);
                daoLists.getPlacesByListId(mExtras.getString(Const.LIST_ID), successCallback, new Runnable() {
                    @Override
                    public void run() {
                        alertController.loadingError();
                    }
                });
                break;
        }
    }
}
