package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    Alarm alarm;
    EditText nameInput;
    TimeAdapter adapter;
    FloatingActionButton addTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarm = (getIntent().getSerializableExtra("activeAlarm") != null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : new Alarm();
        addTime = (FloatingActionButton) findViewById(R.id.add_alarm);
        nameInput = (EditText) findViewById(R.id.alarm_name);

        if(alarm.get_alarmname() != null){
            nameInput.setText(alarm.get_alarmname());
        }

        nameInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                nameInput.setCursorVisible(true);
                addTime.setVisibility(View.GONE);
                return false;
            }
        });
        nameInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(nameInput.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    alarm.set_alarmName(v.getText().toString());
                    nameInput.setCursorVisible(false);
                    addTime.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        checkForFullWeek();

        //Hide button if the whole week has been assigned an alarm
        if(alarm.hasFullWeek){
            addTime.setVisibility(View.GONE);
        } else {
            addTime.setVisibility(View.VISIBLE);
        }

        if(adapter == null){
            adapter = new TimeAdapter(this, alarm.getAlarmTimes());
        }
        ListView listView = (ListView) findViewById(R.id.timesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlarmTime alarmTime = (AlarmTime) adapterView.getItemAtPosition(i);
                openAddTime(alarmTime);
            }
        });

        //Listening to button event
        addTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                openAddTime();
            }
        });

    }

    //Run a check to see if the alarm has used up all the week days
    private void checkForFullWeek(){
        int countDays = 0;
        ArrayList<AlarmTime> alarmTimes = this.alarm.getAlarmTimes();
        for(int t = 0; t < alarmTimes.size(); t++){
            boolean[] usedDays = alarmTimes.get(t).getDays();
            for(int d = 0; d < usedDays.length; d++){
                if(usedDays[d]){
                    countDays++;
                }
            }
        }
        this.alarm.hasFullWeek = (countDays >= 7);
    }

    private void openAddTime(){
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        startActivity(addAlarmScreen);
    }

    private void openAddTime(AlarmTime alarmTime){
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        addAlarmScreen.putExtra("edit_time", alarmTime);
        startActivity(addAlarmScreen);
    }

    public void saveAlarm(View view){
        AlarmDBHandler alarmDBHandler = new AlarmDBHandler(getApplicationContext(), this.alarm.get_alarmname(), null, 8);
        alarmDBHandler.addAlarm(this.alarm);
        startActivity(new Intent(getApplicationContext(), dataBaseOverview.class));
    }

}
