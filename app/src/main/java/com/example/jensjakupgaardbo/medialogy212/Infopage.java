package com.example.jensjakupgaardbo.medialogy212;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Infopage extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addSlide(new layout.firstInfoFragment());

        addSlide(AppIntroFragment.newInstance("Importance of sleep", "Sleep real important yo", R.mipmap.shit, Color.parseColor("#c0392b")));
        addSlide(AppIntroFragment.newInstance("Blue light", "Not good fo ur ass", R.mipmap.shit, Color.parseColor("#e74c3c")));
        addSlide(AppIntroFragment.newInstance("Blue light", "Not good fo ur ass", R.mipmap.shit, Color.parseColor("#f74c3c")));


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