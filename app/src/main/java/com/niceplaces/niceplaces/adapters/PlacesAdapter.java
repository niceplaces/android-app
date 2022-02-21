package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.AppUtils;
import com.niceplaces.niceplaces.utils.ImageUtils;

import java.util.List;

/**
 * Created by Lorenzo on 29/12/2017.
 */

public class PlacesAdapter extends ArrayAdapter<Place> {

    private Context mContext;
    private GoogleMap mMap;
    private LatLng mPosition;

    public PlacesAdapter(Context context, int resource, List<Place> objects, GoogleMap map, LatLng position){
        super(context, resource, objects);
        mContext = context;
        mMap = map;
        mPosition = position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Place place = getItem(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_places, parent, false);
        }
        ImageView imageViewPlaceImage = convertView.findViewById(R.id.imageview_place_image);
        TextView textViewPlaceName = convertView.findViewById(R.id.textview_place_name);
        TextView textViewPlaceDistance = convertView.findViewById(R.id.textview_place_distance);
        ImageUtils.setPlacesImageView(mContext, place, imageViewPlaceImage);
        imageViewPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng center = place.mMarker.getPosition();
                float zoom = mMap.getCameraPosition().zoom;
                LatLng newCenter = new LatLng(center.latitude + 0.003, center.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newCenter));
                place.mMarker.showInfoWindow();
                mMap.getUiSettings().setMapToolbarEnabled(true);
            }
        });
        textViewPlaceName.setText(place.mName);
        textViewPlaceDistance.setText(Place.formatDistance(place.mDistance));
        /*Direction direction = new Direction(mPosition.latitude, mPosition.longitude, place.mLatitude, place.mLongitude);
        (new DirectionAsyncTask(mContext, textViewPlaceDistance)).execute(direction);*/
        Log.i("FREE HEAP SIZE", AppUtils.getAvailableHeapSize());
        return convertView;
    }
}
