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
import com.niceplaces.niceplaces.models.Event;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

    private Context mContext;

    public EventsAdapter(Context context, int resource, List<Event> objects){
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Event event = getItem(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_events, parent, false);
        }
        TextView textViewEventDate = convertView.findViewById(R.id.textview_event_date);
        TextView textViewEventDesc = convertView.findViewById(R.id.textview_event_desc);
        textViewEventDate.setText(event.getDate());
        textViewEventDesc.setText(event.getDescription());
        //TextUtils.justify(textViewEventDesc);
        return convertView;
    }
}
