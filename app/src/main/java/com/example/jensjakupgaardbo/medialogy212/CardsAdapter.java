package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class CardsAdapter extends ArrayAdapter<Alarm> {
    public CardsAdapter(@NonNull Context context, ArrayList<Alarm> alarms) {
        super(context, R.layout.card_row ,alarms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.card_row, parent, false);
        Alarm singleAlarm = getItem(position);
        TextView alarmTextView = (TextView) customView.findViewById(R.id.singleCardTextView);

        TextView nearMarkerTextView = (TextView) customView.findViewById(R.id.nearMarkerTextVIew);
        TextView wakeUptime = (TextView) customView.findViewById(R.id.wakeUpTextView);
        TextView gotoBedTextView  = (TextView) customView.findViewById(R.id.gotoBedTextView);

        alarmTextView.setText( singleAlarm.get_alarmname());

        Location location = tabbedMain.locationListener.getLastLocation();
        if(location != null){
            Location alarmLocation = singleAlarm.getAlarmLocation();
            float distance = alarmLocation.distanceTo(location);
            String inRange = "no";
            if(distance < tabbedMain.SEARCH_RADIUS){
                inRange = "yes";
            }
            nearMarkerTextView.setText("Near Marker : "+inRange);
        }
        ArrayList<String> times = singleAlarm.getNextAlarmTimes();

        if(times != null){
            wakeUptime.setText(times.get(0));

            if(times.size()>1){
                gotoBedTextView.setText(times.get(1));
                gotoBedTextView.setTextColor(Color.WHITE);
            }else{
                gotoBedTextView.setText("past bed time");
                wakeUptime.setTextColor(Color.WHITE);
            }

        }else{
            wakeUptime.setText("no alarm coming up");

        }

        return customView;
    }
}
