package com.example.jensjakupgaardbo.medialogy212;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver{

    AlarmLocationListener locListener;
    private static final int SEARCH_RADIUS = 150;

    @Override
    public void onReceive(Context context, Intent intent) {
        Gson gson = new GsonBuilder().create();
        Alarm alarm = gson.fromJson(intent.getStringExtra("alarmString"), Alarm.class);

        //Check if the user is within range of the alarms location
        if(!isInRange(context, alarm)){
            return;
        }

        boolean isBedTime = intent.getExtras().getBoolean("isBedTime", false);
        if (isBedTime) {
            triggerBedtimeNotification(context);
        } else {
            triggerWakeAlarmActivity(context);
        }
    }

    public boolean isInRange(Context context, Alarm alarm){
        if(!tabbedMain.hasLocationPermission || alarm == null){
            return false;
        }
        locListener = new AlarmLocationListener(context, "gps");
        Location deviceLocation = locListener.getLastLocation();
        Location alarmLocation = getAlarmLocation(alarm);
        float distance = alarmLocation.distanceTo(deviceLocation);
        return (distance < SEARCH_RADIUS);
    }

    public Location getAlarmLocation(Alarm alarm){
        LatLng alarmCoords = alarm.get_latlng();
        Location alarmLocation = new Location("");
        alarmLocation.setLatitude(alarmCoords.latitude);
        alarmLocation.setLongitude(alarmCoords.longitude);
        return alarmLocation;
    }

    public void triggerBedtimeNotification(Context context){
        Intent resultIntent = new Intent(context, tabbedMain.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.moon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.moon))
                        .setContentTitle("Sleep time")
                        .setContentText("Go to sleep bitch! Die motherfucker, die!")
                        .setContentIntent(resultPendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setStyle(new Notification.BigTextStyle()
                            .bigText("In order to get your chosen amount of sleep before tomorrow, you have to go to bed now. Motherfucker!"));

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(0, mBuilder.build());
    }

    public void triggerWakeAlarmActivity(Context context){
        context.startActivity(new Intent(context, WakeTimeActivity.class));
    }


}
