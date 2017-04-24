package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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
        dbHandler = new AlarmDBHandler(this,null,null,8);

        printDatabase();
    }

    public void addButtonClicked(View view){
        Alarm alarmToAdd = new Alarm(nameInput.getText().toString(), new LatLng(Double.parseDouble(latInput.getText().toString()), Double.parseDouble(lngInput.getText().toString())));
        dbHandler.addAlarm(alarmToAdd);
        printDatabase();
        /*nameInput.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
    }

    public void removeButtonClicked(View view){
        String deleteByName = nameInput.getText().toString();
        dbHandler.deleteAlarm(deleteByName);
        printDatabase();
    }

    public void printDatabase() {
        ArrayList<String> dbString = dbHandler.getAlarmNames();
        String inString = "";
        for(String i : dbString){
            inString += i + "\n";
        }


        dataViewer.setText(inString);
        nameInput.setText("");
        latInput.setText("50");
        lngInput.setText("20");
    }


    public void gotoCardsActivity(View view) {
        Intent gotoCards = new Intent(this, CardsTest.class);
        this.startActivity(gotoCards);
    }
}
