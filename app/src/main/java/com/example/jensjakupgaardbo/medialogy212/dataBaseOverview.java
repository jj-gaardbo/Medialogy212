package com.example.jensjakupgaardbo.medialogy212;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class dataBaseOverview extends AppCompatActivity {

    EditText dataInput;
    EditText dBViewer;
    AlarmDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_overview);

        dataInput = (EditText) findViewById(R.id.editText);
        dBViewer = (EditText) findViewById(R.id.displayDBText);
        dbHandler = new AlarmDBHandler(this,null,null,2);
        printDatabase();
    }

    public void addButtonClicked(View view){
        Alarm alarmToAdd = new Alarm(dataInput.getText().toString());
        dbHandler.addAlarm(alarmToAdd);
        printDatabase();
    }

    public void removeButtonClicked(View view){
        String deleteByName = dataInput.getText().toString();
        dbHandler.deleteAlarm(deleteByName);
        printDatabase();
    }

    public void printDatabase() {
        String dbString = dbHandler.databaseToString();
        dBViewer.setText(dbString);
        dataInput.setText("");
    }


}
