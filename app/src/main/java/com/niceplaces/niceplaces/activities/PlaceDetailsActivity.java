package com.niceplaces.niceplaces.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.EventsAdapter;
import com.niceplaces.niceplaces.controllers.DatabaseController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.NonScrollListView;
import com.niceplaces.niceplaces.utils.TextUtils;

import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        mContext = this;
        getSupportActionBar().hide();
        final RelativeLayout layoutTitleBar = findViewById(R.id.layout_titlebar);
        layoutTitleBar.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        final String placeID = extras.getString(MapsActivity.PLACE_ID);
        SQLiteDatabase db = new DatabaseController(this).getReadableDatabase();
        DaoPlaces daoPlaces = new DaoPlaces(db, this);
        final Place place = daoPlaces.getOne(placeID);
        final TextView textViewPlaceName = findViewById(R.id.textview_place_name);
        final TextView textViewPlaceNameActionBar = findViewById(R.id.textview_place_name_titlebar);
        TextView textViewPlaceDesc = findViewById(R.id.textview_place_desc);
        if (place.mDescription.compareTo("") == 0) {
            TextView textViewComingSoon = findViewById(R.id.textview_coming_soon);
            textViewComingSoon.setVisibility(View.VISIBLE);
            LinearLayout layoutPlaceInfo = findViewById(R.id.layout_place_info);
            layoutPlaceInfo.setVisibility(View.GONE);
        }
        LinearLayout layoutNavigation = findViewById(R.id.layout_navigation);
        layoutNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + place.mLatitude + "," + place.mLongitude)));
            }
        });
        ImageView imageViewPlace = findViewById(R.id.imageview_place_image);
        ImageView imageViewPlaceActionBar = findViewById(R.id.imageview_place_image_titlebar);
        ImageView imageViewWikipedia = findViewById(R.id.imageview_wikipedia);
        NonScrollListView listViewEvents = findViewById(R.id.listview_place_events);
        textViewPlaceName.setText(place.mName);
        textViewPlaceNameActionBar.setText(place.mName);
        textViewPlaceDesc.setText(place.mDescription);
        //TextUtils.justify(textViewPlaceDesc);
        ImageUtils.setPlacesImageView(this, place.mImage, imageViewPlace);
        ImageUtils.setPlacesImageView(this, place.mImage, imageViewPlaceActionBar);
        try {
            final String wikipediaUrl = place.getLinks().get(0).getUrl();
            imageViewWikipedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl));
                    startActivity(i);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            imageViewWikipedia.setAlpha(0.5f);
        }
        List<Event> events = place.getEvents();
        if (events != null){
            EventsAdapter adapter = new EventsAdapter(this, R.layout.listview_events, events);
            listViewEvents.setAdapter(adapter);
            listViewEvents.setEnabled(false);
        }
        textViewPlaceName.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int[] location = new int[2];
                textViewPlaceName.getLocationOnScreen(location);
                int pos = location[1];
                textViewPlaceNameActionBar.getLocationOnScreen(location);
                int posTitleBar = location[1];
                if (pos > posTitleBar) {
                    layoutTitleBar.setVisibility(View.GONE);
                } else {
                    layoutTitleBar.setVisibility(View.VISIBLE);
                }
            }
        });
        TextView textViewImageCredits = findViewById(R.id.textview_img_credits);
        textViewImageCredits.setText("Foto: " + place.mCredits);
    }

}
