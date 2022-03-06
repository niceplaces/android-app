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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.niceplaces.niceplaces.BuildConfig;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.MarkerInfoAdapter;
import com.niceplaces.niceplaces.adapters.PlacesAdapter;
import com.niceplaces.niceplaces.controllers.PrefsController;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.GeoPoint;
import com.niceplaces.niceplaces.models.MyClusterItem;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.AppUtils;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.MyRenderer;
import com.niceplaces.niceplaces.utils.MyRunnable;
import com.niceplaces.niceplaces.utils.NonHierarchicalDistanceBasedAlgorithm;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        ClusterManager.OnClusterClickListener<MyClusterItem>, ClusterManager.OnClusterInfoWindowClickListener<MyClusterItem>,
        ClusterManager.OnClusterItemClickListener<MyClusterItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItem> {

    private Context mContext = this;
    private Activity mActivity = this;
    private GoogleMap mMap;
    private List<Place> mPlaces;
    private LinearLayout mListView;
    private LatLng mCurrentPosition = null;
    private MapMode mMapMode;
    private AlertDialog dialogPosLoading;
    private ClusterManager<MyClusterItem> mClusterManager;
    private boolean myPositionAnchored = true;
    private boolean moveToMyPositionClicked = true;

    private enum MapMode {
        ROAD, SATELLITE
    }

    public static int DEFAULT_TILT = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                List<Place> filteredPlaces = new ArrayList<>(mPlaces);
                CollectionUtils.filter(filteredPlaces, new Predicate<Place>() {
                    @Override
                    public boolean evaluate(Place object) {
                        boolean res = object.mName.toLowerCase().contains(pattern);
                        // TODO causa NullPointerException
                        try {
                           // object.mMarker.setVisible(res);
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
        final PrefsController prefs = new PrefsController(mContext);
        final ImageView imageViewAnchor = findViewById(R.id.imageview_map_position);
        imageViewAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMyPositionClicked = true;
                if (!myPositionAnchored){
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder().target(mCurrentPosition).tilt(DEFAULT_TILT).zoom(prefs.getZoom()).build()),
                            new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
                                    moveToMyPositionClicked = false;
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                    imageViewAnchor.setImageResource(R.drawable.crosshairs_gps);
                    myPositionAnchored = true;
                }
            }
        });
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnCameraMoveListener(this);
        mClusterManager = new ClusterManager<>(this, mMap);
        NonHierarchicalDistanceBasedAlgorithm<MyClusterItem> algorithm =
                new NonHierarchicalDistanceBasedAlgorithm<>();
        algorithm.setMaxDistanceBetweenClusteredItems(100); // 100dp
        mClusterManager.setAlgorithm(algorithm);
        mClusterManager.setRenderer(new MyRenderer(this, mMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoAdapter(this));
        // Cambiare l'ordine di queste istruzioni potrebbe causare il blocco del
        // clustering in fase di cambio di zooom
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        LatLng myPosition = new LatLng(prefs.getStoredLocation().latitude, prefs.getStoredLocation().longitude);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(myPosition).tilt(DEFAULT_TILT).zoom(prefs.getZoom()).build()));
    }

    @Override
    public void onCameraMove() {
        float zoom = mMap.getCameraPosition().zoom;
        PrefsController prefs = new PrefsController(mContext);
        prefs.setZoom(zoom);
        LatLng cameraCenter = mMap.getCameraPosition().target;
        if (cameraCenter == null){
            cameraCenter = new LatLng(prefs.getStoredLocation().latitude, prefs.getStoredLocation().longitude);
        }
        Log.i(AppUtils.getTag(), "LATLNG: " + cameraCenter.latitude + ", " +
                cameraCenter.longitude);
        final ImageView imageViewAnchor = findViewById(R.id.imageview_map_position);
        try {
            boolean centeredToMyPosition = cameraCenter.latitude == mCurrentPosition.latitude &&
                    cameraCenter.longitude == mCurrentPosition.longitude;
            if (!moveToMyPositionClicked) {
                if (centeredToMyPosition) {
                    imageViewAnchor.setImageResource(R.drawable.crosshairs_gps);
                } else {
                    imageViewAnchor.setImageResource(R.drawable.crosshairs);
                }
            }
            myPositionAnchored = centeredToMyPosition;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  0: {
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
        dialogPosLoading = new AlertDialog.Builder(MapsActivity.this).create();
        dialogPosLoading.setTitle(R.string.location_loading);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_load_location, null);
        dialogPosLoading.setView(view);
        dialogPosLoading.show();
        final AlertDialog dialogPosDisabled = new AlertDialog.Builder(MapsActivity.this).create();
        dialogPosDisabled.setTitle(R.string.location_disabled);
        dialogPosDisabled.setMessage(getString(R.string.impossible_load_location));
        dialogPosDisabled.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
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
                    PrefsController prefs = new PrefsController(mContext);
                    prefs.setStoredLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
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
                        PrefsController prefs = new PrefsController(mContext);
                        GeoPoint storedLocation = prefs.getStoredLocation();
                        sendNearestPlacesRequest(storedLocation.latitude, storedLocation.longitude);
                        if (!dialogPosDisabled.isShowing()) {
                            dialogPosLoading.dismiss();
                            dialogPosDisabled.show();
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

    public void sendNearestPlacesRequest(final double lat, final double lon) {
        DaoPlaces daoPlaces = new DaoPlaces(mContext);
        daoPlaces.getNearest(lat, lon, new MyRunnable() {
            @Override
            public void run() {
                mPlaces = getPlaces();
                updateLocation(lat, lon);
                dialogPosLoading.dismiss();
            }
        }, new MyRunnable() {
            @Override
            public void run() {
            }
        });
    }

    public void sendNearestPlacesRequest(final Location location) {
        sendNearestPlacesRequest(location.getLatitude(), location.getLongitude());
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
        mClusterManager.clearItems();
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Tu sei qui!")
                .icon(ImageUtils.bitmapDescriptorFromDrawable(this, R.drawable.marker_position)));
        if (myPositionAnchored){
            PrefsController prefs = new PrefsController(mContext);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder().target(myPosition).tilt(DEFAULT_TILT).zoom(prefs.getZoom()).build()));
        }
        mCurrentPosition = myPosition;
        for (int i = 0; i < mPlaces.size(); i++) {
            // Calcola le distanze in linea d'aria
            Location.distanceBetween(latitude, longitude, mPlaces.get(i).mLatitude, mPlaces.get(i).mLongitude, results);
            Place place= mPlaces.get(i);
            place.mDistance = results[0];
            MyClusterItem clusterItem = new MyClusterItem(mPlaces.get(i));
            place.mClusterItem = clusterItem;
            mClusterManager.addItem(clusterItem);
        }
        mClusterManager.cluster();
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
        TVCounter.setText(getString(R.string.places_in_radius_of, String.format("%.2f",prefs.getDistanceRadius()), places.size()));
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

    @Override
    public boolean onClusterClick(Cluster<MyClusterItem> cluster) {
        return false;
    }

    @Override
    public boolean onClusterItemClick(MyClusterItem myClusterItem) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyClusterItem> cluster) {

    }

    @Override
    public void onClusterItemInfoWindowClick(MyClusterItem myClusterItem) {
        Intent intent = new Intent(mActivity, PlaceDetailsActivity.class);
        intent.putExtra(Const.PLACE_ID, myClusterItem.getPlace().getID());
        mActivity.startActivity(intent);
    }
}
