package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class AlarmActivity_addAlarm extends AppCompatActivity {

    private static final int SLEEP_DURATION_MIN_VALUE = 1;
    private static final int SLEEP_DURATION_MAX_VALUE = 12;
    Alarm parentAlarm;
    TimePicker wake_time;
    NumberPicker duration;
    boolean[] days = new boolean[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_alarm);

        parentAlarm = (parentAlarm == null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : null;

        wake_time = (TimePicker) findViewById(R.id.wake_time);
        wake_time.setIs24HourView(true);

        duration = (NumberPicker) findViewById(R.id.sleep_duration);
        duration.setMinValue(SLEEP_DURATION_MIN_VALUE);
        duration.setMaxValue(SLEEP_DURATION_MAX_VALUE);

        Button btnSaveTime = (Button) findViewById(R.id.save_time);

        //Listening to button event
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                saveTime();
            }
        });

    }

    private void checkDays(){
        days[0] = ((ToggleButton) findViewById(R.id.day_mon)).isChecked();
        days[1] = ((ToggleButton) findViewById(R.id.day_tue)).isChecked();
        days[2] = ((ToggleButton) findViewById(R.id.day_wed)).isChecked();
        days[3] = ((ToggleButton) findViewById(R.id.day_thu)).isChecked();
        days[4] = ((ToggleButton) findViewById(R.id.day_fri)).isChecked();
        days[5] = ((ToggleButton) findViewById(R.id.day_sat)).isChecked();
        days[6] = ((ToggleButton) findViewById(R.id.day_sun)).isChecked();
    }

    private void saveTime(){
        checkDays();
        Time time = new Time(String.format("%02d:%02d", wake_time.getHour(), wake_time.getMinute()), duration.getValue(), days);
        ArrayList<Time> times = parentAlarm.getTimes();
        times.add(time);
        Intent alarmScreen = new Intent(getApplicationContext(), AlarmActivity.class);
        alarmScreen.putExtra("activeAlarm", parentAlarm);
        startActivity(alarmScreen);
    }
}
