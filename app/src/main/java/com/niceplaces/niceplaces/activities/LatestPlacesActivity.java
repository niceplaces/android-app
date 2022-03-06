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

    private final LatestPlacesActivity mActivity = this;
    private ListView mListView;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_places);
        FirebaseAnalytics.getInstance(this);
        getSupportActionBar().hide();
        mListView = findViewById(R.id.listview_latest_places);
        mSpinner = findViewById(R.id.spinner_latest_places);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList(mSpinner.getSelectedItemPosition());
    }

    private void loadList(int position){
        final DaoPlaces daoPlaces = new DaoPlaces(this);
        final AlertController alertController = new AlertController(mActivity, R.id.layout_loading);
        switch (position){
            case 0:
                daoPlaces.getLatestInserted(new MyRunnable() {
                    @Override
                    public void run() {
                        LatestPlacesAdapter adapter = new LatestPlacesAdapter(mActivity, R.id.listview_latest_places, getPlaces());
                        mListView.setAdapter(adapter);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(mActivity, PlaceDetailsActivity.class);
                                intent.putExtra(Const.PLACE_ID, getPlaces().get(i).getID());
                                mActivity.startActivity(intent);
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
                break;
            case 1:
                daoPlaces.getLatestUpdated(new MyRunnable() {
                    @Override
                    public void run() {
                        LatestPlacesAdapter adapter = new LatestPlacesAdapter(mActivity, R.id.listview_latest_places, getPlaces());
                        mListView.setAdapter(adapter);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(mActivity, PlaceDetailsActivity.class);
                                intent.putExtra(Const.PLACE_ID, getPlaces().get(i).getID());
                                mActivity.startActivity(intent);
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
                break;
        }
    }
}
