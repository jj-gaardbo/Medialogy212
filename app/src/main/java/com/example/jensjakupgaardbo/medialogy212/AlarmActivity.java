package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Button btnNextScreen = (Button) findViewById(R.id.add_alarm);

        //Listening to button event
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                Intent nextScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
                startActivity(nextScreen);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onPause();
        try{
            String time = getIntent().getStringExtra("pickedTime");
            TextView tv = (TextView) findViewById(R.id.picked_time);
            tv.setText(time);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
