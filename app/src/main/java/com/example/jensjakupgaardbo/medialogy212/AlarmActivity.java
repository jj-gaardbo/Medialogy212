package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    Alarm alarm;
    TimeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarm = (getIntent().getSerializableExtra("activeAlarm") != null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : new Alarm();

        Button btnNextScreen = (Button) findViewById(R.id.add_alarm);

        if(adapter == null){
            adapter = new TimeAdapter(this, alarm.getTimes());
        }
        ListView listView = (ListView) findViewById(R.id.timesList);
        listView.setAdapter(adapter);

        //Listening to button event
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
                addAlarmScreen.putExtra("activeAlarm", alarm);
                startActivity(addAlarmScreen);
            }
        });

    }

}
