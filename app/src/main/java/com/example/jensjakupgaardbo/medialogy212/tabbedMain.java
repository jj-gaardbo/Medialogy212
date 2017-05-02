package com.example.jensjakupgaardbo.medialogy212;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

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
    public static Alarm activeAlarm = null;
    private PendingIntent alarmIntent;
    private ArrayList<PendingIntent> alrmPendIntents = new ArrayList<>();
    FloatingActionButton fab;
    ListAdapter cardAdapter;


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

        SharedPreferences prefs = getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = prefs.getBoolean("firstStart", true);
        if (isFirstStart) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences getPrefs = getDefaultSharedPreferences(getBaseContext());

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

            updateAlarms();
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

    public static LatLng readLastLoc(Context context) {
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        String locationString = prefs.getString("lastLocation", "noLastLocation");
        if (locationString.equals("noLastLocation")) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(locationString, LatLng.class);
    }




    private void updateAlarms() {
        SharedPreferences prefs = getDefaultSharedPreferences(getBaseContext());
        LatLng location = tabbedMain.readLastLoc(this);
        Alarm currentAlarm = getActiveAlarm();
        if (currentAlarm == null){
            setNextAlarm(getFirstAlarmInRange());
        }
    }



    public float compareLatLngs(LatLng location1, LatLng location2) {
        Location loc1 = latLngToLocation(location1);
        Location loc2 = latLngToLocation(location2);
        return loc1.distanceTo(loc2);

    }


    public Location latLngToLocation(LatLng position) {
        Location location = new Location("");
        location.setLatitude(position.latitude);
        location.setLongitude(position.longitude);
        return location;
    }

    public void setNextAlarm(Alarm alarmToSet) {//sets an inexact alarm that goes off half an hour before either wake or gotoBed
        Calendar rightNow = Calendar.getInstance();
        if(alarmToSet == null){
            return;
        }
        int today = getDayOfWeek();
        int tomorrow = getDayOfWeek() + 1;
        if (tomorrow > 7) tomorrow = 0;

        if(isAfterAlarms(rightNow,alarmToSet ,today)){
            setAlarm(alarmToSet,rightNow, tomorrow);
        }else{
            setAlarm(alarmToSet, rightNow ,today);
        }

    }

    public void setAlarm(Alarm alarmToSet,Calendar time, int day){
        Intent intent = new Intent(this, AlarmReceiver.class);
        String bedOrWake = "";
        boolean isBedTime = true;
        String[] wakeTime =  alarmToSet.getWakeTimeOfDay(day).split(":");
        String[] bedTime = alarmToSet.getBedTime(day).split(":");
        if(Integer.parseInt(bedTime[0]) < 12 ){
            bedTime[0] =  String.valueOf((Integer.parseInt(bedTime[0]) + 24));
        }
        wakeTime[0] =  String.valueOf((Integer.parseInt(wakeTime[0]) + 24));

        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);

        //figure out which type of alarm to set
        if (hour < Integer.parseInt(bedTime[0])){
                isBedTime = true;

        } else if ( hour == Integer.parseInt(bedTime[0]) && minute < Integer.parseInt(bedTime[1])) {
                isBedTime = true;
        }else{
                isBedTime = false;
        }




        int alarmHour;
        int alarmMin;

        if(Integer.parseInt(bedTime[0]) > 24 ){
            bedTime[0] =  String.valueOf((Integer.parseInt(bedTime[0]) -24));
        }
        wakeTime[0] =  String.valueOf((Integer.parseInt(wakeTime[0]) -24));



        if(isBedTime){
            alarmHour =  Integer.parseInt(bedTime[0]);
            alarmMin = Integer.parseInt(bedTime[1]);
        }else{
            alarmHour =  Integer.parseInt(wakeTime[0]);
            alarmMin = Integer.parseInt(wakeTime[1]);
        }




        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        time.set(Calendar.HOUR_OF_DAY, alarmHour);
        time.set(Calendar.MINUTE,alarmMin);
        int convertToCallenderDay = day +2;
        if(convertToCallenderDay > 7){
            convertToCallenderDay = 1;
        }
        time.set(Calendar.DAY_OF_WEEK, convertToCallenderDay);
        time.set(Calendar.SECOND, 0);

        intent.putExtra("isBedTime", isBedTime);
        alarmIntent = PendingIntent.getBroadcast(tabbedMain.this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), alarmIntent);

        cancelAlarms();

        Toast.makeText(this, "alarm set for day: " + day + " , and is set to go off on :" + alarmHour + " hour and " + alarmMin +" and isBedTime is :" + isBedTime , Toast.LENGTH_LONG).show();

        setActiveAlarm(alarmToSet);
        alrmPendIntents.add(alarmIntent);

        if(isBedTime){
            //set anotherAlarm
            //save Another notification
        }

    }

    public boolean isAfterAlarms(Calendar rightNow, Alarm alarm, int day){
        if(rightNow.get(Calendar.HOUR_OF_DAY)> Integer.parseInt(alarm.getWakeTimeOfDay(day).split(":")[0]) ){
            return true;
        }
        return false;
    }

    static public int getDayOfWeek() {
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
        return dayOfWeek;
    }

    public Alarm getFirstAlarmInRange() {
        //returns the nearest alarm in range, returns null if no alarms are in range
        //first checks if currentalarm is in range
        LatLng currentLocation = tabbedMain.readLastLoc(this);

        if(currentLocation != null){

            //if not check all alarms in database and return the first in range
            AlarmDBHandler dbHandler = new AlarmDBHandler(this, null, null, DATABASE_VERSION);
            ArrayList<Alarm> alarms = dbHandler.getAlarms();
            if (alarms != null) {
                for (Alarm a : alarms) {
                    if (a.get_latlng() != null && compareLatLngs(a.get_latlng(), currentLocation) < searchRadius) {
                        return a;
                    }
                }
            }

        }

        return null;
    }


    public void setActiveAlarm(Alarm alarm){
        //saves active alarm
        activeAlarm = alarm;

    }
    public Alarm getActiveAlarm() {
        return activeAlarm;
    }

    public void cancelAlarms(){
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        String activeAlarm = "no Alarm";
        for(PendingIntent p: alrmPendIntents){
            alarmManager.cancel(p);
        }

        Alarm alarm = getActiveAlarm();

        if(alarm != null){
            activeAlarm = alarm.get_alarmname();
        }

        setActiveAlarm(null);
        Toast.makeText(this,"Canceled alarm: "  ,Toast.LENGTH_LONG).show();

    }
}









