package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmActivity_addAlarm extends AppCompatActivity {

    TimePicker tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_alarm);

        tp = (TimePicker) findViewById(R.id.timePicker);
        tp.setIs24HourView(true);

        Button btnSaveTime = (Button) findViewById(R.id.save_time);

        //Listening to button event
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                Intent alarmScreen = new Intent(getApplicationContext(), AlarmActivity.class);
                alarmScreen.putExtra("pickedTime", tp.getHour()+" - "+tp.getMinute());
                startActivity(alarmScreen);
            }
        });

    }
}
