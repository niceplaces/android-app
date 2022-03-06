package com.niceplaces.niceplaces.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.AppUtils;
import com.niceplaces.niceplaces.utils.ImageUtils;

/**
 * Created by Lorenzo on 31/12/2017.
 */

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity mActivity;
    private Context mContext;

    public MarkerInfoAdapter(Activity activity, Context context){
        mActivity = activity;
        mContext = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        final Place place = (Place) marker.getTag();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View convertView = layoutInflater.inflate(R.layout.infowindow_places, null);
        ImageView imageViewPlaceStar = convertView.findViewById(R.id.imageview_place_star);
        ImageView imageViewPlaceImage = convertView.findViewById(R.id.infowindow_place_image);
        ImageUtils.setAuthorIcon(place, imageViewPlaceStar);
        TextView textViewPlaceName = convertView.findViewById(R.id.textview_place_name);
        TextView textViewPlaceDistance = convertView.findViewById(R.id.textview_place_distance);
        //Log.i(AppUtils.getTag(), "IMAGE: " + place.mImage);
        ImageUtils.setImageViewWithGlide(mActivity, mContext, marker, place.mImage, imageViewPlaceImage);
        textViewPlaceName.setText(place.mName);
        textViewPlaceDistance.setText(Place.formatDistance(place.mDistance));
        return convertView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
