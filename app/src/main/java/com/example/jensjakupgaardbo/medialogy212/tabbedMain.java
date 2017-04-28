package com.example.jensjakupgaardbo.medialogy212;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class tabbedMain extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    FloatingActionButton fab;
    //ListAdapter cardAdapter;
    //ArrayList<Alarm> alarms;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(getApplicationContext(), AlarmLocationService.class));
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = prefs.getBoolean("firstStart", true);
        if (isFirstStart) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                    Intent i = new Intent(getApplicationContext(), Infopage.class);
                    startActivity(i);

                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);

                    e.apply();
                }
            });

            t.start();

        } else {
            setContentView(R.layout.activity_tabbed_main);

/*
            AlarmDBHandler dbHandler = new AlarmDBHandler(this,null,null,11);
            ArrayList<Alarm> nonSortedAlarms = dbHandler.getAlarms();

            final ArrayList<Alarm> alarms = nonSortedAlarms;

            if(cardAdapter == null){
                cardAdapter = new CardsAdapter(this,alarms);
            }
            ListView editList = (ListView) findViewById(R.id.listOfCards);
            editList.setAdapter(cardAdapter);
            */





            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Create the adapter that will return a fragment for each of the two
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            fab = (FloatingActionButton) findViewById(R.id.fabAdd);

        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_tabbed_settings, container, false);
            } else {
                rootView = inflater.inflate(R.layout.fragment_tabbed_main, container, false);
                AlarmDBHandler dbHandler = new AlarmDBHandler(container.getContext(),null,null,11);
                ArrayList<Alarm> nonSortedAlarms = dbHandler.getAlarms();

                final ArrayList<Alarm> alarms = nonSortedAlarms;
                //if(cardAdapter == null){
                ListAdapter cardAdapter = new CardsAdapter(container.getContext(),alarms);
                //}
                ListView editList = (ListView) rootView.findViewById(R.id.listOfCards);
                editList.setAdapter(cardAdapter);
                editList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Alarm alarm =  alarms.get(position);
                                                        Intent intent = new Intent(getView().getContext(), AlarmActivity.class);
                                                        intent.putExtra("activeAlarm", alarm);
                                                        if(alarm.get_latlng() != null){
                                                            intent.putExtra("activeAlarmLocation", Alarm.getConvertedLocation(alarm.get_latlng()));
                                                            alarm.set_latlng(null);
                                                        }
                                                        intent.putExtra("editing", true);
                                                        startActivity(intent);
                                                    }
                                                }
                );

                //
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Alarms";
                case 1:
                    return "Settings";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    public void goToGallery(View view) {
        Intent intent = new Intent(getApplicationContext(), Gallery.class);
        startActivity(intent);
    }

    public void goToAlarm(View view) {
        Intent openAlarmPage = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(openAlarmPage);
    }

    public void goToIntro(View view) {
        Intent i = new Intent(getApplicationContext(), Infopage.class);
        startActivity(i);
    }
/*
    public LatLng updateLKL() {   //updates the latLng last know location in shared prefs and returns a latLng object
        SharedPreferences sharedPref = getSharedPreferences(lastKnowLocation, MODE_PRIVATE);
        SharedPreferences.Editor updater = sharedPref.edit();
        LatLng location = new LatLng(15, 85);
        updater.putString(lastKnowLocation, location.toString());
        //LocationSer
        updater.commit();
        return location;
    }

*/
}




