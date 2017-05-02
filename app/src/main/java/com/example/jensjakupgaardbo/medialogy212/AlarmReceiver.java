package com.example.jensjakupgaardbo.medialogy212;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.graphics.BitmapFactory;
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
