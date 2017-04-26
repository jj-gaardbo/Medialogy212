package com.example.jensjakupgaardbo.medialogy212;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class ActivityVideoPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);

        Button buttonPlayFire = (Button)findViewById(R.id.button);
        getWindow().setFormat(PixelFormat.UNKNOWN);

        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bonfire);
        videoview.setVideoURI(uri);
        videoview.start();


        buttonPlayFire.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoView videoview = (VideoView) findViewById(R.id.videoView);
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bonfire);
                videoview.setVideoURI(uri);
                videoview.start();
            }
        });

    }

}





