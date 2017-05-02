package com.example.jensjakupgaardbo.medialogy212;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentProvider;
import android.support.v4.app.NotificationCompat;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isBedTime = intent.getExtras().getBoolean("isBedTime", false);
        if (isBedTime) {
            triggerBedtimeNotification(context);
        } else {
            triggerWakeAlarmActivity(context, intent);
        }
    }

    public void triggerBedtimeNotification(Context context){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.moon)
                        .setContentTitle("Sleep time")
                        .setContentText("In order to get your chosen amount of sleep before tomorrow, you have to go to bed now. Motherfucker!")
                        .setPriority(Notification.PRIORITY_HIGH);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(0, mBuilder.build());
    }

    public void triggerWakeAlarmActivity(Context context, Intent intent){
        context.startActivity(new Intent(context, WakeTimeActivity.class));
/*
        if(intent != null){
            context.startActivity(intent);
        } else {
        }
        */
    }


}
