package com.example.jensjakupgaardbo.medialogy212;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class ActivityVideoPage2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page2);
    }

    public void startRain(View v) {
        VideoView videoview = (VideoView) findViewById(R.id.videoView2);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rain);
        videoview.setVideoURI(uri);
        videoview.start();
    }
}
