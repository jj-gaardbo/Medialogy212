package com.example.jensjakupgaardbo.medialogy212;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class tabbedMain extends AppCompatActivity {

    
    //// TODO: 03-05-2017  add week switching functionality to sunday 
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public static final int SEARCH_RADIUS = 500;

    final static public int DATABASE_VERSION = 12;

    public static boolean hasLocationPermission = false;

    public static ArrayList<PendingIntent> alarmPendingIntents = new ArrayList<>();
    FloatingActionButton fab;
    ListAdapter cardAdapter;

    public static AlarmLocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                tabbedMain.hasLocationPermission = true;
                locationListener = new AlarmLocationListener(this, "gps");
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

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelAlarms();
        setAlarms(this);
    }


    public void goToAlarm(View view) {
        Intent openAlarmPage = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(openAlarmPage);
    }

    public void goToIntro(View view) {
        Intent i = new Intent(getApplicationContext(), Infopage.class);
        startActivity(i);
    }

    public static void setAlarms(Context context){
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        AlarmDBHandler dbHandler = new AlarmDBHandler(context, null, null, DATABASE_VERSION);
        ArrayList<Alarm> alarms = dbHandler.getAlarms();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ArrayList<Calendar>  times;

        String methodInfo = "";
        for(Alarm a: alarms){
            boolean isFirst = true;
            times = Alarm.getNextAlarms(context, a);
            if( times != null) {
                for (Calendar c : times) {
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    Gson gson = new GsonBuilder().create();
                    intent.putExtra("alarmString", gson.toJson(a));
                    if (isFirst) {
                        intent.putExtra("isBedTime", false);
                        methodInfo += "waktime: ";
                        isFirst = false;
                    } else {
                        intent.putExtra("isBedTime", true);
                        methodInfo += "Bedtime: ";
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    final int SDK_INT = Build.VERSION.SDK_INT;
                    if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    }
                    else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    }
                    else if (SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    }
                    alarmPendingIntents.add(pendingIntent);
                    methodInfo += "alarm set: " + dayFormat.format(c.getTime()) + "  at :     " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + "\n";

                }

            }
        }
        Toast.makeText(context, methodInfo, Toast.LENGTH_LONG).show();
    }

    public void cancelAlarms(){
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        for(PendingIntent p: alarmPendingIntents){
            alarmManager.cancel(p);
        }
    }
}









