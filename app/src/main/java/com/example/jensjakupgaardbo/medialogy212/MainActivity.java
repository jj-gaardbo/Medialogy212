package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityVideoPage.class);

        startActivity(intent);
    }









    public void gotoAlarm(View view) {
        Intent openAlarmPage = new Intent(getApplicationContext(), AlarmActivity.class);
        //pack stuff with it
        startActivity(openAlarmPage);



    }
}

