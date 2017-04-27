package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import layout.firstInfoFragment;
import layout.fourthInfoFragment;
import layout.secondInfoFragment;
import layout.thirdInfoFragment;


public class Infopage extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSkipButton(true);

        addSlide(new firstInfoFragment());
        addSlide(new secondInfoFragment());
        addSlide(new thirdInfoFragment());
        addSlide(new fourthInfoFragment());

    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        Intent i = new Intent(getApplicationContext(), tabbedMain.class);
        startActivity(i);
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.

    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        Intent i = new Intent(getApplicationContext(), tabbedMain.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed

    }
}
