package com.example.jensjakupgaardbo.medialogy212;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CardsTest extends AppCompatActivity {

    ListAdapter cardAdapter;
    ArrayList<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_test);
        AlarmDBHandler dbHandler = new AlarmDBHandler(this,null,null,11);
        ArrayList<Alarm> nonSortedAlarms = dbHandler.getAlarms();
        /// TODO: 27-04-2017 write method for sorting alarms based on something
        Collections.sort(nonSortedAlarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm o1, Alarm o2) {
                return 0;
            }
        });

        final ArrayList<Alarm> alarms = nonSortedAlarms;

        if(cardAdapter == null){
            cardAdapter = new CardsAdapter(this,alarms);
        }
        ListView editList = (ListView) findViewById(R.id.cardListView);
        //
        editList.setAdapter(cardAdapter);
        editList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarm =  alarms.get(position);
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                intent.putExtra("activeAlarm", alarm);
                if(alarm.get_latlng() != null){
                    intent.putExtra("activeAlarmLocation", Alarm.getConvertedLocation(alarm.get_latlng()));
                    alarm.set_latlng(null);
                }
                intent.putExtra("editing", true);
                startActivity(intent);
            }
        }
       );

    }


    public void goToAddAlarm(View view){
        startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
    }
    public void gotoDatabaseViewer(View view) {
        Intent gotoDatabaseActivity = new Intent(this, dataBaseOverview.class);
        this.startActivity(gotoDatabaseActivity);
    }
    public void setAlarm(View view){

        PendingIntent pendingIntent;

        Alarm closeAlarm = getClosestAlarm();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 600;

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(CardsTest.this, 0, alarmIntent, 0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

    }

    private Alarm getClosestAlarm() {
        //// TODO: 27-04-2017       implement location stuff to compare latLng object for each alarm in the cardsAdapter
        LatLng lkl = new LatLng(45,45);
        float distance = 12000;
        if (!cardAdapter.isEmpty()) return (Alarm) cardAdapter.getItem(0);
        /*
        {
            int itemNr = cardAdapter.getCount();

            for(int i = 0; i < itemNr; i++){
                Alarm singleAlarm = (Alarm) cardAdapter.getItem(i);
                distance = singleAlarm.get_latlng().;
                if(distance)


            }

        }
        */
        return new Alarm();
    }
}
