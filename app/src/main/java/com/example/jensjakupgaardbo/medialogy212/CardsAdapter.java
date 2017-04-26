package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
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
        nearMarkerTextView.setText("Near Marker");
        wakeUptime.setText(singleAlarm.getWakeTimeOfDay(2));
        gotoBedTextView.setText(singleAlarm.getBedTime(2));

        return customView;
    }
}
