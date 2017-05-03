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

    @Override
    public void onReceive(Context context, Intent intent) {
        Gson gson = new GsonBuilder().create();
        Alarm alarm = gson.fromJson(intent.getStringExtra("alarmString"), Alarm.class);
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        locListener = tabbedMain.locationListener;
        //Check if the user is within range of the alarms location
        boolean inRange = alarm.isInRange();
        if(!inRange){
            return;
        }
        boolean isBedTime = intent.getExtras().getBoolean("isBedTime", false);
        if (isBedTime) {
            triggerBedtimeNotification(context);
            vibrateOnce();
        } else {
            triggerWakeAlarmActivity(context, alarm);
            vibrate();
        }
    }

    public void vibrateOnce(){
        v.vibrate(1000);
    }

    public void vibrate(){
        long[] pattern = {0, 100, 1000};
        v.vibrate(pattern, 0);
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

    public void triggerWakeAlarmActivity(Context context, Alarm alarm){
        Gson gson = new GsonBuilder().create();
        Intent wakeTimeActivityIntent = new Intent(context, WakeTimeActivity.class);
        wakeTimeActivityIntent.putExtra("alarmString", gson.toJson(alarm));
        context.startActivity(wakeTimeActivityIntent);
    }


}
