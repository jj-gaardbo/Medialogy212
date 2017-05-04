package com.example.jensjakupgaardbo.medialogy212;

// class for use in the database of the app, this is the base class that tells us what to save about the alarms
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Alarm implements Serializable{

    //these are the attributes we want to save in each alarm
    private int _id;
    private boolean active = false;
    private String _alarmname;
    private ArrayList<AlarmTime> alarmTimes = new ArrayList<>();
    private LatLng _latlng;
    boolean hasFullWeek = false;

    public Alarm() {

    }

    public Alarm(String alarmname, LatLng location) {
        this._alarmname = alarmname;
        this._latlng = location;

    }

    public int[] getWakeTimeOfDay(int dayOfWeek){
        ArrayList<AlarmTime> times = getAlarmTimes();

        if(times != null) {
            String wakeTime = "set for today";
            for (AlarmTime t : times) {
                if (t.days[dayOfWeek]) {
                    wakeTime = t.getWakeUp();
                    break;
                }
            }
            if (wakeTime.equals("set for today")) {
                return null;
            } else {
                String[] timeParts = wakeTime.split(":");
                int[] wakeTimes = new int[timeParts.length];
                int counter = 0;
                for (String s : timeParts) {
                    wakeTimes[counter] = Integer.parseInt(timeParts[counter]);
                    counter++;

                }
                return wakeTimes;
            }
        }
        return  null;
    }
    //getters and setters, use these to get or modify the data in the database
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_alarmname() {        return _alarmname;
    }

    public void set_alarmName(String _alarmname) {
        this._alarmname = _alarmname;
    }

    public ArrayList<AlarmTime> getAlarmTimes() {
        return alarmTimes;
    }

    public void setAlarmTimes(ArrayList<AlarmTime> alarmTimes) {
        this.alarmTimes = alarmTimes;
    }

    public LatLng get_latlng() {
        return _latlng;
    }

    public void set_latlng(LatLng _latlng) {
        this._latlng = _latlng;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int[] getBedTime(int dayOfWeek) {

        String bedtime = "No alarm";
        for (AlarmTime t : alarmTimes) {
            if (t.days[dayOfWeek]) {
                bedtime = t.getBedTime();
                break;
            }
        }
        if (bedtime.equals("No alarm")) {
            return null;
        } else {
            String[] timeParts = bedtime.split(":");
            int[] bedTimes = new int[timeParts.length];
            int counter = 0;
            for (String s : timeParts) {
                bedTimes[counter] = Integer.parseInt(timeParts[counter]);
                counter++;

            }
            return bedTimes;
        }
    }

    public int getSleepDuration(int dayOfWeek){
        int sleepDur = 0;
        for(AlarmTime t : alarmTimes){
            if(t.days[dayOfWeek]){
                sleepDur = t.getDuration();
                break;
            }
        }
        return sleepDur;
    }

    public static LatLng reconvertLocation(Alarm alarm, double[] alarmLocationConverted){
        return new LatLng(alarmLocationConverted[0], alarmLocationConverted[1]);
    }

    public static double[] getConvertedLocation(LatLng location){
        double[] locationArray = new double[2];
        locationArray[0] = location.latitude;
        locationArray[1] = location.longitude;
        return locationArray;
    }

    static int convertToOutTimeSys(int dayFromCalendatObj){
        //this method is used to convert from the calendar system of days (1sun-7sat) to our system (0mon-6sun)
        dayFromCalendatObj += -2;
        if(dayFromCalendatObj < 0){dayFromCalendatObj = 6;}
        return dayFromCalendatObj;
    }

    static public ArrayList<Calendar> getNextAlarms(Context context, Alarm a) {
        ArrayList<Calendar> times = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();
        int today = rightNow.get(Calendar.DAY_OF_WEEK);
        int tomorrow = today + 1;

        int weekOfTomorrow = rightNow.get(Calendar.WEEK_OF_MONTH);

        if (tomorrow > 7) {
            tomorrow = 1;
            weekOfTomorrow += 1;
        }

        //check if todays alarms have gone off
        int[] todayWakeTime = a.getWakeTimeOfDay(convertToOutTimeSys(today));
        //do nothing if no alarms were set yesterday
        if(todayWakeTime != null){
            if((rightNow.get(Calendar.HOUR_OF_DAY) < todayWakeTime[0]) || ((int)rightNow.get(Calendar.HOUR_OF_DAY) == (todayWakeTime[0]) && (rightNow.get(Calendar.MINUTE) < todayWakeTime[1]))){
                //if wake alarm has not gone off give the times for that
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, todayWakeTime[0]);
                time.set(Calendar.MINUTE, todayWakeTime[1]);
                time.set(Calendar.SECOND, 0);
                times.add(time);

                //check if bedTime would go off today, if it would put that with it
                int[] todayBedTimes = todayWakeTime;
                todayBedTimes[0] += - a.getSleepDuration(convertToOutTimeSys(today));

                if(todayBedTimes[0]< 0) {
                    return times;
                }

                if(rightNow.get(Calendar.HOUR_OF_DAY) < todayBedTimes[0] || ((int) rightNow.get(Calendar.HOUR_OF_DAY) == todayBedTimes[0] && rightNow.get(Calendar.MINUTE) < todayBedTimes[1])){
                    time = Calendar.getInstance();
                    time.set(Calendar.HOUR_OF_DAY, todayBedTimes[0]);
                    time.set(Calendar.MINUTE, todayBedTimes[1]);
                    times.add(time);


                    //setting notification
                    Calendar notiTime = (Calendar) time.clone();
                    notiTime.add(Calendar.MINUTE, -30);
                    if(rightNow.get(Calendar.DAY_OF_WEEK) == notiTime.get(Calendar.DAY_OF_WEEK ) && rightNow.getTimeInMillis()<notiTime.getTimeInMillis()) {
                        times.add(notiTime);
                    }
                }
                return times;
            }
        }

        //if we get here there must be no alarms to set for today and so we can move on to tomorrow

        int[] tomorrowWakeTimes = a.getWakeTimeOfDay(convertToOutTimeSys(tomorrow));
        if(tomorrowWakeTimes != null){
            //we can always assume that the tomorrow alarm has not been set
            Calendar time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, tomorrowWakeTimes[0]);
            time.set(Calendar.MINUTE, tomorrowWakeTimes[1]);
            time.set(Calendar.SECOND, 0);
            time.set(Calendar.DAY_OF_WEEK, tomorrow);
            time.set(Calendar.WEEK_OF_MONTH, weekOfTomorrow);
            times.add(time);

            //instanciate new time to be used further down
            time = Calendar.getInstance();
            //check if bedTime would go off today, if it would put that with it
            int[] tomorrowBedTimes = tomorrowWakeTimes;
            tomorrowBedTimes[0] += - a.getSleepDuration(convertToOutTimeSys(tomorrow));

            //if tomorrowBedTime is positive the alarm will go off tomorrow otherwise it must be tested
            if(tomorrowBedTimes[0] > -1) {
                time.set(Calendar.HOUR_OF_DAY, tomorrowBedTimes[0]);
                time.set(Calendar.MINUTE, tomorrowBedTimes[1]);
                time.set(Calendar.DAY_OF_WEEK, tomorrow);
                time.set(Calendar.WEEK_OF_MONTH, weekOfTomorrow);
                times.add(time);

                //setting notification
                Calendar notiTime = (Calendar) time.clone();
                notiTime.add(Calendar.MINUTE, -30);
                if(rightNow.get(Calendar.DAY_OF_WEEK) == notiTime.get(Calendar.DAY_OF_WEEK ) && rightNow.getTimeInMillis()<notiTime.getTimeInMillis()) {
                    times.add(notiTime);
                }
                return times;

            }else {
                //if not the alarm must be today and we need to get its hours today
                tomorrowBedTimes[0] += 24;
            }


            if(rightNow.get(Calendar.HOUR_OF_DAY) < tomorrowBedTimes[0] || ((int) rightNow.get(Calendar.HOUR_OF_DAY) == tomorrowBedTimes[0] && rightNow.get(Calendar.MINUTE) < tomorrowBedTimes[1])){
                time.set(Calendar.HOUR_OF_DAY, tomorrowBedTimes[0]);
                time.set(Calendar.MINUTE, tomorrowBedTimes[1]);
                time.set(Calendar.DAY_OF_WEEK, today);
                times.add(time);
                //setting notification
                Calendar notiTime = (Calendar) time.clone();
                notiTime.add(Calendar.MINUTE, -30);
                if(rightNow.get(Calendar.DAY_OF_WEEK) == notiTime.get(Calendar.DAY_OF_WEEK ) && rightNow.getTimeInMillis()<notiTime.getTimeInMillis()) {
                    times.add(notiTime);
                }
            }
            return times;
            }

        // if we get here there must be no times to return
        return null;
        }

        public Location getAlarmLocation(){
            LatLng alarmCoords = this.get_latlng();
            Location alarmLocation = new Location("");
            alarmLocation.setLatitude(alarmCoords.latitude);
            alarmLocation.setLongitude(alarmCoords.longitude);
            return alarmLocation;
        }

    public boolean isInRange(){
        if(!tabbedMain.hasLocationPermission){
            return false;
        }
        Location deviceLocation = tabbedMain.locationListener.getLastLocation();
        Location alarmLocation = this.getAlarmLocation();
        float distance = alarmLocation.distanceTo(deviceLocation);
        return (distance < tabbedMain.SEARCH_RADIUS);
    }

    public ArrayList<String>  getNextAlarmTimes(){
        ArrayList<String> times = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();
        int today = rightNow.get(Calendar.DAY_OF_WEEK);
        int tomorrow = today + 1;

        if (tomorrow > 7) {tomorrow = 1;}

        //check if todays alarms have gone off
        int[] todayWakeTime = this.getWakeTimeOfDay(convertToOutTimeSys(today));
        //do nothing if no alarms were set yesterday
        if(todayWakeTime != null){
            if((rightNow.get(Calendar.HOUR_OF_DAY) < todayWakeTime[0]) || ((int)rightNow.get(Calendar.HOUR_OF_DAY) == (todayWakeTime[0]) && (rightNow.get(Calendar.MINUTE) < todayWakeTime[1]))){
                //if wake alarm has not gone off give the times for that
                times.add(String.format(Locale.ENGLISH,"%02d:%02d", todayWakeTime[0], todayWakeTime[1]));

                //check if bedTime would go off today, if it would put that with it
                int[] todayBedTimes = todayWakeTime;
                todayBedTimes[0] += - this.getSleepDuration(convertToOutTimeSys(today));

                if(todayBedTimes[0]< 0) {
                    return times;
                }

                if(rightNow.get(Calendar.HOUR_OF_DAY) < todayBedTimes[0] || ((int) rightNow.get(Calendar.HOUR_OF_DAY) == todayBedTimes[0] && rightNow.get(Calendar.MINUTE) < todayBedTimes[1])){
                    times.add(String.format(Locale.ENGLISH,"%02d:%02d", todayBedTimes[0], todayBedTimes[1]));
                }
                return times;
            }
        }

        //if we get here there must be no alarms to set for today and so we can move on to tomorrow

        int[] tomorrowWakeTimes = this.getWakeTimeOfDay(convertToOutTimeSys(tomorrow));
        if(tomorrowWakeTimes != null){
            //we can always assume that the tomorrow alarm has not been set

            times.add(String.format(Locale.ENGLISH,"%02d:%02d", tomorrowWakeTimes[0], tomorrowWakeTimes[1]));

            //check if bedTime would go off today, if it would put that with it
            int[] tomorrowBedTimes = tomorrowWakeTimes;
            tomorrowBedTimes[0] += - this.getSleepDuration(convertToOutTimeSys(tomorrow));

            //if tomorrowBedTime is positive the alarm will go off tomorrow otherwise it must be tested
            if(tomorrowBedTimes[0] > -1) {

                times.add(String.format(Locale.ENGLISH,"%02d:%02d", tomorrowBedTimes[0], tomorrowBedTimes[1]));
                return times;
            }else {
                //if not the alarm must be today and we need to get its hours today
                tomorrowBedTimes[0] += 24;
            }


            if(rightNow.get(Calendar.HOUR_OF_DAY) < tomorrowBedTimes[0] || ((int) rightNow.get(Calendar.HOUR_OF_DAY) == tomorrowBedTimes[0] && rightNow.get(Calendar.MINUTE) < tomorrowBedTimes[1])){
                times.add(String.format(Locale.ENGLISH,"%02d:%02d", tomorrowBedTimes[0], tomorrowBedTimes[1]));
            }
            return times;
        }

        // if we get here there must be no times to return
        return null;
    }



}


