package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class CardsTest extends AppCompatActivity {

    ListAdapter cardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_test);
        AlarmDBHandler dbHandler = new AlarmDBHandler(this,null,null,11);
        final ArrayList<Alarm> alarms = dbHandler.getAlarms();


        if(cardAdapter == null){
            cardAdapter = new CardsAdapter(this,alarms);
        }
        ListView editList = (ListView) findViewById(R.id.cardListView);
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
}
