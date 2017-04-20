package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TimeAdapter extends ArrayAdapter{

    public TimeAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Time time = (Time) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.times_layout, parent, false);
        }

        if(time != null){
            // Lookup view for data population
            TextView time_wakeupTime = (TextView) convertView.findViewById(R.id.time_Wake_time);
            TextView time_duration = (TextView) convertView.findViewById(R.id.time_Duration);
            TextView time_days = (TextView) convertView.findViewById(R.id.time_Days);

            // Populate the data into the template view using the data object
            time_wakeupTime.setText(time.getWakeUp());
            time_duration.setText(String.valueOf(time.getDuration()));
            time_days.setText("days");
        }

        // Return the completed view to render on screen
        return convertView;

    }
}
