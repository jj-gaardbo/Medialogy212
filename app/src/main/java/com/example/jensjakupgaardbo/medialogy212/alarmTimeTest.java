package com.example.jensjakupgaardbo.medialogy212;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class alarmTimeTest extends AppCompatActivity {

    private PendingIntent pendingIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time_test);

        Intent alarmIntent = new Intent(alarmTimeTest.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(alarmTimeTest.this, 0, alarmIntent, 0);
    }

    public void showToast(){
        Toast.makeText(this, "Something happend", Toast.LENGTH_SHORT).show();
    }
    public void setAlarm(View view){
        /*
        AlarmManager alarmMgr;

        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        10 * 1000, alarmIntent);

*/      AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 600;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

    }

}
