package com.example.jensjakupgaardbo.medialogy212.alarmServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.example.jensjakupgaardbo.medialogy212.Alarm;
import com.example.jensjakupgaardbo.medialogy212.AlarmLocationListener;
import com.example.jensjakupgaardbo.medialogy212.WakeTimeActivity;
import com.example.jensjakupgaardbo.medialogy212.tabbedMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WakeUpAlarmService extends Service{

    private static final String TAG = "WAKEUPSERVICE";

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
            locationListener.locationManager.removeUpdates(locationListener);
            locationListener = null;
            Intent dialogIntent = new Intent(this, WakeTimeActivity.class);
            dialogIntent.putExtra("alarmString", gson.toJson(alarm));
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        } else {
            stopSelf();
            tabbedMain.cancelAlarms(getApplicationContext());
            tabbedMain.setAlarms(getApplicationContext());
        }
        return START_NOT_STICKY;
    }


}
