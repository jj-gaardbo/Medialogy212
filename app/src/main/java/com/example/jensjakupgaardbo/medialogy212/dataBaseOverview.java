package com.example.jensjakupgaardbo.medialogy212;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class dataBaseOverview extends AppCompatActivity {

    EditText nameInput;
    EditText latInput;
    EditText lngInput;
    TextView dataViewer;
    AlarmDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_overview);

        nameInput = (EditText) findViewById(R.id.pinName);
        latInput = (EditText) findViewById(R.id.pinLat);
        lngInput = (EditText) findViewById(R.id.pinLng);
        dataViewer = (TextView) findViewById(R.id.dataViewer);
        dbHandler = new AlarmDBHandler(this,null,null,7);

        printDatabase();
    }

    public void addButtonClicked(View view){
        Alarm alarmToAdd = new Alarm(nameInput.getText().toString(), new LatLng(Double.parseDouble(latInput.getText().toString()), Double.parseDouble(lngInput.getText().toString())));
        dbHandler.addAlarm(alarmToAdd);
        printDatabase();
    }

    public void removeButtonClicked(View view){
        String deleteByName = nameInput.getText().toString();
        dbHandler.deleteAlarm(deleteByName);
        printDatabase();
    }

    public void printDatabase() {
        String dbString = dbHandler.databaseNamesToString();
        dataViewer.setText(dbString);
        nameInput.setText("");
        latInput.setText("50");
        lngInput.setText("20");
    }


}
