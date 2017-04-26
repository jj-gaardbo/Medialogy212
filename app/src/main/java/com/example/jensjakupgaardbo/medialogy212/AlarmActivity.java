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

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    boolean editing;
    String oldAlarmName = "";

    Alarm alarm;
    EditText nameInput;
    TimeAdapter adapter;
    FloatingActionButton addTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        editing = getIntent().getBooleanExtra("editing", false);
        alarm = (getIntent().getSerializableExtra("activeAlarm") != null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : new Alarm();
        if(getIntent().getStringExtra("editing_name") != null){
            oldAlarmName = getIntent().getStringExtra("editing_name");
        } else if(editing){
            oldAlarmName = alarm.get_alarmname();
        }
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
                    nameInput.setCursorVisible(false);
                    addTime.setVisibility(View.VISIBLE);
                }
                alarm.set_alarmName(v.getText().toString());
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
                if(editing){
                    openAddTime(alarm);
                } else {
                    openAddTime();
                }
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

    //Open the add time activity when creating a new alarm
    private void openAddTime(){
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        startActivity(addAlarmScreen);
    }

    //Open the add time activity when the alarm is being edited
    private void openAddTime(Alarm alarm){
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        addAlarmScreen.putExtra("editing_parent", editing);
        addAlarmScreen.putExtra("editing_name", oldAlarmName);
        startActivity(addAlarmScreen);
    }

    //Open the add time activity when alarm already contains other times
    private void openAddTime(AlarmTime alarmTime){
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        addAlarmScreen.putExtra("edit_time", alarmTime);
        addAlarmScreen.putExtra("editing_parent", editing);
        addAlarmScreen.putExtra("editing_name", oldAlarmName);
        startActivity(addAlarmScreen);
    }

    //Checks if the alarm has the required data for saving
    public String isValid(){
        if(this.alarm.get_alarmname() == null) {
            return "missing_alarm_name";
        } else if(this.alarm.getAlarmTimes().size() <= 0){
            return "no_times_set";
        } else if(!editing && (new AlarmDBHandler(getApplicationContext(), "", null, 8)).alarmExists(this.alarm.get_alarmname())){
            return "alarm_name_exists";
        } else {
            return "valid";
        }
    }

    public void cancelAlarm(View view){
        startActivity(new Intent(getApplicationContext(), CardsTest.class));
    }

    public void saveAlarm(View view){
        String errorString = "";
        switch(isValid()){
            case "valid":
                AlarmDBHandler alarmDBHandler = new AlarmDBHandler(getApplicationContext(), this.alarm.get_alarmname(), null, 8);
                if(editing){
                    alarmDBHandler.updateAlarm(oldAlarmName, this.alarm);
                } else {
                    alarmDBHandler.addAlarm(this.alarm);
                }
                startActivity(new Intent(getApplicationContext(), CardsTest.class));
                break;

            case "no_times_set":
                errorString = "The alarm needs at least one time to be set";
                break;

            case "missing_alarm_name":
                errorString = "The alarm needs a name";
                break;

            case "alarm_name_exists":
                errorString = "The name already exists. Please add a unique alarm name.";
                break;
        }

        if(!errorString.equals("")){
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        }

    }

}
