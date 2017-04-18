package com.example.jensjakupgaardbo.medialogy212;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.*;

public class Infopage extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addSlide(new layout.firstInfoFragment());

        addSlide(AppIntroFragment.newInstance("Importance of sleep", "Sleep real important yo", R.mipmap.shit, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Blue light", "Not good fo ur ass", R.mipmap.shit, Color.parseColor("#FF4081")));


    }
    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}
