package com.niceplaces.niceplaces.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.MarkerInfoAdapter;
import com.niceplaces.niceplaces.adapters.PlacesAdapter;
import com.niceplaces.niceplaces.dao.DaoPlaces;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.RepeatListener;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VirtualTourActivity extends FragmentActivity implements OnMapReadyCallback {

    private Context mContext;
    private Activity mActivity;
    private GoogleMap mMap;
    private List<Place> mPlaces;
    private LinearLayout mListView;
    private LatLng mCurrentPosition;
    private MapMode mMapMode;
    private Marker mMyPositionMarker;

    public static final String PLACE_ID = "place_id";

    private enum MapMode {
        ROAD, SATELLITE
    }

    private enum Direction {
        UP, LEFT, RIGHT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        mCurrentPosition = null;
        setContentView(R.layout.activity_virtual_tour);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mListView = findViewById(R.id.listview);
        ImageView imageViewSearchButton = findViewById(R.id.imageview_search_button);
        imageViewSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if no view has focus:
                View focusView = mActivity.getCurrentFocus();
                if (focusView != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        object.mClusterItem.getMarker().setVisible(res);
                        return res;
                    }
                });
                updatePlacesListView(filteredPlaces);
            }
        });
        DaoPlaces daoPlaces = new DaoPlaces(this);
        //mPlaces = daoPlaces.getAll();
        Button buttonMoveUp = findViewById(R.id.button_up);
        buttonMoveUp.setOnTouchListener(new RepeatListener(10, 10, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMyPositionMarker(Direction.UP);
            }
        }));
        Button buttonMoveLeft = findViewById(R.id.button_left);
        buttonMoveLeft.setOnTouchListener(new RepeatListener(100, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMyPositionMarker(Direction.LEFT);
            }
        }));
        Button buttonMoveRight = findViewById(R.id.button_right);
        buttonMoveRight.setOnTouchListener(new RepeatListener(100, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMyPositionMarker(Direction.RIGHT);
            }
        }));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapMode = MapMode.ROAD;
        final ImageView imageViewMapMode = findViewById(R.id.imageview_map_mode);
        imageViewMapMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mMapMode){
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
                LatLng newCenter = new LatLng(center.latitude + 0.003, center.longitude);
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
        updateLocation(43.3228, 11.3278);
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
        mMyPositionMarker = mMap.addMarker(new MarkerOptions().position(myPosition).title("Tu sei qui!")
                .icon(ImageUtils.bitmapDescriptorFromDrawable(this, R.drawable.navigation)));
        if (mCurrentPosition == null){
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(myPosition).tilt(80).zoom(18).build()));
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
            mPlaces.get(i).mClusterItem.setMarker(mMap.addMarker(marker));
        }
        Collections.sort(mPlaces, new Comparator<Place>() {
            @Override
            public int compare(Place g1, Place g2) {
                return (int) (g1.mDistance - g2.mDistance);
            }
        });
        updatePlacesListView(mPlaces);
    }

    private void updatePlacesListView(List<Place> places){
        mListView.removeAllViews();
        PlacesAdapter adapter = new PlacesAdapter(this, R.layout.listview_places, places, mMap, mCurrentPosition);
        for (int i = 0; i < adapter.getCount(); i++) {
            mListView.addView(adapter.getView(i, null, mListView));
        }
        TextView TVCounter = findViewById(R.id.places_counter);
        TVCounter.setText("Luoghi più vicini (" + places.size() + ")");
    }

    public void moveMyPositionMarker(Direction direction){
        LatLng newPosition = mCurrentPosition;
        float bearing =mMap.getCameraPosition().bearing;
        double lat = Math.toRadians(mCurrentPosition.latitude);
        double lon = Math.toRadians(mCurrentPosition.longitude);
        double distance = 20;
        int earthRadius = 6371000;
        switch (direction){
            case UP:
                double bearingInRadiants = Math.toRadians(bearing);
                double newLat = Math.asin(Math.sin(lat)*Math.cos(distance/earthRadius) +
                        Math.cos(lat)*Math.sin(distance/earthRadius)*Math.cos(bearingInRadiants));
                double newLon = lon + Math.atan2(Math.sin(bearingInRadiants)*Math.sin(distance/earthRadius)*Math.cos(lat),
                        Math.cos(distance/earthRadius)- Math.sin(lat)*Math.sin(newLat));
                newPosition = new LatLng(Math.toDegrees(newLat), Math.toDegrees(newLon));
                updateLocation(newPosition.latitude, newPosition.longitude);
                break;
            case LEFT:
                bearing -= 10;
                break;
            case RIGHT:
                bearing += 10;
                break;
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(newPosition).tilt(80).zoom(18).bearing(bearing).build()));
        mMyPositionMarker.setPosition(newPosition);
        mCurrentPosition = newPosition;
    }
}
