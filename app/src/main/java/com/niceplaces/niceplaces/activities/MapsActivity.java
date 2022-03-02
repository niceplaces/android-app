package com.niceplaces.niceplaces.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.MarkerInfoAdapter;
import com.niceplaces.niceplaces.adapters.PlacesAdapter;
import com.niceplaces.niceplaces.controllers.PrefsController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.AppUtils;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.MyRunnable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Context mContext;
    private Activity mActivity;
    private GoogleMap mMap;
    private List<Place> mPlaces;
    private LinearLayout mListView;
    private LatLng mCurrentPosition;
    private MapMode mMapMode;

    public static final String PLACE_ID = "place_id";

    private enum MapMode {
        ROAD, SATELLITE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        mCurrentPosition = null;
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mListView = findViewById(R.id.listview);
        ImageView imageViewSearchButton = findViewById(R.id.imageview_search_button);
        ImageView imageViewSettingsButton = findViewById(R.id.imageview_map_settings_button);
        imageViewSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if no view has focus:
                View focusView = mActivity.getCurrentFocus();
                if (focusView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                EditText editTextSearch = findViewById(R.id.edittext_search);
                final String pattern = editTextSearch.getText().toString().toLowerCase();
                List<Place> filteredPlaces = new ArrayList<>();
                filteredPlaces.addAll(mPlaces);
                CollectionUtils.filter(filteredPlaces, new Predicate<Place>() {
                    @Override
                    public boolean evaluate(Place object) {
                        boolean res = object.mName.toLowerCase().contains(pattern);
                        // TODO causa NullPointerException
                        try {
                            object.mMarker.setVisible(res);
                        } catch (NullPointerException e) {
                            FirebaseCrash.logcat(Log.ERROR, AppUtils.getTag(), "Marker NPE ex");
                            FirebaseCrash.report(e);
                        }
                        return res;
                    }
                });
                updatePlacesListView(filteredPlaces);
            }
        });
        imageViewSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapMode = MapMode.ROAD;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            Log.i("Permission", "Granted");
            setupLocationListener();
        }
        final ImageView imageViewMapMode = findViewById(R.id.imageview_map_mode);
        imageViewMapMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mMapMode) {
                    case ROAD:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        imageViewMapMode.setImageResource(R.drawable.road);
                        mMapMode = MapMode.SATELLITE;
                        break;
                    case SATELLITE:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        imageViewMapMode.setImageResource(R.drawable.mountains);
                        mMapMode = MapMode.ROAD;
                        break;
                }
            }
        });
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setInfoWindowAdapter(new MarkerInfoAdapter(this));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng center = marker.getPosition();
                LatLng newCenter = new LatLng(center.latitude, center.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newCenter));
                marker.showInfoWindow();
                mMap.getUiSettings().setMapToolbarEnabled(true);
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                final Place place = (Place) marker.getTag();
                Intent intent = new Intent(mContext, PlaceDetailsActivity.class);
                intent.putExtra(PLACE_ID, place.getID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLocationListener();
                } else {
                    //
                }
            }
        }
    }

    public void setupLocationListener() {
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
        alertDialog.setTitle("Posizione disabilitata");
        alertDialog.setMessage("Non è possibile caricare la posizione attuale perché la geolocalizzazione è disattivata.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        try {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(mContext, "Location update " + String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
                    }
                    sendNearestPlacesRequest(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    Log.i("OnProvider", "StatusChanged");
                }

                @Override
                public void onProviderEnabled(String s) {
                    Log.i("OnProviderEnabled", s);
                }

                @Override
                public void onProviderDisabled(String s) {
                    Log.i("OnProviderDisabled", s);
                    boolean oneProviderAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (!oneProviderAvailable) {
                        if (!alertDialog.isShowing()) {
                            alertDialog.show();
                        }
                    }
                }
            };
            PrefsController prefs = new PrefsController(this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, prefs.getLocationRefreshTime(), prefs.getLocationRefreshDistance(), locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, prefs.getLocationRefreshTime(), prefs.getLocationRefreshDistance(), locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("getLastKnownLocation", Boolean.toString(location == null));
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("getLastKnownLocation2", Boolean.toString(location == null));
            }
            if (location != null) {
                // TODO genera crash
                //updateLocation(location);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void sendNearestPlacesRequest(final Location location) {
        DaoPlaces daoPlaces = new DaoPlaces(mContext);
        daoPlaces.getNearest(location.getLatitude(), location.getLongitude(),
                new MyRunnable() {
                    @Override
                    public void run() {
                        mPlaces = getPlaces();
                        updateLocation(location);
                    }
                });
    }

    public void updateLocation(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        updateLocation(latitude, longitude);
    }

    public void updateLocation(double latitude, double longitude) {
        float[] results = new float[1];
        Log.i("LOCATION", String.valueOf(latitude) + " " + String.valueOf(longitude));
        LatLng myPosition = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Tu sei qui!")
                .icon(ImageUtils.bitmapDescriptorFromDrawable(this, R.drawable.marker_position)));
        if (mCurrentPosition == null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(myPosition).tilt(80).zoom(16).build()));
        }
        mCurrentPosition = myPosition;
        for (int i = 0; i < mPlaces.size(); i++) {
            // TODO Calcola le distanze in linea d'aria
            Location.distanceBetween(latitude, longitude, mPlaces.get(i).mLatitude, mPlaces.get(i).mLongitude, results);
            Log.i("DISTANCE", String.valueOf(results[0]));
            mPlaces.get(i).mDistance = results[0];
            LatLng point = new LatLng(mPlaces.get(i).mLatitude, mPlaces.get(i).mLongitude);
            MarkerOptions marker = new MarkerOptions().position(point)
                    .icon(ImageUtils.bitmapDescriptorFromDrawable(this, R.drawable.marker_places))
                    .title(mPlaces.get(i).mName + ", " + Place.formatDistance(mPlaces.get(i).mDistance));
            mPlaces.get(i).mMarker = mMap.addMarker(marker);
            mPlaces.get(i).mMarker.setTag(mPlaces.get(i));
        }
        Collections.sort(mPlaces, new Comparator<Place>() {
            @Override
            public int compare(Place g1, Place g2) {
                return (int) (g1.mDistance - g2.mDistance);
            }
        });
        updatePlacesListView(mPlaces);
    }

    private void updatePlacesListView(List<Place> places) {
        mListView.removeAllViews();
        PlacesAdapter adapter = new PlacesAdapter(this, R.layout.listview_places, places, mMap, mCurrentPosition);
        for (int i = 0; i < adapter.getCount(); i++) {
            mListView.addView(adapter.getView(i, null, mListView));
        }
        View morePlaces = getLayoutInflater().inflate(R.layout.more_places, null);
        mListView.addView(morePlaces);
        TextView TVCounter = findViewById(R.id.places_counter);
        PrefsController prefs = new PrefsController(mContext);
        TVCounter.setText("Luoghi nel raggio di " + String.valueOf(prefs.getDistanceRadius()) + " km (" + places.size() + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentPosition != null) {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(mCurrentPosition.latitude);
            location.setLongitude(mCurrentPosition.longitude);
            sendNearestPlacesRequest(location);
        }
    }
}
