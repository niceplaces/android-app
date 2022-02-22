package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.ImageUtils;

/**
 * Created by Lorenzo on 31/12/2017.
 */

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public MarkerInfoAdapter(Context context){
        mContext = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        final Place place = (Place) marker.getTag();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View convertView = layoutInflater.inflate(R.layout.infowindow_marker, null);
        ImageView imageViewPlaceImage = convertView.findViewById(R.id.imageview_place_image);
        TextView textViewPlaceName = convertView.findViewById(R.id.textview_place_name);
        TextView textViewPlaceDistance = convertView.findViewById(R.id.textview_place_distance);
        ImageUtils.setImageViewWithGlide(mContext, place.mImage, imageViewPlaceImage);
        textViewPlaceName.setText(place.mName);
        textViewPlaceDistance.setText(Place.formatDistance(place.mDistance));
        return convertView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
