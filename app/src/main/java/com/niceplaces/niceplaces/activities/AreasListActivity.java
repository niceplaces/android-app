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
import com.niceplaces.niceplaces.adapters.AreasAdapter;
import com.niceplaces.niceplaces.controllers.AlertController;
import com.niceplaces.niceplaces.dao.DaoRegions;
import com.niceplaces.niceplaces.utils.MyRunnable;

public class AreasListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        final AreasListActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().hide();
        TextView textViewAreaName = findViewById(R.id.explore_area_name);
        textViewAreaName.setText(extras.getString("REGION_NAME"));
        final ListView listView = findViewById(R.id.listview_areas);
        final AlertController alertController = new AlertController(this, R.id.layout_loading);
        DaoRegions daoRegions = new DaoRegions(this);
        daoRegions.getAreas(extras.getString(Const.REGION_ID), new MyRunnable() {
            @Override
            public void run() {
                AreasAdapter adapter = new AreasAdapter(thisActivity, R.id.listview_areas, getAreas());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(thisActivity, PlacesListActivity.class);
                        intent.putExtra(Const.AREA_ID, getAreas().get(i).getID());
                        intent.putExtra("AREA_NAME", getAreas().get(i).getName());
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
