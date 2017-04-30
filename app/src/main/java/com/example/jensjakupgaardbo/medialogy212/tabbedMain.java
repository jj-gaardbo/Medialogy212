package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

public class tabbedMain extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    final static public int DATABASE_VERSION = 11;
    final int searchRadius = 150;
    final String currentAlarmName = "";

    FloatingActionButton fab;
    ListAdapter cardAdapter;
    //ArrayList<Alarm> alarms;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(getApplicationContext(), AlarmLocationService.class));
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

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
            setContentView(R.layout.activity_tabbed_main);
            fab = (FloatingActionButton) findViewById(R.id.fabAdd);
            AlarmDBHandler dbHandler = new AlarmDBHandler(this, null, null, DATABASE_VERSION);
            ArrayList<Alarm> nonSortedAlarms = dbHandler.getAlarms();

            final ArrayList<Alarm> alarms = nonSortedAlarms;
            if (cardAdapter == null) {
                cardAdapter = new CardsAdapter(this, alarms);
            }
            ListView editList = (ListView) findViewById(R.id.listOfCards);
            editList.setAdapter(cardAdapter);
            editList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Alarm alarm = alarms.get(position);
                                                    Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                                                    intent.putExtra("activeAlarm", alarm);
                                                    if (alarm.get_latlng() != null) {
                                                        intent.putExtra("activeAlarmLocation", Alarm.getConvertedLocation(alarm.get_latlng()));
                                                        alarm.set_latlng(null);
                                                    }
                                                    intent.putExtra("editing", true);
                                                    startActivity(intent);
                                                }
                                            }
            );
        }
    }

    public void goToGallery(View view) {
        Intent intent = new Intent(getApplicationContext(), Gallery.class);
        startActivity(intent);
    }

    public void goToAlarm(View view) {
        Intent openAlarmPage = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(openAlarmPage);
    }

    public void goToIntro(View view) {
        Intent i = new Intent(getApplicationContext(), Infopage.class);
        startActivity(i);
    }


/*

    private void updateAlarms(){

        Alarm closestAlarm = getFirstAlarmInRange();

    }
*/

        public float compareLatLngs(LatLng location1, LatLng location2){
            Location loc1 = latLngToLocation(location1);
            Location loc2 = latLngToLocation(location2);
            return loc1.distanceTo(loc2);

        }


        public Location latLngToLocation(LatLng position){
        Location location = new Location("");
        location.setLatitude(position.latitude);
        location.setLongitude(position.longitude);
        return location;
        }

    public void setHalfHourBefore(Alarm alarmToSet){//sets an inexact alarm that goes off half an hour before either wake or gotoBed
        Calendar rightNow = Calendar.getInstance();
        AlarmDBHandler dbHandler = new AlarmDBHandler(this,null,null,DATABASE_VERSION);


    }

    static public int getDayOfWeek(){
        Calendar rightNow = Calendar.getInstance();
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeek = 6;
                break;

            case Calendar.MONDAY:
                dayOfWeek = 0;
                break;

            case Calendar.TUESDAY:
                dayOfWeek = 1;
                break;

            case Calendar.WEDNESDAY:
                dayOfWeek = 2;
                break;

            case Calendar.THURSDAY:
                dayOfWeek = 3;
                break;

            case Calendar.FRIDAY:
                dayOfWeek = 4;
                break;

            case Calendar.SATURDAY:
                dayOfWeek = 5;
                break;
        }
        return  dayOfWeek;
    }

    public Alarm getFirstAlarmInRange(LatLng currentLocation){
        //returns the nearest alarm in range, returns null if no alarms are in range
        AlarmDBHandler dbHandler = new AlarmDBHandler(this,null,null,DATABASE_VERSION);
        ArrayList<Alarm> alarms =  dbHandler.getAlarms();
        if(alarms == null){
            return null;
        }
        for(Alarm a : alarms){
            if(compareLatLngs(a.get_latlng(),currentLocation)<searchRadius){
                return a;
            }
        }

        return null;
    }
}







