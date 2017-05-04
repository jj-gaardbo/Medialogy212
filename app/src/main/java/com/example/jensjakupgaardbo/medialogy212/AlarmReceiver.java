package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.example.jensjakupgaardbo.medialogy212.alarmServices.BedTimeActivityService;
import com.example.jensjakupgaardbo.medialogy212.alarmServices.NotificationService;
import com.example.jensjakupgaardbo.medialogy212.alarmServices.WakeUpAlarmService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlarmReceiver extends WakefulBroadcastReceiver {

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
        //Check if the user is within range of the alarms location
        boolean inRange = alarm.isInRange(context);
        if(!inRange){
            return;
        }

        switch(alarmType){
            case ALARM_TYPE_NOTIFICATION:
                Intent notificationIntent = new Intent(context, NotificationService.class);
                context.startService(notificationIntent);
                break;

            case ALARM_TYPE_GO_TO_SLEEP:
                Intent bedTimeActivityIntent = new Intent(context, BedTimeActivityService.class);
                bedTimeActivityIntent.putExtra("alarmString", gson.toJson(alarm));
                context.startService(bedTimeActivityIntent);
                break;

            case ALARM_TYPE_WAKE_UP:
                Intent wakeTimeActivityIntent = new Intent(context, WakeUpAlarmService.class);
                wakeTimeActivityIntent.putExtra("alarmString", gson.toJson(alarm));
                context.startService(wakeTimeActivityIntent);
                break;
        }

    }

}
