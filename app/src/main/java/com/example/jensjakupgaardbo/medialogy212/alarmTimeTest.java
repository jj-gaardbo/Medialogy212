package com.example.jensjakupgaardbo.medialogy212;

import android.app.PendingIntent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class alarmTimeTest extends AppCompatActivity {

    private PendingIntent pendingIntent;
/*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time_test);

        Intent alarmIntent = new Intent(alarmTimeTest.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(alarmTimeTest.this, 0, alarmIntent, 0);
    }
*/
    public void showToast(){
        Toast.makeText(this, "Something happend", Toast.LENGTH_SHORT).show();
    }


}
