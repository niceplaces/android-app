package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.Place;
import com.niceplaces.niceplaces.utils.ImageUtils;

import java.util.List;

public class LatestPlacesAdapter extends ArrayAdapter<Place> {

    private Context mContext;

    public LatestPlacesAdapter(Context context, int resource, List<Place> objects){
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Place place = getItem(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_new_places, parent, false);
        }
        TextView textViewName = convertView.findViewById(R.id.textview_place_name);
        TextView textViewArea = convertView.findViewById(R.id.textview_place_area);
        ImageView imageViewPlaceImage = convertView.findViewById(R.id.imageview_place_image);
        ImageView IVStar = convertView.findViewById(R.id.imageview_place_star);
        if (place.mHasDescription){
            IVStar.setVisibility(View.VISIBLE);
        } else {
            IVStar.setVisibility(View.GONE);
        }
        textViewName.setText(place.mName);
        textViewArea.setText(place.mArea + ", " + place.mRegion);
        ImageUtils.setImageViewWithGlide(mContext, place.mImage, imageViewPlaceImage);
        return convertView;
    }
}
