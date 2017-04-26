package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity_addAlarm extends AppCompatActivity {

    private static final int SLEEP_DURATION_MIN_VALUE = 1;
    private static final int SLEEP_DURATION_MAX_VALUE = 12;
    private static final int SLEEP_DURATION_DEFAULT_VALUE = 7;
    boolean editing;
    Alarm parentAlarm;
    TimePicker wake_time;
    NumberPicker duration;
    AlarmTime eAlarmTime;
    List<ToggleButton> dayBtns = new ArrayList<>();
    boolean[] days = new boolean[7];
    Button deleteBtn;
    boolean editing_parent;
    String editing_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_alarm);

        parentAlarm = (parentAlarm == null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : null;

        editing_parent = getIntent().getBooleanExtra("editing_parent", false);
        editing_name = getIntent().getStringExtra("editing_name");

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

        eAlarmTime = (AlarmTime) getIntent().getSerializableExtra("edit_time");
        if(eAlarmTime != null){
            deleteBtn.setVisibility(View.VISIBLE);
            editing = true;
            wake_time.setHour(Integer.parseInt(eAlarmTime.getWakeUp().substring(0,2)));
            wake_time.setMinute(Integer.parseInt(eAlarmTime.getWakeUp().substring(3,5)));
            duration.setValue(eAlarmTime.getDuration());
            days = eAlarmTime.getDays();
        }

        Button btnSaveTime = (Button) findViewById(R.id.save_time);

        //Listening to button event
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                saveTime();
            }
        });
        checkDays();

        if(editing){
            reEnableDays();
        }

    }

    //Check the days that the currently edited time has
    private void reEnableDays(){
        for(int i = 0; i < eAlarmTime.getDays().length; i++){
            if(eAlarmTime.days[i]) {
                dayBtns.get(i).setEnabled(true);
                dayBtns.get(i).setChecked(true);
            }
        }
    }

    private void checkDays(){

        //Create an array that holds the indexes of used days in the current alarm
        List<Integer> disableIndexes = new ArrayList<>();

        //Disable the days that are already used unless you are currently editing an existing time
        ArrayList<AlarmTime> alarmTimes = parentAlarm.getAlarmTimes();
        for(int t = 0; t < alarmTimes.size(); t++){
            boolean[] usedDays = alarmTimes.get(t).getDays();
            for(int d = 0; d < usedDays.length; d++){
                if(usedDays[d]){
                    disableIndexes.add(d);
                }
            }
        }

        for(int i = 0; i < dayBtns.size(); i++){
            if(disableIndexes.contains(i)){
                dayBtns.get(i).setEnabled(false);
            }
            else if(dayBtns.get(i).isChecked()){
                disableIndexes.add(i);
                dayBtns.get(i).setEnabled(false);
            }
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
        checkDays();
        ArrayList<AlarmTime> alarmTimes = parentAlarm.getAlarmTimes();
        AlarmTime alarmTime;
        if(editing){
            eAlarmTime.setWakeUp(String.format("%02d:%02d", wake_time.getHour(), wake_time.getMinute()));
            eAlarmTime.setDuration(duration.getValue());
            eAlarmTime.setDays(days);
            for(int i = 0; i < alarmTimes.size(); i++) {
                if(eAlarmTime.getTimeID().equals(alarmTimes.get(i).getTimeID())){
                    alarmTimes.set(i, eAlarmTime);
                }
            }
        } else {
            alarmTime = new AlarmTime(String.format("%02d:%02d", wake_time.getHour(), wake_time.getMinute()), duration.getValue(), days);
            alarmTimes.add(alarmTime);
        }
        goToAlarmPage();
    }

    public void deleteTime(View view){
        ArrayList<AlarmTime> alarmTimes = parentAlarm.getAlarmTimes();
        for(int i = 0; i < alarmTimes.size(); i++) {
            if(eAlarmTime.getTimeID().equals(alarmTimes.get(i).getTimeID())){
                alarmTimes.remove(i);
                goToAlarmPage();
            }
        }
    }

    private void goToAlarmPage(){
        Intent alarmScreen = new Intent(getApplicationContext(), AlarmActivity.class);
        alarmScreen.putExtra("activeAlarm", parentAlarm);
        alarmScreen.putExtra("editing", editing_parent);
        alarmScreen.putExtra("editing_name", editing_name);

        startActivity(alarmScreen);
    }

}
