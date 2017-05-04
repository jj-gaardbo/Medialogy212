package com.example.jensjakupgaardbo.medialogy212.alarmServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import com.example.jensjakupgaardbo.medialogy212.Alarm;
import com.example.jensjakupgaardbo.medialogy212.AlarmLocationListener;
import com.example.jensjakupgaardbo.medialogy212.R;
import com.example.jensjakupgaardbo.medialogy212.tabbedMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NotificationService extends Service {

    private static final String TAG = "NOTIFICATIONSERVICE";

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
            Notification.Builder mBuilder =
                    new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.moon)
                            .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.moon))
                            .setContentTitle("Approaching bed time")
                            .setContentText("It is almost bed time.")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setStyle(new Notification.BigTextStyle()
                                    .bigText("In order to get the appropriate amount of sleep, you have to go to bed in 30 minutes."));

            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(0, mBuilder.build());
        } else {
            stopSelf();
            tabbedMain.cancelAlarms(getApplicationContext());
            tabbedMain.setAlarms(getApplicationContext());
        }
        return START_NOT_STICKY;
    }
}
