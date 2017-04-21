package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;



public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private TabsPagerAdapter adapter;
    private ActionBar actionBar;

    private String[] tabs = {"Alarms", "Stuff", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = prefs.getBoolean("firstStart", true);
        if (isFirstStart) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                    Intent i = new Intent(getApplicationContext(), Infopage.class);
                    startActivity(i);

                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);

                    e.apply();
                }
            });

            t.start();

        } else {
            setContentView(R.layout.activity_main);


        }

    }


    public void goToGallery(View view) {
        Intent intent = new Intent(getApplicationContext(), Gallery.class);
        startActivity(intent);
    }

    public void gotoAlarm(View view) {
        Intent openAlarmPage = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(openAlarmPage);

    }

    public void goToIntro(View view) {
        Intent i = new Intent(getApplicationContext(), Infopage.class);
        //pack stuff with it
        startActivity(i);
    }

}

