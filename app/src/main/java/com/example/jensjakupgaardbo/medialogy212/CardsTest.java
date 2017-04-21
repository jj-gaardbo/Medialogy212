package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardsTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_test);
        AlarmDBHandler dbHandler = new AlarmDBHandler(this,null,null,7);
        final ArrayList<Alarm> alarms = dbHandler.getAlarms();

        ListAdapter cardAdapter = new CardsAdapter(this,alarms);
        ListView editList = (ListView) findViewById(R.id.cardListView);
        editList.setAdapter(cardAdapter);
        editList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value =  alarms.get(position).get_alarmname();;
                Toast.makeText(CardsTest.this, value, Toast.LENGTH_SHORT).show();

            }
        }
       );

    }

    public void gotoDatabaseViewer(View view) {
        Intent gotoDatabaseActivity = new Intent(this, dataBaseOverview.class);
        this.startActivity(gotoDatabaseActivity);

    }
}
