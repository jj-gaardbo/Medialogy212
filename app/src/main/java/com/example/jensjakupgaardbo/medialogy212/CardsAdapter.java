package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
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
        ImageView cardImage = (ImageView) customView.findViewById(R.id.singleCardImageView);
        TextView nearMarkerTextView = (TextView) customView.findViewById(R.id.nearMarkerTextVIew);
        TextView activeMarkerTextView = (TextView) customView.findViewById(R.id.markerActiveTextView);
        TextView wakeUptime = (TextView) customView.findViewById(R.id.wakeUpTextView);

        Switch activeSwitch = (Switch) customView.findViewById(R.id.activeSwitch) ;


        alarmTextView.setText((CharSequence) singleAlarm.get_alarmname());
        cardImage.setImageResource(R.drawable.card);
        nearMarkerTextView.setText("Near Marker");
        if(singleAlarm.isActive()){
            activeMarkerTextView.setText("Active");
            activeSwitch.toggle();
        }else{
            activeMarkerTextView.setText("deactive");
        }
        wakeUptime.setText(singleAlarm.getWakeTimeOfDay(2));

        return customView;
    }
}
