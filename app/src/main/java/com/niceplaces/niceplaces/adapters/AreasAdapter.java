package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.Area;
import com.niceplaces.niceplaces.models.Event;

import java.util.List;

public class AreasAdapter extends ArrayAdapter<Area> {

    public AreasAdapter(Context context, int resource, List<Area> objects){
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Area area = getItem(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_areas, parent, false);
        }
        TextView textViewAreaName = convertView.findViewById(R.id.textview_area_name);
        TextView textViewAreaCount = convertView.findViewById(R.id.textview_area_count);
        textViewAreaName.setText(area.getName());
        textViewAreaCount.setText("(" + area.getCount() + ")");
        return convertView;
    }
}
