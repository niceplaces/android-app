package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.AreasAdapter;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.utils.MyRunnable;

public class AreasListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas_list);
        getSupportActionBar().setTitle("Zone");
        final AreasListActivity thisActivity = this;
        FirebaseAnalytics.getInstance(this);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        final ListView listView = findViewById(R.id.listview_areas);
        progressBar.setVisibility(View.VISIBLE);
        DaoPlaces daoPlaces = new DaoPlaces(this);
        daoPlaces.getAreas(new MyRunnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                AreasAdapter adapter = new AreasAdapter(thisActivity, R.id.listview_areas, getAreas());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(thisActivity, PlacesListActivity.class);
                        intent.putExtra("AREA_ID", getAreas().get(i).getID());
                        intent.putExtra("AREA_NAME", getAreas().get(i).getName());
                        thisActivity.startActivity(intent);
                    }
                });
            }
        });
    }
}
