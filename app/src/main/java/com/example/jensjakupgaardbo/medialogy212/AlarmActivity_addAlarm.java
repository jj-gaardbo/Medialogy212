package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlarmActivity_addAlarm extends AppCompatActivity {

    private static final int SLEEP_DURATION_MIN_VALUE = 1;
    private static final int SLEEP_DURATION_MAX_VALUE = 12;
    private static final int SLEEP_DURATION_DEFAULT_VALUE = 7;
    boolean editing;
    Alarm parentAlarm;
    TimePicker wake_time;
    NumberPicker duration;
    Time eTime;
    List<ToggleButton> dayBtns = new ArrayList<>();
    boolean[] days = new boolean[7];
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_alarm);

        parentAlarm = (parentAlarm == null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : null;

        wake_time = (TimePicker) findViewById(R.id.wake_time);
        wake_time.setIs24HourView(true);
        wake_time.setHour(7);
        wake_time.setMinute(0);

        duration = (NumberPicker) findViewById(R.id.sleep_duration);
        duration.setMinValue(SLEEP_DURATION_MIN_VALUE);
        duration.setMaxValue(SLEEP_DURATION_MAX_VALUE);
        duration.setValue(SLEEP_DURATION_DEFAULT_VALUE);

        dayBtns.add((ToggleButton) findViewById(R.id.day_mon));
        dayBtns.add((ToggleButton) findViewById(R.id.day_tue));
        dayBtns.add((ToggleButton) findViewById(R.id.day_wed));
        dayBtns.add((ToggleButton) findViewById(R.id.day_thu));
        dayBtns.add((ToggleButton) findViewById(R.id.day_fri));
        dayBtns.add((ToggleButton) findViewById(R.id.day_sat));
        dayBtns.add((ToggleButton) findViewById(R.id.day_sun));

        editing = false;

        deleteBtn = (Button) findViewById(R.id.remove_time);
        deleteBtn.setVisibility(View.GONE);

        eTime = (Time) getIntent().getSerializableExtra("edit_time");
        if(eTime != null){
            deleteBtn.setVisibility(View.VISIBLE);
            editing = true;
            wake_time.setHour(Integer.parseInt(eTime.getWakeUp().substring(0,2)));
            wake_time.setMinute(Integer.parseInt(eTime.getWakeUp().substring(3,5)));
            duration.setValue(eTime.getDuration());
            days = eTime.getDays();
        }

        Button btnSaveTime = (Button) findViewById(R.id.save_time);

        //Listening to button event
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                saveTime();
            }
        });
        checkDays(false);

        if(editing){
            reEnableDays();
        }

    }

    //Check the days that the currently edited time has
    private void reEnableDays(){
        for(int i = 0; i < eTime.getDays().length; i++){
            if(eTime.days[i]) {
                dayBtns.get(i).setEnabled(true);
                dayBtns.get(i).setChecked(true);
            }
        }
    }

    private void checkDays(boolean update){

        //Create an array that holds the indexes of used days in the current alarm
        List<Integer> disableIndexes = new ArrayList<>();

        //Disable the days that are already used unless you are currently editing an existing time
        ArrayList<Time> times = parentAlarm.getTimes();
        for(int t = 0; t < times.size(); t++){
            boolean[] usedDays = times.get(t).getDays();
            for(int d = 0; d < usedDays.length; d++){
                if(usedDays[d]){
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
            if(update){
                days[i] = dayBtns.get(i).isChecked();
            }
        }

        if(disableIndexes.size() >= 7){
            parentAlarm.hasFullWeek = true;
        }

    }

    public void refreshDays(View view){
        for(int i = 0; i < dayBtns.size(); i++){
            if(dayBtns.get(i).isEnabled()){
                days[i] = dayBtns.get(i).isChecked();
            }
        }
    }

    private void saveTime(){
        checkDays(true);
        ArrayList<Time> times = parentAlarm.getTimes();
        Time time;
        if(editing){
            eTime.setWakeUp(String.format("%02d:%02d", wake_time.getHour(), wake_time.getMinute()));
            eTime.setDuration(duration.getValue());
            eTime.setDays(days);
            for(int i = 0; i < times.size(); i++) {
                if(eTime.getTimeID().equals(times.get(i).getTimeID())){
                    times.set(i, eTime);
                }
            }
        } else {
            time = new Time(String.format("%02d:%02d", wake_time.getHour(), wake_time.getMinute()), duration.getValue(), days);
            times.add(time);
        }
        Intent alarmScreen = new Intent(getApplicationContext(), AlarmActivity.class);
        alarmScreen.putExtra("activeAlarm", parentAlarm);
        startActivity(alarmScreen);
    }

    public void deleteTime(View view){
        ArrayList<Time> times = parentAlarm.getTimes();
        for(int i = 0; i < times.size(); i++) {
            if(eTime.getTimeID().equals(times.get(i).getTimeID())){
                times.remove(i);
                Intent alarmScreen = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(alarmScreen);
            }
        }
    }

}
