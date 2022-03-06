package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.activities.MapsActivity;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.ImageUtils;
import com.niceplaces.niceplaces.utils.MapUtils;

import java.util.List;

public class PlacesAdapter extends ArrayAdapter<Place> {

    private Context mContext;
    private GoogleMap mMap;

    public PlacesAdapter(Context context, int resource, List<Place> objects, GoogleMap map, LatLng position){
        super(context, resource, objects);
        mContext = context;
        mMap = map;
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
        ImageView imageViewPlaceStar = convertView.findViewById(R.id.imageview_place_star);
        ImageUtils.setAuthorIcon(place, imageViewPlaceStar);
        TextView textViewPlaceName = convertView.findViewById(R.id.textview_place_name);
        TextView textViewPlaceDistance = convertView.findViewById(R.id.textview_place_distance);
        ImageUtils.setImageViewWithGlide(mContext, place.mImage, imageViewPlaceImage);
        imageViewPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LatLng center = new LatLng(place.mLatitude, place.mLongitude);
                GoogleMap.CancelableCallback callback = new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(MapUtils.offset(mMap, center, 0, -150))
                                        .tilt(MapsActivity.DEFAULT_TILT)
                                        .zoom(mMap.getCameraPosition().zoom).build()));
                        place.mClusterItem.getMarker().showInfoWindow();
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                    }

                    @Override
                    public void onCancel() {

                    }
                };
                float zoom = mMap.getCameraPosition().zoom;
                if (place.mClusterItem.isMarkerClustered()){
                    zoom = 18;
                }
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(center)
                                .tilt(MapsActivity.DEFAULT_TILT)
                                .zoom(zoom).build()), callback);
            }
        });
        textViewPlaceName.setText(place.mName);
        textViewPlaceDistance.setText(Place.formatDistance(place.mDistance));
        /*Direction direction = new Direction(mPosition.latitude, mPosition.longitude, place.mLatitude, place.mLongitude);
        (new DirectionAsyncTask(mContext, textViewPlaceDistance)).execute(direction);*/
        return convertView;
    }
}
