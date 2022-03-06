package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.PlacesList;

import java.util.List;

public class ListsAdapter extends ArrayAdapter<PlacesList> {

    public ListsAdapter(Context context, int resource, List<PlacesList> objects){
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PlacesList list = getItem(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_areas, parent, false);
        }
        TextView textViewAreaName = convertView.findViewById(R.id.textview_area_name);
        TextView textViewAreaCount = convertView.findViewById(R.id.textview_area_count);
        textViewAreaName.setText(list.getName());
        textViewAreaCount.setText("(" + list.getCount() + ")");
        return convertView;
    }
}
