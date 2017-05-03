package com.example.jensjakupgaardbo.medialogy212;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver{

    Vibrator v;
    AlarmLocationListener locListener;
    public static final int ALARM_TYPE_NOTIFICATION = 1;
    public static final int ALARM_TYPE_GO_TO_SLEEP = 2;
    public static final int ALARM_TYPE_WAKE_UP = 3;

    @Override
    public void onReceive(Context context, Intent intent) {

        int alarmType = intent.getExtras().getInt("alarmType", 0);

        if(alarmType == 0){
            return;
        }

        Gson gson = new GsonBuilder().create();
        Alarm alarm = gson.fromJson(intent.getStringExtra("alarmString"), Alarm.class);
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        locListener = tabbedMain.locationListener;
        //Check if the user is within range of the alarms location
        boolean inRange = alarm.isInRange();
        if(!inRange){
            return;
        }

        //todo: Remove this if statement code and replaced with switch/case based on the ALARM TYPE INTEGER
        boolean isBedTime = intent.getExtras().getBoolean("isBedTime", false);
        if (isBedTime) {
            triggerBedtimeActivity(context, alarm);
        } else {
            triggerWakeAlarmActivity(context, alarm);
        }

        switch(alarmType){
            case ALARM_TYPE_NOTIFICATION:
                triggerPreBedTimeNotification(context);
                break;

            case ALARM_TYPE_GO_TO_SLEEP:
                triggerBedtimeActivity(context, alarm);
                break;

            case ALARM_TYPE_WAKE_UP:
                triggerWakeAlarmActivity(context, alarm);
                break;
        }

    }

    public void triggerPreBedTimeNotification(Context context){
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
                        .setContentTitle("Approaching bed time")
                        .setContentText("It is almost bed time.")
                        .setContentIntent(resultPendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setStyle(new Notification.BigTextStyle()
                                .bigText("In order to get the appropriate amount of sleep, you have to go to bed in 30 minutes."));

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(0, mBuilder.build());
    }

    public void triggerBedtimeActivity(Context context, Alarm alarm){
        Gson gson = new GsonBuilder().create();
        Intent bedTimeActivityIntent = new Intent(context, BedTimeActivity.class);
        bedTimeActivityIntent.putExtra("alarmString", gson.toJson(alarm));
        context.startActivity(bedTimeActivityIntent);
    }

    public void triggerWakeAlarmActivity(Context context, Alarm alarm){
        Gson gson = new GsonBuilder().create();
        Intent wakeTimeActivityIntent = new Intent(context, WakeTimeActivity.class);
        wakeTimeActivityIntent.putExtra("alarmString", gson.toJson(alarm));
        context.startActivity(wakeTimeActivityIntent);
    }


}
