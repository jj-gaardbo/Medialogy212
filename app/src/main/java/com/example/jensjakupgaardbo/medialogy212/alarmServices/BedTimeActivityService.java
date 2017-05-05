package com.example.jensjakupgaardbo.medialogy212.alarmServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.example.jensjakupgaardbo.medialogy212.Alarm;
import com.example.jensjakupgaardbo.medialogy212.AlarmLocationListener;
import com.example.jensjakupgaardbo.medialogy212.BedTimeActivity;
import com.example.jensjakupgaardbo.medialogy212.tabbedMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BedTimeActivityService extends Service {

    private static final String TAG = "BEDTIMESERVICE";

    AlarmLocationListener locationListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Gson gson = new GsonBuilder().create();
        Alarm alarm = gson.fromJson(intent.getStringExtra("alarmString"), Alarm.class);

        Log.d(TAG, "OnStartCommand");

        locationListener = new AlarmLocationListener(getBaseContext(), "");
        locationListener.getLocation(getBaseContext());

        if(alarm.isInRange(locationListener.getLastLocation())){
            Intent dialogIntent = new Intent(this, BedTimeActivity.class);
            dialogIntent.putExtra("alarmString", gson.toJson(alarm));
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        } else {
            stopSelf();
            tabbedMain.cancelAlarms(getApplicationContext());
            tabbedMain.setAlarms(getApplicationContext());
        }
        locationListener.locationManager.removeUpdates(locationListener);
        locationListener = null;
        return START_NOT_STICKY;
    }

}