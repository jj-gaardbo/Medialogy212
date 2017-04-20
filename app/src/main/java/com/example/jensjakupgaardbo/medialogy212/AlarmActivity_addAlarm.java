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
import java.util.List;

public class AlarmActivity_addAlarm extends AppCompatActivity {

    private static final int SLEEP_DURATION_MIN_VALUE = 1;
    private static final int SLEEP_DURATION_MAX_VALUE = 12;
    private static final int SLEEP_DURATION_DEFAULT_VALUE = 7;
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
        duration.setValue(SLEEP_DURATION_DEFAULT_VALUE);

        Button btnSaveTime = (Button) findViewById(R.id.save_time);

        //Listening to button event
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                saveTime();
            }
        });
        checkDays();
    }

    private void checkDays(){
        List<ToggleButton> dayBtns = new ArrayList<>();
        dayBtns.add((ToggleButton) findViewById(R.id.day_mon));
        dayBtns.add((ToggleButton) findViewById(R.id.day_tue));
        dayBtns.add((ToggleButton) findViewById(R.id.day_wed));
        dayBtns.add((ToggleButton) findViewById(R.id.day_thu));
        dayBtns.add((ToggleButton) findViewById(R.id.day_fri));
        dayBtns.add((ToggleButton) findViewById(R.id.day_sat));
        dayBtns.add((ToggleButton) findViewById(R.id.day_sun));

        //Disable the days that are already used
        List<Integer> disableIndexes = new ArrayList<>();
        ArrayList<Time> times = parentAlarm.getTimes();
        for(int t = 0; t < times.size(); t++){
            boolean[] days = times.get(t).getDays();
            for(int d = 0; d < days.length; d++){
                if(days[d]){
                    disableIndexes.add(d);
                }
            }
        }

        for(int i = 0; i < dayBtns.size(); i++){
            if(disableIndexes.contains(i)){
                dayBtns.get(i).setEnabled(false);
            } else if(dayBtns.get(i).isChecked()){
                disableIndexes.add(i);
                dayBtns.get(i).setEnabled(false);
            }
            days[i] = dayBtns.get(i).isChecked();
        }

        if(disableIndexes.size() >= 7){
            parentAlarm.hasFullWeek = true;
        }

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
