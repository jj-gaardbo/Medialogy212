package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    private ViewPager viewPager;
    private TabsPagerAdapter adapter;
    private ActionBar actionBar;

    private String[] tabs = {"Alarms", "Stuff", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = prefs.getBoolean("firstStart", true);
        if (isFirstStart) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                    boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                    Intent i = new Intent(getApplicationContext(), Infopage.class);
                    startActivity(i);

                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);

                    e.apply();
                }
            });

            t.start();

        } else {
            setContentView(R.layout.activity_main);
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            actionBar = getSupportActionBar();
            adapter = new TabsPagerAdapter(getSupportFragmentManager());

            viewPager.setAdapter(adapter);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            for(String tabName : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
            }

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int pos) {
                    actionBar.setSelectedNavigationItem(pos);
                }
                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

    }


    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityVideoPage.class);
        startActivity(intent);
    }

    public void gotoAlarm(View view) {
        Intent openAlarmPage = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(openAlarmPage);

    }

    public void goToIntro(View view) {
        Intent i = new Intent(getApplicationContext(), Infopage.class);
        //pack stuff with it
        startActivity(i);
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }
}

