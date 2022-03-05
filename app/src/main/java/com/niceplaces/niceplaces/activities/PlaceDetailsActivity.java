package com.niceplaces.niceplaces.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.EventsAdapter;
import com.niceplaces.niceplaces.controllers.AlertController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.NonScrollListView;
import com.niceplaces.niceplaces.utils.MyRunnable;

import java.util.List;
import java.util.Locale;

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
        final String placeID = extras.getString(Const.PLACE_ID);
        final ScrollView scrollView = findViewById(R.id.scrollView);
        final LinearLayout layoutPlaceShare = findViewById(R.id.layout_place_share);
        final ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        final ImageView IVInstagram = findViewById(R.id.imageview_instagram);
        final AlertController alertController = new AlertController(this, R.id.layout_loading);
        DaoPlaces daoPlaces = new DaoPlaces(this);
        daoPlaces.getOne(placeID, new MyRunnable() {
            @Override
            public void run() {
                final TextView textViewPlaceName = findViewById(R.id.textview_place_name);
                final TextView textViewPlaceNameActionBar = findViewById(R.id.textview_place_name_titlebar);
                TextView textViewPlaceDesc = findViewById(R.id.textview_place_desc);
                if (getPlace().mDescription.compareTo("") == 0) {
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
                                Uri.parse("google.navigation:q=" + getPlace().mLatitude + "," + getPlace().mLongitude)));
                    }
                });
                ImageView imageViewPlace = findViewById(R.id.imageview_place_image);
                ImageView imageViewPlaceActionBar = findViewById(R.id.imageview_place_image_titlebar);
                TextView placeArea = findViewById(R.id.place_area);
                ImageView imageViewWikipedia = findViewById(R.id.imageview_wikipedia);
                NonScrollListView listViewEvents = findViewById(R.id.listview_place_events);
                textViewPlaceName.setText(getPlace().mName);
                textViewPlaceNameActionBar.setText(getPlace().mName);
                textViewPlaceDesc.setText(getPlace().mDescription);
                placeArea.setText(getPlace().mArea + ", " + getPlace().mRegion);
                //TextUtils.justify(textViewPlaceDesc);
                ImageUtils.setImageViewWithGlide(mContext, getPlace().mImage, imageViewPlace);
                ImageUtils.setImageViewWithGlide(mContext, getPlace().mImage, imageViewPlaceActionBar);
                try {
                    final String wikipediaUrl = getPlace().mWikiUrl;
                    if (!wikipediaUrl.equals("")) {
                        imageViewWikipedia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl));
                                startActivity(i);
                            }
                        });
                    } else {
                        imageViewWikipedia.setAlpha(0.5f);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    imageViewWikipedia.setAlpha(0.5f);
                }
                List<Event> events = getPlace().getEvents();
                if (!events.isEmpty() && Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())) {
                    EventsAdapter adapter = new EventsAdapter(mContext, R.layout.listview_events, events);
                    listViewEvents.setAdapter(adapter);
                    listViewEvents.setEnabled(false);
                } else {
                    LinearLayout layoutHistory = findViewById(R.id.layout_history);
                    layoutHistory.setVisibility(View.GONE);
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
                TextView textViewDescSources = findViewById(R.id.textview_desc_sources);
                TextView textViewImageCredits = findViewById(R.id.textview_img_credits);
                if (!getPlace().mSources.equals("")) {
                    textViewDescSources.setText(getString(R.string.sources, getPlace().mSources));
                } else {
                    textViewDescSources.setText("");
                }
                if (!getPlace().mCredits.equals("")) {
                    textViewImageCredits.setText(getString(R.string.photo, getPlace().mCredits));
                } else {
                    textViewImageCredits.setText("");
                }
                if (getPlace().mFacebook.equals("") && getPlace().mInstagram.equals("")) {
                    layoutPlaceShare.setVisibility(View.GONE);
                } else {
                    IVFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getPlace().mFacebook));
                            startActivity(i);
                        }
                    });
                    IVInstagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getPlace().mInstagram));
                            startActivity(i);
                        }
                    });
                }
                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final ViewTreeObserver.OnGlobalLayoutListener listener = this;
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.scrollTo(0, 0);
                                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
                            }
                        });
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
