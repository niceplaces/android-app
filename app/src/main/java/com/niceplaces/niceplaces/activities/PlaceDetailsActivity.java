package com.niceplaces.niceplaces.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.niceplaces.niceplaces.controllers.UserListsController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Event;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.NonScrollListView;
import com.niceplaces.niceplaces.utils.MyRunnable;

import java.util.List;
import java.util.Locale;

public class PlaceDetailsActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private boolean visitedChecked, wishChecked, favChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        mContext = this;
        mActivity = this;
        getSupportActionBar().hide();
        final RelativeLayout layoutTitleBar = findViewById(R.id.layout_titlebar);
        layoutTitleBar.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        final String placeID = extras.getString(Const.PLACE_ID);
        final ScrollView scrollView = findViewById(R.id.scrollView);
        /*final LinearLayout layoutPlaceShare = findViewById(R.id.layout_place_share);
        final ImageView IVFacebook = findViewById(R.id.imageview_facebook);
        final ImageView IVInstagram = findViewById(R.id.imageview_instagram);*/
        final ImageView IVVisited = findViewById(R.id.imageview_visited);
        final ImageView IVWish = findViewById(R.id.imageview_wish);
        final ImageView IVFav = findViewById(R.id.imageview_fav);
        final UserListsController prefs = new UserListsController(mContext);
        visitedChecked = prefs.isVisited(placeID);
        wishChecked = prefs.isWished(placeID);
        favChecked = prefs.isFavourite(placeID);
        TextView TVCaption = mActivity.findViewById(R.id.textview_visited);
        if (visitedChecked){
            IVVisited.setImageResource(R.drawable.check);
            TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorVisited));
        } else {
            IVVisited.setImageResource(R.drawable.check_outline);
            TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorDisabled));
        }
        IVVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView TVCaption = mActivity.findViewById(R.id.textview_visited);
                if (!visitedChecked){
                    ((ImageView)v).setImageResource(R.drawable.check);
                    TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorVisited));
                    prefs.addVisited(placeID);
                } else {
                    ((ImageView)v).setImageResource(R.drawable.check_outline);
                    TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorDisabled));
                    prefs.removeVisited(placeID);
                }
                visitedChecked = !visitedChecked;
            }
        });
        TVCaption = mActivity.findViewById(R.id.textview_wish);
        if (wishChecked){
            IVWish.setImageResource(R.drawable.wish);
            TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorWish));
        } else {
            IVWish.setImageResource(R.drawable.wish_outline);
            TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorDisabled));
        }
        IVWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView TVCaption = mActivity.findViewById(R.id.textview_wish);
                if (!wishChecked){
                    ((ImageView)v).setImageResource(R.drawable.wish);
                    TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorWish));
                    prefs.addWished(placeID);
                } else {
                    ((ImageView)v).setImageResource(R.drawable.wish_outline);
                    TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorDisabled));
                    prefs.removeWished(placeID);
                }
                wishChecked = !wishChecked;
            }
        });
        TVCaption = mActivity.findViewById(R.id.textview_fav);
        if (favChecked){
            IVFav.setImageResource(R.drawable.heart);
            TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorFavourite));
        } else {
            IVFav.setImageResource(R.drawable.heart_outline);
            TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorDisabled));
        }
        IVFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView TVCaption = mActivity.findViewById(R.id.textview_fav);
                if (!favChecked){
                    ((ImageView)v).setImageResource(R.drawable.heart);
                    TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorFavourite));
                    prefs.addFavourite(placeID);
                } else {
                    ((ImageView)v).setImageResource(R.drawable.heart_outline);
                    TVCaption.setTextColor(mActivity.getResources().getColor(R.color.colorDisabled));
                    prefs.removeFavourite(placeID);
                }
                favChecked = !favChecked;
            }
        });
        final AlertController alertController = new AlertController(this, R.id.layout_loading);
        DaoPlaces daoPlaces = new DaoPlaces(this);
        daoPlaces.getOne(placeID, new MyRunnable() {
            @Override
            public void run() {
                final TextView textViewPlaceName = findViewById(R.id.textview_place_name);
                final TextView textViewPlaceNameActionBar = findViewById(R.id.textview_place_name_titlebar);
                TextView textViewPlaceDesc = findViewById(R.id.textview_place_desc);
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
                if (getPlace().mDescription.compareTo("") == 0) {
                    TextView textViewComingSoon = findViewById(R.id.textview_coming_soon);
                    textViewComingSoon.setVisibility(View.VISIBLE);
                    LinearLayout layoutPlaceInfo = findViewById(R.id.layout_place_info);
                    layoutPlaceInfo.setVisibility(View.GONE);
                } else {
                    LinearLayout badgeProLoco = findViewById(R.id.badge_proloco);
                    LinearLayout badgeViaSacra = findViewById(R.id.badge_viasacra);
                    switch (getPlace().mAuthor){
                        case "1":
                            badgeProLoco.setVisibility(View.GONE);
                            badgeViaSacra.setVisibility(View.GONE);
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
                            imageViewWikipedia.setVisibility(View.VISIBLE);
                            findViewById(R.id.imageview_proloco).setVisibility(View.GONE);
                            findViewById(R.id.imageview_viasacra).setVisibility(View.GONE);
                            break;
                        case "2":
                            View.OnClickListener listener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(Const.PROLOCO_URL));
                                    startActivity(i);
                                }
                            };
                            badgeProLoco.setOnClickListener(listener);
                            badgeProLoco.setVisibility(View.VISIBLE);
                            badgeViaSacra.setVisibility(View.GONE);
                            imageViewWikipedia.setVisibility(View.GONE);
                            findViewById(R.id.imageview_proloco).setVisibility(View.VISIBLE);
                            findViewById(R.id.imageview_proloco).setOnClickListener(listener);
                            findViewById(R.id.imageview_viasacra).setVisibility(View.GONE);
                            break;
                        case "3":
                            View.OnClickListener listener1 = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(Const.VIASACRA_URL));
                                    startActivity(i);
                                }
                            };
                            badgeViaSacra.setOnClickListener(listener1);
                            badgeViaSacra.setVisibility(View.VISIBLE);
                            badgeProLoco.setVisibility(View.GONE);
                            imageViewWikipedia.setVisibility(View.GONE);
                            findViewById(R.id.imageview_proloco).setVisibility(View.GONE);
                            findViewById(R.id.imageview_viasacra).setVisibility(View.VISIBLE);
                            findViewById(R.id.imageview_viasacra).setOnClickListener(listener1);
                            break;
                    }
                }
                List<Event> events = getPlace().getEvents();
                if (!events.isEmpty() && Locale.getDefault().getDisplayLanguage().equals(Locale.ITALIAN.getDisplayLanguage())
                && getPlace().mAuthor.equals("1")) {
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
                /*if (getPlace().mFacebook.equals("") && getPlace().mInstagram.equals("")) {
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
                }*/
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
