package com.example.jensjakupgaardbo.medialogy212;

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
        if (isBedTime()) {
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
                        .setContentText("In order to get your chosen amount of sleep before tomorrow, you have to go to bed now. Motherfucker!");

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(0, mBuilder.build());
    }

    public boolean isBedTime(){
        return true;
    }

    public void triggerWakeAlarmActivity(Context context, Intent intent){
        if(intent != null){
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, WakeTimeActivity.class));
        }
    }


}
