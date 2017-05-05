package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class BedTimeActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    TextView goToBedAlarmName;
    TextView goToBedAlarmTime;
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_bed_time);


        Gson gson = new GsonBuilder().create();
        Alarm alarm = gson.fromJson(getIntent().getStringExtra("alarmString"), Alarm.class);
        if(alarm != null){
            goToBedAlarmName = (TextView) findViewById(R.id.go_to_sleep_alarm_name);
            goToBedAlarmName.setText(alarm.get_alarmname());

            goToBedAlarmTime = (TextView) findViewById(R.id.go_to_sleep_alarm_time);
            Calendar cal = Calendar.getInstance();
            goToBedAlarmTime.setText(String.format(Locale.ENGLISH,"%02d:%02d", cal.getTime().getHours(), cal.getTime().getMinutes()));
        }

        stopButton = (Button) findViewById(R.id.go_to_sleep_reaction);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop_alarm();
            }
        });

        playSound(this, getAlarmUri());

    }


    //Play a sound for the alarm
    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            Log.e("ALARM RECEIVER", "Exception" + e.toString());
        }
    }

    //Get the sound URI
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    //Stop the wake alarm sound and the activity
    private void stop_alarm(){
        mMediaPlayer.stop();
        tabbedMain.cancelAlarms(getApplicationContext());
        tabbedMain.setAlarms(getApplicationContext());
        finish();
    }

}
